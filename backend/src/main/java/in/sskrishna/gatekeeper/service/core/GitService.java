package in.sskrishna.gatekeeper.service.core;

import in.sskrishna.gatekeeper.model.Commit;
import in.sskrishna.gatekeeper.model.GitRepo;
import in.sskrishna.gatekeeper.provider.GitNativeUtil;
import in.sskrishna.gatekeeper.provider.GitProvider;
import in.sskrishna.gatekeeper.provider.GitProviderImpl;
import in.sskrishna.gatekeeper.repository.api.CommitRepo;
import in.sskrishna.gatekeeper.repository.api.GitRepoRepository;
import in.sskrishna.gatekeeper.service.core.locks.GlobalKeys;
import in.sskrishna.gatekeeper.service.core.locks.GlobalLockRepo;
import io.sskrishna.rest.response.ErrorCodeLookup;
import io.sskrishna.rest.response.ErrorDetail;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;


@Service
@Slf4j
public class GitService {
    private final ExecutorService globalExecutorService;
    private final ErrorCodeLookup errorCodeLookup;
    private final GitRepoRepository gitRepository;
    private final CommitRepo commitRepository;

    public GitService(@Qualifier("GlobalExecutorService") ExecutorService globalExecutorService,
                      GitRepoRepository gitRepository,
                      ErrorCodeLookup errorCodeLookup,
                      CommitRepo commitRepository) {
        this.globalExecutorService = globalExecutorService;
        this.gitRepository = gitRepository;
        this.errorCodeLookup = errorCodeLookup;
        this.commitRepository = commitRepository;
    }

    public void refresh(GitRepo repo) {
        if (!this.lockResource(GlobalKeys.REPO_INDEX, repo.getId())) {
            log.info("Repository is already being indexed. skiping reaquest to refresh: ", repo.getId());
            return;
        }

        repo.getStatus().setProgress(GitRepo.Status.Progress.QUEUED);
        this.gitRepository.save(repo);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    refreshSync(repo);
                } finally {
                    unlockResource(GlobalKeys.REPO_INDEX, repo.getId());
                    ThreadPoolExecutor tpe = (ThreadPoolExecutor) globalExecutorService;
                    log.info("repository refresh queue size: {}", tpe.getQueue().size());
                }
            }
        };
        this.globalExecutorService.submit(runnable);
    }

    public void refreshSync(GitRepo repo) {
        String repoId = repo.getId();
        ZonedDateTime now = ZonedDateTime.now();
        GitProvider gitProvider = new GitProviderImpl(repo);
        repo.getStatus().setProgress(GitRepo.Status.Progress.IN_PROGRESS);
        this.gitRepository.save(repo);
        try {
            log.info("removing all info from db that belongs to: {}", repoId);
            this.commitRepository.removeAllByRepoId(repo.getId());

            if (!gitProvider.exists()) {
                log.info("repo does not exists. attempting to clone: {}", repoId);
                gitProvider.cloneGit();
                log.info("fetching repository: {}", repoId);
                gitProvider.fetch();
            } else {
                log.info("repo exists. skipping clone: {}", repoId);
                log.info("fetching repository: {}", repoId);
                gitProvider.fetch();
            }
            ZonedDateTime taskNow = ZonedDateTime.now();
            log.info("retrieving branches info from git for: {}",   repoId);
            Set<GitRepo.Branch> branches = gitProvider.getBranches();
            repo.setBranches(branches);
            log.info("saving branches info for: {}", repoId);
            this.gitRepository.save(repo);
            log.info("updated branches info. time taken: {}", taskNow.until(ZonedDateTime.now(), ChronoUnit.SECONDS));

            taskNow = ZonedDateTime.now();
            log.info("retrieving commits info from git for: {}",   repoId);
            Map<String, Commit> commitMap = gitProvider.getCommits();
            this.commitRepository.save(commitMap.values());
            log.info("saving commits info for: {}", repoId);
            log.info("updated commit history. time taken: {}", taskNow.until(ZonedDateTime.now(), ChronoUnit.SECONDS));

            taskNow = ZonedDateTime.now();
            log.info("updating repo info for: " + repoId);
            repo.setTotalCommits(commitMap.size());
            repo.getStatus().setProgress(GitRepo.Status.Progress.DONE);
            repo.setDiskUsage(new GitNativeUtil(repo).getDiskUsage());
            repo.getStatus().setLastRefreshedAt(System.currentTimeMillis());
            this.gitRepository.save(repo);
            log.info("updated repo info. time taken: {}", taskNow.until(ZonedDateTime.now(), ChronoUnit.SECONDS));
        } catch (InvalidRemoteException exception) {
            this.handleException(repo, "repo.invalid.remote", exception);
        } catch (TransportException exception) {
            this.handleException(repo, "repo.network.error", exception);
        } catch (Exception exception) {
            this.handleException(repo, "repo.initialization.failed", exception);
        } finally {
            long seconds = now.until(ZonedDateTime.now(), ChronoUnit.SECONDS);
            log.info("repo refresh finished for: {} after: {} sec", repoId, seconds);
        }
    }

    private void handleException(GitRepo repo, String code, Exception exception) {
        ErrorDetail errorDetail = this.errorCodeLookup.getErrorCode(code);
        errorDetail.setCause(exception.getMessage());
        repo.getStatus().addError(errorDetail);
        repo.getStatus().setProgress(GitRepo.Status.Progress.ERROR);
        this.gitRepository.save(repo);
    }

    private boolean lockResource(String... keys) {
        boolean isLocked = GlobalLockRepo.lock(keys);
        return isLocked;
    }

    private void unlockResource(String... keys) {
        GlobalLockRepo.unlock(keys);
    }
}

package in.sskrishna.convoy.service;

import in.sskrishna.convoy.model.GitRepo;
import in.sskrishna.convoy.repository.GitRepoRepository;
import in.sskrishna.convoy.service.core.GitService;
import in.sskrishna.convoy.service.core.locks.GlobalKeys;
import in.sskrishna.convoy.service.core.locks.GlobalLockRepo;
import io.sskrishna.rest.response.RestErrorBuilder;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

@Service
@Slf4j
public class RepoActionService {

    private final RestErrorBuilder errorBuilder;
    private final ExecutorService globalExecutorService;

    private final GitService gitService;
    private final GitRepoRepository gitRepository;

    public RepoActionService(RestErrorBuilder errorBuilder,
                             @Qualifier("GlobalExecutorService") ExecutorService globalExecutorService,
                             GitService gitService,
                             GitRepoRepository gitRepository) {
        this.errorBuilder = errorBuilder;
        this.globalExecutorService = globalExecutorService;

        this.gitRepository = gitRepository;
        this.gitService = gitService;
    }

    public void refreshGit(String repoId) throws GitAPIException, IOException {
        this.lockResource(GlobalKeys.REPO_INDEX, repoId);
        GitRepo repo = this.gitRepository.findOne(repoId);

        Runnable runnable = () -> {
            try {
                gitService.refresh(repo);
            } catch (Exception e) {
                log.warn("Unable to refresh git repo: " + repoId, e);
            } finally {
                this.unlockResource(GlobalKeys.REPO_INDEX, repoId);
            }
        };
        this.globalExecutorService.submit(runnable);
    }

    private void lockResource(String... keys) {
        boolean isLocked = GlobalLockRepo.lock(keys);
        if (!isLocked) {
            errorBuilder.restError().throwError(423, "repo.locked");
        }
    }

    private void unlockResource(String... keys) {
        GlobalLockRepo.unlock(keys);
    }
}
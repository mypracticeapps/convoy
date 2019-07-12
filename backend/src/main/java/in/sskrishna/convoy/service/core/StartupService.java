package in.sskrishna.convoy.service.core;

import in.sskrishna.convoy.model.GitRepo;
import in.sskrishna.convoy.provider.GitProvider;
import in.sskrishna.convoy.repository.GitRepoRepository;
import in.sskrishna.convoy.service.core.locks.GlobalLockRepo;
import io.sskrishna.rest.response.ErrorCodeLookup;
import io.sskrishna.rest.response.ErrorDetail;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.InvalidRemoteException;
import org.eclipse.jgit.api.errors.TransportException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Iterator;

@Service
@Slf4j
public class StartupService {
    private final ErrorCodeLookup errorCodeLookup;
    private final GitRepoRepository gitRepository;
    private final GitService gitService;
    private final GitProvider gitProvider;

    @Autowired
    public StartupService(ErrorCodeLookup errorCodeLookup, GitRepoRepository gitRepository,
                          GitProvider gitProvider,
                          GitService gitService) {
        this.errorCodeLookup = errorCodeLookup;
        this.gitRepository = gitRepository;
        this.gitService = gitService;
        this.gitProvider = gitProvider;
    }

    public void startUp() throws GitAPIException, IOException {
        try {
            GlobalLockRepo.lock(GlobalLockRepo.KEYS.SERVER_BOOTING);
            ZonedDateTime now = ZonedDateTime.now();
            log.info("Found {} repositories", this.gitRepository.findAll().size());

            for (GitRepo repo : this.gitRepository.findAll()) {
                try {
                    if (!this.gitProvider.exits(repo)) {
                        log.info("git repo: {} does not exits. attempting to clone", repo.getId());
                        this.gitProvider.clone(repo);
                    }
                    this.gitService.refresh(repo);
                    this.removeError("repo.initialization.failed", repo);
                    this.gitRepository.save(repo);
                } catch (InvalidRemoteException exception) {
                    ErrorDetail errorDetail = this.errorCodeLookup.getErrorCode("repo.invalid.remote");
                    errorDetail.setCause(exception.getMessage());
                    repo.getStatus().addError(errorDetail);
                    this.gitRepository.save(repo);
                } catch (TransportException exception) {
                    ErrorDetail errorDetail = this.errorCodeLookup.getErrorCode("repo.invalid.token");
                    errorDetail.setCause(exception.getMessage());
                    repo.getStatus().addError(errorDetail);
                    this.gitRepository.save(repo);
                } catch (GitAPIException exception) {
                    ErrorDetail errorDetail = this.errorCodeLookup.getErrorCode("repo.initialization.failed");
                    errorDetail.setCause(exception.getMessage());
                    repo.getStatus().addError(errorDetail);
                    this.gitRepository.save(repo);
                }
            }

            long seconds = now.until(ZonedDateTime.now(), ChronoUnit.SECONDS);
            log.info("Startup process finished. time taken: {} sec", seconds);
        } finally {
            GlobalLockRepo.unlock(GlobalLockRepo.KEYS.SERVER_BOOTING);
        }
    }

    private void removeError(String code, GitRepo repo) {
        Iterator<ErrorDetail> iterator = repo.getStatus().getErrors().iterator();
        while (iterator.hasNext()) {
            ErrorDetail detail = iterator.next();
            if (detail.getCode().equals(code)) {
                iterator.remove();
            }
        }
    }
}
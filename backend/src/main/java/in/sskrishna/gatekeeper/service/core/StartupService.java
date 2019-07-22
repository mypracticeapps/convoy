package in.sskrishna.gatekeeper.service.core;

import in.sskrishna.gatekeeper.model.GitRepo;
import in.sskrishna.gatekeeper.repository.api.GitRepoRepository;
import in.sskrishna.gatekeeper.service.core.locks.GlobalLockRepo;
import io.sskrishna.rest.response.ErrorCodeLookup;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Set;
import java.util.concurrent.ExecutorService;

@Service
@Slf4j
public class StartupService {
    private final ExecutorService globalExecutorService;
    private final ErrorCodeLookup errorCodeLookup;
    private final GitRepoRepository gitRepository;
    private final GitService gitService;

    @Autowired
    public StartupService(@Qualifier("GlobalExecutorService") ExecutorService globalExecutorService,
                          ErrorCodeLookup errorCodeLookup,
                          GitRepoRepository gitRepository,
                          GitService gitService) {
        this.globalExecutorService = globalExecutorService;
        this.errorCodeLookup = errorCodeLookup;
        this.gitRepository = gitRepository;
        this.gitService = gitService;
    }

    public void startUp() throws GitAPIException, IOException {
        try {
            GlobalLockRepo.lock(GlobalLockRepo.KEYS.SERVER_BOOTING);
            ZonedDateTime now = ZonedDateTime.now();
            log.info("Found {} repositories", this.gitRepository.size());

            Set<GitRepo> repoSet = this.gitRepository.findAll();
            for (GitRepo repo : repoSet) {
                this.gitService.refresh(repo);
            }

            long seconds = now.until(ZonedDateTime.now(), ChronoUnit.SECONDS);
            log.info("Startup process finished. time taken: {} sec", seconds);
        } finally {
            GlobalLockRepo.unlock(GlobalLockRepo.KEYS.SERVER_BOOTING);
        }
    }
}

package in.sskrishna.convoy.service.core;

import in.sskrishna.convoy.model.GitRepo;
import in.sskrishna.convoy.provider.GitProvider;
import in.sskrishna.convoy.repository.GitRepoRepository;
import in.sskrishna.convoy.service.core.locks.GlobalLockRepo;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;

@Service
@Slf4j
public class StartupService {
    private final GitRepoRepository gitRepository;
    private final GitService gitService;
    private final GitProvider gitProvider;

    @Autowired
    public StartupService(GitRepoRepository gitRepository,
                          GitProvider gitProvider,
                          GitService gitService) {
        this.gitRepository = gitRepository;
        this.gitService = gitService;
        this.gitProvider = gitProvider;
    }

    public void startUp() throws GitAPIException, IOException {
        try {
            GlobalLockRepo.lock(GlobalLockRepo.KEYS.SERVER_BOOTING);
            ZonedDateTime now = ZonedDateTime.now();
            log.info("Found {} repositories", this.gitRepository.findAll().size());

            for (GitRepo repository : this.gitRepository.findAll()) {
                if (!this.gitProvider.exits(repository)) {
                    log.info("git repo: {} does not exits. attempting to clone", repository.getId());
                    this.gitProvider.clone(repository);
                }
                this.gitService.refresh(repository);
            }

            long seconds = now.until(ZonedDateTime.now(), ChronoUnit.SECONDS);
            log.info("Startup process finished. time taken: {} sec", seconds);
        } finally {
            GlobalLockRepo.unlock(GlobalLockRepo.KEYS.SERVER_BOOTING);
        }
    }
}
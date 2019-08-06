package in.sskrishna.gatekeeper.service;

import in.sskrishna.gatekeeper.model.MyGit;
import in.sskrishna.gatekeeper.repository.api.MyGitRepository;
import in.sskrishna.gatekeeper.service.core.GitService;
import in.sskrishna.gatekeeper.validators.RepoServiceValidator;
import io.sskrishna.rest.response.RestErrorBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

@Service
@Slf4j
public class RepoActionService {
    private final RepoServiceValidator repoServiceValidator;
    private final RestErrorBuilder errorBuilder;
    private final ExecutorService globalExecutorService;

    private final GitService gitService;
    private final MyGitRepository gitRepository;

    public RepoActionService(RestErrorBuilder errorBuilder,
                             RepoServiceValidator repoServiceValidator,
                             @Qualifier("GlobalExecutorService") ExecutorService globalExecutorService,
                             GitService gitService,
                             MyGitRepository gitRepository) {
        this.repoServiceValidator = repoServiceValidator;
        this.errorBuilder = errorBuilder;
        this.globalExecutorService = globalExecutorService;

        this.gitRepository = gitRepository;
        this.gitService = gitService;
    }

    public void refreshGit(String repoId) {
        repoServiceValidator.validateGetOne(repoId);
        MyGit repo = this.gitRepository.findById(repoId).get();
        this.gitService.refresh(repo);
    }

    public void refreshAll() {
        for (MyGit repo : this.gitRepository.findAll()) {
            this.gitService.refresh(repo);
        }
    }
}

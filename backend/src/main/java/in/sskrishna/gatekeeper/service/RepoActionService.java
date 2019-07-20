package in.sskrishna.gatekeeper.service;

import in.sskrishna.gatekeeper.model.GitRepo;
import in.sskrishna.gatekeeper.repository.api.GitRepoRepository;
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
    private final GitRepoRepository gitRepository;

    public RepoActionService(RestErrorBuilder errorBuilder,
                             RepoServiceValidator repoServiceValidator,
                             @Qualifier("GlobalExecutorService") ExecutorService globalExecutorService,
                             GitService gitService,
                             GitRepoRepository gitRepository) {
        this.repoServiceValidator = repoServiceValidator;
        this.errorBuilder = errorBuilder;
        this.globalExecutorService = globalExecutorService;

        this.gitRepository = gitRepository;
        this.gitService = gitService;
    }

    public void refreshGit(String repoId) {
        repoServiceValidator.validateGetOne(repoId);
        GitRepo repo = this.gitRepository.findOne(repoId);
        this.gitService.refresh(repo);
    }

    public void refreshAll() {
        for(GitRepo repo: this.gitRepository.findAll()){
            this.gitService.refresh(repo);
        }
    }
}

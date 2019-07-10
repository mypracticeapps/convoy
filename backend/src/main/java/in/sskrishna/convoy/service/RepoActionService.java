package in.sskrishna.convoy.service;

import in.sskrishna.convoy.model.GitRepo;
import in.sskrishna.convoy.repository.GitRepoRepository;
import in.sskrishna.convoy.service.core.GitService;
import in.sskrishna.convoy.service.core.locks.RepoRefreshLock;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RepoActionService {
    private final GitService gitService;
    private final GitRepoRepository gitRepository;

    public RepoActionService(GitService gitService, GitRepoRepository gitRepository) {
        this.gitRepository = gitRepository;
        this.gitService = gitService;
    }

    @RepoRefreshLock
    public void refreshGit(String repoId) throws GitAPIException, IOException {
        GitRepo repo = this.gitRepository.findOne(repoId);
        gitService.refresh(repo);
    }
}
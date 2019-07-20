package in.sskrishna.gatekeeper.service;

import in.sskrishna.gatekeeper.model.GitRepo;
import in.sskrishna.gatekeeper.repository.GitRepoRepository;
import in.sskrishna.gatekeeper.service.core.locks.GlobalKeys;
import in.sskrishna.gatekeeper.service.core.locks.GlobalLockRepo;
import in.sskrishna.gatekeeper.validators.RepoServiceValidator;
import io.sskrishna.rest.response.RestErrorBuilder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepoService {
    private final RestErrorBuilder errorBuilder;
    private final RepoServiceValidator repoServiceValidator;
    private final GitRepoRepository repository;

    public RepoService(RestErrorBuilder errorBuilder,
                       RepoServiceValidator repoServiceValidator,
                       GitRepoRepository repository) {
        this.errorBuilder = errorBuilder;
        this.repoServiceValidator = repoServiceValidator;
        this.repository = repository;
    }

    public List<GitRepo> getRepos() {
        this.verifyLock(GlobalKeys.SERVER_BOOT);
        return this.repository.findAll().stream().sorted(this.sortByName()).collect(Collectors.toList());
    }

    public GitRepo getOne(String repoId) {
        this.repoServiceValidator.validateGetOne(repoId);
        return this.repository.findOne(repoId);
    }

    public void isLocked(String repoId) {
        this.verifyLock(GlobalKeys.REPO_INDEX, repoId);
    }

    private Comparator<GitRepo> sortByName() {
        Comparator<GitRepo> commitComparator = new Comparator<GitRepo>() {
            @Override
            public int compare(GitRepo repo1, GitRepo repo2) {
                return repo1.getName().compareTo(repo2.getName());
            }
        };

        return commitComparator;
    }

    public void verifyLock(String... keys) {
        boolean isLocked = GlobalLockRepo.isLocked(keys);
        if (isLocked) {
            errorBuilder.restError().throwError(470, "repo.locked");
        }
    }
}

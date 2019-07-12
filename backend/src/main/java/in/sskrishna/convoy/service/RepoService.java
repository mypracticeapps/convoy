package in.sskrishna.convoy.service;

import in.sskrishna.convoy.model.GitRepo;
import in.sskrishna.convoy.repository.GitRepoRepository;
import in.sskrishna.convoy.service.core.locks.GlobalKeys;
import in.sskrishna.convoy.service.core.locks.GlobalLockRepo;
import io.sskrishna.rest.response.RestErrorBuilder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepoService {
    private final RestErrorBuilder errorBuilder;
    private final GitRepoRepository repository;

    public RepoService(RestErrorBuilder errorBuilder, GitRepoRepository repository) {
        this.errorBuilder = errorBuilder;
        this.repository = repository;
    }

    public List<GitRepo> getRepos() {
        this.verifyLock(GlobalKeys.SERVER_BOOT);
        return this.repository.findAll().stream().sorted(this.sortByName()).collect(Collectors.toList());
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
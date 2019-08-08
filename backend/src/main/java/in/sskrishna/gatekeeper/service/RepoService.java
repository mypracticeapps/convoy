package in.sskrishna.gatekeeper.service;

import in.sskrishna.gatekeeper.model.MyGit;
import in.sskrishna.gatekeeper.repository.api.MyGitRepository;
import in.sskrishna.gatekeeper.service.core.locks.GlobalKeys;
import in.sskrishna.gatekeeper.service.core.locks.GlobalLockRepo;
import in.sskrishna.gatekeeper.util.StreamUtil;
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
    private final MyGitRepository repository;

    public RepoService(RestErrorBuilder errorBuilder,
                       RepoServiceValidator repoServiceValidator,
                       MyGitRepository repository) {
        this.errorBuilder = errorBuilder;
        this.repoServiceValidator = repoServiceValidator;
        this.repository = repository;
    }

    public List<MyGit> getRepos() {
        this.verifyLock(GlobalKeys.SERVER_BOOT);
        return StreamUtil.from(this.repository.findAll()).sorted(this.sortByName()).collect(Collectors.toList());
    }

    public MyGit getOne(String repoId) {
        this.repoServiceValidator.validateGetOne(repoId);
        return this.repository.findById(repoId).get();
    }

    public void isLocked(String repoId) {
        this.verifyLock(GlobalKeys.REPO_INDEX, repoId);
    }

    private Comparator<MyGit> sortByName() {
        Comparator<MyGit> commitComparator = new Comparator<MyGit>() {
            @Override
            public int compare(MyGit repo1, MyGit repo2) {
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

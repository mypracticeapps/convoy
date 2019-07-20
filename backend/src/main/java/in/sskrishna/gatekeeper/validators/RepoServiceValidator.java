package in.sskrishna.gatekeeper.validators;

import in.sskrishna.gatekeeper.model.GitRepo;
import in.sskrishna.gatekeeper.repository.GitRepoRepository;
import in.sskrishna.gatekeeper.service.core.locks.GlobalLockRepo;
import io.sskrishna.rest.response.FormError;
import io.sskrishna.rest.response.RestErrorBuilder;
import org.springframework.stereotype.Service;

@Service
public class RepoServiceValidator {
    private final RestErrorBuilder errorBuilder;
    private final GitRepoRepository gitRepository;

    public RepoServiceValidator(RestErrorBuilder errorBuilder,
                                GitRepoRepository gitRepository) {
        this.errorBuilder = errorBuilder;
        this.gitRepository = gitRepository;
    }

    public void validateGetOne(String repoId) {
        FormError formError = errorBuilder.formError();
        formError.rejectIfEmpty(repoId, "repoId", "repo.id.required");
        formError.throwIfContainsErrors(422, "repo.form.invalid");

        GitRepo repo = this.gitRepository.findOne(repoId);
        if (repo == null) {
            formError.rejectField("repoId", repoId, "repo.not.found");
        }
        // (experimental) no longer required due to front repo progress implementation
//        this.verifyLock(GlobalKeys.REPO_INDEX, repoId);
    }


    public void verifyLock(String... keys) {
        boolean isLocked = GlobalLockRepo.isLocked(keys);
        if (isLocked) {
            errorBuilder.restError().throwError(423, "repo.locked");
        }
    }
}

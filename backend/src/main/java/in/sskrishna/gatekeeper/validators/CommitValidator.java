package in.sskrishna.gatekeeper.validators;


import in.sskrishna.gatekeeper.model.Commit;
import in.sskrishna.gatekeeper.model.GitRepo;
import in.sskrishna.gatekeeper.repository.CommitRepository;
import in.sskrishna.gatekeeper.repository.GitRepoRepository;
import in.sskrishna.gatekeeper.service.core.locks.GlobalKeys;
import in.sskrishna.gatekeeper.service.core.locks.GlobalLockRepo;
import io.sskrishna.rest.response.FormError;
import io.sskrishna.rest.response.RestErrorBuilder;
import org.springframework.stereotype.Service;

@Service
public class CommitValidator {
    private final RestErrorBuilder errorBuilder;
    private final GitRepoRepository gitRepository;
    private final CommitRepository commitRepository;

    public CommitValidator(RestErrorBuilder errorBuilder,
                           GitRepoRepository gitRepository,
                           CommitRepository commitRepository) {
        this.errorBuilder = errorBuilder;
        this.gitRepository = gitRepository;
        this.commitRepository = commitRepository;
    }

    public void validateGetCommits(String repoId, String fromCommitId, int size) {
        FormError formError = errorBuilder.formError();
        formError.rejectIfEmpty(repoId, "repoId", "repo.id.required");
        formError.rejectIfEmpty(fromCommitId, "commitId", "repo.commit.id.required");
        if (size <= 0) {
            formError.rejectField("size", Integer.valueOf(size), "repo.size.invalid");
        }
        formError.throwIfContainsErrors(422, "repo.form.invalid");

        // validate repository
        GitRepo repo = this.gitRepository.findOne(repoId);
        if (repo == null) {
            formError.rejectField("repoId", repoId, "repo.not.found");
        } else if (!repo.getStatus().isInitialized()) {
            formError.rejectField("repoId", repoId, "repo.not.initialized");
        }
        formError.throwIfContainsErrors(422, "repo.form.invalid");

        // validate commit
        Object commitObj = this.commitRepository.findOne(fromCommitId);
        if (commitObj == null) {
            formError.rejectField("commitId", fromCommitId, "repo.commit.not.found");
        } else {
            Commit commit = (Commit) commitObj;
            if (!commit.getRepoId().equals(repoId)) {
                formError.rejectField("commitId", fromCommitId, "repo.commit.mapping.invalid");
            }
        }
        formError.throwIfContainsErrors(422, "repo.form.invalid");

        this.verifyLock(GlobalKeys.REPO_INDEX, repoId);
    }

    public void verifyLock(String... keys) {
        boolean isLocked = GlobalLockRepo.isLocked(keys);
        if (isLocked) {
            errorBuilder.restError().throwError(423, "repo.locked");
        }
    }
}

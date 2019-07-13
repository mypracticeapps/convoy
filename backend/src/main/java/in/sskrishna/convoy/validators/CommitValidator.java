package in.sskrishna.convoy.validators;


import in.sskrishna.convoy.model.Commit;
import in.sskrishna.convoy.model.GitRepo;
import in.sskrishna.convoy.repository.CommitRepository;
import in.sskrishna.convoy.repository.GitRepoRepository;
import in.sskrishna.convoy.service.core.locks.GlobalKeys;
import in.sskrishna.convoy.service.core.locks.GlobalLockRepo;
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

    public void validateGetCommitsByBranch(String repoId, String branchName, int size) {
        FormError formError = errorBuilder.formError();
        formError.rejectIfEmpty(repoId, "repoId", "repo.id.required");
        formError.rejectIfEmpty(repoId, "branchName", "repo.branch.name.required");

        if (!formError.containsErrorCode("repo.id.required")) {
            GitRepo repo = this.gitRepository.findOne(repoId);
            if (repo == null) {
                formError.rejectField("repoId", repoId, "repo.not.found");
            } else {
                boolean isBranchPresent = repo.getBranches()
                        .stream().map(branch -> branch.getName())
                        .anyMatch(name -> name.equals(branchName));
                if (!isBranchPresent) {
                    formError.rejectField("branchName", branchName, "repo.branch.not.found");
                }
            }
        }

        if (size < 0) {
            formError.rejectField("size", Integer.valueOf(size), "repo.size.invalid");
        }

        if (formError.hasErrors()) {
            formError.throwError(422, "repo.form.invalid");
        }

        this.verifyLock(GlobalKeys.REPO_INDEX, repoId);
    }

    public void validateGetCommitsByCommit(String repoId, String commitId, int size) {
        FormError formError = errorBuilder.formError();
        formError.rejectIfEmpty(repoId, "repoId", "repo.id.required");
        formError.rejectIfEmpty(repoId, "branchName", "repo.branch.name.required");

        if (!formError.containsErrorCode("repo.id.required")) {
            GitRepo repo = this.gitRepository.findOne(repoId);
            if (repo == null) {
                formError.rejectField("repoId", repoId, "repo.not.found");
            } else {
                Commit commit = (Commit) this.commitRepository.findOne(commitId);
                if (commit == null) {
                    formError.rejectField("commitId", commitId, "repo.commit.not.found");
                }
            }
        }

        if (size < 0) {
            formError.rejectField("size", Integer.valueOf(size), "repo.size.invalid");
        }

        if (formError.hasErrors()) {
            formError.throwError(422, "repo.form.invalid");
        }

        this.verifyLock(GlobalKeys.REPO_INDEX, repoId);
    }

    public void validateGetRepos() {
        this.verifyLock(GlobalKeys.SERVER_BOOT);
    }

    public void verifyLock(String... keys) {
        boolean isLocked = GlobalLockRepo.isLocked(keys);
        if (isLocked) {
            errorBuilder.restError().throwError(423, "repo.locked");
        }
    }
}

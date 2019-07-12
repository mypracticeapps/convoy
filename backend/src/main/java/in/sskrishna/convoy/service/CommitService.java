package in.sskrishna.convoy.service;

import in.sskrishna.convoy.model.Commit;
import in.sskrishna.convoy.model.GitRepo;
import in.sskrishna.convoy.repository.CommitRepository;
import in.sskrishna.convoy.repository.GitRepoRepository;
import in.sskrishna.convoy.service.core.locks.GlobalKeys;
import in.sskrishna.convoy.service.core.locks.GlobalLockRepo;
import io.sskrishna.rest.response.RestErrorBuilder;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class CommitService {
    private final RestErrorBuilder errorBuilder;
    private final GitRepoRepository gitRepository;
    private final CommitRepository commitRepository;

    public CommitService(RestErrorBuilder errorBuilder, GitRepoRepository gitRepository, CommitRepository commitRepository) {
        this.errorBuilder = errorBuilder;
        this.gitRepository = gitRepository;
        this.commitRepository = commitRepository;
    }

    public List<Commit> getCommitsByBranch(String repoId, String branchName, int size) {
        this.verifyLockedStatus(GlobalKeys.REPO_INDEX, repoId);
        GitRepo repo = this.gitRepository.findOne(repoId);
        GitRepo.Branch branch = null;

        for (GitRepo.Branch branchTmp : repo.getBranches()) {
            if (branchTmp.getName().equals(branchName)) {
                branch = branchTmp;
                break;
            }
        }

        String nextCommitId = branch.getLatestCommitId();
        List<Commit> commitList = new LinkedList<>();
        for (int ii = 0; ii < size; ii++) {
            if (nextCommitId == null) {
                break;
            }
            Commit commit = (Commit) this.commitRepository.findOne(nextCommitId);
            commitList.add(commit);
            nextCommitId = this.getNextCommitId(commit);
        }

        return commitList;
    }

    public List<Commit> getCommitsByCommitId(String repoId, String commitId, int size) {
        this.verifyLockedStatus(GlobalKeys.REPO_INDEX, repoId);
        Commit previousCommit = (Commit) this.commitRepository.findOne(commitId);
        String nextCommitId = this.getNextCommitId(previousCommit);
        List<Commit> commitList = new LinkedList<>();

        for (int ii = 0; ii < size; ii++) {
            if (nextCommitId == null) {
                break;
            }
            Commit commit = (Commit) this.commitRepository.findOne(nextCommitId);
            commitList.add(commit);
            nextCommitId = this.getNextCommitId(commit);
        }

        return commitList;
    }

    // TODO BUG FIX. only taking one parent for pagination
    private String getNextCommitId(Commit commit) {
        if (commit.getParentIds().size() > 0)
            return commit.getParentIds().first();
        return null;
    }

    private void verifyLockedStatus(String... keys) {
        boolean isLocked = GlobalLockRepo.isLocked(keys);
        if (isLocked) {
            errorBuilder.restError().throwError(423, "repo.locked");
        }
    }
}
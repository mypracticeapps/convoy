package in.sskrishna.convoy.service;

import in.sskrishna.convoy.model.Commit;
import in.sskrishna.convoy.model.GitRepo;
import in.sskrishna.convoy.repository.CommitRepository;
import in.sskrishna.convoy.repository.GitRepoRepository;
import in.sskrishna.convoy.service.core.locks.RepoRefreshLock;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.List;

@Service
public class CommitService {
    private final GitRepoRepository gitRepository;
    private final CommitRepository commitRepository;

    public CommitService(GitRepoRepository gitRepository, CommitRepository commitRepository) {
        this.gitRepository = gitRepository;
        this.commitRepository = commitRepository;
    }

    @RepoRefreshLock
    public List<Commit> getCommitsByBranch(String repoId, String branchName, int size) {
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

    @RepoRefreshLock
    public List<Commit> getCommitsByCommitId(String repoId, String commitId, int size) {
        Commit previousCommit = (Commit) this.commitRepository.findOne(commitId);
        String nextCommitId = this.getNextCommitId(previousCommit);
        List<Commit> commitList = new LinkedList<>();

        for (int ii = 0; ii < size; ii++) {
            if(nextCommitId == null){
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
}
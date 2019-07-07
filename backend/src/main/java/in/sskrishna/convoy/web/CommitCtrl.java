package in.sskrishna.convoy.web;

import in.sskrishna.convoy.model.Commit;
import in.sskrishna.convoy.model.GitRepo;
import in.sskrishna.convoy.repository.CommitRepository;
import in.sskrishna.convoy.repository.GitRepoRepository;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedList;
import java.util.List;

@RestController()
@RequestMapping(path = "/api/v1")
@CrossOrigin
public class CommitCtrl {
    private final GitRepoRepository gitRepoRepository;
    private final CommitRepository commitRepository;

    public CommitCtrl(
            GitRepoRepository gitRepoRepository,
            CommitRepository commitRepository) {
        this.gitRepoRepository = gitRepoRepository;
        this.commitRepository = commitRepository;
    }

    @GetMapping("/commits")
    public List<Commit> getRepos(@RequestParam("repoId") String repoId, @RequestParam("branchName") String branchName) {
        GitRepo repo = this.gitRepoRepository.findOne(repoId);
        GitRepo.Branch branch = null;
        for(GitRepo.Branch branchTmp : repo.getBranches()){
            if(branchTmp.getName().equals(branchName)){
                branch = branchTmp;
                break;
            }
        }
        String nextCommitId = branch.getLatestCommitId();
        List<Commit> commitList = new LinkedList<>();
        for (int ii = 0; ii < 100; ii++) {
            if(nextCommitId == null){
                break;
            }
            Commit commit = (Commit) this.commitRepository.findOne(nextCommitId);
            commitList.add(commit);
            nextCommitId = commit.getParentId();
        }

        return commitList;
    }
}
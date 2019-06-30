package in.sskrishna.convoy.web;

import in.sskrishna.convoy.model.Commit;
import in.sskrishna.convoy.model.CommitSet;
import in.sskrishna.convoy.repository.CommitRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController()
@RequestMapping(path = "/api/v1")
public class CommitCtrl {

    private final CommitRepository commitRepository;

    public CommitCtrl(CommitRepository commitRepository) {
        this.commitRepository = commitRepository;
    }

    @GetMapping("/commits")
    public List<Commit> getRepos(@RequestParam("repoId") String repoId, @RequestParam("branchName") String branchName) {
        CommitSet commitSet = this.commitRepository.findOne(repoId + "/" + branchName);
        return commitSet.getCommitList();
    }

}
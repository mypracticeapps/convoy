package in.sskrishna.convoy.web;

import in.sskrishna.convoy.model.Commit;
import in.sskrishna.convoy.service.CommitService;
import in.sskrishna.convoy.service.core.locks.RepoRefreshLock;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping(path = "/api/v1")
@CrossOrigin
public class CommitCtrl {
    private final CommitService commitService;

    public CommitCtrl(CommitService commitService) {
        this.commitService = commitService;
    }

    @GetMapping("/commits")
    public List<Commit> getRepos(@RequestParam("repoId") String repoId,
                                 @RequestParam("searchBy") String searchBy,
                                 @RequestParam("searchTerm") String searchTerm,
                                 @RequestParam("size") int size) {
        if (searchBy.equals("branch")) {
            return this.commitService.getCommitsByBranch(repoId, searchTerm, size);
        } else {
            return this.commitService.getCommitsByCommitId(repoId, searchTerm, size);
        }
    }
}
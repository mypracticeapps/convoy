package in.sskrishna.gatekeeper.web;

import in.sskrishna.gatekeeper.model.Commit;
import in.sskrishna.gatekeeper.service.CommitService;
import io.sskrishna.rest.response.RestResponse;
import org.springframework.http.ResponseEntity;
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
    public ResponseEntity getRepos(@RequestParam("repoId") String repoId,
                                   @RequestParam("commitId") String commitId,
                                   @RequestParam("size") int size) {
        List<Commit> commits = this.commitService.getCommits(repoId, commitId, size);
        return RestResponse.init().status(200).data(commits).success();
    }
}

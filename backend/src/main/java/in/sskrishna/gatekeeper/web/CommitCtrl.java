package in.sskrishna.gatekeeper.web;

import in.sskrishna.gatekeeper.model.Commit;
import in.sskrishna.gatekeeper.service.CommitService;
import in.sskrishna.gatekeeper.util.Timer;
import io.sskrishna.rest.response.RestResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController()
@RequestMapping(path = "/api/v1")
@CrossOrigin
@Slf4j
public class CommitCtrl {
    private final CommitService commitService;

    public CommitCtrl(CommitService commitService) {
        this.commitService = commitService;
    }

    @GetMapping("/commits")
    public ResponseEntity getRepos(@RequestParam("repoId") String repoId,
                                   @RequestParam("commitId") String commitId,
                                   @RequestParam("size") int size) {
        Timer timer = Timer.start();
        List<Commit> commits = this.commitService.getCommits(repoId, commitId, size);
        log.info("time taken to build commits: {} milli sec", timer.diff());
        return RestResponse.init().status(200).data(commits).success();
    }
}

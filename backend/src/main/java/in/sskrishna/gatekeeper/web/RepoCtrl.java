package in.sskrishna.gatekeeper.web;

import in.sskrishna.gatekeeper.service.RepoService;
import io.sskrishna.rest.response.RestResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController()
@RequestMapping(path = "/api/v1")
@CrossOrigin
public class RepoCtrl {

    private final RepoService repoService;

    public RepoCtrl(RepoService repoService) {
        this.repoService = repoService;
    }

    @GetMapping("/repos")
    public ResponseEntity getRepos() {
        return RestResponse.init().data(this.repoService.getRepos()).status(200).success();
    }

    @GetMapping("/repos/one")
    public ResponseEntity getOne(@RequestParam("repoId") String repoId) {
        return RestResponse.init().data(this.repoService.getOne(repoId)).status(200).success();
    }
}

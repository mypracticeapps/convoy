package in.sskrishna.convoy.web;

import in.sskrishna.convoy.service.RepoActionService;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController()
@RequestMapping(path = "/api/v1/repos/actions")
@CrossOrigin
public class RepoActionsCtrl {

    private final RepoActionService repoActionService;

    public RepoActionsCtrl(RepoActionService repoActionService) {
        this.repoActionService = repoActionService;
    }

    @GetMapping("/refresh/git")
    public void refreshGit(@RequestParam("repoId") String repoId) throws GitAPIException, IOException {
        this.repoActionService.refreshGit(repoId);
    }

    @GetMapping("/refresh/stories")
    public void refreshStory(@RequestParam("repoId") String repoId) {

    }
}
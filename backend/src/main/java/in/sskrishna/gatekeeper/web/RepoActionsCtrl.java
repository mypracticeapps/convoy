package in.sskrishna.gatekeeper.web;

import in.sskrishna.gatekeeper.service.RepoActionService;
import in.sskrishna.gatekeeper.validators.RepoServiceValidator;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController()
@RequestMapping(path = "/api/v1/repos/actions")
@CrossOrigin
public class RepoActionsCtrl {
    private final RepoActionService repoActionService;

    public RepoActionsCtrl(RepoServiceValidator repoServiceValidator,
                           RepoActionService repoActionService) {
        this.repoActionService = repoActionService;
    }

    @GetMapping("/refresh/git")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void refreshGit(@RequestParam("repoId") String repoId) throws GitAPIException, IOException {
        this.repoActionService.refreshGit(repoId);
    }

    @GetMapping("/refresh/git/all")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void refreshGitAll() throws GitAPIException, IOException {
        this.repoActionService.refreshAll();
    }

    @GetMapping("/refresh/stories")
    public void refreshStory(@RequestParam("repoId") String repoId) {

    }
}

package in.sskrishna.convoy.web;

import in.sskrishna.convoy.model.GitRepo;
import in.sskrishna.convoy.service.RepoService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController()
@RequestMapping(path = "/api/v1")
@CrossOrigin
public class RepoCtrl {

    private final RepoService repoService;

    public RepoCtrl(RepoService repoService) {
        this.repoService = repoService;
    }

    @GetMapping("/repos")
    public List<GitRepo> getRepos() {
        return this.repoService.getRepos();
    }
}
package in.sskrishna.convoy.web;

import in.sskrishna.convoy.model.GitRepo;
import in.sskrishna.convoy.repository.GitRepoRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController()
@RequestMapping(path = "/api/v1")
@CrossOrigin
public class RepositoryCtrl {

    private final GitRepoRepository repository;

    public RepositoryCtrl(GitRepoRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/repos")
    public Set<GitRepo> getRepos() {
        return this.repository.findAll();
    }
}
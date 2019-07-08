package in.sskrishna.convoy.web;

import in.sskrishna.convoy.model.GitRepo;
import in.sskrishna.convoy.repository.GitRepoRepository;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;
import java.util.stream.*;

@RestController()
@RequestMapping(path = "/api/v1")
@CrossOrigin
public class RepositoryCtrl {

    private final GitRepoRepository repository;

    public RepositoryCtrl(GitRepoRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/repos")
    public List<GitRepo> getRepos() {
        return this.repository.findAll().stream().sorted(this.sortByName()).collect(Collectors.toList()); 
    }

    private Comparator<GitRepo> sortByName() {
        Comparator<GitRepo> commitComparator = new Comparator<GitRepo>() {
            @Override
            public int compare(GitRepo repo1, GitRepo repo2) {
                System.out.println("repo1: " + repo1.getName() + " repo2: " + repo2.getName() + " ret: " + repo1.getName().compareTo(repo2.getName()));
                return repo1.getName().compareTo(repo2.getName());
            }
        };

        return commitComparator;
    }
}
package in.sskrishna.convoy.service;

import in.sskrishna.convoy.model.GitRepo;
import in.sskrishna.convoy.repository.GitRepoRepository;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RepoService {
    private final GitRepoRepository repository;

    public RepoService(GitRepoRepository repository) {
        this.repository = repository;
    }

    public List<GitRepo> getRepos() {
        return this.repository.findAll().stream().sorted(this.sortByName()).collect(Collectors.toList());
    }

    private Comparator<GitRepo> sortByName() {
        Comparator<GitRepo> commitComparator = new Comparator<GitRepo>() {
            @Override
            public int compare(GitRepo repo1, GitRepo repo2) {
                return repo1.getName().compareTo(repo2.getName());
            }
        };

        return commitComparator;
    }
}

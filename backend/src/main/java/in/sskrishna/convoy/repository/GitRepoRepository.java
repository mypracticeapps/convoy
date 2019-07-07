package in.sskrishna.convoy.repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import in.sskrishna.convoy.model.GitRepo;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class GitRepoRepository{
    private Cache<String, GitRepo> cache;

    GitRepoRepository() {
        cache = Caffeine.newBuilder()
                .expireAfterWrite(999, TimeUnit.DAYS)
                .maximumSize(1000)
                .build();
    }

    public void save(GitRepo repo) {
        this.cache.put(repo.getId(), repo);
    }

    public void delete(GitRepo repo) {
        this.cache.invalidate(repo.getId());
    }

    public Set<GitRepo> findAll() {
        Set<GitRepo> set = this.cache.asMap().entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toSet());
        return Collections.unmodifiableSet(set);
    }

    public GitRepo findOne(String id) {
        return this.cache.getIfPresent(id);
    }

    public long size() {
        return this.findAll().size();
    }
}
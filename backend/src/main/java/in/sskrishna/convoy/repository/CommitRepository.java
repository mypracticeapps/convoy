package in.sskrishna.convoy.repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import in.sskrishna.convoy.model.CommitSet;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class CommitRepository {
    private Cache<String, CommitSet> cache;

    public CommitRepository() {
        cache = Caffeine.newBuilder()
                .expireAfterWrite(999, TimeUnit.DAYS)
                .maximumSize(1000)
                .build();
    }

    public void save(CommitSet commitSet) {
        this.cache.put(commitSet.getId(), commitSet);
    }

    public void delete(CommitSet commitSet) {
        this.cache.invalidate(commitSet.getId());
    }

    public Set<CommitSet> findAll() {
        Set<CommitSet> set = this.cache.asMap().entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toSet());
        return Collections.unmodifiableSet(set);
    }

    public CommitSet findOne(String id) {
        return this.cache.getIfPresent(id);
    }

    public long size() {
        return this.findAll().size();
    }
}
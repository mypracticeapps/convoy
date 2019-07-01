package in.sskrishna.convoy.repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import in.sskrishna.convoy.model.BranchSet;
import in.sskrishna.convoy.model.BranchSet;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
public class BranchesRepository {
    private Cache<String, BranchSet> cache;

    public BranchesRepository() {
        cache = Caffeine.newBuilder()
                .expireAfterWrite(999, TimeUnit.DAYS)
                .maximumSize(1000)
                .build();
    }

    public void save(BranchSet branchSet) {
        this.cache.put(branchSet.getId(), branchSet);
    }

    public void delete(BranchSet branchSet) {
        this.cache.invalidate(branchSet.getId());
    }

    public Set<BranchSet> findAll() {
        Set<BranchSet> set = this.cache.asMap().entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toSet());
        return Collections.unmodifiableSet(set);
    }

    public BranchSet findOne(String id) {
        return this.cache.getIfPresent(id);
    }

    public long size() {
        return this.findAll().size();
    }
}
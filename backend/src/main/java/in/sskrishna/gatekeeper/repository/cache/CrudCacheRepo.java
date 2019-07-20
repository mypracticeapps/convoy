package in.sskrishna.gatekeeper.repository.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import in.sskrishna.gatekeeper.model.Entity;
import in.sskrishna.gatekeeper.repository.api.CrudRepo;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public abstract class CrudCacheRepo<K extends String, V extends Entity> implements CrudRepo<K, V> {
    private Cache<String, V> cache;

    public CrudCacheRepo() {
        this(10_00_000);
    }

    public CrudCacheRepo(int cacheSize) {
        cache = Caffeine.newBuilder()
                .expireAfterWrite(999, TimeUnit.DAYS)
                .maximumSize(cacheSize)
                .build();
    }

    @Override
    public void save(V entity) {
        this.cache.put(entity.getId(), entity);
    }

    @Override
    public void save(Collection<V> iterable) {
        iterable.forEach(v -> this.save(v));
    }

    @Override
    public void delete(V entity) {
        this.cache.invalidate(entity.getId());
    }

    @Override
    public Set<V> findAll() {
        Set<V> set = this.cache.asMap().entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toSet());
        return Collections.unmodifiableSet(set);
    }

    @Override
    public V findOne(String id) {
        return this.cache.getIfPresent(id);
    }

    @Override
    public long size() {
        return this.findAll().size();
    }

}

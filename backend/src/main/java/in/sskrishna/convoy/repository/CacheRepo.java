package in.sskrishna.convoy.repository;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import in.sskrishna.convoy.model.Entity;

import java.util.Collection;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public abstract class CacheRepo<K extends String, V extends Entity> {
    private Cache<String, V> cache;

    public CacheRepo() {
        this(10_00_000);
    }

    public CacheRepo(int cacheSize){
        cache = Caffeine.newBuilder()
                .expireAfterWrite(999, TimeUnit.DAYS)
                .maximumSize(cacheSize)
                .build();
    }

    public void save(V entity) {
        this.cache.put(entity.getId(), entity);
    }

//    public void save(List<V> iterable){
//        iterable.forEach(v->this.save(v));
//    }
//
//    public void save(Set<V> iterable){
//        iterable.forEach(v->this.save(v));
//    }

    public void save(Collection<V> iterable){
        iterable.forEach(v->this.save(v));
    }

    public void delete(V entity) {
        this.cache.invalidate(entity.getId());
    }

    public Set<V> findAll() {
        Set<V> set = this.cache.asMap().entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toSet());
        return Collections.unmodifiableSet(set);
    }

    public V findOne(String id) {
        return this.cache.getIfPresent(id);
    }

    public long size() {
        return this.findAll().size();
    }

}
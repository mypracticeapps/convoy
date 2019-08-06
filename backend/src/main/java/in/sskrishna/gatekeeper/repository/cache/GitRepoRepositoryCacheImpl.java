//package in.sskrishna.gatekeeper.repository.cache;
//
//import com.github.benmanes.caffeine.cache.Cache;
//import com.github.benmanes.caffeine.cache.Caffeine;
//import in.sskrishna.gatekeeper.model.GitRepo;
//import in.sskrishna.gatekeeper.repository.api.GitRepoRepository;
//
//import java.util.Collection;
//import java.util.Collections;
//import java.util.Set;
//import java.util.UUID;
//import java.util.concurrent.TimeUnit;
//import java.util.stream.Collectors;
//
//public class GitRepoRepositoryCacheImpl implements GitRepoRepository {
//    private Cache<String, GitRepo> cache;
//
//    public GitRepoRepositoryCacheImpl() {
//        cache = Caffeine.newBuilder()
//                .expireAfterWrite(999, TimeUnit.DAYS)
//                .maximumSize(1000)
//                .build();
//    }
//
//    @Override
//    public void save(GitRepo repo) {
//        repo.setVersion(UUID.randomUUID().toString());
//        this.cache.put(repo.getId(), repo);
//    }
//
//    @Override
//    public void save(Collection<GitRepo> iterable) {
//        iterable.forEach(this::save);
//    }
//
//    @Override
//    public void delete(GitRepo repo) {
//        this.cache.invalidate(repo.getId());
//    }
//
//    @Override
//    public Set<GitRepo> findAll() {
//        Set<GitRepo> set = this.cache.asMap().entrySet().stream().map(entry -> entry.getValue()).collect(Collectors.toSet());
//        return Collections.unmodifiableSet(set);
//    }
//
//    @Override
//    public GitRepo findOne(String id) {
//        return this.cache.getIfPresent(id);
//    }
//
//    @Override
//    public long size() {
//        return this.findAll().size();
//    }
//}

//package in.sskrishna.gatekeeper.config.repo;
//
//import in.sskrishna.gatekeeper.repository.api.CommitRepo;
//import in.sskrishna.gatekeeper.repository.api.GitRepoRepository;
//import in.sskrishna.gatekeeper.repository.cache.CommitCacheRepo;
//import in.sskrishna.gatekeeper.repository.cache.GitRepoRepositoryCacheImpl;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//@ConditionalOnProperty(name = "gatekeeper.database.type", havingValue = "cache")
//public class CacheRepoConfig {
//
//    @Bean
//    public GitRepoRepository gitRepoRepository() {
//        return new GitRepoRepositoryCacheImpl();
//    }
//
//    @Bean
//    public CommitRepo commitRepo() {
//        return new CommitCacheRepo();
//    }
//}

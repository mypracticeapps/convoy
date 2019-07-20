package in.sskrishna.gatekeeper.config;

import in.sskrishna.gatekeeper.repository.api.CommitMappingRepo;
import in.sskrishna.gatekeeper.repository.api.CommitRepo;
import in.sskrishna.gatekeeper.repository.api.GitRepoRepository;
import in.sskrishna.gatekeeper.repository.cache.CommitCacheRepo;
import in.sskrishna.gatekeeper.repository.cache.CommitMappingCacheRepo;
import in.sskrishna.gatekeeper.repository.cache.GitRepoRepositoryCacheImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CacheRepoConfig {

    @Bean
    public GitRepoRepository gitRepoRepository() {
        return new GitRepoRepositoryCacheImpl();
    }

    @Bean
    public CommitRepo commitRepo(){
        return new CommitCacheRepo();
    }

    @Bean
    public CommitMappingRepo commitMappingRepo() {return new CommitMappingCacheRepo();}
}

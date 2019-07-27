package in.sskrishna.gatekeeper.config.repo;

import com.mongodb.MongoClient;
import in.sskrishna.gatekeeper.repository.api.CommitRepo;
import in.sskrishna.gatekeeper.repository.api.GitRepoRepository;
import in.sskrishna.gatekeeper.repository.mongo.CommitRepoMongoImpl;
import in.sskrishna.gatekeeper.repository.mongo.GitRepoRepositoryMongoImpl;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;

@Configuration
@ConditionalOnProperty(name = "gatekeeper.database.type", havingValue = "mongodb")
public class MongoConfig {

    @Bean
    public MongoClient mongoClient() {
        return new MongoClient("127.0.0.1", 27017);
    }

    @Bean
    public MongoTemplate mongoTemplate() throws Exception {
        MongoTemplate mongoTemplate = new MongoTemplate(mongoClient(), "test");
        mongoTemplate.executeCommand("{ buildInfo: 1 }");
        return mongoTemplate;
    }

    @Bean
    public GitRepoRepository gitRepoRepository(MongoTemplate mongoTemplate) {
        return new GitRepoRepositoryMongoImpl(mongoTemplate);
    }

    @Bean
    public CommitRepo commitRepo(MongoTemplate mongoTemplate) {
        return new CommitRepoMongoImpl(mongoTemplate);
    }
}

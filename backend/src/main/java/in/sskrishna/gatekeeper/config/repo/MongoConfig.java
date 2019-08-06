package in.sskrishna.gatekeeper.config.repo;

import com.mongodb.MongoClient;
import in.sskrishna.gatekeeper.model.Commit;
import in.sskrishna.gatekeeper.model.MyGit;
import in.sskrishna.gatekeeper.repository.api.CommitRepo;
import in.sskrishna.gatekeeper.repository.api.MyGitRepository;
import in.sskrishna.gatekeeper.repository.mongo.MongoCommitRepo;
import in.sskrishna.gatekeeper.repository.mongo.MongoMyGitRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.repository.support.MongoRepositoryFactoryBean;

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
    public MyGitRepository gitRepoRepository(MongoTemplate mongoTemplate) {
        MongoRepositoryFactoryBean<MongoMyGitRepository, MyGit, String> myFactory = new MongoRepositoryFactoryBean(MongoMyGitRepository.class);
        myFactory.setMongoOperations(mongoTemplate);
        myFactory.afterPropertiesSet();
        return myFactory.getObject();
    }

    @Bean
    public CommitRepo commitRepo(MongoTemplate mongoTemplate) {
        MongoRepositoryFactoryBean<MongoCommitRepo, Commit, String> myFactory = new MongoRepositoryFactoryBean<>(MongoCommitRepo.class);
        myFactory.setMongoOperations(mongoTemplate);
        myFactory.afterPropertiesSet();
        return myFactory.getObject();
    }
}

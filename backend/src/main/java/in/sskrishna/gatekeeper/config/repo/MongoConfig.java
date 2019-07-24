package in.sskrishna.gatekeeper.config.repo;

import com.mongodb.MongoClient;
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
        return new MongoTemplate(mongoClient(), "test");
    }
}

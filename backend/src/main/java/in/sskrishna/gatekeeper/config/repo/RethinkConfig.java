package in.sskrishna.gatekeeper.config.repo;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import in.sskrishna.gatekeeper.repository.api.CommitRepo;
import in.sskrishna.gatekeeper.repository.api.GitRepoRepository;
import in.sskrishna.gatekeeper.repository.rethink.CommitRepoRethinkImpl;
import in.sskrishna.gatekeeper.repository.rethink.GitRepoRepositoryRethinkImpl;
import in.sskrishna.gatekeeper.repository.rethink.RethinkUtil;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConditionalOnProperty(name = "gatekeeper.database.type", havingValue = "rethinkdb")
public class RethinkConfig {

    @Qualifier("rethink")
    @Bean
    public Connection rethinkConnection() {
        return RethinkDB.r.connection().hostname("localhost").port(28015).connect();
    }

    @Bean
    public RethinkUtil rethinkUtil(@Qualifier("rethink") Connection connection) {
        return new RethinkUtil(connection);
    }

    @Bean
    public GitRepoRepository gitRepoRepository(RethinkUtil rUtil) {
        return new GitRepoRepositoryRethinkImpl(rUtil);
    }

    @Bean
    public CommitRepo commitRepo(RethinkUtil rUtil) {
        return new CommitRepoRethinkImpl(rUtil);
    }
}

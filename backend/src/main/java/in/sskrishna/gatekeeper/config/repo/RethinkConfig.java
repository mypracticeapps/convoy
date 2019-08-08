package in.sskrishna.gatekeeper.config.repo;


import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import in.sskrishna.gatekeeper.repository.api.CommitRepo;
import in.sskrishna.gatekeeper.repository.api.MyGitRepository;
import in.sskrishna.gatekeeper.repository.rethink.RethinkCommitRepo;
import in.sskrishna.gatekeeper.repository.rethink.RethinkMyGitRepository;
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
    public MyGitRepository gitRepoRepository(@Qualifier("rethink") Connection connection) {
        return new RethinkMyGitRepository(connection);
    }

    @Bean
    public CommitRepo commitRepo(@Qualifier("rethink") Connection connection) {
        return new RethinkCommitRepo(connection);
    }
}

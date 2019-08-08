package in.sskrishna.gatekeeper.repository.rethink;

import com.rethinkdb.net.Connection;
import in.sskrishna.gatekeeper.model.MyGit;
import in.sskrishna.gatekeeper.repository.api.MyGitRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
public class RethinkMyGitRepository implements MyGitRepository {

    private final RethinkUtil rUtil;

    public RethinkMyGitRepository(Connection connection) {
        this.rUtil = new RethinkUtil(connection);
    }

    @Override
    public <S extends MyGit> S save(S s) {
        return null;
    }

    @Override
    public <S extends MyGit> Iterable<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<MyGit> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public Iterable<MyGit> findAll() {
        return null;
    }

    @Override
    public Iterable<MyGit> findAllById(Iterable<String> iterable) {
        return null;
    }

    @Override
    public long count() {
        return 0;
    }

    @Override
    public void deleteById(String s) {

    }

    @Override
    public void delete(MyGit myGit) {

    }

    @Override
    public void deleteAll(Iterable<? extends MyGit> iterable) {

    }

    @Override
    public void deleteAll() {

    }
}

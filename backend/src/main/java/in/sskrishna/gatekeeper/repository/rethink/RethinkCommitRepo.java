package in.sskrishna.gatekeeper.repository.rethink;

import com.rethinkdb.net.Connection;
import in.sskrishna.gatekeeper.model.Commit;
import in.sskrishna.gatekeeper.repository.api.CommitRepo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.Optional;

@NoRepositoryBean
@Slf4j
public class RethinkCommitRepo implements CommitRepo {

    private final RethinkUtil rUtil;

    public RethinkCommitRepo(Connection connection) {
        this.rUtil = new RethinkUtil(connection);
    }

    @Override
    public void removeAllByRepoId(String repoId) {

    }

    @Override
    public <S extends Commit> S save(S s) {
        return null;
    }

    @Override
    public <S extends Commit> Iterable<S> saveAll(Iterable<S> iterable) {
        return null;
    }

    @Override
    public Optional<Commit> findById(String s) {
        return Optional.empty();
    }

    @Override
    public boolean existsById(String s) {
        return false;
    }

    @Override
    public Iterable<Commit> findAll() {
        return null;
    }

    @Override
    public Iterable<Commit> findAllById(Iterable<String> iterable) {
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
    public void delete(Commit commit) {

    }

    @Override
    public void deleteAll(Iterable<? extends Commit> iterable) {

    }

    @Override
    public void deleteAll() {

    }
}

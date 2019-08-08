package in.sskrishna.gatekeeper.repository.api;

import in.sskrishna.gatekeeper.model.Commit;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CommitRepo extends CrudRepository<Commit, String> {
    void removeAllByRepoId(String repoId);
}

package in.sskrishna.gatekeeper.repository.api;

import in.sskrishna.gatekeeper.model.Commit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface CommitRepo extends MongoRepository<Commit, String> {
    void removeAllByRepoId(String repoId);
}

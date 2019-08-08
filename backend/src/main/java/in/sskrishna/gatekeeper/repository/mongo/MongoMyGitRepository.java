package in.sskrishna.gatekeeper.repository.mongo;

import in.sskrishna.gatekeeper.model.MyGit;
import in.sskrishna.gatekeeper.repository.api.MyGitRepository;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface MongoMyGitRepository extends MyGitRepository, MongoRepository<MyGit, String> {
}

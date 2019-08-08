package in.sskrishna.gatekeeper.repository.api;

import in.sskrishna.gatekeeper.model.MyGit;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface MyGitRepository extends CrudRepository<MyGit, String> {

}

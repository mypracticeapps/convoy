package in.sskrishna.gatekeeper.repository.api;

import in.sskrishna.gatekeeper.model.GitRepo;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface GitRepoRepository extends MongoRepository<GitRepo, String> {

}

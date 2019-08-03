package in.sskrishna.gatekeeper.repository.api;

import in.sskrishna.gatekeeper.model.GitRepo;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ExpGitRepoRepository extends MongoRepository<GitRepo, String> {

}

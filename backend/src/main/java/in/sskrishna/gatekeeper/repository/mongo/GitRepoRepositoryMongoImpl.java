package in.sskrishna.gatekeeper.repository.mongo;

import in.sskrishna.gatekeeper.model.GitRepo;
import in.sskrishna.gatekeeper.repository.api.GitRepoRepository;
import org.springframework.data.mongodb.core.MongoTemplate;

public class GitRepoRepositoryMongoImpl extends MongoCrudRepo<String, GitRepo> implements GitRepoRepository {
    public GitRepoRepositoryMongoImpl(MongoTemplate template) {
        super(template, null, GitRepo.class);
    }
}
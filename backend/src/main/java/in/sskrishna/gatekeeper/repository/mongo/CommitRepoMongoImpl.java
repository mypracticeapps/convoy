package in.sskrishna.gatekeeper.repository.mongo;

import in.sskrishna.gatekeeper.model.Commit;
import in.sskrishna.gatekeeper.repository.api.CommitRepo;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

public class CommitRepoMongoImpl extends MongoCrudRepo<String, Commit> implements CommitRepo {
    private static final String TABLE_NAME = "commits";

    public CommitRepoMongoImpl(MongoTemplate template) {
        super(template, new String[]{"repoId"}, Commit.class);
    }

    @Override
    public void removeAllByRepoId(String id) {
        Query query = Query.query(Criteria.where("repoId").is(id));
        super.template.remove(query, Commit.class);
    }
}
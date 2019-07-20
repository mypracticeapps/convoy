package in.sskrishna.gatekeeper.repository.cache;

import in.sskrishna.gatekeeper.model.Commit;
import in.sskrishna.gatekeeper.repository.api.CommitRepo;

public class CommitCacheRepo extends CrudCacheRepo<String, Commit> implements CommitRepo {
}

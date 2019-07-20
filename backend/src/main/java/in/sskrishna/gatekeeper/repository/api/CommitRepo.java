package in.sskrishna.gatekeeper.repository.api;

import in.sskrishna.gatekeeper.model.Commit;

public interface CommitRepo extends CrudRepo<String, Commit> {
    public void removeAllByRepoId(String id);
}

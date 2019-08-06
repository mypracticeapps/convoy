//package in.sskrishna.gatekeeper.repository.cache;
//
//import in.sskrishna.gatekeeper.model.Commit;
//import in.sskrishna.gatekeeper.repository.api.CommitRepo;
//
//import java.util.Set;
//import java.util.stream.Collectors;
//
//public class CommitCacheRepo extends CrudCacheRepo<String, Commit> implements CommitRepo {
//    @Override
//    public void removeAllByRepoId(String id) {
//        Set<Commit> ids = this.findAll().stream().filter(commit -> commit.getRepoId().equals(id)).collect(Collectors.toSet());
//        ids.forEach(super::delete);
//    }
//}

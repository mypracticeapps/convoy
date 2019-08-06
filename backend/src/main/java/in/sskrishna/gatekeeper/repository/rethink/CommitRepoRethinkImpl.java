//package in.sskrishna.gatekeeper.repository.rethink;
//
//import com.rethinkdb.RethinkDB;
//import in.sskrishna.gatekeeper.model.Commit;
//import in.sskrishna.gatekeeper.repository.api.CommitRepo;
//
//public class CommitRepoRethinkImpl extends CrudRethinkRepo<String, Commit> implements CommitRepo {
//    private static final RethinkDB r = RethinkDB.r;
//    private final RethinkUtil rUtil;
//    private static final String TABLE_NAME = "commits";
//
//    public CommitRepoRethinkImpl(RethinkUtil rUtil) {
//        super(rUtil, TABLE_NAME, new String[]{"repoId"}, Commit.class);
//        this.rUtil = rUtil;
//    }
//
//    @Override
//    public void removeAllByRepoId(String id) {
//        rUtil.deleteAll(TABLE_NAME, r.hashMap("repoId", id));
//    }
//}

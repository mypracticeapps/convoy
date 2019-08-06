//package in.sskrishna.gatekeeper.repository.rethink;
//
//import com.rethinkdb.RethinkDB;
//import in.sskrishna.gatekeeper.model.GitRepo;
//import in.sskrishna.gatekeeper.repository.api.GitRepoRepository;
//
//public class GitRepoRepositoryRethinkImpl extends CrudRethinkRepo<String, GitRepo> implements GitRepoRepository {
//    private static final RethinkDB r = RethinkDB.r;
//    private final RethinkUtil rUtil;
//    private static final String TABLE_NAME = "git_repos";
//
//    public GitRepoRepositoryRethinkImpl(RethinkUtil rUtil) {
//        super(rUtil, TABLE_NAME, null, GitRepo.class);
//        this.rUtil = rUtil;
//    }
//}

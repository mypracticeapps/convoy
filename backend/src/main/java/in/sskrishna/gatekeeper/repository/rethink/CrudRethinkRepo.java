package in.sskrishna.gatekeeper.repository.rethink;

import com.google.gson.Gson;
import com.rethinkdb.RethinkDB;
import in.sskrishna.gatekeeper.model.Entity;
import in.sskrishna.gatekeeper.repository.api.CrudRepo;

import java.util.Collection;
import java.util.Set;

public abstract class CrudRethinkRepo<K extends String, V extends Entity> implements CrudRepo<K, V> {
    private static final Gson gson = new Gson();
    private static final RethinkDB r = RethinkDB.r;
    private final RethinkUtil rUtil;
    private final String TABLE_NAME;
    private final Class modelCls;

    public CrudRethinkRepo(RethinkUtil rUtil, String tableName, Class modelCls) {
        this.rUtil = rUtil;
        this.TABLE_NAME = tableName;
        this.modelCls = modelCls;
        this.rUtil.createTableIfNotExists(tableName);
    }

    public void save(V entity) {
        this.rUtil.save(TABLE_NAME, entity);
    }

    public void save(Collection<V> iterable) {
        iterable.forEach(this::save);
    }

    public void delete(V entity) {
        this.rUtil.delete(TABLE_NAME, entity.getId());
    }

    public Set<V> findAll() {
        return this.rUtil.findAll(TABLE_NAME, this.modelCls);
    }

    public V findOne(String id) {
        return (V) this.rUtil.findOne(TABLE_NAME, id, modelCls);
    }

    public long size() {
        return this.rUtil.count(TABLE_NAME);
    }
}

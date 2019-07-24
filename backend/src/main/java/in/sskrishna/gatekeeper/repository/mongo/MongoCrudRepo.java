package in.sskrishna.gatekeeper.repository.mongo;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import in.sskrishna.gatekeeper.model.Entity;
import in.sskrishna.gatekeeper.repository.api.CrudRepo;
import in.sskrishna.gatekeeper.util.StreamUtil;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class MongoCrudRepo<K extends String, V extends Entity> implements CrudRepo<K, V> {
    protected MongoTemplate template;
    private final Class modelCls;

    public MongoCrudRepo(MongoTemplate template, String[] indexes, Class modelCls) {
        this.template = template;
        this.modelCls = modelCls;
        MongoDatabase database = this.template.getDb();
        MongoCollection collection = database.getCollection("gitrepos");
    }

    @Override
    public void save(V entity) {
//        this.template.update(entity);
    }

    @Override
    public void save(Collection<V> iterable) {
        StreamUtil.chunked(iterable.stream(), 1000).parallelStream().forEach(list -> {
            this.template.insertAll(list);

        });
    }

    // TODO verify
    public void update(V entity) {
        this.template.remove(entity);
        this.template.save(entity);
    }

    @Override
    public V findOne(String id) {
        Query query = Query.query(Criteria.where("_id").is(id));
        V t = (V) this.template.findOne(query, this.modelCls);
        return t;
    }

    @Override
    public Set<V> findAll() {
        return this.findAllWith(0, 0);
    }

    protected Set<V> findAllWith(int offset, int limit) {
        Query query = new Query();
        query.skip(offset);
        query.limit(limit);
        Collection<V> infos = this.template.find(query, this.modelCls);
        if (infos == null) {
            infos = new HashSet<>();
        }
        return new HashSet<>(infos);
    }

    @Override
    public long size() {
        Query query = Query.query(Criteria.where("").is(""));
        return this.template.count(query, this.modelCls);
    }

    @Override
    public void delete(V entity) {
        Query query = Query.query(Criteria.where("_id").is(entity.getId()));
        this.template.remove(query, this.modelCls);
    }
}
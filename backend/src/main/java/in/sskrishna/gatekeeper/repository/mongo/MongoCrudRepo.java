package in.sskrishna.gatekeeper.repository.mongo;

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

    private MongoTemplate template;
    private final String COLLECTION_NAME;
    private final Class modelCls;

    public MongoCrudRepo(MongoTemplate template, String collectionName, String[] indexes, Class modelCls) {
        this.template = template;
        this.COLLECTION_NAME = collectionName;
        this.modelCls = modelCls;
    }

    @Override
    public void save(V entity) {
        this.template.insert(entity);
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

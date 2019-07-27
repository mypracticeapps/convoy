package in.sskrishna.gatekeeper.repository.mongo;

import in.sskrishna.gatekeeper.model.Entity;
import in.sskrishna.gatekeeper.repository.api.CrudRepo;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Collection;
import java.util.Set;

public class MongoCrudRepo<K extends String, V extends Entity> implements CrudRepo<K, V> {
    protected MongoTemplate template;
    protected MongoUtil mUtil;
    private final Class modelCls;

    public MongoCrudRepo(MongoTemplate template, String[] indexes, Class modelCls) {
        this.template = template;
        this.mUtil = new MongoUtil(template, modelCls, indexes);
        this.modelCls = modelCls;
    }

    @Override
    public void save(V entity) {
        this.mUtil.save(entity);
    }

    @Override
    public void save(Collection<V> iterable) {
        this.mUtil.saveAll(iterable);
    }

    @Override
    public V findOne(String id) {
        return (V) this.mUtil.findOne(id, this.modelCls);
    }

    @Override
    public Set<V> findAll() {
        return this.mUtil.findAll();
    }

    @Override
    public long size() {
        return mUtil.size();
    }

    @Override
    public void delete(V entity) {
        this.mUtil.delete(entity.getId());
    }
}

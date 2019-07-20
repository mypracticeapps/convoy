package in.sskrishna.gatekeeper.repository.api;

import in.sskrishna.gatekeeper.model.Entity;

import java.util.Collection;
import java.util.Set;

public interface CrudRepo<K extends String, V extends Entity> {
    void save(V entity);

    void save(Collection<V> iterable);

    void delete(V entity);

    Set<V> findAll();

    V findOne(String id);

    long size();
}

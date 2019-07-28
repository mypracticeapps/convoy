package in.sskrishna.gatekeeper.repository.mongo;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOneModel;
import com.mongodb.client.model.ReplaceOptions;
import in.sskrishna.gatekeeper.model.Entity;
import in.sskrishna.gatekeeper.util.StreamUtil;
import lombok.extern.slf4j.Slf4j;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
public class MongoUtil {
    private final MongoTemplate template;
    private final Class modelCls;
    private final String[] indexes;

    private final MongoCollection<Document> collection;
    private static final Gson gson = new Gson();

    public MongoUtil(MongoTemplate template, Class modelCls, String indexes[]) {
        this.template = template;
        this.modelCls = modelCls;
        this.indexes = indexes;
        this.collection = this.template.getDb().getCollection(modelCls.getName());
    }

    // TODO OPTIMIZE
    public void save(Entity entity) {
        this.saveAll(Arrays.asList(entity));
    }

    public void saveAll(Collection collection) {
        ZonedDateTime taskNow = ZonedDateTime.now();
        StreamUtil.chunked(collection.stream(), 1000).parallelStream().forEach(list -> {
            List l = (List) list;
            this.collection.bulkWrite(asBulkWrite(l));
        });
        log.trace("time taken to save bulk entities: {}", taskNow.until(ZonedDateTime.now(), ChronoUnit.SECONDS));
    }

    public void delete(String id) {
        Bson filter = Filters.eq("_id", id);
        this.collection.deleteOne(filter);
    }

    public void deleteAll(String[] ids) {
        Bson filter = Filters.in("_id", ids);
        this.collection.deleteMany(filter);
    }

    public void deleteAll() {
        BasicDBObject document = new BasicDBObject();
        this.collection.deleteMany(document);
    }

    public <T> T findOne(String id, Class<T> modelCls) {
        Bson filter = Filters.eq("_id", id);
        Iterator<Document> iterator = this.collection.find(filter).iterator();
        Set<T> set = map(iterator);
        if (set.isEmpty()) {
            return null;
        } else {
            return set.iterator().next();
        }
    }

    public <T> Set<T> findAll() {
        Iterator<Document> iterator = this.collection.find().iterator();
        return this.map(iterator);
    }

    private <T> Set<T> map(Iterator<Document> iterator) {
        Set<T> set = new HashSet<>();
        while (iterator.hasNext()) {
            Document document = iterator.next();
            String json = gson.toJson(document);
            T entity = (T) gson.fromJson(json, this.modelCls);
            set.add(entity);
        }
        return set;
    }

    public long size() {
        return this.collection.countDocuments();
    }

    private List<ReplaceOneModel<Document>> asBulkWrite(Collection collection) {
        List<ReplaceOneModel<Document>> documents = new LinkedList<>();
        for (Object item : collection) {
            Entity entity = (Entity) item;
            Bson filter = Filters.eq("_id", entity.getId());
            Document document = Document.parse(gson.toJson(entity));
            ReplaceOptions options = new ReplaceOptions().upsert(true).bypassDocumentValidation(true);
            ReplaceOneModel<Document> rom = new ReplaceOneModel<Document>(filter, document, options);
            documents.add(rom);
        }
        return documents;
    }
}
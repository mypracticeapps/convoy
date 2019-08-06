package in.sskrishna.gatekeeper.repository.rethink;

import com.google.gson.Gson;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;
import in.sskrishna.gatekeeper.util.StreamUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.*;

@Slf4j
public class RethinkUtil {
    private static final RethinkDB r = RethinkDB.r;
    private static final Gson gson = new Gson();
    private final Connection connection;

    public RethinkUtil(Connection connection) {
        this.connection = connection;
    }

    public void createTableIfNotExists(String tableName, String[] indexes) {
        ArrayList<String> arrayList = RethinkDB.r.tableList().run(connection);
        if (!arrayList.contains(tableName)) {
            RethinkDB.r.tableCreate(tableName).run(connection);
            if (indexes == null) return;
            for (String index : indexes) {
                RethinkDB.r.table(tableName).indexCreate(index).run(connection);
            }
        }
    }

    public void save(String tableName, Object obj) {
        String st = gson.toJson(obj);
        r.table(tableName)
                .insert(r.json(st))
                .optArg("conflict", "replace")
                .run(connection);
    }

    public void saveAll(String tableName, Collection objSet) {
        ZonedDateTime taskNow = ZonedDateTime.now();
        StreamUtil.chunked(objSet.stream(), 1000).parallelStream().forEach(list -> {
            this.save(tableName, list);
        });
        log.trace("time taken to save bulk entities: {}", taskNow.until(ZonedDateTime.now(), ChronoUnit.SECONDS));
    }

    public boolean contains(String tableName, String id) {
        Object obj = this.getOne(tableName, id);
        return obj != null ? true : false;
    }

    public void delete(String tableName, String id) {
        r.table(tableName)
                .filter(r.hashMap("id", id))
                .delete()
                .run(connection);
    }

    public void deleteAll(String tableName, Object filter) {
        r.table(tableName)
                .filter(filter)
                .delete()
                .run(connection);
    }

    public void deleteAll(String tableName) {
        r.table(tableName)
                .delete()
                .run(connection);
    }

    public <T> T findOne(String tableName, String id, Class<T> cls) {
        Object obj = getOne(tableName, id);
        T t = this.cast(obj, cls);
        return t;
    }

    public <T> Set<T> findAll(String tableName, Class<T> cls) {
        Cursor cursor = r.table(tableName)
                .run(connection);
        List list = cursor.toList();
        return this.cast(list, cls);
    }

    public long count(String tableName) {
        return r.table(tableName)
                .count()
                .run(connection);
    }

    private Object getOne(String tableName, String id) {
        Cursor cursor = r.table(tableName)
                .filter(r.hashMap("id", id))
                .run(connection);
        List list = cursor.toList();
        if (list.isEmpty()) {
            return null;
        } else {
            return list.get(0);
        }
    }

    private <T> T cast(Object obj, Class<T> cls) {
        String serial = gson.toJson(obj);
        T t = gson.fromJson(serial, cls);
        return t;
    }

    private <T> Set<T> cast(List objList, Class<T> cls) {
        Set<T> set = new HashSet<>();
        for (Object obj : objList) {
            set.add(this.cast(obj, cls));
        }
        return set;
    }
}

package in.sskrishna.gatekeeper.repository.rethink;

import com.google.gson.Gson;
import com.rethinkdb.RethinkDB;
import com.rethinkdb.net.Connection;
import com.rethinkdb.net.Cursor;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RethinkUtil {
    private static final RethinkDB r = RethinkDB.r;
    private static final Gson gson = new Gson();
    private final Connection connection;

    public RethinkUtil(@Qualifier("rethink") Connection connection) {
        this.connection = connection;
    }

    public void createTableIfNotExists(String tableName) {
        ArrayList<String> arrayList = RethinkDB.r.tableList().run(connection);
        if (!arrayList.contains(tableName)) {
            RethinkDB.r.tableCreate(tableName).run(connection);
        }
    }

    public void save(String tableName, Object obj) {
        String st = gson.toJson(obj);
        r.table(tableName)
                .insert(r.json(st))
                .optArg("conflict", "replace")
                .run(connection);
    }

    public void saveAll(String tableName, Set objSet) {
        String st = gson.toJson(objSet);
        r.table(tableName)
                .insert(r.json(st))
                .optArg("conflict", "replace")
                .run(connection);
    }

    public void saveAll(String tableName, List objList) {
        String st = gson.toJson(objList);
        r.table(tableName)
                .insert(r.json(st))
                .optArg("conflict", "replace")
                .run(connection);
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

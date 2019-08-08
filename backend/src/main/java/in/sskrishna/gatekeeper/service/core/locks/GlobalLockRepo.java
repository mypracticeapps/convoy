package in.sskrishna.gatekeeper.service.core.locks;

import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public final class GlobalLockRepo {
    private static Set<String> locks = Collections.newSetFromMap(new ConcurrentHashMap<String, Boolean>());

    public static synchronized boolean lock(Object... keys) {
        if (GlobalLockRepo.isLocked(keys)) {
            return false;
        }
        String key = buildKey(keys);
        log.info("locking key: " + key);
        locks.add(key);
        return true;
    }

    public static synchronized void unlock(Object... keys) {
        String key = buildKey(keys);
        log.info("unlocking key: " + key);
        locks.remove(key);
    }

    public static synchronized boolean isLocked(Object... keys) {
        String key = buildKey(keys);
        return locks.contains(key);
    }

    public static Set<String> getAllLockedKeys() {
        return Collections.unmodifiableSet(locks);
    }

    private static String buildKey(Object... keys) {
        String key = "";
        for (Object s : keys) {
            key += s + ":";
        }
        key = key.substring(0, key.length() - 1);
        return key;
    }


    public static class KEYS {
        public static String SERVER_BOOTING = "SERVER_BOOTING";
        public static String REPO_REFRESH = "REPO_REFRESH";
    }
}

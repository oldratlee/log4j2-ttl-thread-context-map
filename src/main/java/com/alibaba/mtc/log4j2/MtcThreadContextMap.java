package com.alibaba.mtc.log4j2;

import com.alibaba.mtc.MtContextThreadLocal;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.ThreadContext;
import org.apache.logging.log4j.spi.DefaultThreadContextMap;
import org.apache.logging.log4j.spi.ThreadContextMap;

/**
 * @author Jerry Lee (oldratlee at gmail dot com)
 * @see DefaultThreadContextMap
 */
public class MtcThreadContextMap implements ThreadContextMap {
    private final ThreadLocal<Map<String, String>> localMap;

    public MtcThreadContextMap() {
        this.localMap = createThreadLocalMap();
    }

    static ThreadLocal<Map<String, String>> createThreadLocalMap() {
        return new MtContextThreadLocal<Map<String, String>>() {
            @Override
            protected void beforeExecute() {
                final Map<String, String> log4j2Context = get();
                for (Map.Entry<String, String> entry : log4j2Context.entrySet()) {
                    ThreadContext.put(entry.getKey(), entry.getValue());
                }
            }

            @Override
            protected void afterExecute() {
                ThreadContext.clearAll();
            }

            @Override
            protected Map<String, String> initialValue() {
                return new HashMap<String, String>();
            }
        };
    }

    @Override
    public void put(final String key, final String value) {
        Map<String, String> map = localMap.get();
        map = map == null ? new HashMap<String, String>() : new HashMap<String, String>(map);
        map.put(key, value);
        localMap.set(Collections.unmodifiableMap(map));
    }

    @Override
    public String get(final String key) {
        final Map<String, String> map = localMap.get();
        return map == null ? null : map.get(key);
    }

    @Override
    public void remove(final String key) {
        final Map<String, String> map = localMap.get();
        if (map != null) {
            final Map<String, String> copy = new HashMap<String, String>(map);
            copy.remove(key);
            localMap.set(Collections.unmodifiableMap(copy));
        }
    }

    @Override
    public void clear() {
        localMap.remove();
    }

    @Override
    public boolean containsKey(final String key) {
        final Map<String, String> map = localMap.get();
        return map != null && map.containsKey(key);
    }

    @Override
    public Map<String, String> getCopy() {
        final Map<String, String> map = localMap.get();
        return map == null ? new HashMap<String, String>() : new HashMap<String, String>(map);
    }

    @Override
    public Map<String, String> getImmutableMapOrNull() {
        return localMap.get();
    }

    @Override
    public boolean isEmpty() {
        final Map<String, String> map = localMap.get();
        return map == null || map.size() == 0;
    }

    @Override
    public String toString() {
        final Map<String, String> map = localMap.get();
        return map == null ? "{}" : map.toString();
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        final Map<String, String> map = this.localMap.get();
        result = prime * result + ((map == null) ? 0 : map.hashCode());
        result = prime * result;
        return result;
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof ThreadContextMap)) {
            return false;
        }
        final ThreadContextMap other = (ThreadContextMap) obj;
        final Map<String, String> map = this.localMap.get();
        final Map<String, String> otherMap = other.getImmutableMapOrNull();
        if (map == null) {
            if (otherMap != null) {
                return false;
            }
        } else if (!map.equals(otherMap)) {
            return false;
        }
        return true;
    }
}

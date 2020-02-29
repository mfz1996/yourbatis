package com.mfz.yourbatis.cache;

import org.apache.ibatis.cache.Cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

public class DefaultCache implements Cache {

    private String id;
    private Map<Object,Object> baseCache= new HashMap<Object, Object>();

    public DefaultCache(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public void putObject(Object key, Object value) {
        baseCache.put(key,value);
    }

    @Override
    public Object getObject(Object key) {
        return baseCache.get(key);
    }

    @Override
    public Object removeObject(Object key) {
        return baseCache.remove(key);
    }

    @Override
    public void clear() {
        baseCache.clear();
    }

    @Override
    public int getSize() {
        return baseCache.size();
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null ||!(o instanceof Cache)) return false;
        Cache that = (Cache) o;
        return id.equals(that.getId());
    }

    @Override
    public int hashCode() {
        return getId().hashCode();
    }
}

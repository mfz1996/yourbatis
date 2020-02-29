package com.mfz.yourbatis.cache;

import org.apache.ibatis.cache.Cache;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.locks.ReadWriteLock;

public class LruCache implements Cache {

    private Cache delegate;
    private Map<Object, Object> keyMap;
    private Object eldestKey;
    private String id;

    public LruCache(String id,Cache delegate) {
        this.id = id;
        this.delegate = delegate;
        initMap(1024);
    }

    public LruCache(String id) {
        this.id = id;
        this.delegate = new DefaultCache("LocalCache");
        initMap(1024);
    }

    private void initMap(int size){
        keyMap = new LinkedHashMap<Object, Object>(size, .75F, true){
            @Override
            protected boolean removeEldestEntry(Map.Entry<Object, Object> eldest) {
                boolean tooBig = size() > size;
                if (tooBig){
                    eldestKey = eldest.getKey();
                }
                return tooBig;
            }
        };
    }

    @Override
    public String getId() {
        return delegate.getId();
    }

    @Override
    public void putObject(Object key, Object value) {
        delegate.putObject(key,value);
        keyMap.put(key,value);
        if (eldestKey != null){
            delegate.removeObject(key);
            eldestKey = null;
        }
    }

    @Override
    public Object getObject(Object key) {
        keyMap.get(key);
        return delegate.getObject(key);
    }

    @Override
    public Object removeObject(Object key) {
        keyMap.remove(key);
        return delegate.removeObject(key);
    }

    @Override
    public void clear() {
        delegate.clear();
        keyMap.clear();
    }

    @Override
    public int getSize() {
        return 0;
    }

    @Override
    public ReadWriteLock getReadWriteLock() {
        return null;
    }
}

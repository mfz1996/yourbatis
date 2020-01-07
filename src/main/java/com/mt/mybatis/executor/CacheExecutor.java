package com.mt.mybatis.executor;

import com.mt.mybatis.cache.CacheKey;
import com.mt.mybatis.cache.DefaultCache;
import com.mt.mybatis.config.Configuration;
import com.mt.mybatis.mapper.MyMappedStatement;
import com.mt.mybatis.resulthandler.MyResultHandler;
import org.apache.ibatis.cache.Cache;
import org.junit.Assert;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class CacheExecutor implements Executor {

    private Configuration configuration;
    private Executor wrapper;

    public CacheExecutor(Configuration configuration, Executor wrapper) {
        this.configuration = configuration;
        this.wrapper = wrapper;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <E> List<E> query(MyMappedStatement ms, Object parameter, MyResultHandler resultHandler) throws SQLException {
        CacheKey cacheKey = new CacheKey(ms.getMapperName(),ms.getSql(),parameter);
        Cache cache = ms.getSecondCache();
        if (cache.getObject(cacheKey) != null){
            System.out.print("get result from cache:");
            return (List<E>) cache.getObject(cacheKey);
        }
        List<E> results = wrapper.query(ms,parameter,resultHandler);
        if (!results.isEmpty()) cache.putObject(cacheKey,results);
        return results;
    }

    @Override
    public int update(MyMappedStatement ms, Object parameter) throws SQLException {
        ms.getSecondCache().clear();
        return wrapper.update(ms,parameter);
    }

    @Override
    public void close() {
        wrapper.close();
    }

    @Override
    public boolean isClosed() {
        return wrapper.isClosed();
    }

    @Override
    public void setExecutorWrapper(Executor wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return wrapper.getConnection();
    }
}

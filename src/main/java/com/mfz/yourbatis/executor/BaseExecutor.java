package com.mfz.yourbatis.executor;

import com.mfz.yourbatis.mapper.MyMappedStatement;
import com.mfz.yourbatis.cache.DefaultCache;
import com.mfz.yourbatis.config.Configuration;
import com.mfz.yourbatis.resulthandler.MyResultHandler;
import com.mfz.yourbatis.transaction.Transaction;
import org.apache.ibatis.cache.Cache;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public abstract class BaseExecutor implements Executor{

    private Configuration configuration;
    private Executor wrapper;
    private Transaction transaction;
    private boolean closed;
    protected Cache localCache;

    protected BaseExecutor(Configuration configuration, Transaction tx) {
        this.configuration = configuration;
        this.transaction = tx;
        this.closed = false;
        this.wrapper = this;
        localCache = new DefaultCache("LocalCache");
    }

    @Override
    public <E> List<E> query(MyMappedStatement ms, Object parameter, MyResultHandler resultHandler) throws SQLException {
        return doQuery(ms, parameter, resultHandler);
    }

    @Override
    public int update(MyMappedStatement ms, Object parameter) throws SQLException {
        return doUpdate(ms,parameter);
    }

    @Override
    public void close() {
        this.localCache.clear();
        try {
            transaction.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isClosed() {
        return closed;
    }

    @Override
    public void setExecutorWrapper(Executor wrapper) {
        this.wrapper = wrapper;
    }

    @Override
    public Connection getConnection() throws SQLException {
        return transaction.getConnection();
    }

    protected abstract <E> List<E> doQuery(MyMappedStatement ms, Object parameter, MyResultHandler resultHandler)
            throws SQLException;

    protected abstract int doUpdate(MyMappedStatement ms, Object parameter)
            throws SQLException;
}

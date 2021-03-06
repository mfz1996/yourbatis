package com.mfz.yourbatis.session.defaults;

import com.mfz.yourbatis.config.Configuration;
import com.mfz.yourbatis.executor.Executor;
import com.mfz.yourbatis.mapper.MyMappedStatement;
import com.mfz.yourbatis.resulthandler.MyResultHandler;
import com.mfz.yourbatis.session.SqlSession;
import com.mfz.yourbatis.mapper.MyMapperProxy;


import java.io.IOException;
import java.lang.reflect.Proxy;
import java.sql.SQLException;
import java.util.List;

public class DefaultSqlSession implements SqlSession {

    private Configuration configuration;

    private Executor executor;

    private MyMapperProxy mapperProxy;

    public DefaultSqlSession(Configuration configuration, Executor executor) {
        this.configuration = configuration;
        this.executor = executor;
    }

    @Override
    public int update(MyMappedStatement statement) throws SQLException {
        return update(statement,null);
    }

    @Override
    public int update(MyMappedStatement statement, Object parameter) throws SQLException {
        return executor.update(statement, parameter);
    }

    @Override
    public <E> List<E> selectList(MyMappedStatement statement) {
        try {
            MyResultHandler resultHandler = new MyResultHandler(executor,configuration,statement);
            return executor.query(statement,null,resultHandler);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <E> List<E> selectList(MyMappedStatement statement, Object parameter) {
        try {
            // TODO 2020/1/3 resultHandler注册中心为空
            MyResultHandler resultHandler = new MyResultHandler(executor,configuration,statement);
            return executor.query(statement,parameter,resultHandler);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public <T> T getMapper(Class<T> clazz) {
        return (T) Proxy.newProxyInstance(this.getClass().getClassLoader(),new Class[]{clazz},new MyMapperProxy(this,clazz));
    }

    @Override
    public Configuration getConfiguration() {
        return configuration;
    }

    @Override
    public void close() throws IOException {
        executor.close();
    }
}

package com.mt.mybatis.executor;

import com.mt.mybatis.mapper.MyMappedStatement;
import com.mt.mybatis.resulthandler.MyResultHandler;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface Executor {

    <E> List<E> query(MyMappedStatement ms, Object parameter, MyResultHandler resultHandler) throws SQLException;

    int update(MyMappedStatement ms, Object parameter) throws SQLException;

    void close();

    boolean isClosed();

    void setExecutorWrapper(Executor wrapper);

    Connection getConnection() throws SQLException;
}

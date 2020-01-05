package com.mt.mybatis.session;

import com.mt.mybatis.config.Configuration;
import com.mt.mybatis.mapper.MyMappedStatement;

import java.io.Closeable;
import java.sql.SQLException;
import java.util.List;

public interface SqlSession extends Closeable {

    int update(MyMappedStatement statement) throws SQLException;

    int update(MyMappedStatement statement, Object parameter) throws SQLException;

    <E> List<E> selectList(MyMappedStatement statement);

    <E> List<E> selectList(MyMappedStatement statement, Object parameter);

    <T> T getMapper(Class<T> clazz);

    Configuration getConfiguration();
}

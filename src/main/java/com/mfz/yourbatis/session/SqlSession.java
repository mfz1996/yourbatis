package com.mfz.yourbatis.session;

import com.mfz.yourbatis.mapper.MyMappedStatement;
import com.mfz.yourbatis.config.Configuration;

import java.io.Closeable;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface SqlSession extends Closeable {

    int update(MyMappedStatement statement) throws SQLException;

    int update(MyMappedStatement statement, Object parameter) throws SQLException;

    <E> List<E> selectList(MyMappedStatement statement);

    <E> List<E> selectList(MyMappedStatement statement, Object parameter);

    <T> T getMapper(Class<T> clazz);

    Configuration getConfiguration();

    @Override
    void close() throws IOException;
}

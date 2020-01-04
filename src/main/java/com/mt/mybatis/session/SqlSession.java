package com.mt.mybatis.session;

import com.mt.mybatis.config.Configuration;
import com.mt.mybatis.mapper.MyMappedStatement;

import java.io.Closeable;
import java.util.List;

public interface SqlSession extends Closeable {

    <T> T selectOne(MyMappedStatement statement);

    <T> T selectOne(MyMappedStatement statement, Object parameter);

    <E> List<E> selectList(MyMappedStatement statement);

    <E> List<E> selectList(MyMappedStatement statement, Object parameter);

    <T> T getMapper(Class<T> clazz);

    Configuration getConfiguration();
}

package com.mt.mybatis.mapper;

import com.mt.mybatis.config.Configuration;
import com.mt.mybatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;

public class MyMapperProxy<T> implements InvocationHandler {

    private SqlSession sqlSession;
    private Class<T> clazz;

    public MyMapperProxy(SqlSession sqlSession, Class<T> clazz) {
        this.clazz = clazz;
        this.sqlSession = sqlSession;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        Configuration configuration = sqlSession.getConfiguration();
        String mapperKey = clazz.getName() + "." + method.getName();
        MyMappedStatement mappedStatement = configuration.getMappedStatementMap().get(mapperKey);
        if (mappedStatement != null) {
            try {
                Class<?> resultClass = configuration.getReturnTypeMapping().get(mapperKey);
                if (resultClass != null) {
                    if (args.length == 0) {
                        return sqlSession.selectList(mappedStatement);
                    } else {
                        List<Object> list =  sqlSession.selectList(mappedStatement, args);
                        if (resultClass.isAssignableFrom(Collection.class)){
                            return list;
                        }
                        return list.get(0);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}

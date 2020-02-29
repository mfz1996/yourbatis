package com.mfz.yourbatis.mapper;

import com.mfz.yourbatis.config.Configuration;
import com.mfz.yourbatis.session.SqlSession;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

public class MyMapperProxy<T> implements InvocationHandler {

    private SqlSession sqlSession;
    private Class<T> clazz;

    public
    MyMapperProxy(SqlSession sqlSession, Class<T> clazz) {
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
                String sqlType = mappedStatement.getSql().trim().split(" ")[0].toUpperCase();
                Boolean isQuery;
                if (sqlType.equals( "SELECT")){
                    isQuery = true;
                }else {
                    isQuery = false;
                }
                    if (args == null) {
                        return isQuery?sqlSession.selectList(mappedStatement):sqlSession.update(mappedStatement);
                    } else {
                        if (isQuery) {
                            List<Object> list = sqlSession.selectList(mappedStatement, args);
                            if (list.isEmpty()) return null;
                            if (resultClass.isAssignableFrom(Collection.class)) {
                                return list;
                            }
                            return list.get(0);
                        }else {
                            return sqlSession.update(mappedStatement,args);
                        }
                    }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            method.invoke(proxy,args);
        }
        return null;
    }
}

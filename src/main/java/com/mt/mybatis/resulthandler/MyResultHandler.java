package com.mt.mybatis.resulthandler;

import com.mt.mybatis.config.Configuration;
import com.mt.mybatis.executor.Executor;
import com.mt.mybatis.mapper.MyMappedStatement;
import com.mt.mybatis.typehandler.MyTypeHandler;
import org.apache.ibatis.reflection.factory.DefaultObjectFactory;
import org.apache.ibatis.type.JdbcType;
import org.apache.ibatis.type.TypeHandler;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.sql.JDBCType;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MyResultHandler<E> {

    private Executor executor;
    private Configuration configuration;
    private MyMappedStatement ms;
    private List<Object> list = new ArrayList<>();

    public MyResultHandler(Executor executor, Configuration configuration, MyMappedStatement ms) {
        this.executor = executor;
        this.configuration = configuration;
        this.ms = ms;
    }

    public List<Object> handle(ResultSet rs){
        try {
            String mapperName = ms.getMapperName();
            Class<?> type = configuration.getResultHandlerRegistry().get(mapperName);
            if (type == null){
                type = configuration.getReturnTypeMapping().get(mapperName);
            }
            ResultSetMetaData metaData = rs.getMetaData();
            MyTypeHandler typeHandler = configuration.getTypeHandlerRegistry().getTypeHandler(type);
            while (rs.next()) {
                Object resultObject = null;
                if (typeHandler != null){
                    // 处理基本类型结果集
                    resultObject = typeHandler.handle(rs, metaData.getColumnName(1));
                }else {
                    resultObject = new DefaultObjectFactory().create(type);
                    for (Field field : resultObject.getClass().getDeclaredFields()) {
                        Method method = type.getMethod("set"+upperCapital(field.getName()),field.getType());
                        method.invoke(resultObject,parseColumnType(field,rs));
                        // TODO 2019/12/28 将resultset返回行封装成对象操作
                        // 2020/1/3 开始完成上个TODO
                    }
                }
                list.add((E)resultObject);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
        return list;
    }

    private String upperCapital(String lowCap){
        if (lowCap == null) return null;
        if (lowCap.length()==1) return lowCap.toUpperCase();
        return lowCap.substring(0, 1).toUpperCase() + lowCap.substring(1);
    }

    private Object parseColumnType(Field field,ResultSet resultSet) throws SQLException {
        Class<?> type = field.getType();
        String columnName = field.getName();
        MyTypeHandler typeHandler = configuration.getTypeHandlerRegistry().getTypeHandler(type);
        if (typeHandler != null){
            return typeHandler.handle(resultSet,columnName);
        }
        return null;
    }


}

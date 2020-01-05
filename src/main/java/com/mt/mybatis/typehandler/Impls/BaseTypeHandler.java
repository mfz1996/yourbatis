package com.mt.mybatis.typehandler.Impls;

import com.mt.mybatis.typehandler.MyTypeHandler;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public abstract class BaseTypeHandler<T> implements MyTypeHandler<T> {

    @Override
    public T handle(ResultSet resultSet, String columnName) throws SQLException {
        T result = null;
        try{
            result = getNullableResult(resultSet,columnName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        if (resultSet.wasNull()) {
            return null;
        } else {
            return result;
        }
    }

    public abstract T getNullableResult(ResultSet rs, String columnName) throws SQLException;

    public abstract T getNullableResult(ResultSet rs, int columnIndex) throws SQLException;

}

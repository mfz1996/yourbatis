package com.mfz.yourbatis.typehandler.Impls;

import com.mfz.yourbatis.typehandler.MyTypeHandler;

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

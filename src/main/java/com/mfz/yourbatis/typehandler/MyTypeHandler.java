package com.mfz.yourbatis.typehandler;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;

public interface MyTypeHandler<T> {

    T handle(ResultSet resultSet,String columnName) throws SQLException;
}

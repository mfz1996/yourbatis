package com.mt.mybatis.typehandler.Impls;

import java.sql.ResultSet;
import java.sql.SQLException;

public class StringTypeHandler extends BaseTypeHandler<String>{
    @Override
    public String getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return rs.getString(columnName);
    }

    @Override
    public String getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return rs.getString(columnIndex);
    }
}

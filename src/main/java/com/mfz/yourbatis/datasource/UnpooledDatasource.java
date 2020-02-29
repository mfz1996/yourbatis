package com.mfz.yourbatis.datasource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class UnpooledDatasource extends MyDataSource {

    private Connection conn;

    public UnpooledDatasource(String url, String driver, String userName, String passWord) {
        super(url, driver, userName, passWord);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return DriverManager.getConnection(getUrl(),getUserName(),getPassWord());
    }
}

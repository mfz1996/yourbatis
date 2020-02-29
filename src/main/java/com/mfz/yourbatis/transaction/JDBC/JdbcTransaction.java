package com.mfz.yourbatis.transaction.JDBC;

import com.mfz.yourbatis.datasource.MyDataSource;
import com.mfz.yourbatis.transaction.Transaction;

import java.sql.Connection;
import java.sql.SQLException;

public class JdbcTransaction implements Transaction {

    private Connection connection;

    private MyDataSource dataSource;

    private boolean autoCommit;

    public JdbcTransaction(Connection connection, MyDataSource dataSource, boolean autoCommit) {
        this.connection = connection;
        this.dataSource = dataSource;
        this.autoCommit = autoCommit;
    }

    public JdbcTransaction(Connection connection) {
        this.connection = connection;
    }

    public Connection getConnection() throws SQLException {
        return connection;
    }

    public void commit() throws SQLException {

    }

    public void rollback() throws SQLException {

    }

    public void close() throws SQLException {
        this.connection.close();
    }
}

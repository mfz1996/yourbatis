package com.mt.mybatis.session.defaults;

import com.mt.mybatis.config.Configuration;
import com.mt.mybatis.datasource.MyDataSource;
import com.mt.mybatis.executor.Executor;
import com.mt.mybatis.session.SqlSession;
import com.mt.mybatis.session.SqlSessionFactory;
import com.mt.mybatis.transaction.JDBC.JdbcTransaction;
import com.mt.mybatis.transaction.Transaction;

import java.sql.Connection;
import java.sql.SQLException;

public class DefaultSqlSessionFactory implements SqlSessionFactory {

    private Configuration configuration;

    public DefaultSqlSessionFactory(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public SqlSession openSession(Configuration configuration) {
        return openSessionFromDataSource(configuration);
    }

    @Override
    public SqlSession openSession(){
        return openSession(configuration);
    }

    private SqlSession openSessionFromDataSource(Configuration configuration){
        Connection conn = null;
        Transaction tx = null;
        try{
            MyDataSource dataSource = configuration.getDataSource();
            conn = dataSource.getConnection();
            tx = new JdbcTransaction(conn,dataSource,true);
            Executor executor = configuration.newExecutor(tx);
            return new DefaultSqlSession(configuration,executor);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}

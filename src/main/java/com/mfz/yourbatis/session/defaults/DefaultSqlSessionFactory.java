package com.mfz.yourbatis.session.defaults;

import com.mfz.yourbatis.executor.Executor;
import com.mfz.yourbatis.session.SqlSession;
import com.mfz.yourbatis.transaction.JDBC.JdbcTransaction;
import com.mfz.yourbatis.config.Configuration;
import com.mfz.yourbatis.datasource.MyDataSource;
import com.mfz.yourbatis.session.SqlSessionFactory;
import com.mfz.yourbatis.transaction.Transaction;

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

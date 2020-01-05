package com.mt.mybatis.executor;

import com.mt.mybatis.config.Configuration;
import com.mt.mybatis.exception.UnmatchParameterException;
import com.mt.mybatis.mapper.MyMappedStatement;
import com.mt.mybatis.resulthandler.MyResultHandler;
import com.mt.mybatis.resulthandler.MyStatementHandler;
import com.mt.mybatis.transaction.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class SimpleExecutor extends BaseExecutor {
    public SimpleExecutor(Configuration configuration, Transaction tx) {
        super(configuration, tx);
    }

    @Override
    protected <E> List<E> doQuery(MyMappedStatement ms, Object parameter, MyResultHandler resultHandler) throws SQLException {
        MyStatementHandler handler = ms.getConfiguration().newStatementHandler(ms,parameter);
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            // handle()→sql语句赋值
            statement = handler.handle();
            statement.execute();
            rs = statement.getResultSet();
            return resultHandler.<E> handle(rs);
        } catch (UnmatchParameterException e) {
            e.printStackTrace();
        }finally {
            Connection conn = statement.getConnection();
            if (rs!=null) rs.close();
            if (statement!=null) {
                statement.close();
            }
            conn.close();
        }
        return null;
    }

    @Override
    protected int doUpdate(MyMappedStatement ms, Object parameter) throws SQLException {
        MyStatementHandler handler = ms.getConfiguration().newStatementHandler(ms,parameter);
        PreparedStatement statement = null;
        ResultSet rs = null;
        try {
            // handle()→sql语句赋值
            statement = handler.handle();
            statement.execute();
            return statement.getUpdateCount();
        } catch (UnmatchParameterException e) {
            e.printStackTrace();
        }finally {
            Connection conn = statement.getConnection();
            if (statement!=null) {
                statement.close();
            }
            conn.close();
        }
        return 0;
    }
}

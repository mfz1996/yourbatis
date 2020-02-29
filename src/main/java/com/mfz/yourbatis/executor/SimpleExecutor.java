package com.mfz.yourbatis.executor;

import com.mfz.yourbatis.mapper.MyMappedStatement;
import com.mfz.yourbatis.resulthandler.MyResultHandler;
import com.mfz.yourbatis.config.Configuration;
import com.mfz.yourbatis.exception.UnmatchParameterException;
import com.mfz.yourbatis.resulthandler.MyStatementHandler;
import com.mfz.yourbatis.transaction.Transaction;

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
        MyStatementHandler handler = ms.getConfiguration().newStatementHandler(ms,parameter,this);
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
        }
        return null;
    }

    @Override
    protected int doUpdate(MyMappedStatement ms, Object parameter) throws SQLException {
        MyStatementHandler handler = ms.getConfiguration().newStatementHandler(ms,parameter,this);
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
        }
        return 0;
    }
}

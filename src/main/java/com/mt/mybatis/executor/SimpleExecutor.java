package com.mt.mybatis.executor;

import com.mt.mybatis.config.Configuration;
import com.mt.mybatis.exception.UnmatchParameterException;
import com.mt.mybatis.mapper.MyMappedStatement;
import com.mt.mybatis.resulthandler.MyResultHandler;
import com.mt.mybatis.resulthandler.MyStatementHandler;
import com.mt.mybatis.transaction.Transaction;

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
            if (statement!=null) statement.close();
            if (rs!=null) rs.close();
        }
        // TODO 12/27 22:37 添加resultsethandle过程
        return null;
    }
}

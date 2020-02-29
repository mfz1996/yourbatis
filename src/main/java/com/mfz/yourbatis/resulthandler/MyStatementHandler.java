package com.mfz.yourbatis.resulthandler;

import com.mfz.yourbatis.executor.Executor;
import com.mfz.yourbatis.mapper.MyMappedStatement;
import com.mfz.yourbatis.config.Configuration;
import com.mfz.yourbatis.exception.UnmatchParameterException;

import java.sql.*;

/**
 * StatementHandler负责创建Statement对象并对statement进行参数填充
 */
public class MyStatementHandler {

    private MyMappedStatement ms;
    private Object[] parameters;
    private Configuration configuration;
    private Executor executor;

    public MyStatementHandler(MyMappedStatement ms, Object parameters,Configuration configuration,Executor executor) {
        this.ms=ms;
        this.parameters = (Object[]) parameters;
        this.configuration = configuration;
        this.executor = executor;
    }
    // 获取sql→设置参数→返回Statement
    public PreparedStatement handle() throws UnmatchParameterException {
        String rawSql = ms.getSql();
        Connection conn = null;
        PreparedStatement psmt = null;
        if (parameters == null) {
            try {
                return getConnection().prepareStatement(rawSql);
            } catch (SQLException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                return conn.prepareStatement(rawSql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        try{
            conn = getConnection();
            if (rawSql.split("\\?").length != parameters.length+1){
                throw new UnmatchParameterException("Sql unmatch with amount of parameter");
            }
            psmt = conn.prepareStatement(rawSql);
            for (int i = 0;i<parameters.length;i++) {
                if(parameters[i] instanceof Integer){
                    psmt.setInt(i+1,(Integer)parameters[i]);
                }else if(parameters[i] instanceof Long){
                    psmt.setLong(i+1,(Long)parameters[i]);
                }else if(parameters[i] instanceof String){
                    psmt.setString(i+1,String.valueOf(parameters[i]));
                }else if(parameters[i] instanceof Boolean){
                    psmt.setBoolean(i+1,(Boolean)parameters[i]);
                }else{
                    psmt.setString(i+1,String.valueOf(parameters[i]));
                }
            }
            return psmt;
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Connection getConnection() throws SQLException, ClassNotFoundException {
        return executor.getConnection();
    }

}

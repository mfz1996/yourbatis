package com.mfz.yourbatis.datasource;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PoolState {
    private PooledDatasource dataSource;
    public final List<PooledConnection> idleConnections = new ArrayList<>();
    public final List<PooledConnection> activeConnections = new ArrayList<>();
    public PoolState(PooledDatasource dataSource){
        this.dataSource = dataSource;
        try {
            for (int i=0;i <PooledDatasource.poolMaximumIdleConnections;i++){
                idleConnections.add(new PooledConnection(dataSource, DriverManager.getConnection(dataSource.getUrl(),dataSource.getUserName(),dataSource.getPassWord())));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    /**
     * 最大空闲连接数
     * @return
     */
    public synchronized int getIdleConnectionCount() {
        return idleConnections.size();
    }

    /**
     * 最大活动连接数
     * @return
     */
    public synchronized int getActiveConnectionCount() {
        return activeConnections.size();
    }

}

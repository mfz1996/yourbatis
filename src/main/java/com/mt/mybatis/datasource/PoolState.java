package com.mt.mybatis.datasource;

import java.util.ArrayList;
import java.util.List;

public class PoolState {
    private MyDataSource dataSource;
    public final List<PooledConnection> idleConnections = new ArrayList<>();
    public final List<PooledConnection> activeConnections = new ArrayList<>();
    public PoolState(MyDataSource dataSource){
        this.dataSource = dataSource;
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

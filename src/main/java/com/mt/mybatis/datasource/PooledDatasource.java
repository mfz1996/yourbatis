package com.mt.mybatis.datasource;

import java.sql.Connection;
import java.sql.SQLException;

public class PooledDatasource extends MyDataSource {

    private UnpooledDatasource datasource;
    private PoolState state = new PoolState(this);
    protected int poolMaximumActiveConnections = 10;//默认最大活动连接是10
    protected int poolMaximumIdleConnections = 5;//默认最大空闲连接是5
    protected int poolMaximumCheckoutTime = 20000;
    protected int poolTimeToWait = 20000;

    public PooledDatasource(String url, String driver, String userName, String passWord) {
        super(url, driver, userName, passWord);
        this.datasource = new UnpooledDatasource(url, driver, userName, passWord);
    }

    @Override
    public Connection getConnection() throws SQLException {
        return popConnection(getUserName(),getPassWord()).getProxyConnection();
    }

    public PooledConnection popConnection(String userName, String password) throws SQLException {
        PooledConnection conn = null;
        Boolean countedWait = false;
        while (conn == null){
            synchronized (state){
                if (!state.idleConnections.isEmpty()){
                    // 连接池有空闲连接,取出idle第一个，放在active队尾
                    conn = state.idleConnections.remove(0);
                    state.activeConnections.add(conn);
                }else {
                    // 连接池没有空闲连接
                    if (state.getActiveConnectionCount()<poolMaximumActiveConnections){
                        // active没到最大数
                        conn = new PooledConnection(this,datasource.getConnection());
                        state.activeConnections.add(conn);
                    }else {
                        // active到了最大数并且没有空闲连接
                        PooledConnection firstActiveConn = state.activeConnections.get(0);
                        Long time2Wait = firstActiveConn.getInvalidTimestamp() - System.currentTimeMillis();
                        if (time2Wait <= 0){
                            // active队列最先进入的连接已过期
                            state.activeConnections.remove(0);
                            PooledConnection newPolledConn = new PooledConnection(this,firstActiveConn.getRealConnection());
                            state.activeConnections.add(newPolledConn);
                            firstActiveConn = null;
                        } else {
                            try {
                                if (!countedWait){
                                    countedWait = true;
                                }
                                state.wait(time2Wait);
                            }catch (InterruptedException e){
                                e.printStackTrace();
                                break;
                            }
                        }
                    }
                }
                if (conn != null){
                    conn.setLastUsedTimestamp(System.currentTimeMillis());
                    conn.setInvalidTimestamp(System.currentTimeMillis()+poolMaximumCheckoutTime);
                }
            }
        }
        return conn;
    }

    public void pushConnectin(PooledConnection conn) throws SQLException {
        synchronized (state){
            state.activeConnections.remove(conn);
            if (state.idleConnections.size() < poolMaximumIdleConnections){
                // 可以加入idle队列
                PooledConnection newConn = new PooledConnection(this,conn.getRealConnection());
                state.idleConnections.add(newConn);
                newConn.setCreatedTimestamp(System.currentTimeMillis());
                newConn.setLastUsedTimestamp(System.currentTimeMillis());
                newConn.setInvalidTimestamp(System.currentTimeMillis()+poolMaximumCheckoutTime);
                conn = null;
                state.notifyAll();
            } else {
                conn.getRealConnection().close();
                conn = null;
            }
        }
    }

    public void forceCloseAll(){
        synchronized (state){
            for (int i = state.activeConnections.size();i>0;i--){
                try {
                    PooledConnection current = state.activeConnections.remove(i);
                    current.getRealConnection().close();
                    current = null;
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
            for (int i = state.idleConnections.size();i>0;i--){
                try {
                    PooledConnection current = state.idleConnections.remove(i);
                    current.getRealConnection().close();
                    current = null;
                }catch (SQLException e){
                    e.printStackTrace();
                }
            }
        }
    }
}

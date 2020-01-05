package com.mt.mybatis.datasource;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.SQLException;

public class PooledConnection implements InvocationHandler {

    //所创建它的datasource引用
    private PooledDatasource dataSource;
    //真正的Connection对象
    private Connection realConnection;
    //代理自己的代理Connection
    private Connection proxyConnection;
    private long invalidTimestamp;
    private long createdTimestamp;
    private long lastUsedTimestamp;

    public void setProxyConnection(Connection proxyConnection) {
        this.proxyConnection = proxyConnection;
    }

    public long getInvalidTimestamp() {
        return invalidTimestamp;
    }

    public void setInvalidTimestamp(long invalidTimestamp) {
        this.invalidTimestamp = invalidTimestamp;
    }

    public long getCreatedTimestamp() {
        return createdTimestamp;
    }

    public void setCreatedTimestamp(long createdTimestamp) {
        this.createdTimestamp = createdTimestamp;
    }

    public long getLastUsedTimestamp() {
        return lastUsedTimestamp;
    }

    public void setLastUsedTimestamp(long lastUsedTimestamp) {
        this.lastUsedTimestamp = lastUsedTimestamp;
    }


    public Connection getRealConnection() {
        return realConnection;
    }

    public void setRealConnection(Connection realConnection) {
        this.realConnection = realConnection;
    }

    public PooledConnection(PooledDatasource dataSource, Connection realConnection) {
        this.dataSource = dataSource;
        this.realConnection = realConnection;
        this.proxyConnection = (Connection) Proxy.newProxyInstance(Connection.class.getClassLoader(),new Class<?>[] { Connection.class },this);
        this.createdTimestamp = System.currentTimeMillis();
        this.lastUsedTimestamp = System.currentTimeMillis();
    }

    public Connection getProxyConnection(){
        return proxyConnection;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        String methodName = method.getName().toUpperCase();
        if (methodName.equals("CLOSE") && methodName.hashCode() == "CLOSE".hashCode()) {
            dataSource.pushConnectin(this);
            return null;
        } else {
            return method.invoke(realConnection,args);
        }
    }
}

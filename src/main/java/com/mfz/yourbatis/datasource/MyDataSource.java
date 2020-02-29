package com.mfz.yourbatis.datasource;

import javax.sql.CommonDataSource;
import java.sql.Connection;
import java.sql.SQLException;

public abstract class MyDataSource {
    private String url;
    private String driver;
    private String userName;
    private String passWord;

    public MyDataSource(String url, String driver, String userName, String passWord) {
        this.url = url;
        this.driver = driver;
        this.userName = userName;
        this.passWord = passWord;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getDriver() {
        return driver;
    }

    public void setDriver(String driver) {
        this.driver = driver;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassWord() {
        return passWord;
    }

    public void setPassWord(String passWord) {
        this.passWord = passWord;
    }

    public abstract Connection getConnection() throws SQLException;
}

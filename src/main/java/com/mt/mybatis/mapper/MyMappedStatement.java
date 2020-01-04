package com.mt.mybatis.mapper;

import com.mt.mybatis.config.Configuration;

public class MyMappedStatement {
    private Configuration configuration;
    private String mapperName;
    private String sql;

    public MyMappedStatement(Configuration configuration, String mapperName, String sql) {
        this.configuration = configuration;
        this.mapperName = mapperName;
        this.sql = sql;
    }

    public Configuration getConfiguration(){
        return configuration;
    }

    public String getSql(){
        return sql;
    }

    public String getMapperName() {
        return mapperName;
    }

    public void setMapperName(String mapperName) {
        this.mapperName = mapperName;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }
}

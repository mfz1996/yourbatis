package com.mt.mybatis.mapper;

import com.mt.mybatis.config.Configuration;
import org.apache.ibatis.cache.Cache;

public class MyMappedStatement {
    private Configuration configuration;
    private String mapperName;
    private String sql;
    private Cache secondCache;

    public MyMappedStatement(Configuration configuration, String mapperName, String sql,Cache cache) {
        this.configuration = configuration;
        this.mapperName = mapperName;
        this.sql = sql;
        this.secondCache = cache;
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

    public Cache getSecondCache() {
        return secondCache;
    }
}

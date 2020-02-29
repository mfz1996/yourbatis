package com.mfz.yourbatis.cache;

import java.util.Arrays;
import java.util.Objects;

public class CacheKey {
    private String mapperName;
    private String sql;
    private Object[] parameters;

    public CacheKey(String mapperName, String sql, Object parameters) {
        this.mapperName = mapperName;
        this.sql = sql;
        this.parameters = (Object[]) parameters;
    }

    public String getMapperName() {
        return mapperName;
    }

    public void setMapperName(String mapperName) {
        this.mapperName = mapperName;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public Object[] getParameters() {
        return parameters;
    }

    public void setParameters(Object[] parameters) {
        this.parameters = parameters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CacheKey cacheKey = (CacheKey) o;
        return mapperName.equals(cacheKey.mapperName) &&
                sql.equals(cacheKey.sql) &&
                Arrays.equals(parameters, cacheKey.parameters);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(mapperName, sql);
        result = 31 * result + Arrays.hashCode(parameters);
        return result;
    }

    @Override
    public String toString() {
        return "CacheKey{" +
                "mapperName='" + mapperName + '\'' +
                ", sql='" + sql + '\'' +
                ", parameters=" + Arrays.toString(parameters) +
                '}';
    }
}

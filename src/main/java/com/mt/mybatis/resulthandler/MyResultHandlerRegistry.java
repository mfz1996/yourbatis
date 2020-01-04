package com.mt.mybatis.resulthandler;

import java.sql.JDBCType;
import java.util.HashMap;
import java.util.Map;

public class MyResultHandlerRegistry {

    private Map<String, Class<?>> resultHandlerMap = new HashMap<>();

    public Map<String, Class<?>> getResultHandlerMap() {
        return resultHandlerMap;
    }

    public void regist(String mapperKey, Class<?> clazz){
        resultHandlerMap.put(mapperKey,clazz);
    }

    public Class<?> get(String mapperKey){
        return resultHandlerMap.get(mapperKey);
    }
}

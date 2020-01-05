package com.mt.mybatis.config;

import com.mt.mybatis.cache.DefaultCache;
import com.mt.mybatis.cache.LruCache;
import com.mt.mybatis.datasource.MyDataSource;
import com.mt.mybatis.datasource.PooledDatasource;
import com.mt.mybatis.datasource.UnpooledDatasource;
import com.mt.mybatis.executor.CacheExecutor;
import com.mt.mybatis.executor.Executor;
import com.mt.mybatis.executor.SimpleExecutor;
import com.mt.mybatis.mapper.MyMappedStatement;
import com.mt.mybatis.resulthandler.MyResultHandlerRegistry;
import com.mt.mybatis.resulthandler.MyStatementHandler;
import com.mt.mybatis.transaction.Transaction;
import com.mt.mybatis.typehandler.MyTypeHandlerRegistry;
import com.mt.mybatis.utils.ClassUtil;
import org.apache.ibatis.cache.Cache;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.*;

public class Configuration {
    private Properties properties = new Properties();
    private MyDataSource dataSource;
    private Boolean secondCache;
    private String configLocation;

    private MyTypeHandlerRegistry typeHandlerRegistry = new MyTypeHandlerRegistry();
    private MyResultHandlerRegistry resultHandlerRegistry = new MyResultHandlerRegistry();
    private Map<String,MyMappedStatement> mappedStatementMap = new HashMap<String, MyMappedStatement>();
    private Map<String,Class<?>> returnTypeMapping = new HashMap<>();
    public Map<String, MyMappedStatement> getMappedStatementMap() {
        return mappedStatementMap;
    }


    public Configuration() {
        try {
            this.configLocation = "mybatis-config.properties";
            init();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Configuration(String configLocation){
        try {
            this.configLocation = configLocation;
            init();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void init() throws ClassNotFoundException {
        if (configLocation == null){
            throw new RuntimeException("Configuration Location should not be null");
        }try {
            InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(configLocation);
            this.properties.load(inputStream);
            InputStream mapperStream = this.getClass().getClassLoader().getResourceAsStream(properties.getProperty("mapperLocation","mybatis-mapper.properties"));
            Properties mapperProperties = new Properties();
            mapperProperties.load(mapperStream);
            if (properties.getProperty("pooledDataSource","true").equals("false")){
                dataSource = new UnpooledDatasource(properties.getProperty("jdbc.url"),properties.getProperty("jdbc.driver"),properties.getProperty("jdbc.userName"),properties.getProperty("jdbc.passWord"));
            }else {
                dataSource = new PooledDatasource(properties.getProperty("jdbc.url"),properties.getProperty("jdbc.driver"),properties.getProperty("jdbc.userName"),properties.getProperty("jdbc.passWord"));
            }
            this.secondCache = properties.getProperty("secondCache","true").equals("true");
            String packageName = properties.getProperty("mapperScan");
            List<Class<?>> classes = ClassUtil.getClasses(packageName);
            for (Class<?> cls:classes){
                Method[] methods = cls.getMethods();
                for (Method method:methods){
                    String mapperKey = cls.getName()+ "." + method.getName();
                    if (mapperProperties.getProperty(mapperKey) != null){
                        String[] mapperComp = mapperProperties.getProperty(mapperKey).split("#");
                        if (mapperComp.length == 1){
                            String sql = mapperProperties.getProperty(mapperKey).split("#")[0];
                            MyMappedStatement ms = new MyMappedStatement(this,mapperKey,sql);
                            mappedStatementMap.put(mapperKey,ms);
                            Type type = method.getGenericReturnType();
                            if (type instanceof ParameterizedType){
                                Type[] typesto = ((ParameterizedType) type).getActualTypeArguments();
                                resultHandlerRegistry.regist(mapperKey, (Class<?>) typesto[0]);
                            }
                            returnTypeMapping.put(mapperKey,method.getReturnType());
                        }else {
                            String sql = mapperProperties.getProperty(mapperKey).split("#")[0];
                            String resultType = mapperProperties.getProperty(mapperKey).split("#")[1];
                            MyMappedStatement ms = new MyMappedStatement(this,mapperKey,sql);
                            mappedStatementMap.put(mapperKey,ms);
                            resultHandlerRegistry.regist(mapperKey,Class.forName(resultType));
                            returnTypeMapping.put(mapperKey, (Class<?>) method.getGenericReturnType());
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Executor newExecutor(Transaction tx){
        Executor simpleExecutor = new SimpleExecutor(this,tx);
        if (isCache()){
            String cacheType = properties.getProperty("cacheType","DEFAULT");
            Cache cache = null;
            switch (cacheType.toUpperCase()){
                case "DEFAULT":
                    cache = new DefaultCache("LocalCacheLocalCache");
                case "LRU":
                    cache = new LruCache();
                default:
                    cache = new DefaultCache("LocalCacheLocalCache");
            }
            return new CacheExecutor(this,simpleExecutor,cache);
        }
        return simpleExecutor;
    }

    public MyDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(MyDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public MyStatementHandler newStatementHandler(MyMappedStatement ms, Object parameter){
        return new MyStatementHandler(ms, parameter,this);
    }
    public Boolean isCache() {
        return secondCache;
    }

    public MyResultHandlerRegistry getResultHandlerRegistry() {
        return resultHandlerRegistry;
    }

    public MyTypeHandlerRegistry getTypeHandlerRegistry() {
        return typeHandlerRegistry;
    }

    public Map<String, Class<?>> getReturnTypeMapping() {
        return returnTypeMapping;
    }

}

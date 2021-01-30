package com.mfz.yourbatis.config;

import com.mfz.yourbatis.annotation.*;
import com.mfz.yourbatis.cache.DefaultCache;
import com.mfz.yourbatis.cache.LruCache;
import com.mfz.yourbatis.cache.RedisConfig;
import com.mfz.yourbatis.executor.CacheExecutor;
import com.mfz.yourbatis.executor.Executor;
import com.mfz.yourbatis.executor.SimpleExecutor;
import com.mfz.yourbatis.mapper.MyMappedStatement;
import com.mfz.yourbatis.resulthandler.MyResultHandlerRegistry;
import com.mfz.yourbatis.resulthandler.MyStatementHandler;
import com.mfz.yourbatis.typehandler.MyTypeHandlerRegistry;
import com.mfz.yourbatis.datasource.MyDataSource;
import com.mfz.yourbatis.datasource.PooledDatasource;
import com.mfz.yourbatis.datasource.UnpooledDatasource;
import com.mfz.yourbatis.transaction.Transaction;
import com.mfz.yourbatis.utils.ClassUtil;
import org.apache.ibatis.cache.Cache;

import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.*;

public class Configuration {
    private Properties properties = new Properties();
    private MyDataSource dataSource;
    private Boolean firstCache;
    private Boolean secondCache;
    private String configLocation;

    private MyTypeHandlerRegistry typeHandlerRegistry = new MyTypeHandlerRegistry();
    private MyResultHandlerRegistry resultHandlerRegistry = new MyResultHandlerRegistry();
    private Map<String, MyMappedStatement> mappedStatementMap = new HashMap<String, MyMappedStatement>();
    private Map<String,Class<?>> returnTypeMapping = new HashMap<>();
    private Map<String,Cache> secondCaches = new HashMap<>();

    public Map<String, MyMappedStatement> getMappedStatementMap() {
        return mappedStatementMap;
    }
    private Set<Class<? extends Annotation>> sqlAnnotationTypes = new HashSet<Class<? extends Annotation>>();

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
            initSqlAnnotationTypes();
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
            this.firstCache = properties.getProperty("firstCache","true").equals("true");
            this.secondCache = properties.getProperty("secondCache","true").equals("true");
            // 20200330

            String cacheType = properties.getProperty("cacheType","DEFAULT");
            String packageName = properties.getProperty("mapperScan");
            List<Class<?>> classes = ClassUtil.getClasses(packageName);
            for (Class<?> cls:classes){
                Method[] methods = cls.getMethods();
                String cacheName = cls.getName();
                Cache cache = null;
                switch (cacheType.toUpperCase()){
                    case "DEFAULT":
                        cache = new DefaultCache(cacheName);
                    case "LRU":
                        cache = new LruCache(cacheName);
                    default:
                        cache = new DefaultCache(cacheName);
                }
                secondCaches.putIfAbsent(cls.getName(),cache);
                for (Method method:methods){
                    String mapperKey = cls.getName()+ "." + method.getName();
                    if (mapperProperties.getProperty(mapperKey) != null){
                        String[] mapperComp = mapperProperties.getProperty(mapperKey).split("#");
                        if (mapperComp.length == 1){
                            // 重构：提取重复的注册过程，register函数与下面多行代码存在其一
                            registerMappedStatement(method,mapperComp[0],null);
//                            String sql = mapperProperties.getProperty(mapperKey).split("#")[0];
//                            MyMappedStatement ms = new MyMappedStatement(this,mapperKey,sql,cache);
//                            mappedStatementMap.put(mapperKey,ms);
//                            Type type = method.getGenericReturnType();
//                            if (type instanceof ParameterizedType){
//                                Type[] typesto = ((ParameterizedType) type).getActualTypeArguments();
//                                resultHandlerRegistry.regist(mapperKey, (Class<?>) typesto[0]);
//                            }
//                            returnTypeMapping.put(mapperKey,method.getReturnType());
                        }else {
                            registerMappedStatement(method,mapperComp[0],Class.forName(mapperComp[1]));
//                            String sql = mapperProperties.getProperty(mapperKey).split("#")[0];
//                            String resultType = mapperProperties.getProperty(mapperKey).split("#")[1];
//                            MyMappedStatement ms = new MyMappedStatement(this,mapperKey,sql,cache);
//                            mappedStatementMap.put(mapperKey,ms);
//                            resultHandlerRegistry.regist(mapperKey,Class.forName(resultType));
//                            returnTypeMapping.put(mapperKey, (Class<?>) method.getGenericReturnType());
                        }
                    }
                }
                // 加入注解配置
                Annotation[] annotations = cls.getDeclaredAnnotations();
                if (hasMapperAnnotation(cls)){
                    parseAnnotationMethods(cls);
                }
            }
        } catch (IOException | NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
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
                    cache = new DefaultCache("LocalCache");
                case "LRU":
                    cache = new LruCache("LocalCache");
                default:
                    cache = new DefaultCache("LocalCache");
            }
            return new CacheExecutor(this,simpleExecutor);
        }
        return simpleExecutor;
    }

    public MyDataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(MyDataSource dataSource) {
        this.dataSource = dataSource;
    }

    public MyStatementHandler newStatementHandler(MyMappedStatement ms, Object parameter, Executor executor){
        return new MyStatementHandler(ms, parameter,this,executor);
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

    private void initSqlAnnotationTypes(){
        this.sqlAnnotationTypes.add(Select.class);
        this.sqlAnnotationTypes.add(Insert.class);
        this.sqlAnnotationTypes.add(Update.class);
        this.sqlAnnotationTypes.add(Delete.class);
    }

    private Boolean hasMapperAnnotation(Class<?> clazz){
        Annotation[] annotations = clazz.getDeclaredAnnotations();
        for (Annotation annotation:annotations){
            if (annotation instanceof Mapper) return true;
        }
        return false;
    }

    private void parseAnnotationMethods(Class<?> clazz) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Method[] methods = clazz.getDeclaredMethods();
        for (Method method:methods){
            for (Class<? extends Annotation> annotation:sqlAnnotationTypes){
                Annotation sqlAnnotation = method.getAnnotation(annotation);
                if (sqlAnnotation!=null){
                    final String sqlString = (String) sqlAnnotation.getClass().getMethod("value").invoke(sqlAnnotation);
                    registerMappedStatement(method,sqlString,null);
                }
            }
        }
    }

    private void registerMappedStatement(Method method,String sql,Class<?> clazz){
        String mapperKey = method.getDeclaringClass().getName()+ "." + method.getName();
        MyMappedStatement ms = new MyMappedStatement(this,mapperKey,sql,secondCaches.get(method.getDeclaringClass().getName()));
        mappedStatementMap.put(mapperKey,ms);
        Type type;
        if (clazz == null){
            type = method.getGenericReturnType();
            if (type instanceof ParameterizedType){
                Type[] typesto = ((ParameterizedType) type).getActualTypeArguments();
                resultHandlerRegistry.regist(mapperKey, (Class<?>) typesto[0]);
            }
        }else {
            resultHandlerRegistry.regist(mapperKey, clazz);
        }
        returnTypeMapping.put(mapperKey,method.getReturnType());
    }

    public void setFirstCache(Boolean firstCache) {
        this.firstCache = firstCache;
    }

    public void setSecondCache(Boolean secondCache) {
        this.secondCache = secondCache;
    }
}

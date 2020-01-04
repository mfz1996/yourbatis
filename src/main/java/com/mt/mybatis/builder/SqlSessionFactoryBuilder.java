package com.mt.mybatis.builder;

import com.mt.mybatis.config.Configuration;
import com.mt.mybatis.session.SqlSessionFactory;
import com.mt.mybatis.session.defaults.DefaultSqlSessionFactory;

public class SqlSessionFactoryBuilder {

    public static SqlSessionFactory build(Configuration configuration){
        return new DefaultSqlSessionFactory(configuration);
    }
}

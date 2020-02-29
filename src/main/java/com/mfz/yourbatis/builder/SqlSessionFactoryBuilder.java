package com.mfz.yourbatis.builder;

import com.mfz.yourbatis.config.Configuration;
import com.mfz.yourbatis.session.SqlSessionFactory;
import com.mfz.yourbatis.session.defaults.DefaultSqlSessionFactory;

public class SqlSessionFactoryBuilder {

    public static SqlSessionFactory build(Configuration configuration){
        return new DefaultSqlSessionFactory(configuration);
    }
}

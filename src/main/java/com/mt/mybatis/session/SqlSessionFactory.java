package com.mt.mybatis.session;

import com.mt.mybatis.config.Configuration;

public interface SqlSessionFactory {
    SqlSession openSession(Configuration configuration);
    SqlSession openSession();
}

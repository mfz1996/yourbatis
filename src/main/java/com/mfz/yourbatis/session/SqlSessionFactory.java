package com.mfz.yourbatis.session;

import com.mfz.yourbatis.config.Configuration;

public interface SqlSessionFactory {
    SqlSession openSession(Configuration configuration);
    SqlSession openSession();
}

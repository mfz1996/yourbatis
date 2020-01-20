package com.mt.mybatis.example;

import com.mt.mybatis.config.Configuration;
import com.mt.mybatis.example.Domain.Person;
import com.mt.mybatis.example.Mapper.PersonDao;
import com.mt.mybatis.example.Mapper.UserDao;
import com.mt.mybatis.session.SqlSession;
import com.mt.mybatis.session.SqlSessionFactory;
import com.mt.mybatis.builder.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Configuration configuration = new Configuration("mybatis-config.properties");
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryBuilder.build(configuration);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        PersonDao personDao = sqlSession.getMapper(PersonDao.class);
        UserDao userDao = sqlSession.getMapper(UserDao.class);
        System.out.println(personDao.queryPersonById(11));
        System.out.println(userDao.getPersonById(11));
        System.out.println(userDao.getPersonById(11));
        System.out.println(userDao.getUserById(1));
        personDao.addPerson("155",1,12);
        System.out.println(userDao.getPersonById(11));
        System.out.println(userDao.getUserById(1));
        // 测试数据库连接池
//        while (true) {
//            Thread t = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    SqlSession sqlSession = sqlSessionFactory.openSession();
//                    PersonDao personDao = sqlSession.getMapper(PersonDao.class);
//                    System.out.println(Thread.currentThread() + ":" + personDao.getAll());
//                    try {
//                        sqlSession.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            });
//            t.start();
//            Thread.sleep(1);
//        }
    }
}




package com.mfz.yourbatis.example;

import com.mfz.yourbatis.config.Configuration;
import com.mfz.yourbatis.example.Mapper.PersonDao;
import com.mfz.yourbatis.example.Mapper.UserDao;
import com.mfz.yourbatis.session.SqlSession;
import com.mfz.yourbatis.session.SqlSessionFactory;
import com.mfz.yourbatis.builder.SqlSessionFactoryBuilder;

import java.io.IOException;

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




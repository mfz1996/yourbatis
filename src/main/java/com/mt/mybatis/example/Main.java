package com.mt.mybatis.example;

import com.mt.mybatis.config.Configuration;
import com.mt.mybatis.example.Domain.Person;
import com.mt.mybatis.example.Mapper.PersonDao;
import com.mt.mybatis.session.SqlSession;
import com.mt.mybatis.session.SqlSessionFactory;
import com.mt.mybatis.builder.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException {
        Configuration configuration = new Configuration("mybatis-config.properties");
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryBuilder.build(configuration);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        PersonDao personDao = sqlSession.getMapper(PersonDao.class);
        Person person = personDao.queryPersonById(1);
        System.out.println(person.toString());
        Person person2 = personDao.queryPersonById(1);
        System.out.println(person2.toString());
    }
}


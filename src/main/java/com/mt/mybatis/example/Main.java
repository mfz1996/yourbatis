package com.mt.mybatis.example;

import com.mt.mybatis.config.Configuration;
import com.mt.mybatis.example.Domain.Person;
import com.mt.mybatis.example.Mapper.PersonDao;
import com.mt.mybatis.session.SqlSession;
import com.mt.mybatis.session.SqlSessionFactory;
import com.mt.mybatis.builder.SqlSessionFactoryBuilder;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException, InterruptedException {
        Configuration configuration = new Configuration("mybatis-config.properties");
        SqlSessionFactory sqlSessionFactory = SqlSessionFactoryBuilder.build(configuration);
        SqlSession sqlSession = sqlSessionFactory.openSession();
        PersonDao personDao = sqlSession.getMapper(PersonDao.class);
        System.out.println(personDao.getAll());
        Person person = personDao.queryPersonById(1);
        System.out.println(person == null);
        Person person2 = personDao.queryPersonById(1);
        System.out.println(person2.toString());
//        Integer sex = personDao.getSexById(11);
//        System.out.println(sex);
//        Integer sex2 = personDao.getSexById(11);
//        System.out.println(sex2);
//        String name = personDao.getNameById(11);
//        System.out.println(name);
//        String name2 = personDao.getNameById(11);
//        System.out.println(name2);
//        List<Person> persons = personDao.getAll();
//        for (Person p:persons){
//            System.out.println(p);
//        }
//        while(true){
//            Thread.currentThread().sleep(1);
//            Thread t = new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    System.out.println(personDao.getAll());
//                }
//            });
//            t.start();
//        }

        personDao.editPersonById("MMMM", 1);
        personDao.addPerson(22, "wewe", 2, 22);
        personDao.deletePersonById(1);
        sqlSession.close();
    }
}


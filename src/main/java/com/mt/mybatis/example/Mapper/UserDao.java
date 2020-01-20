package com.mt.mybatis.example.Mapper;

import com.mt.mybatis.annotation.Mapper;
import com.mt.mybatis.annotation.Select;
import com.mt.mybatis.example.Domain.Person;
import com.mt.mybatis.example.Domain.User;

@Mapper
public interface UserDao {

    @Select("select * from user where id = ?;")
    User getUserById(int id);

    @Select("select * from person where id = ?;")
    Person getPersonById(int id);
}

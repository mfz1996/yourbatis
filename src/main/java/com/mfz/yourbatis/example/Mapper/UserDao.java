package com.mfz.yourbatis.example.Mapper;

import com.mfz.yourbatis.annotation.Mapper;
import com.mfz.yourbatis.annotation.Select;
import com.mfz.yourbatis.example.Domain.Person;
import com.mfz.yourbatis.example.Domain.User;

@Mapper
public interface UserDao {

    @Select("select * from user where id = ?;")
    User getUserById(int id);

    @Select("select * from person where id = ?;")
    Person getPersonById(int id);
}

package com.mt.mybatis.example.Mapper;
import com.mt.mybatis.example.Domain.Person;

public interface PersonDao {
    Person queryPersonById(int id);
}

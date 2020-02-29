package com.mfz.yourbatis.example.Mapper;
import com.mfz.yourbatis.annotation.*;
import com.mfz.yourbatis.annotation.*;
import com.mfz.yourbatis.example.Domain.Person;

import java.util.List;
@Mapper
public interface PersonDao {
    @Select("select * from person where id = ?;")
    Person queryPersonById(int id);

    @Select("select sex from person where id = ?;")
    Integer getSexById(int id);

    @Select("select name from person where id = ?;")
    String getNameById(int id);

    @Select("select * from person;")
    List<Person> getAll();

    @Select("select * from person where name = ?;")
    Person getPersonByName(String name);

    @Delete("delete from person where id = ?;")
    void deletePersonById(int id);

    @Update("update person set name = ? where id = ?;")
    void editPersonById(String newName, int id);

    @Insert("insert into person( name, sex, age) values ( ?, ?, ?);")
    void addPerson( String name, int sex, int age);
}

package com.mt.mybatis.example.Mapper;
import com.mt.mybatis.example.Domain.Person;

import java.util.List;

public interface PersonDao {
    Person queryPersonById(int id);

    Integer getSexById(int id);

    String getNameById(int id);

    List<Person> getAll();

    Person getPersonByName(String name);

    void deletePersonById(int id);

    void editPersonById(String newName, int id);

    void addPerson(int id, String name, int sex, int age);
}

package com.mfz.yourbatis.example.Mapper;

import com.mfz.yourbatis.annotation.Mapper;
import com.mfz.yourbatis.annotation.Update;

@Mapper
public interface AMapper {

    @Update("update a set version = version + 1 where id = ?;")
    void incr(int id);
}

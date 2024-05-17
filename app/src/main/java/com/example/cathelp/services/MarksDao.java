package com.example.cathelp.services;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.cathelp.model.LinkMark;

import java.util.List;

@Dao
public interface MarksDao {
    @Query("SELECT * FROM linkmark WHERE author = :author")
    List<LinkMark> getAll(String author);

    @Query("SELECT * FROM linkmark WHERE nameEvent = :name")
    LinkMark getById(String name);
    @Insert
    void insert(LinkMark linkMark);

    @Update
    void update(LinkMark linkMark);

    @Delete
    void delete(LinkMark linkMark);
}

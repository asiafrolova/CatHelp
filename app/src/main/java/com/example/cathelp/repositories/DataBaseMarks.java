package com.example.cathelp.repositories;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.example.cathelp.services.MarksDao;
import com.example.cathelp.model.LinkMark;

@Database(entities = {LinkMark.class}, version = 2)
public abstract class DataBaseMarks extends RoomDatabase {
    public abstract MarksDao marksDao();
}

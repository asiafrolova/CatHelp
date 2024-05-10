package com.example.cathelp.repositories;

import android.app.Application;

import androidx.room.Room;

public class App extends Application {

    public static App instance;

    private DataBaseMarks database;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        database = Room.databaseBuilder(this, DataBaseMarks.class, "database")
                .build();
    }

    public static App getInstance() {
        return instance;
    }

    public DataBaseMarks getDatabase() {
        return database;
    }
}

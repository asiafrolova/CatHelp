package com.example.cathelp.repositories;

import android.app.Application;
import android.util.Log;

import androidx.room.Room;

import com.google.firebase.auth.FirebaseAuth;

public class App extends Application {

    public static App instance;
    public FirebaseAuth mAuth;

    private DataBaseMarks database;

    @Override
    public void onCreate() {
        super.onCreate();
        mAuth = FirebaseAuth.getInstance();
        instance = this;
        if( mAuth.getCurrentUser()!=null) {
            database = Room.databaseBuilder(this, DataBaseMarks.class,"database")
                    .fallbackToDestructiveMigration()
                    .build();
            Log.d("Start", mAuth.getCurrentUser().getUid() + " ");
        }
    }


    public static App getInstance() {
        return instance;
    }

    public DataBaseMarks getDatabase() {
        return database;
    }
}

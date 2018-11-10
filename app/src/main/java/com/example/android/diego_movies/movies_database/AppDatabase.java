package com.example.android.diego_movies.movies_database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.android.diego_movies.movies;

@Database(entities = {movies.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    private static final Object OBJECT = new Object();
    private static final String DATABASE_NAME = "db-movies";
    private static AppDatabase dbInstance;

    public static AppDatabase createInstance (Context context){
        if (dbInstance == null){
            synchronized (OBJECT){
                dbInstance = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,DATABASE_NAME)
                        .build();
            }
        }
        return dbInstance;
    }

    public abstract MoviesDAO getMoviesDAO();
}

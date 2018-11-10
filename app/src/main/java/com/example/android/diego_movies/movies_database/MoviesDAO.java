package com.example.android.diego_movies.movies_database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.diego_movies.movies;

import java.util.List;

@Dao
public interface MoviesDAO {

    @Query("SELECT * FROM FAVORITES")
    LiveData<List<movies>> getMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertMovie(movies movie);

    @Update
    public void updateMovie(movies movie);

    @Delete
    public void deleteMovie(movies movie);

}

package com.example.android.diego_movies;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.example.android.diego_movies.movies_database.AppDatabase;

import java.util.List;

public class MainViewModel extends AndroidViewModel {
    private LiveData<List<movies>> favoriteList;

    public MainViewModel(@NonNull Application application) {
        super(application);
        AppDatabase appDB = AppDatabase.createInstance(this.getApplication());
        favoriteList = appDB.getMoviesDAO().getMovies();
    }

    public LiveData<List<movies>> getFavoriteList() {
        return favoriteList;
    }
}

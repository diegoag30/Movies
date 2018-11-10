package com.example.android.diego_movies;

import android.os.Looper;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import android.os.Handler;

public class AppExecutor {
    //This Class was adapted from the course videos, in the android architecture lesson
    //https://github.com/udacity/ud851-Exercises/blob/student/Lesson09b
    private static final Object nObject = new Object();
    private static AppExecutor mInstance;
    private final Executor mainExecutor;
    private final Executor mThread;
    private final Executor networkIO;

    private AppExecutor(Executor mainExecutor, Executor networkIO, Executor mainThread){
        this.mainExecutor = mainExecutor;
        this.networkIO = networkIO;
        this.mThread = mainThread;
    }

    public static AppExecutor getInstance() {
        if (mInstance == null){
            synchronized (nObject){
                mInstance = new AppExecutor(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        new MainThreadExecutor());
            }
        }
        return mInstance;
    }

    public Executor mainExecutor(){
        return mainExecutor;
    }

    public Executor mainThread() {
        return mThread;
    }

    public Executor networkIO() {
        return networkIO;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable runnable) {
            mThreadHandler.post(runnable);
        }
    }
}

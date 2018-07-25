package com.example.android.employeesmanagementapp.data;


import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import androidx.annotation.NonNull;

/**
 * Executors class used to run database operations
 * on background thread.
 * if there is multiple calls they are queued.
 * used specifically for (insert , update , delete)
 * <p>
 * follows singleton design pattern
 */
public class AppExecutor {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static AppExecutor sInstance;
    private final Executor diskIO;
    private final Executor mainThread;

    private AppExecutor(Executor diskIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.mainThread = mainThread;
    }

    public static AppExecutor getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppExecutor(Executors.newSingleThreadExecutor(),
                        new MainThreadExecutor());
            }
        }
        return sInstance;
    }

    //single thread executor runs on background thread
    public Executor diskIO() {
        return diskIO;
    }

    //main thread executor runs on ui thread
    public Executor mainThread() {
        return mainThread;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }


}

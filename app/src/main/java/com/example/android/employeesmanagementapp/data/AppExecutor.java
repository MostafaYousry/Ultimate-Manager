package com.example.android.employeesmanagementapp.data;


import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Executors used to make database operation
 * specifically (insert , update , delete)
 */
public class AppExecutor {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static AppExecutor sInstance;
    private final Executor diskIO;

    private AppExecutor(Executor diskIO) {
        this.diskIO = diskIO;
    }

    public static AppExecutor getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new AppExecutor(Executors.newSingleThreadExecutor());
            }
        }
        return sInstance;
    }

    public Executor diskIO() {
        return diskIO;
    }

}

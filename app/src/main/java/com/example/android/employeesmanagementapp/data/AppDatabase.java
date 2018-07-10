package com.example.android.employeesmanagementapp.data;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@Database(entities = {EmployeeEntry.class,TaskEntry.class,DepartmentEntry.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    private static final String TAG = AppDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "employee_management";
    private static final Object LOCK = new Object();
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context){
        if (sInstance == null){
            synchronized (LOCK){
                sInstance = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class,AppDatabase.DATABASE_NAME)
                        //temporarily only for testing
                        .allowMainThreadQueries()
                        .build();
            }
        }

        return sInstance;
    }

    public abstract EmployeesDao employeesDao();

    public abstract TasksDao tasksDao();

    public abstract DepartmentsDao departmentsDao();
}

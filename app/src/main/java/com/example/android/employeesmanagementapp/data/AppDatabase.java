package com.example.android.employeesmanagementapp.data;

import android.content.Context;

import com.example.android.employeesmanagementapp.data.daos.DepartmentsDao;
import com.example.android.employeesmanagementapp.data.daos.EmployeesDao;
import com.example.android.employeesmanagementapp.data.daos.EmployeesTasksDao;
import com.example.android.employeesmanagementapp.data.daos.TasksDao;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeesTasksEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

/**
 * class for main app database
 * follows singleton design pattern
 * defines all entities in the database , database name , database version , type converters used
 */
@Database(entities = {EmployeeEntry.class, TaskEntry.class, DepartmentEntry.class, EmployeesTasksEntry.class}, version = 1, exportSchema = false)
@TypeConverters(DateTypeConverter.class)
public abstract class AppDatabase extends RoomDatabase {
    private static final String TAG = AppDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "employee_management";
    private static final Object LOCK = new Object();
    private static AppDatabase sInstance;

    public static AppDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .build();
            }
        }

        return sInstance;
    }

    public abstract EmployeesDao employeesDao();

    public abstract TasksDao tasksDao();

    public abstract DepartmentsDao departmentsDao();

    public abstract EmployeesTasksDao employeesTasksDao();
}

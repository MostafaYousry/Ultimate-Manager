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

import java.util.Date;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

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

    public static AppDatabase getInstance(final Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, AppDatabase.DATABASE_NAME)
                        .addCallback(new Callback() {
                            @Override
                            public void onCreate(SupportSQLiteDatabase db) {
                                super.onCreate(db);
                                AppExecutor.getInstance().diskIO().execute(new Runnable() {
                                    @Override
                                    public void run() {
                                        AppDatabase db = getInstance(context);

                                        db.departmentsDao().addDepartment(new DepartmentEntry(1, "dep1",false));
                                        db.departmentsDao().addDepartment(new DepartmentEntry(2, "dep2",false));
                                        db.departmentsDao().addDepartment(new DepartmentEntry(3, "dep3",false));
                                        db.departmentsDao().addDepartment(new DepartmentEntry(4, "dep4",false));
                                        db.departmentsDao().addDepartment(new DepartmentEntry(5, "dep5",false));
                                        db.departmentsDao().addDepartment(new DepartmentEntry(6, "dep6",false));
                                        db.departmentsDao().addDepartment(new DepartmentEntry(7, "dep7",false));
                                        db.departmentsDao().addDepartment(new DepartmentEntry(8, "dep8",false));
                                        db.departmentsDao().addDepartment(new DepartmentEntry(9, "dep9",false));


                                        db.employeesDao().addEmployee(new EmployeeEntry(1, 1, "mostafa", 23, new Date(),false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(2, 2, "kizo", 23, new Date(),false));
                                        //3ndo completed tasks bas
                                        db.employeesDao().addEmployee(new EmployeeEntry(3, 3, "ammar", 23, new Date(),false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(4, 2, "swidan", 23, new Date(),false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(5, 2, "zew", 23, new Date(),false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(6, 3, "3as3oos", 23, new Date(),false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(7, 4, "abdelrahman", 23, new Date(),false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(8, 5, "amr", 23, new Date(),false));
                                        //3ndo running bas
                                        db.employeesDao().addEmployee(new EmployeeEntry(9, 6, "hussein", 23, new Date(),false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(10, 6, "fakhr", 23, new Date(),false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(11, 7, "wagdy", 23, new Date(),false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(12, 8, "ziad", 23, new Date(),false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(13, 9, "omar", 23, new Date(),false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(14, 2, "zanaty", 23, new Date(),false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(15, 2, "ismaeil", 23, new Date(),false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(16, 5, "abdallah", 23, new Date(),false));
                                        //m3ndosh taskat
                                        db.employeesDao().addEmployee(new EmployeeEntry(17, 5, "shawky", 23, new Date(),false));


                                        db.tasksDao().addTask(new TaskEntry(1, 1, "task1", "askasmas", new Date(), new Date(), 3, true));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(1, 1));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(5, 1));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(3, 1));

                                        db.tasksDao().addTask(new TaskEntry(2, 5, "task2", "askasmas", new Date(), new Date(), 2, true));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(1, 2));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(5, 2));

                                        db.tasksDao().addTask(new TaskEntry(3, 2, "task3", "askasmas", new Date(), new Date(), 1, true));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(1, 3));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(5, 3));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(7, 3));

                                        db.tasksDao().addTask(new TaskEntry(4, 2, "task4", "askasmas", new Date(), new Date(), 0, true));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(2, 4));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(4, 4));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(1, 4));

                                        db.tasksDao().addTask(new TaskEntry(5, 8, "task5", "askasmas", new Date(), new Date(), 0, false));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(7, 5));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(8, 5));

                                        db.tasksDao().addTask(new TaskEntry(6, 9, "task6", "askasmas", new Date(), new Date(), 0, false));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(4, 6));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(9, 6));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(5, 6));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(1, 6));

                                        db.tasksDao().addTask(new TaskEntry(7, 2, "task7", "askasmas", new Date(), new Date(), 0, false));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(9, 7));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(5, 7));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(1, 7));

                                        db.tasksDao().addTask(new TaskEntry(8, 5, "task8", "askasmas", new Date(), new Date(), 0, false));
                                        db.tasksDao().addTask(new TaskEntry(9, 1, "task9", "askasmas", new Date(), new Date(), 0, false));
                                        db.tasksDao().addTask(new TaskEntry(10, 1, "task10", "askasmas", new Date(), new Date(), 0, false));
                                    }
                                });
                            }
                        })
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

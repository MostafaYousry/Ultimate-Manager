package com.example.android.employeesmanagementapp.data;

import android.content.Context;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.data.daos.DepartmentsDao;
import com.example.android.employeesmanagementapp.data.daos.EmployeesDao;
import com.example.android.employeesmanagementapp.data.daos.EmployeesTasksDao;
import com.example.android.employeesmanagementapp.data.daos.TasksDao;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeesTasksEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

import java.util.Calendar;
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
@TypeConverters({CalendarTypeConverter.class, DateTypeConverter.class})
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

                                        db.departmentsDao().addDepartment(new DepartmentEntry(1, "dep1", new Date(), "", false));
                                        db.departmentsDao().addDepartment(new DepartmentEntry(2, "dep2", new Date(), "", false));
                                        db.departmentsDao().addDepartment(new DepartmentEntry(3, "dep3", new Date(), "", false));
                                        db.departmentsDao().addDepartment(new DepartmentEntry(4, "dep4", new Date(), "", false));
                                        db.departmentsDao().addDepartment(new DepartmentEntry(5, "dep5", new Date(), "", false));
                                        db.departmentsDao().addDepartment(new DepartmentEntry(6, "dep6", new Date(), "", false));
                                        db.departmentsDao().addDepartment(new DepartmentEntry(7, "dep7", new Date(), "", false));
                                        db.departmentsDao().addDepartment(new DepartmentEntry(8, "dep8", new Date(), "", false));
                                        db.departmentsDao().addDepartment(new DepartmentEntry(9, "dep9", new Date(), "", false));

                                        db.employeesDao().addEmployee(new EmployeeEntry(1, 1, "mostafa", "yousry", null, 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(2, 2, "karim", "hamdy", "abdel azziz", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(3, 3, "ammar", "yasser", "ismaeil", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(4, 2, "amir", "swidan", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(5, 2, "omar", "zawawi", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(6, 3, "mohamed", null, "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(7, 4, "abdelrahman", "wael", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(8, 5, "amr", "saleh", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(9, 6, "hussein", null, "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(10, 6, "loay", "fakhr", "el din", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(11, 7, "yamany", null, null, 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(12, 8, "ziad", "ashraf", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(13, 9, "ramy", "essam", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(14, 2, "zorba", "zanaty", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(15, 2, "ismaeil", "el", "yamany", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(16, 5, "abdallah", "amr", "el maradny", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(17, 5, "shawky", "maom", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(18, 1, "mostafa", "yousry", null, 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(19, 2, "karim", "hamdy", "abdel azziz", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(20, 3, "ammar", "yasser", "ismaeil", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(21, 2, "amir", "swidan", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(22, 2, "omar", "zawawi", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(23, 3, "mohamed", null, "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(24, 4, "abdelrahman", "wael", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(25, 5, "amr", "saleh", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(26, 6, "hussein", null, "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(27, 6, "loay", "fakhr", "el din", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(28, 7, "yamany", null, null, 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(29, 8, "ziad", "ashraf", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(30, 9, "ramy", "essam", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(31, 2, "zorba", "zanaty", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(32, 2, "ismaeil", "el", "yamany", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(33, 5, "abdallah", "amr", "el maradny", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(35, 5, "shawky", "maom", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(36, 1, "mostafa", "yousry", null, 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(37, 2, "karim", "hamdy", "abdel azziz", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(38, 3, "ammar", "yasser", "ismaeil", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(39, 2, "amir", "swidan", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(40, 2, "omar", "zawawi", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(41, 3, "mohamed", null, "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(42, 4, "abdelrahman", "wael", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(43, 5, "amr", "saleh", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(44, 6, "hussein", null, "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(45, 6, "loay", "fakhr", "el din", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(46, 7, "yamany", null, null, 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(47, 8, "ziad", "ashraf", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(48, 9, "ramy", "essam", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(49, 2, "zorba", "zanaty", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(50, 2, "ismaeil", "el", "yamany", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(51, 5, "abdallah", "amr", "el maradny", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(52, 5, "shawky", "maom", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(53, 1, "mostafa", "yousry", null, 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(54, 2, "karim", "hamdy", "abdel azziz", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(55, 3, "ammar", "yasser", "ismaeil", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(56, 2, "amir", "swidan", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(57, 2, "omar", "zawawi", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(58, 3, "mohamed", null, "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(59, 4, "abdelrahman", "wael", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(60, 5, "amr", "saleh", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(61, 6, "hussein", null, "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(62, 6, "loay", "fakhr", "el din", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(63, 7, "yamany", null, null, 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(64, 8, "ziad", "ashraf", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(65, 9, "ramy", "essam", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(66, 2, "zorba", "zanaty", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(67, 2, "ismaeil", "el", "yamany", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(68, 5, "abdallah", "amr", "el maradny", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(69, 5, "shawky", "maom", "dfmnweifmw", 23, new Date(), "fef@gmail.com", "013232333", "rvmrivm", "", false));

                                        db.tasksDao().addTask(new TaskEntry(1, 1, "task1", "askasmas", Calendar.getInstance(), Calendar.getInstance(), 3, true, R.color.task_color_1));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(1, 1));

                                        db.tasksDao().addTask(new TaskEntry(2, 5, "task2", "askasmas", Calendar.getInstance(), Calendar.getInstance(), 2, true, R.color.task_color_1));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(8, 2));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(16, 2));

                                        db.tasksDao().addTask(new TaskEntry(3, 2, "task3", "askasmas", Calendar.getInstance(), Calendar.getInstance(), 1, true, R.color.task_color_1));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(4, 3));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(5, 3));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(2, 3));

                                        db.tasksDao().addTask(new TaskEntry(4, 2, "task4", "askasmas", Calendar.getInstance(), Calendar.getInstance(), 0, true, R.color.task_color_1));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(5, 4));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(4, 4));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(15, 4));

                                        db.tasksDao().addTask(new TaskEntry(5, 8, "task5", "askasmas", Calendar.getInstance(), Calendar.getInstance(), 0, false, R.color.task_color_1));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(12, 5));

                                        db.tasksDao().addTask(new TaskEntry(6, 9, "task6", "askasmas", Calendar.getInstance(), Calendar.getInstance(), 0, false, R.color.task_color_1));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(13, 6));

                                        db.tasksDao().addTask(new TaskEntry(7, 2, "task7", "askasmas", Calendar.getInstance(), Calendar.getInstance(), 0, false, R.color.task_color_1));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(14, 7));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(15, 7));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(2, 7));

                                        db.tasksDao().addTask(new TaskEntry(8, 5, "task8", "askasmas", Calendar.getInstance(), Calendar.getInstance(), 0, false, R.color.task_color_1));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(8, 8));

                                        db.tasksDao().addTask(new TaskEntry(9, 6, "task9", "askasmas", Calendar.getInstance(), Calendar.getInstance(), 0, false, R.color.task_color_1));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(9, 9));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(10, 9));

                                        db.tasksDao().addTask(new TaskEntry(10, 6, "task10", "askasmas", Calendar.getInstance(), Calendar.getInstance(), 0, false, R.color.task_color_1));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(9, 10));
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
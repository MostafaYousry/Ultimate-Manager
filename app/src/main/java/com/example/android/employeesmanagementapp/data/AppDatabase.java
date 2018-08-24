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
@TypeConverters(CalendarTypeConverter.class)
public abstract class AppDatabase extends RoomDatabase {
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

                                        db.departmentsDao().addDepartment(new DepartmentEntry(1, "Health care", Calendar.getInstance(), "", false));
                                        db.departmentsDao().addDepartment(new DepartmentEntry(2, "IT", Calendar.getInstance(), "", false));
                                        db.departmentsDao().addDepartment(new DepartmentEntry(3, "Construction", Calendar.getInstance(), "", false));
                                        db.departmentsDao().addDepartment(new DepartmentEntry(4, "Logistics", Calendar.getInstance(), "", false));
                                        db.departmentsDao().addDepartment(new DepartmentEntry(5, "Sales & marketing", Calendar.getInstance(), "", false));
                                        db.departmentsDao().addDepartment(new DepartmentEntry(6, "Research & Development", Calendar.getInstance(), "", false));
                                        db.departmentsDao().addDepartment(new DepartmentEntry(7, "Human resources", Calendar.getInstance(), "", false));
                                        db.departmentsDao().addDepartment(new DepartmentEntry(8, "Finance", Calendar.getInstance(), "", false));
                                        db.departmentsDao().addDepartment(new DepartmentEntry(9, "Mathematics and Commerce", Calendar.getInstance(), "", false));

                                        db.employeesDao().addEmployee(new EmployeeEntry(1, 1, "Themba", "Corwin", "Eckhart", 23, Calendar.getInstance(), "foo@gmail.com", "0123456789", "John would be an excellent addition to your sales team. Furthermore, I believe he’s ready for an entry-level management position. He has an excellent rapport with his co-workers, and they enjoy working with him. I’m sure he’d be excellent as a shift-supervisor or assistant manager.", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(2, 2, "Samantha", "Roscoe", "Verity", 23, Calendar.getInstance(), "foo@gmail.com", "0123456789", "", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(3, 3, "Terrell", "Donna", "Allen", 23, Calendar.getInstance(), "foo@gmail.com", "0123456789", "", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(4, 2, "Ann", "Garfield", "Stacks", 23, Calendar.getInstance(), "foo@gmail.com", "0123456789", "", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(5, 2, "Gloria", "Angela", "Oliverson", 23, Calendar.getInstance(), "foo@gmail.com", "0123456789", "", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(6, 3, "Belinda", "Thelma", "Spears", 23, Calendar.getInstance(), "foo@gmail.com", "0123456789", "", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(7, 4, "Hilda", "Oral", "Coke", 23, Calendar.getInstance(), "foo@gmail.com", "0123456789", "", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(8, 5, "Orson", "Nelson", "Woods", 23, Calendar.getInstance(), "foo@gmail.com", "0123456789", "", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(9, 6, "Kennith", "Ramsey", "Baker", 23, Calendar.getInstance(), "foo@gmail.com", "0123456789", "", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(10, 6, "Wilfred", "Darnell", "Banister", 23, Calendar.getInstance(), "foo@gmail.com", "0123456789", "", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(11, 7, "Lewis", "Seth", "Ikin", 23, Calendar.getInstance(), "foo@gmail.com", "0123456789", "", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(12, 8, "Emmeline", "Burton", "King", 23, Calendar.getInstance(), "foo@gmail.com", "0123456789", "", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(13, 9, "Bryon", "Knox", "Browne", 23, Calendar.getInstance(), "foo@gmail.com", "0123456789", "", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(14, 2, "Dominic", "Lovell", "Barton", 23, Calendar.getInstance(), "foo@gmail.com", "0123456789", "", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(15, 2, "Josiah", "Jemima", "Dean", 23, Calendar.getInstance(), "foo@gmail.com", "0123456789", "", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(16, 5, "Gerald", "Colleen", "Stacy", 23, Calendar.getInstance(), "foo@gmail.com", "0123456789", "", "", false));
                                        db.employeesDao().addEmployee(new EmployeeEntry(17, 5, "Maurice", "Fabian", "Garland", 23, Calendar.getInstance(), "foo@gmail.com", "0123456789", "", "", false));


                                        db.tasksDao().addTask(new TaskEntry(1, 1, "Volunteer Walk Leader", "Volunteers walk leaders are crucial to the project. As part of the project we offer short led walks for groups of people at set times. Each walk has at least 2 Walk Leaders, one at the front and back of the groups. Currently we insert information about locations/number of walks and walk leaders .\n" +
                                                "\n" +
                                                "Volunteers are to:\n" +
                                                "    • Lead or be back marker on a short health walk in one of our walking areas in the manner specified at the walk leader training.\n" +
                                                "    • Create a warm and friendly atmosphere in the group and welcome new walkers.\n" +
                                                "    • Ensure all the walkers have read the Health Walk Agreement card.\n" +
                                                "    • Take a register of walkers who attend and send in to the office.\n" +
                                                "    • Ensure new walkers fill out a New Walkers Form and send in to the office.\n" +
                                                "    • Explain how the walk will proceed, stating the route and length of the walk at the start. \n" +
                                                "    • Lead the group confidently and work with other Walk Leaders to ensure the safety of the group.\n" +
                                                "    • Let the project coordinator know, well in advance, if unable to make a walk to allow time for a replacement to be found or to let them know that cover has been arranged.\n" +
                                                "    • Attend training and support sessions.\n" +
                                                "    • Carry out tasks following health and safety procedures and with regard for themselves and others. \n" +
                                                "    • Fill in and return accident forms when required.\n" +
                                                "    • Disclose information that may affect their suitability to volunteer. Such information will be dealt with confidentially.\n" +
                                                "    • Promote the aims and objectives of Health Walk Project Name.\n" +
                                                "\n" +
                                                "In addition to leading walks, volunteers can take on a number of other roles for example:\n" +
                                                "    • Newsletter editing and contribution \n" +
                                                "    • Photographer\n" +
                                                "    • Identifying suitable routes and risk assessing them \n" +
                                                "    • Writing route descriptions for independent walking leaflets\n" +
                                                "    • Checking walk leaflets for accuracy\n" +
                                                "    • Attending events to help raise awareness of the project\n" +
                                                "    • Distributing promotional leaflets and posters for the project\n" +
                                                "    • Providing local/natural history information on walks\n" +
                                                "    • Administrative tasks including database entry and office duties", Calendar.getInstance(), Calendar.getInstance(), 0, false, R.color.task_color_1));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(1, 1));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(8, 1));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(16, 1));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(4, 1));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(5, 1));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(2, 1));

                                        db.tasksDao().addTask(new TaskEntry(2, 5, "Maintenance check", "", Calendar.getInstance(), Calendar.getInstance(), 2, true, R.color.task_color_1));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(8, 2));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(16, 2));

                                        db.tasksDao().addTask(new TaskEntry(3, 2, "Accept incoming supplies", "", Calendar.getInstance(), Calendar.getInstance(), 1, true, R.color.task_color_1));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(4, 3));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(5, 3));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(2, 3));

                                        db.tasksDao().addTask(new TaskEntry(4, 2, "Investigate power loss", "", Calendar.getInstance(), Calendar.getInstance(), 0, true, R.color.task_color_1));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(5, 4));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(4, 4));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(15, 4));

                                        db.tasksDao().addTask(new TaskEntry(5, 8, "Delegate paperwork to officials", "", Calendar.getInstance(), Calendar.getInstance(), 0, false, R.color.task_color_1));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(12, 5));

                                        db.tasksDao().addTask(new TaskEntry(6, 9, "Secure the incoming shipment", "", Calendar.getInstance(), Calendar.getInstance(), 0, false, R.color.task_color_1));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(13, 6));

                                        db.tasksDao().addTask(new TaskEntry(7, 2, "Co-operate with another company", "", Calendar.getInstance(), Calendar.getInstance(), 0, false, R.color.task_color_1));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(14, 7));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(15, 7));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(2, 7));

                                        db.tasksDao().addTask(new TaskEntry(8, 5, "Book meetings with clients", "", Calendar.getInstance(), Calendar.getInstance(), 0, false, R.color.task_color_1));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(8, 8));

                                        db.tasksDao().addTask(new TaskEntry(9, 6, "Pay due taxes", "", Calendar.getInstance(), Calendar.getInstance(), 0, false, R.color.task_color_1));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(9, 9));
                                        db.employeesTasksDao().addEmployeeTask(new EmployeesTasksEntry(10, 9));

                                        db.tasksDao().addTask(new TaskEntry(10, 6, "Write a contract with the client", "", Calendar.getInstance(), Calendar.getInstance(), 0, false, R.color.task_color_1));
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

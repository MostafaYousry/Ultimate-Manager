package com.example.android.employeesmanagementapp.data.viewmodels;

import android.app.Application;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.DepartmentWithExtras;
import com.example.android.employeesmanagementapp.data.EmployeeWithExtras;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

/**
 * View Model class
 * <p>
 * caches data for MainActivity
 * ---> runningTasksList for RunningTasksFragment
 * ---> departmentsList for DepartmentsFragment
 * ---> employeesList for EmployeesFragment
 */
public class MainViewModel extends AndroidViewModel {

    public final LiveData<PagedList<DepartmentWithExtras>> allDepartmentsWithExtrasList;
    public final LiveData<List<DepartmentEntry>> allDepartmentsList;
    public final LiveData<PagedList<TaskEntry>> runningTasksList;
    public final LiveData<PagedList<TaskEntry>> completedTasksList;
    public final LiveData<PagedList<EmployeeWithExtras>> employeesWithExtrasList;


    public MainViewModel(final Application application) {
        super(application);

        AppDatabase db = AppDatabase.getInstance(application.getApplicationContext());

        runningTasksList = new LivePagedListBuilder<>(db.tasksDao().loadRunningTasks(), /* page size */ 20).build();
        completedTasksList = new LivePagedListBuilder<>(db.tasksDao().loadCompletedTasks(), /* page size */ 20).build();
        employeesWithExtrasList = new LivePagedListBuilder<>(db.employeesDao().loadEmployees(), /* page size */ 20).build();
        allDepartmentsWithExtrasList = new LivePagedListBuilder<>(db.departmentsDao().loadDepartmentsWithExtras(), /* page size */ 20).build();

        allDepartmentsList = db.departmentsDao().loadDepartments();

    }

}

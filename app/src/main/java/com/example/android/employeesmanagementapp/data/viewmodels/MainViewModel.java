package com.example.android.employeesmanagementapp.data.viewmodels;

import android.app.Application;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.EmployeeWithExtras;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

/**
 * View Model class
 * <p>
 * caches data for MainActivity
 * ---> tasksList for TasksFragment
 * ---> departmentsList for DepartmentsFragment
 * ---> employeesList for EmployeesFragment
 */
public class MainViewModel extends AndroidViewModel {

    private LiveData<List<DepartmentEntry>> allDepartmentsList;
    private LiveData<List<TaskEntry>> tasksList;
    private LiveData<List<EmployeeWithExtras>> employeesWithExtrasList;


    public MainViewModel(Application application) {
        super(application);

        allDepartmentsList = AppDatabase.getInstance(application.getApplicationContext()).departmentsDao().loadDepartments();
        tasksList = AppDatabase.getInstance(application.getApplicationContext()).tasksDao().loadRunningTasks();
        employeesWithExtrasList = AppDatabase.getInstance(application.getApplicationContext()).employeesDao().loadEmployees();
    }


    public LiveData<List<DepartmentEntry>> getAllDepartmentsList() {
        return allDepartmentsList;
    }

    public LiveData<List<TaskEntry>> getTasksList() {
        return tasksList;
    }

    public LiveData<List<EmployeeWithExtras>> getEmployeesWithExtrasList() {
        return employeesWithExtrasList;
    }
}

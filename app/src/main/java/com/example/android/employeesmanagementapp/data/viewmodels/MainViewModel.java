package com.example.android.employeesmanagementapp.data.viewmodels;

import android.app.Application;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

import java.util.List;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

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
    private LiveData<List<EmployeeEntry>> allEmployeesList;


    public MainViewModel(Application application) {
        super(application);
        AppDatabase database = AppDatabase.getInstance(this.getApplication());
        allDepartmentsList = database.departmentsDao().loadDepartments();
        tasksList = database.tasksDao().loadRunningTasks();
        allEmployeesList = database.employeesDao().loadEmployees();

    }

    public LiveData<List<DepartmentEntry>> getAllDepartmentsList() {
        return allDepartmentsList;
    }

    public LiveData<List<TaskEntry>> getTasksList() {
        return tasksList;
    }

    public LiveData<List<EmployeeEntry>> getAllEmployeesList() {
        return allEmployeesList;
    }
}

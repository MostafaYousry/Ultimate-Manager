package com.example.android.employeesmanagementapp.data.viewmodels;

import android.app.Application;
import android.view.View;

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
 * ---> runningTasksList for RunningTasksFragment
 * ---> departmentsList for DepartmentsFragment
 * ---> employeesList for EmployeesFragment
 */
public class MainViewModel extends AndroidViewModel {

    private LiveData<List<DepartmentEntry>> allDepartmentsList;
    private LiveData<List<TaskEntry>> runningTasksList;
    private LiveData<List<TaskEntry>> completedTasksList;
    private LiveData<List<EmployeeWithExtras>> employeesWithExtrasList;

    private LiveData<View.OnClickListener> tasksFabClickListener;
    private LiveData<View.OnClickListener> employeesFabClickListener;
    private LiveData<View.OnClickListener> departmentsFabClickListener;


    public MainViewModel(final Application application) {
        super(application);

        allDepartmentsList = AppDatabase.getInstance(application.getApplicationContext()).departmentsDao().loadDepartments();
        runningTasksList = AppDatabase.getInstance(application.getApplicationContext()).tasksDao().loadRunningTasks();
        completedTasksList = AppDatabase.getInstance(application.getApplicationContext()).tasksDao().loadCompletedTasks();
        employeesWithExtrasList = AppDatabase.getInstance(application.getApplicationContext()).employeesDao().loadEmployees();

    }


    public LiveData<List<DepartmentEntry>> getAllDepartmentsList() {
        return allDepartmentsList;
    }

    public LiveData<List<TaskEntry>> getRunningTasksList() {
        return runningTasksList;
    }

    public LiveData<List<TaskEntry>> getCompletedTasksList() {
        return completedTasksList;
    }

    public LiveData<List<EmployeeWithExtras>> getEmployeesWithExtrasList() {
        return employeesWithExtrasList;
    }

    public LiveData<View.OnClickListener> getTasksFabClickListener() {
        return tasksFabClickListener;
    }

    public LiveData<View.OnClickListener> getEmployeesFabClickListener() {
        return employeesFabClickListener;
    }

    public LiveData<View.OnClickListener> getDepartmentsFabClickListener() {
        return departmentsFabClickListener;
    }
}

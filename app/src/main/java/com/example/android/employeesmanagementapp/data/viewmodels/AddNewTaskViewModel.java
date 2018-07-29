package com.example.android.employeesmanagementapp.data.viewmodels;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class AddNewTaskViewModel extends ViewModel {
    private LiveData<TaskEntry> task;
    private LiveData<List<EmployeeEntry>> taskEmployees;
    private LiveData<List<DepartmentEntry>> allDepartments;
    private LiveData<List<EmployeeEntry>> restOfEmployeesInDep;
    private AppDatabase mAppDatabase;

    public AddNewTaskViewModel(AppDatabase appDatabase, int taskId) {
        mAppDatabase = appDatabase;
        if (taskId > 0) {
            task = appDatabase.tasksDao().loadTaskById(taskId);
        } else {
            allDepartments = appDatabase.departmentsDao().loadDepartments();
        }
    }


    public LiveData<TaskEntry> getTask() {
        return task;
    }

    public LiveData<List<EmployeeEntry>> getTaskEmployees(int depId) {
        taskEmployees = mAppDatabase.employeesDao().loadEmployees(depId);
        return taskEmployees;
    }

    public LiveData<List<DepartmentEntry>> getAllDepartments() {
        return allDepartments;
    }

    public LiveData<List<DepartmentEntry>> getAllDepartments(int depId) {
        allDepartments = mAppDatabase.departmentsDao().loadDepartments(depId);
        return allDepartments;
    }

    public LiveData<List<EmployeeEntry>> getRestOfEmployeesInDep(int depId, int taskId) {
        restOfEmployeesInDep = mAppDatabase.employeesDao().loadEmployeesNotInDep(depId, taskId);
        return restOfEmployeesInDep;
    }
}

package com.example.android.employeesmanagementapp.data.viewmodels;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class AddNewDepViewModel extends ViewModel {
    private LiveData<DepartmentEntry> mDepartment;
    private LiveData<List<EmployeeEntry>> mEmployees;
    private LiveData<List<TaskEntry>> mCompletedTasks;

    public AddNewDepViewModel(AppDatabase database, int depId) {
        if (depId > 0) {
            mDepartment = database.departmentsDao().loadDepartmentById(depId);
            mEmployees = database.employeesDao().loadEmployees(depId);
            mCompletedTasks = database.tasksDao().loadTasksForDepartment(depId, true);
        }
    }

    public LiveData<DepartmentEntry> getDepartment() {
        return mDepartment;
    }

    public LiveData<List<EmployeeEntry>> getEmployees() {
        return mEmployees;
    }

    public LiveData<List<TaskEntry>> getCompletedTasks() {
        return mCompletedTasks;
    }
}

package com.example.android.employeesmanagementapp.data.viewmodels;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class AddNewTaskViewModel extends ViewModel {
    private LiveData<TaskEntry> task;
    private LiveData<List<EmployeeEntry>> depEmployees;

    public AddNewTaskViewModel(AppDatabase appDatabase, int taskId, int depId) {
        if (taskId > 0) {
            task = appDatabase.tasksDao().loadTaskById(taskId);
        }
        if (depId > 0) {
            depEmployees = appDatabase.employeesDao().loadEmployees(depId);
        }
    }


    public LiveData<TaskEntry> getTask() {
        return task;
    }

    public LiveData<List<EmployeeEntry>> getDepEmployees() {
        return depEmployees;
    }
}

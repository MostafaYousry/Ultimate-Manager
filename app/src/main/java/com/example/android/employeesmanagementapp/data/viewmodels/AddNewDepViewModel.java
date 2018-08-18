package com.example.android.employeesmanagementapp.data.viewmodels;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public class AddNewDepViewModel extends ViewModel {
    public LiveData<DepartmentEntry> departmentEntry;

    public LiveData<PagedList<EmployeeEntry>> departmentEmployees;
    public LiveData<PagedList<TaskEntry>> departmentCompletedTasks;
    public LiveData<PagedList<TaskEntry>> departmentRunningTasks;

    public AddNewDepViewModel(AppDatabase database, int depId) {
        if (depId > 0) {
            departmentEntry = database.departmentsDao().loadDepartmentById(depId);

            departmentEmployees = new LivePagedListBuilder<>(database.employeesDao().loadEmployeesInDep(depId), /* page size */ 20).build();
            departmentCompletedTasks = new LivePagedListBuilder<>(database.tasksDao().loadTasksForDepartment(depId, true), /* page size */ 20).build();
            departmentRunningTasks = new LivePagedListBuilder<>(database.tasksDao().loadTasksForDepartment(depId, false), /* page size */ 20).build();
        }
    }

}

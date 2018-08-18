package com.example.android.employeesmanagementapp.data.viewmodels;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.EmployeeWithExtras;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;

public class AddNewEmployeeViewModel extends ViewModel {
    public LiveData<EmployeeWithExtras> employeeEntry;

    public LiveData<List<DepartmentEntry>> allDepartments;

    public LiveData<PagedList<TaskEntry>> employeeCompletedTasks;
    public LiveData<PagedList<TaskEntry>> employeeRunningTasks;


    public AddNewEmployeeViewModel(AppDatabase database, int empID) {
        allDepartments = database.departmentsDao().loadDepartments();

        if (empID != -1) {

            employeeEntry = database.employeesDao().loadEmployeeById(empID);

            employeeCompletedTasks = new LivePagedListBuilder<>(database.tasksDao().loadTasksForEmployee(empID, true), /* page size */ 20).build();
            employeeRunningTasks = new LivePagedListBuilder<>(database.tasksDao().loadTasksForEmployee(empID, false), /* page size */ 20).build();


        }
    }

}

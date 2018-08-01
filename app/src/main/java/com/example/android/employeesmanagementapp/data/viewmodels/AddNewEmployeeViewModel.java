package com.example.android.employeesmanagementapp.data.viewmodels;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.EmployeeWithExtras;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class AddNewEmployeeViewModel extends ViewModel {
    private LiveData<EmployeeWithExtras> mEmployee;
    private LiveData<List<DepartmentEntry>> allDepartments;
    private LiveData<List<TaskEntry>> employeeCompletedTasks;

    public AddNewEmployeeViewModel(AppDatabase database, int empID) {
        allDepartments = database.departmentsDao().loadDepartments();
        if (empID != -1) {
            mEmployee = database.employeesDao().loadEmployeeById(empID);
            employeeCompletedTasks = database.tasksDao().loadTasksForEmployee(empID, true);
        }
    }

    public LiveData<EmployeeWithExtras> getEmployee() {
        return mEmployee;
    }

    public LiveData<List<DepartmentEntry>> getAllDepartments() {
        return allDepartments;
    }

    public LiveData<List<TaskEntry>> getEmployeeCompletedTasks() {
        return employeeCompletedTasks;
    }
}

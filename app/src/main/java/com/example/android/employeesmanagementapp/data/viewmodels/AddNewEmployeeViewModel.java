package com.example.android.employeesmanagementapp.data.viewmodels;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.EmployeeWithExtras;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

public class AddNewEmployeeViewModel extends ViewModel {
    private LiveData<EmployeeWithExtras> mEmployee;
    private LiveData<List<DepartmentEntry>> allDepartments;
    private LiveData<List<TaskEntry>> employeeCompletedTasks;
    private LiveData<List<TaskEntry>> employeeRunningTasks;
    private AppDatabase mAppDatabase;


    public AddNewEmployeeViewModel(AppDatabase database, int empID) {
        mAppDatabase=database;
        allDepartments = database.departmentsDao().loadDepartments();
        if (empID != -1) {
            mEmployee = database.employeesDao().loadEmployeeById(empID);
            employeeCompletedTasks = database.tasksDao().loadTasksForEmployee(empID, true);
            employeeRunningTasks = database.tasksDao().loadTasksForEmployee(empID,false);


        }
    }

    public LiveData<EmployeeWithExtras> getEmployee() {
        return mEmployee;
    }

    public LiveData<List<DepartmentEntry>> getAllDepartments() {
        return allDepartments;
    }


    public LiveData<List<TaskEntry>> getEmployeeRunningTasks() {
        return employeeRunningTasks;
    }


    public void deleteEmployeeFromAllTasks(int empID){
        mAppDatabase.employeesTasksDao().deleteEmployeeFromAllTasks(empID);
    }

    public void deleteEmployeeFromRunningTasks(int empID){
        mAppDatabase.employeesTasksDao().deleteEmployeeFromRunningTasks(empID);}

    public LiveData<List<TaskEntry>> getEmployeeCompletedTasks() {
        return employeeCompletedTasks;
    }
}

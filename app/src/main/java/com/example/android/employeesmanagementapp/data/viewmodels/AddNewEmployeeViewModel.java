package com.example.android.employeesmanagementapp.data.viewmodels;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class AddNewEmployeeViewModel extends ViewModel {
    private LiveData<EmployeeEntry> mEmployee;
    private LiveData<List<DepartmentEntry>> allDepartments;
    private AppDatabase mDatabase;

    public AddNewEmployeeViewModel(AppDatabase database, int empID) {
        mDatabase = database;
        if (empID == -1) {
            allDepartments = database.departmentsDao().loadDepartments();
        } else {
            mEmployee = database.employeesDao().loadEmployeeById(empID);
        }
    }

    public LiveData<EmployeeEntry> getEmployee() {
        return mEmployee;
    }

    public LiveData<List<DepartmentEntry>> getAllDepartments() {
        return allDepartments;
    }

    public LiveData<List<DepartmentEntry>> getAllDepartments(int depId) {
        allDepartments = mDatabase.departmentsDao().loadDepartments(depId);
        return allDepartments;
    }
}

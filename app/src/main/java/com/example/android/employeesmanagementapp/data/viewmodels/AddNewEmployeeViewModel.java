package com.example.android.employeesmanagementapp.data.viewmodels;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class AddNewEmployeeViewModel extends ViewModel {
    private LiveData<EmployeeEntry> mEmployee;

    public AddNewEmployeeViewModel(AppDatabase database, int empID) {
        mEmployee = database.employeesDao().loadEmployeeById(empID);
    }

    public LiveData<EmployeeEntry> getEmployee() {
        return mEmployee;
    }
}

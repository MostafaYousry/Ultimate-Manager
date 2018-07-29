package com.example.android.employeesmanagementapp;

import android.content.Context;
import android.view.View;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

public class UndoDeleteAction implements View.OnClickListener {
    private EmployeeEntry mEmployeeEntry;
    private TaskEntry mTaskEntry;
    private AppDatabase mDb;

    public UndoDeleteAction(final EmployeeEntry employeeEntry, final TaskEntry taskEntry, Context context) {
        mEmployeeEntry = employeeEntry;
        mTaskEntry = taskEntry;
        mDb = AppDatabase.getInstance(context);
    }

    @Override
    public void onClick(View view) {
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mEmployeeEntry != null)
                    mDb.employeesDao().addEmployee(mEmployeeEntry);
                else
                    mDb.tasksDao().addTask(mTaskEntry);
            }
        });

    }

}

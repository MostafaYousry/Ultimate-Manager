package com.example.android.employeesmanagementapp;

import android.view.View;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

public class UndoDeleteAction implements View.OnClickListener {
    private EmployeeEntry mEmployeeEntry;
    private TaskEntry mTaskEntry;
    private AppDatabase mDb;

    public UndoDeleteAction(final EmployeeEntry employeeEntry, AppDatabase database) {
        mEmployeeEntry = employeeEntry;
        mDb = database;
    }

    public UndoDeleteAction(final TaskEntry taskEntry, AppDatabase database) {
        mTaskEntry = taskEntry;
        mDb = database;
    }


    @Override
    public void onClick(View view) {
        AppExecutor.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                if (mEmployeeEntry != null)
                    mDb.employeesDao().addEmployee(mEmployeeEntry);

                if (mTaskEntry != null)
                    mDb.tasksDao().addTask(mTaskEntry);
            }
        });

    }

}

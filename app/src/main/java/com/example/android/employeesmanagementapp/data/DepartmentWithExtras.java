package com.example.android.employeesmanagementapp.data;

import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;

public class DepartmentWithExtras {

    @Embedded
    public DepartmentEntry departmentEntry;

    @ColumnInfo(name = "num_running_tasks")
    public int numRunningTasks;

    @ColumnInfo(name = "num_completed_tasks")
    public int numCompletedTasks;

    @ColumnInfo(name = "num_of_employees")
    public int numOfEmployees;
}

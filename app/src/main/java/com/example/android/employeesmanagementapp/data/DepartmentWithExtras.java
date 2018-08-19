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


    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof DepartmentWithExtras))
            return false;

        DepartmentWithExtras entry = (DepartmentWithExtras) obj;

        return departmentEntry.equals(entry.departmentEntry)
                && Integer.compare(numRunningTasks, entry.numRunningTasks) == 0
                && Integer.compare(numCompletedTasks, entry.numCompletedTasks) == 0
                && Integer.compare(numOfEmployees, entry.numOfEmployees) == 0;

    }

}

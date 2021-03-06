package com.example.android.employeesmanagementapp.data;

import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;

import java.util.Objects;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;

/**
 * class that maps to a query result
 * the room query result is a
 * department entry object (all department entry columns),
 * column for number of running tasks in department,
 * column for number of completed tasks in department
 */
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

    @Override
    public int hashCode() {
        return Objects.hash(departmentEntry, numRunningTasks, numCompletedTasks, numOfEmployees);
    }
}

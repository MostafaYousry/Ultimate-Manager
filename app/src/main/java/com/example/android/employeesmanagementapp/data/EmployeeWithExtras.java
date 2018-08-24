package com.example.android.employeesmanagementapp.data;

import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;

import java.util.Objects;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;

/**
 * class that maps to a query result
 * the room query result is a
 * employee entry object (all employee entry columns),
 * column for employee rating,
 * column for employee's number of running tasks
 */
public class EmployeeWithExtras {

    @Embedded
    public EmployeeEntry employeeEntry;

    @ColumnInfo(name = "average_completed_task_rating")
    public Float employeeRating;

    @ColumnInfo(name = "incomplete_task_count")
    public int employeeNumRunningTasks;


    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof EmployeeWithExtras))
            return false;

        EmployeeWithExtras entry = (EmployeeWithExtras) obj;

        return employeeEntry.equals(entry.employeeEntry)
                && Float.compare(employeeRating, entry.employeeRating) == 0
                && Integer.compare(employeeNumRunningTasks, entry.employeeNumRunningTasks) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(employeeEntry, employeeRating, employeeNumRunningTasks);
    }
}

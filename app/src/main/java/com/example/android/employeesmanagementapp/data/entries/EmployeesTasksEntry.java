package com.example.android.employeesmanagementapp.data.entries;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;

/**
 * EmployeesTasks Entry POJO
 * defines columns and keys in employees_tasks table
 * and constructors for RoomDatabase
 */
@Entity(tableName = "employees_tasks",
        primaryKeys = {"employee_id", "task_id"},
        foreignKeys = {@ForeignKey(entity = EmployeeEntry.class, parentColumns = "employee_id", childColumns = "employee_id"),
                @ForeignKey(entity = TaskEntry.class, parentColumns = "task_id", childColumns = "task_id")})
public class EmployeesTasksEntry {

    @ColumnInfo(name = "employee_id")
    private int employeeId;

    @ColumnInfo(name = "task_id")
    private int taskId;

    //used for reading and creating an EmployeesTasksEntry object
    public EmployeesTasksEntry(int employeeId, int taskId) {
        this.employeeId = employeeId;
        this.taskId = taskId;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }
}

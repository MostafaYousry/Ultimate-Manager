package com.example.android.employeesmanagementapp.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

//@Entity(tableName = "employees" ,foreignKeys = {@ForeignKey(
//        entity=DepartmentEntry.class,
//        parentColumns="department_id",
//        childColumns="employee_id"),
//        @ForeignKey(
//        entity=TaskEntry.class,
//        parentColumns={"task_id",},
//        childColumns="employee_id")} , primaryKeys = {"department_id","task_id","employee_id"})
@Entity(tableName = "employees")
public class EmployeeEntry {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "employee_id")
    private int employeeID;

    @ColumnInfo(name = "task_id")
    private int taskId;

    @ColumnInfo(name = "department_id")
    private int departmentId;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "num_tasks_completed")
    private int numTasksCompleted;

    @Ignore
    public EmployeeEntry(String name, int numTasksCompleted) {
        this.name = name;
        this.numTasksCompleted = numTasksCompleted;
    }

    public EmployeeEntry(int employeeID, int taskId, int departmentId, String name, int numTasksCompleted) {
        this.employeeID = employeeID;
        this.taskId = taskId;
        this.departmentId = departmentId;
        this.name = name;
        this.numTasksCompleted = numTasksCompleted;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumTasksCompleted() {
        return numTasksCompleted;
    }

    public void setNumTasksCompleted(int numTasksCompleted) {
        this.numTasksCompleted = numTasksCompleted;
    }
}

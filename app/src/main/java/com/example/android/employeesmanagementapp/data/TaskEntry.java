package com.example.android.employeesmanagementapp.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;


//@Entity(tableName = "tasks",foreignKeys = @ForeignKey(
//        entity=DepartmentEntry.class,
//        parentColumns="department_id",
//        childColumns="task_id") , primaryKeys = {"department_id","task_id"})
@Entity(tableName = "tasks")
public class TaskEntry {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    private int taskId;

    @ColumnInfo(name = "department_id")
    private int departmentID;

    @ColumnInfo(name = "title")
    private String title;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "start_time")
    private Date startTime;

    @ColumnInfo(name = "due_date")
    private Date dueDate;

    @Ignore
    public TaskEntry(String title, String description, Date startTime, Date dueDate) {
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.dueDate = dueDate;
    }

    public TaskEntry(int taskId, int departmentID, String title, String description, Date startTime, Date dueDate) {
        this.taskId = taskId;
        this.departmentID = departmentID;
        this.title = title;
        this.description = description;
        this.startTime = startTime;
        this.dueDate = dueDate;
    }

    public int getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }
}

package com.example.android.employeesmanagementapp.data.entries;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;


@Entity(tableName = "tasks",
foreignKeys = @ForeignKey(entity = DepartmentEntry.class,parentColumns = "department_id",childColumns = "department_id"))
public class TaskEntry {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    private int taskId;

    @ColumnInfo(name = "department_id")
    private int departmentID;

    @ColumnInfo(name = "task_title")
    private String taskTitle;

    @ColumnInfo(name = "task_description")
    private String taskDescription;

    @ColumnInfo(name = "task_start_date")
    private Date taskStartDate;

    @ColumnInfo(name = "task_due_date")
    private Date taskDueDate;

    @ColumnInfo(name = "task_rating")
    private int taskRating;              //from 0 to 5

    @ColumnInfo(name = "task_is_completed")
    private boolean taskIsCompleted;


    //used when creating new tasks
    @Ignore
    public TaskEntry(int departmentID, String taskTitle, String taskDescription, Date taskStartDate, Date taskDueDate) {
        this.departmentID = departmentID;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskStartDate = taskStartDate;
        this.taskDueDate = taskDueDate;

        taskRating = 0;
        taskIsCompleted = false;
    }


    //used by room when reading from db
    public TaskEntry(int taskId, int departmentID, String taskTitle, String taskDescription, Date taskStartDate, Date taskDueDate, int taskRating, boolean taskIsCompleted) {
        this.taskId = taskId;
        this.departmentID = departmentID;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskStartDate = taskStartDate;
        this.taskDueDate = taskDueDate;
        this.taskRating = taskRating;
        this.taskIsCompleted = taskIsCompleted;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public int getDepartmentID() {
        return departmentID;
    }

    public void setDepartmentID(int departmentID) {
        this.departmentID = departmentID;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public Date getTaskStartDate() {
        return taskStartDate;
    }

    public void setTaskStartDate(Date taskStartDate) {
        this.taskStartDate = taskStartDate;
    }

    public Date getTaskDueDate() {
        return taskDueDate;
    }

    public void setTaskDueDate(Date taskDueDate) {
        this.taskDueDate = taskDueDate;
    }

    public int getTaskRating() {
        return taskRating;
    }

    public void setTaskRating(int taskRating) {
        this.taskRating = taskRating;
    }

    public boolean isTaskIsCompleted() {
        return taskIsCompleted;
    }

    public void setTaskIsCompleted(boolean taskIsCompleted) {
        this.taskIsCompleted = taskIsCompleted;
    }
}

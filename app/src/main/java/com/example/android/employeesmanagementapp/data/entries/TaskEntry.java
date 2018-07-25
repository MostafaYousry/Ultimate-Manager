package com.example.android.employeesmanagementapp.data.entries;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Task Entry POJO
 * defines columns and keys in tasks table
 * and constructors for RoomDatabase
 */
@Entity(tableName = "tasks",
        foreignKeys = @ForeignKey(entity = DepartmentEntry.class, parentColumns = "department_id", childColumns = "department_id"))
public class TaskEntry {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "task_id")
    private int taskId;

    @NonNull
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
    private float taskRating;              //from 0 to 5

    @ColumnInfo(name = "task_is_completed")
    private boolean taskIsCompleted;


    //used when creating new TaskEntry object
    @Ignore
    public TaskEntry(@NonNull int departmentID, String taskTitle, String taskDescription, Date taskStartDate, Date taskDueDate) {
        this.departmentID = departmentID;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskStartDate = taskStartDate;
        this.taskDueDate = taskDueDate;

        taskRating = 0; //rating defaults to false
        taskIsCompleted = false; //new tasks are not yet completed
    }


    //used by room when reading from database
    public TaskEntry(int taskId, int departmentID, String taskTitle, String taskDescription, Date taskStartDate, Date taskDueDate, float taskRating, boolean taskIsCompleted) {
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

    public float getTaskRating() {
        return taskRating;
    }

    public void setTaskRating(float taskRating) {
        this.taskRating = taskRating;
    }

    public boolean isTaskIsCompleted() {
        return taskIsCompleted;
    }

    public void setTaskIsCompleted(boolean taskIsCompleted) {
        this.taskIsCompleted = taskIsCompleted;
    }
}

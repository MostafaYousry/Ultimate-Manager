package com.example.android.employeesmanagementapp.data.entries;

import com.example.android.employeesmanagementapp.R;

import java.util.Calendar;
import java.util.Objects;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Task Entry POJO
 * defines columns and keys in tasks table
 * and constructors for RoomDatabase
 */
@Entity(tableName = "tasks",
        indices = {@Index(value = {"department_id"})},
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
    private Calendar taskStartDate;

    @ColumnInfo(name = "task_due_date")
    private Calendar taskDueDate;

    @ColumnInfo(name = "task_rating")
    private float taskRating;              //from 0 to 5

    @ColumnInfo(name = "task_is_completed")
    private boolean taskIsCompleted;

    @ColumnInfo(name = "task_color_resource")
    private int taskColorResource;


    //used when creating new TaskEntry object
    @Ignore
    public TaskEntry(@NonNull int departmentID, String taskTitle, String taskDescription, Calendar taskStartDate, Calendar taskDueDate) {
        this.departmentID = departmentID;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskStartDate = taskStartDate;
        this.taskDueDate = taskDueDate;

        taskColorResource = R.color.task_color_1;
    }


    //used by room when reading from database
    public TaskEntry(int taskId, int departmentID, String taskTitle, String taskDescription, Calendar taskStartDate, Calendar taskDueDate, float taskRating, boolean taskIsCompleted, int taskColorResource) {
        this.taskId = taskId;
        this.departmentID = departmentID;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskStartDate = taskStartDate;
        this.taskDueDate = taskDueDate;
        this.taskRating = taskRating;
        this.taskIsCompleted = taskIsCompleted;
        this.taskColorResource = taskColorResource;
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

    public Calendar getTaskStartDate() {
        return taskStartDate;
    }

    public void setTaskStartDate(Calendar taskStartDate) {
        this.taskStartDate = taskStartDate;
    }

    public Calendar getTaskDueDate() {
        return taskDueDate;
    }

    public void setTaskDueDate(Calendar taskDueDate) {
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

    public int getTaskColorResource() {
        return taskColorResource;
    }

    public void setTaskColorResource(int taskColorResource) {
        this.taskColorResource = taskColorResource;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof TaskEntry))
            return false;

        TaskEntry entry = (TaskEntry) obj;

        return Integer.compare(taskId, entry.taskId) == 0
                && Integer.compare(departmentID, entry.departmentID) == 0
                && taskTitle.equals(entry.taskTitle)
                && taskDescription.equals(entry.taskDescription)
                && taskStartDate.equals(entry.taskStartDate)
                && taskDueDate.equals(entry.taskDueDate)
                && Float.compare(taskRating, entry.taskRating) == 0
                && Boolean.compare(taskIsCompleted, entry.taskIsCompleted) == 0
                && Integer.compare(taskColorResource, entry.taskColorResource) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId, departmentID, taskTitle, taskDescription, taskStartDate, taskDueDate, taskRating, taskIsCompleted, taskColorResource);
    }
}

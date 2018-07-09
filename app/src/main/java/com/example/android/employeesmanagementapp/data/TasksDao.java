package com.example.android.employeesmanagementapp.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TasksDao {

    @Query("SELECT * FROM tasks")
    List<TaskEntry> loadAllTasks();


//    List<TaskEntry> loadTasksByDepartment();

    //List<TaskEntry> loadTasksByEmployee();

    @Insert
    void addTask(TaskEntry taskEntry);

    @Delete
    void deleteTask(TaskEntry taskEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(TaskEntry taskEntry);
}

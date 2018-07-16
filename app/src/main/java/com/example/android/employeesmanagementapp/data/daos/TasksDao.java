package com.example.android.employeesmanagementapp.data.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

import java.util.List;

@Dao
public interface TasksDao {

    @Query("Select * From tasks WHERE task_is_completed = 0")
    List<TaskEntry> loadAllRunningTasks();

    @Query("SELECT * FROM tasks WHERE task_is_completed = 1")
    List<TaskEntry> loadAllCompletedTasks();

    @Insert
    long addTask(TaskEntry taskEntry);

    @Delete
    void deleteTask(TaskEntry taskEntry);

    @Update
    void  updateTask(TaskEntry taskEntry);


    //load tasks done by a certain employee
    @Query("select  tasks.task_id , department_id , task_title , task_description,task_start_date,task_due_date,task_rating,task_is_completed from tasks " +
            "inner join employees_tasks on tasks.task_id = employees_tasks.task_id where employees_tasks.employee_id = :employeeId AND task_is_completed = 1")
    List<TaskEntry> loadCompletedTasksByEmployeeId(int employeeId);



    //load tasks done by a certain employee
    @Query("select  tasks.task_id , department_id , task_title , task_description,task_start_date,task_due_date,task_rating,task_is_completed from tasks " +
            "inner join employees_tasks on tasks.task_id = employees_tasks.task_id where employees_tasks.employee_id = :employeeId AND task_is_completed = 0")
    List<TaskEntry> loadRunningTasksByEmployeeId(int employeeId);


    @Query("SELECT * FROM tasks WHERE department_id = :departmentId AND task_is_completed = 1")
    List<TaskEntry> loadCompletedTasksByDepartmentId(int departmentId);


    @Query("SELECT * FROM tasks WHERE department_id = :departmentId AND task_is_completed = 0")
    List<TaskEntry> loadRunningTasksByDepartmentId(int departmentId);


}

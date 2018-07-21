package com.example.android.employeesmanagementapp.data.daos;

import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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

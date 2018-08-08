package com.example.android.employeesmanagementapp.data.daos;

import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

/**
 * Tasks entry Data Access Object
 * <p>
 * defines how to read/write operations are done to Tasks Entry (database table)
 **/
@Dao
public interface TasksDao {

    /**
     * load all tasks that are running
     *
     * @return list of TaskEntry objects wrapped with LiveData
     */
    @Query("SELECT * FROM tasks WHERE task_is_completed = 0")
    LiveData<List<TaskEntry>> loadRunningTasks();

    /**
     * load all tasks that are running
     *
     * @return list of TaskEntry objects wrapped with LiveData
     */
    @Query("SELECT * FROM tasks WHERE task_is_completed = 1")
    LiveData<List<TaskEntry>> loadCompletedTasks();

    /**
     * load all tasks that are running or completed for an existing employee
     *
     * @param taskIsCompleted: true for completed tasks / false for running tasks
     * @param employeeId:      the employee record id for loading tasks for
     * @return list of TaskEntry objects wrapped with LiveData
     */
    @Query("select  tasks.task_id , department_id , task_title , task_description,task_start_date,task_due_date,task_rating,task_is_completed from tasks " +
            "inner join employees_tasks on tasks.task_id = employees_tasks.task_id where employees_tasks.employee_id = :employeeId AND task_is_completed = :taskIsCompleted")
    LiveData<List<TaskEntry>> loadTasksForEmployee(int employeeId, boolean taskIsCompleted);

    /**
     * load all tasks that are running or completed for an existing department
     *
     * @param taskIsCompleted: true for completed tasks / false for running tasks
     * @param departmentId:    the department record id for loading tasks for
     * @return list of TaskEntry objects wrapped with LiveData
     */
    @Query("SELECT * FROM tasks WHERE department_id = :departmentId AND task_is_completed = :taskIsCompleted")
    LiveData<List<TaskEntry>> loadTasksForDepartment(int departmentId, boolean taskIsCompleted);

    /**
     * loads an existing task record
     *
     * @param taskId : the id of the task record to be loaded
     * @return TaskEntry object wrapped with LiveData
     */
    @Query("SELECT * FROM tasks WHERE task_id = :taskId")
    LiveData<TaskEntry> loadTaskById(int taskId);

    /**
     * insert a new task record
     *
     * @param taskEntry
     * @return row id of the inserted task record
     */
    @Insert
    long addTask(TaskEntry taskEntry);

    /**
     * deletes an existing task record
     *
     * @param taskEntry
     */
    @Delete
    void deleteTask(TaskEntry taskEntry);

    /**
     * updates columns of an existing task record
     *
     * @param taskEntry
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(TaskEntry taskEntry);


}

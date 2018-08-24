package com.example.android.employeesmanagementapp.data.daos;

import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

import java.util.Calendar;
import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
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
     * @return Data source factory to be used in paged list adapter for paging lists
     */
    @Query("SELECT * FROM tasks WHERE task_is_completed = 0")
    DataSource.Factory<Integer, TaskEntry> loadRunningTasks();

    /**
     * load all tasks that are completed
     *
     * @return Data source factory to be used in paged list adapter for paging lists
     */
    @Query("SELECT * FROM tasks WHERE task_is_completed = 1")
    DataSource.Factory<Integer, TaskEntry> loadCompletedTasks();

    /**
     * load all tasks that are running or completed for an existing employee
     *
     * @param taskIsCompleted: true for completed tasks / false for running tasks
     * @param employeeId:      the employee record id for loading tasks for
     * @return Data source factory to be used in paged list adapter for paging lists
     */
    @Query("select  tasks.task_id , department_id , task_title , task_description,task_start_date,task_due_date,task_rating,task_is_completed , task_color_resource from tasks " +
            "inner join employees_tasks on tasks.task_id = employees_tasks.task_id where employees_tasks.employee_id = :employeeId AND task_is_completed = :taskIsCompleted")
    DataSource.Factory<Integer, TaskEntry> loadTasksForEmployee(int employeeId, boolean taskIsCompleted);

    /**
     * load all tasks that are running or completed for an existing department
     *
     * @param taskIsCompleted: true for completed tasks / false for running tasks
     * @param departmentId:    the department record id for loading tasks for
     * @return list of TaskEntry objects wrapped with LiveData
     */
    @Query("SELECT * FROM tasks WHERE department_id = :departmentId AND task_is_completed = :taskIsCompleted")
    DataSource.Factory<Integer, TaskEntry> loadTasksForDepartment(int departmentId, boolean taskIsCompleted);

    /**
     * loads an existing taskEntry record
     *
     * @param taskId : the id of the taskEntry record to be loaded
     * @return TaskEntry object wrapped with LiveData
     */
    @Query("SELECT * FROM tasks WHERE task_id = :taskId")
    LiveData<TaskEntry> loadTaskById(int taskId);

    @Query("SELECT task_due_date FROM tasks WHERE task_is_completed = 0")
    List<Calendar> getAllTasksDueDate();

    @Query("SELECT task_id FROM tasks WHERE task_is_completed = 0")
    List<Integer> getAllTasksId();

    /**
     * update rating of a completed taskEntry
     *
     * @param taskRating : int number of stars
     * @param taskID
     */
    @Query("UPDATE tasks SET task_rating = :taskRating , task_is_completed = 1 WHERE task_id=:taskID")
    void rateTask(float taskRating, int taskID);

    /**
     * update task color
     *
     * @param colorRes : color resource
     * @param taskID
     */
    @Query("UPDATE tasks SET task_color_resource = :colorRes WHERE task_id=:taskID")
    void updateTaskColor(int colorRes, int taskID);

    /**
     * deletes tasks with no employees if exists
     */
    @Query("delete from tasks where (SELECT COUNT(*) FROM employees INNER JOIN employees_tasks ON employees.employee_id=employees_tasks.employee_id WHERE employees_tasks.task_id= tasks.task_id )=0")
    void deleteTasksWithNoEmployees();

    /**
     * insert a new taskEntry record
     *
     * @param taskEntry
     * @return row id of the inserted taskEntry record
     */
    @Insert
    long addTask(TaskEntry taskEntry);

    /**
     * deletes an existing taskEntry record
     *
     * @param taskEntry
     */
    @Delete
    void deleteTask(TaskEntry taskEntry);

    /**
     * updates columns of an existing taskEntry record
     *
     * @param taskEntry
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(TaskEntry taskEntry);

    /**
     * update rating of a completed taskEntry
     *
     * @param taskRating : int number of stars
     * @param taskID
     */
    @Query("UPDATE tasks SET task_rating = :taskRating , task_is_completed = 1 WHERE task_id=:taskID")
    void rateTask(float taskRating, int taskID);


    /**
     * update taskEntry color
     *
     * @param colorRes : color resource
     * @param taskID
     */
    @Query("UPDATE tasks SET task_color_resource = :colorRes WHERE task_id=:taskID")
    void updateTaskColor(int colorRes, int taskID);

    @Query("delete from tasks where (SELECT COUNT(*) FROM employees INNER JOIN employees_tasks ON employees.employee_id=employees_tasks.employee_id WHERE employees_tasks.task_id= tasks.task_id )=0")
    void deleteEmptyTasks();

    @Query("SELECT task_id from tasks where (SELECT COUNT(*) FROM employees INNER JOIN employees_tasks ON employees.employee_id=employees_tasks.employee_id WHERE employees_tasks.task_id= tasks.task_id )=0")
    List<Integer> selectEmptyTasksId();

}

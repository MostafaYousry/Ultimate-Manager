package com.example.android.employeesmanagementapp.data.daos;

import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeesTasksEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

/**
 * Employees_ Task(join table) Data Access Object
 * <p>
 * defines how to read/write operations are done to Employees Tasks Entry (database table)
 **/
@Dao
public interface EmployeesTasksDao {


    @Query("SELECT * FROM employees " +
            "INNER JOIN employees_tasks " +
            "ON employees.employee_id=employees_tasks.employee_id " +
            "WHERE employees_tasks.task_id=:taskId")
    LiveData<List<EmployeeEntry>> getEmployeesForTask(final int taskId);

    @Query("SELECT * FROM tasks " +
            "INNER JOIN employees_tasks " +
            "ON tasks.task_id=employees_tasks.task_id " +
            "WHERE employees_tasks.employee_id=:empId")
    List<TaskEntry> getTasksForEmployee(final int empId);


    @Query("SELECT COUNT(*) FROM tasks " +
            "INNER JOIN employees_tasks " +
            "ON tasks.task_id=employees_tasks.task_id " +
            "WHERE employees_tasks.employee_id=:empId AND tasks.task_is_completed = 1")
    int getNumCompletedTasksEmployee(int empId);

    /**
     * insert a new employees tasks record
     *
     * @param employeesTasksEntry
     */
    @Insert
    void addEmployeeTask(EmployeesTasksEntry employeesTasksEntry);

    /**
     * delete an existing employees tasks record
     *
     * @param employeesTasksEntry
     */
    @Delete
    void deleteEmployeeTask(EmployeesTasksEntry employeesTasksEntry);

    @Query("DELETE FROM employees_tasks WHERE employee_id = :empID")
    void deleteEmployeeFromAllTasks(int empID);

    @Query("delete from employees_tasks where task_id in(" +
            "select employees_tasks.task_id from employees_tasks" +
            " inner join tasks on (tasks.task_id = employees_tasks.task_id and tasks.task_is_completed = 0) where" +
            " employees_tasks.employee_id = :empID )and employee_id  = :empID")
    void deleteEmployeeFromRunningTasks(int empID);

    @Query("delete from employees_tasks where task_id = :taskId")
    void deleteDepartmentRunningTasks(int taskId);

}

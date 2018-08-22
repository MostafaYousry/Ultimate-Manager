package com.example.android.employeesmanagementapp.data.daos;

import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeesTasksEntry;

import androidx.paging.DataSource;
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

    /**
     * loads all employees assigned to a certain task
     *
     * @param taskId : task id
     * @return Data source factory to be used in paged list adapter for paging lists
     */
    @Query("SELECT employees.* FROM employees " +
            "INNER JOIN employees_tasks " +
            "ON employees.employee_id=employees_tasks.employee_id " +
            "WHERE employees_tasks.task_id=:taskId")
    DataSource.Factory<Integer, EmployeeEntry> getEmployeesForTask(int taskId);

    /**
     * @param empId :employee id
     * @return number of completed tasks for an employee
     */
    @Query("SELECT COUNT(*) FROM tasks " +
            "INNER JOIN employees_tasks " +
            "ON tasks.task_id=employees_tasks.task_id " +
            "WHERE employees_tasks.employee_id=:empId AND tasks.task_is_completed = 1")
    int getNumCompletedTasksEmployee(int empId);

    /**
     * @param depID : department id
     * @return number of completed tasks for a department
     */
    @Query("select count(*) from tasks where tasks.task_is_completed = 1 And tasks.department_id = :depID")
    int getNumCompletedTasksDepartment(int depID);

    /**
     * removes employee from all of his running tasks
     *
     * @param empID : employee id
     */
    @Query("delete from employees_tasks where task_id in(" +
            "select employees_tasks.task_id from employees_tasks" +
            " inner join tasks on (tasks.task_id = employees_tasks.task_id and tasks.task_is_completed = 0) where" +
            " employees_tasks.employee_id = :empID )and employee_id  = :empID")
    void deleteEmployeeFromRunningTasks(int empID);

    /**
     * delete all join records that links this employee to any task
     *
     * @param empID : employee id
     */
    @Query("DELETE FROM employees_tasks WHERE employee_id = :empID")
    void deleteEmployeeJoinRecords(int empID);

    /**
     * delete all join records that links this task to any employees
     *
     * @param taskId : task id
     */
    @Query("delete from employees_tasks where task_id = :taskId")
    void deleteTaskJoinRecords(int taskId);

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
}

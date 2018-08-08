package com.example.android.employeesmanagementapp.data.daos;

import com.example.android.employeesmanagementapp.data.EmployeeWithExtras;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


/**
 * Employees entry Data Access Object
 * <p>
 * defines how to read/write operations are done to Employees Entry (database table)
 **/
@Dao
public interface EmployeesDao {

    /**
     * load all employees
     *
     * @return list of EmployeeWithExtras objects wrapped with LiveData
     */
    @Query("WITH \n" +
            "  -- Common Table Expression 1 - Average of Completed Tasks per employee\n" +
            "    employee_completedtask_info AS (\n" +
            "        SELECT employees.employee_id,avg(tasks.task_rating) AS atr\n" +
            "            FROM employees_tasks\n" +
            "                JOIN tasks ON employees_tasks.task_id = tasks.task_id\n" +
            "                JOIN employees ON employees_tasks.employee_id = employees.employee_id\n" +
            "            WHERE tasks.task_is_completed > 0\n" +
            "            GROUP BY employees.employee_id\n" +
            "    ),\n" +
            "    -- Common Table Expression 2 - Incompleted Taks per employee\n" +
            "    employee_notcompleted_info AS (\n" +
            "        SELECT employees.employee_id,count() AS itc\n" +
            "            FROM employees_tasks\n" +
            "                JOIN tasks ON employees_tasks.task_id = tasks.task_id\n" +
            "                JOIN employees ON employees_tasks.employee_id = employees.employee_id\n" +
            "            WHERE tasks.task_is_completed = 0\n" +
            "            GROUP BY employees.employee_id\n" +
            "    )\n" +
            "    SELECT employees.employee_id,employees.department_id,employees.employee_name , employees.employee_salary,employees.employee_hire_date,employees.employee_is_deleted,\n" +
            "        CASE WHEN atr IS NOT NULL THEN atr ELSE 0 END AS average_completed_task_rating,\n" +
            "        CASE WHEN itc IS NOT NULL THEN itc ELSE 0 END AS incomplete_task_count\n" +
            "        FROM employees \n" +
            "            LEFT JOIN employee_completedtask_info ON employees.employee_id = employee_completedtask_info.employee_id\n" +
            "            LEFT JOIN employee_notcompleted_info ON employees.employee_id = employee_notcompleted_info.employee_id\n" +
            " \tWHERE employees.employee_is_deleted = 0   ;")
    LiveData<List<EmployeeWithExtras>> loadEmployees();

    /**
     * load all employees in an existing department
     *
     * @param departmentId : the department's record id to get the employees for
     * @return list of EmployeeWithExtras objects wrapped with LiveData
     */
    @Query("Select * from employees where department_id = :departmentId AND employee_is_deleted = 0")
    LiveData<List<EmployeeEntry>> loadEmployees(int departmentId);

    /**
     * load all employees not in this department
     *
     * @return list of EmployeeWithExtras objects wrapped with LiveData
     */
    @Query(" SELECT * from employees where department_id = :departmentId Except SELECT DISTINCT employees.* from employees  INNER JOIN employees_tasks ON employees.employee_id = employees_tasks.employee_id where employees_tasks.task_id = :taskId and employees.department_id = :departmentId AND employee_is_deleted = 0")
    LiveData<List<EmployeeEntry>> loadEmployeesNotInDep(int departmentId, int taskId);

    /**
     * load an existing employee record by it's id
     *
     * @param employeeId : the employee record's id
     * @return EmployeeWithExtras object wrapped with LiveData
     */
    @Query("WITH \n" +
            "  -- Common Table Expression 1 - Average of Completed Tasks per employee\n" +
            "    employee_completedtask_info AS (\n" +
            "        SELECT employees.employee_id,avg(tasks.task_rating) AS atr\n" +
            "            FROM employees_tasks\n" +
            "                JOIN tasks ON employees_tasks.task_id = tasks.task_id\n" +
            "                JOIN employees ON employees_tasks.employee_id = employees.employee_id\n" +
            "            WHERE tasks.task_is_completed > 0\n" +
            "            GROUP BY employees.employee_id\n" +
            "    ),\n" +
            "    -- Common Table Expression 2 - Incompleted Taks per employee\n" +
            "    employee_notcompleted_info AS (\n" +
            "        SELECT employees.employee_id,count() AS itc\n" +
            "            FROM employees_tasks\n" +
            "                JOIN tasks ON employees_tasks.task_id = tasks.task_id\n" +
            "                JOIN employees ON employees_tasks.employee_id = employees.employee_id\n" +
            "            WHERE tasks.task_is_completed = 0\n" +
            "            GROUP BY employees.employee_id\n" +
            "    )\n" +
            "    SELECT employees.employee_id,employees.department_id,employees.employee_name , employees.employee_salary,employees.employee_hire_date,employees.employee_is_deleted,\n" +
            "        CASE WHEN atr IS NOT NULL THEN atr ELSE 0 END AS average_completed_task_rating,\n" +
            "        CASE WHEN itc IS NOT NULL THEN itc ELSE 0 END AS incomplete_task_count\n" +
            "        FROM employees \n" +
            "            LEFT JOIN employee_completedtask_info ON employees.employee_id = employee_completedtask_info.employee_id\n" +
            "            LEFT JOIN employee_notcompleted_info ON employees.employee_id = employee_notcompleted_info.employee_id\n" +
            "\tWHERE employees.employee_id = :employeeId;")
    LiveData<EmployeeWithExtras> loadEmployeeById(int employeeId);


    /**
     * @return : number of employees in the company
     */
    @Query("SELECT COUNT(*) FROM employees where employee_is_deleted = 0")
    int getNumEmployees();

    /**
     * insert a new employee record
     *
     * @param employeeEntry
     */
    @Insert
    void addEmployee(EmployeeEntry employeeEntry);

    /**
     * delete an existing employee record
     *
     * @param employeeEntry
     */
    @Delete
    void deleteEmployee(EmployeeEntry employeeEntry);

    /**
     * update columns of an existing employee record
     *
     * @param employeeEntry
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateEmployee(EmployeeEntry employeeEntry);

    /**
     * update columns of an existing employee record to fire him
     *
     * @param empID
     */
    @Query("UPDATE employees SET employee_is_deleted = 1 WHERE employee_id=:empID")
    void deleteEmployeeFromCompletedTask(int empID);

    @Query("UPDATE employees SET employee_is_deleted = 1 WHERE employee_id=:empId")
    void deleteEmployeeFromDepartmentTask(int empId);

}

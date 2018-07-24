package com.example.android.employeesmanagementapp.data.daos;

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
     * @return list of EmployeeEntry objects wrapped with LiveData
     */
    @Query("Select * From employees")
    LiveData<List<EmployeeEntry>> loadEmployees();

    /**
     * load all employees in an existing department
     *
     * @param departmentId : the department's record id to get the employees for
     * @return list of EmployeeEntry objects wrapped with LiveData
     */
    @Query("Select * from employees where department_id = :departmentId")
    LiveData<List<EmployeeEntry>> loadEmployees(int departmentId);

    /**
     * load an existing employee record by it's id
     *
     * @param employeeId : the employee record's id
     * @return EmployeeEntry object wrapped with LiveData
     */
    @Query("SELECT * FROM employees WHERE employee_id = :employeeId")
    LiveData<EmployeeEntry> loadEmployeeById(int employeeId);

    /**
     * @return : number of employees in the company
     */
    @Query("SELECT COUNT(*) FROM employees")
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

}

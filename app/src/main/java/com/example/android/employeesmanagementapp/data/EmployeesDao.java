package com.example.android.employeesmanagementapp.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface EmployeesDao {

    @Query("SELECT * FROM employees")
    List<EmployeeEntry> loadAllEmployees();

    @Query("SELECT * FROM employees WHERE department_id = :depId")
    List<EmployeeEntry> loadEmployeesByDepartment(int depId);

    @Query("SELECT * FROM employees WHERE task_id = :taskID")
    List<EmployeeEntry> loadEmployeesByTask(int taskID);

    @Insert
    void hireEmployee(EmployeeEntry employeeEntry);

    @Delete
    void fireEmployee(EmployeeEntry employeeEntry);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateEmployee(EmployeeEntry employeeEntry);
}

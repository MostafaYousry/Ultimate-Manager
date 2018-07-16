package com.example.android.employeesmanagementapp.data.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;

import java.util.List;

@Dao
public interface EmployeesDao {

    @Query("Select * From employees")
    List<EmployeeEntry> loadAllEmployees();

    @Insert
    void addEmployee(EmployeeEntry employeeEntry);

    @Delete
    void deleteEmployee(EmployeeEntry employeeEntry);

    @Update
    void  updateEmployee(EmployeeEntry employeeEntry);

    @Query("Select * from employees where department_id = :departmentId")
    List<EmployeeEntry>loadEmployeesByDepartmentId(int departmentId);

}

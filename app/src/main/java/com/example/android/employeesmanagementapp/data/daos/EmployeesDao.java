package com.example.android.employeesmanagementapp.data.daos;

import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

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

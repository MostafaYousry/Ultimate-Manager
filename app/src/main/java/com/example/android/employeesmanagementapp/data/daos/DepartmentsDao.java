package com.example.android.employeesmanagementapp.data.daos;

import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

@Dao
public interface DepartmentsDao {

    @Query("Select * From departments")
    List<DepartmentEntry> loadAllDepartments();

    @Insert
    void addDepartment(DepartmentEntry departmentEntry);

    @Delete
    void deleteDepartment(DepartmentEntry departmentEntry);

    @Update
    void  updateDepartment(DepartmentEntry departmentEntry);


}

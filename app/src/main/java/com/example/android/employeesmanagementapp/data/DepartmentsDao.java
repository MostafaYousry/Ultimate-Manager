package com.example.android.employeesmanagementapp.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface DepartmentsDao {

    @Query("SELECT * FROM departments")
    List<DepartmentEntry> loadAllDepartments();

    @Insert
    void addDepartment(DepartmentEntry departmentEntry);

    @Delete
    void deleteDepartment(DepartmentEntry departmentEntry);
}

package com.example.android.employeesmanagementapp.data.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;

import java.util.List;

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

package com.example.android.employeesmanagementapp.data.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;

import com.example.android.employeesmanagementapp.data.entries.EmployeesTasksEntry;

@Dao
public interface EmployeesTasksDao {

    @Insert
    void addEmployeeTask(EmployeesTasksEntry employeesTasksEntry);


    @Delete
    void deleteEmployeeTask(EmployeesTasksEntry employeesTasksEntry);
}

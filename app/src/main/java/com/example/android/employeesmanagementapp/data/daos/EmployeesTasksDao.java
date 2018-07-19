package com.example.android.employeesmanagementapp.data.daos;

import com.example.android.employeesmanagementapp.data.entries.EmployeesTasksEntry;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

@Dao
public interface EmployeesTasksDao {

    @Insert
    void addEmployeeTask(EmployeesTasksEntry employeesTasksEntry);


    @Delete
    void deleteEmployeeTask(EmployeesTasksEntry employeesTasksEntry);
}

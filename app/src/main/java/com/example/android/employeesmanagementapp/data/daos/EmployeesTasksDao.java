package com.example.android.employeesmanagementapp.data.daos;

import com.example.android.employeesmanagementapp.data.entries.EmployeesTasksEntry;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;

/**
 * Employees_ Task(join table) Data Access Object
 * <p>
 * defines how to read/write operations are done to Employees Tasks Entry (database table)
 **/
@Dao
public interface EmployeesTasksDao {

    /**
     * insert a new employees tasks record
     *
     * @param employeesTasksEntry
     */
    @Insert
    void addEmployeeTask(EmployeesTasksEntry employeesTasksEntry);

    /**
     * delete an existing employees tasks record
     *
     * @param employeesTasksEntry
     */
    @Delete
    void deleteEmployeeTask(EmployeesTasksEntry employeesTasksEntry);
}

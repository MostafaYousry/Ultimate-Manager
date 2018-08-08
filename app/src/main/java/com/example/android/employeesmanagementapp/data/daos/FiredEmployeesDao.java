package com.example.android.employeesmanagementapp.data.daos;

import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeesTasksEntry;
import com.example.android.employeesmanagementapp.data.entries.FiredEmployeeEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

/**
 * Employees_ Task(join table) Data Access Object
 * <p>
 * defines how to read/write operations are done to Employees Tasks Entry (database table)
 **/
@Dao
public interface FiredEmployeesDao {


    @Query("SELECT * FROM fired_employees WHERE employee_id = :empId")
    LiveData<EmployeeEntry> getFriedEmployeeById(final int empId);

    @Query("SELECT * FROM fired_employees")
    LiveData<List<EmployeeEntry>> getFiredEmployees();


    /**
     * insert a new fired employee record
     *
     * @param firedEmployeeEntry
     */
    @Insert
    void addFiredEmployee(EmployeeEntry firedEmployeeEntry);

    /**
     * delete an existing fired employee record
     *
     * @param firedEmployeeEntry
     */
    @Delete
    void deleteFiredEmployee(EmployeeEntry firedEmployeeEntry);

    @Query("DELETE FROM fired_employees WHERE employee_id = :empID")
    void deleteFiredEmployeeByID(int empID);


    /**
     * update columns of an existing fired employee record
     *
     * @param firedEmployeeEntry
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateFiredEmployee(EmployeeEntry firedEmployeeEntry);




}

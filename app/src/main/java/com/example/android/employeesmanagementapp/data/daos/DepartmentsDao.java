package com.example.android.employeesmanagementapp.data.daos;

import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;


/**
 * Departments entry Data Access Object
 * <p>
 * defines how to read/write operations are done to Departments Entry (database table)
 **/
@Dao
public interface DepartmentsDao {

    /**
     * load all  deleted departments
     *
     * @return list of DepartmentEntry objects wrapped with LiveData
     */
    @Query("Select * From departments ")
    LiveData<List<DepartmentEntry>> loadDepartments();


    /**
     * load all non deleted departments
     *
     * @return list of DepartmentEntry objects wrapped with LiveData
     */
    @Query("Select * From departments where department_is_deleted = 0")
    LiveData<List<DepartmentEntry>> loadNonDeletedDepartments();

    /**
     * load a department record by it's id
     *
     * @param departmentId : the department record's id
     * @return DepartmentEntry object wrapped with LiveData
     */
    @Query("SELECT * FROM departments WHERE department_id = :departmentId")
    LiveData<DepartmentEntry> loadDepartmentById(int departmentId);

    /**
     * loads all departments with one chosen department on top
     *
     * @param firstRowDepartmentID : department's id to put on top
     * @return list of DepartmentEntry objects wrapped with LiveData
     */
    @Query("SELECT * FROM departments WHERE department_id = :firstRowDepartmentID UNION ALL SELECT * FROM departments WHERE department_id != :firstRowDepartmentID")
    LiveData<List<DepartmentEntry>> loadDepartments(int firstRowDepartmentID);


    /**
     * @return : number of departments in the company
     */
    @Query("SELECT COUNT(*) FROM departments where department_is_deleted = 0 ")
    int getNumDepartments();

    /**
     * insert a new department record
     *
     * @param departmentEntry
     */
    @Insert
    void addDepartment(DepartmentEntry departmentEntry);

    /**
     * delete an existing department record
     *
     * @param departmentEntry
     */
    @Delete
    void deleteDepartment(DepartmentEntry departmentEntry);

    /**
     * update columns of an existing department record
     *
     * @param departmentEntry
     */
    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateDepartment(DepartmentEntry departmentEntry);


}

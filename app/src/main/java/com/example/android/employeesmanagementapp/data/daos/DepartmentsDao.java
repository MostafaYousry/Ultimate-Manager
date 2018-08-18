package com.example.android.employeesmanagementapp.data.daos;

import com.example.android.employeesmanagementapp.data.DepartmentWithExtras;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;

import java.util.List;

import androidx.lifecycle.LiveData;
import androidx.paging.DataSource;
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
     * load all departments with their extras
     *
     * @return list of DepartmentWithExtras objects wrapped with LiveData
     */
    @Query("Select * ,\n" +
            "(select count(tasks.task_title) from tasks where tasks.task_is_completed = 0 And tasks.department_id = departments.department_id) as num_running_tasks,\n" +
            "(select count(tasks.task_title) from tasks where tasks.task_is_completed = 1 And tasks.department_id = departments.department_id) as num_completed_tasks,\n" +
            "(select count(employees.employee_first_name) from employees where employees.employee_is_deleted = 0 And employees.department_id = departments.department_id) as num_of_employees \n" +
            "From departments where department_is_deleted = 0")
    DataSource.Factory<Integer, DepartmentWithExtras> loadDepartmentsWithExtras();


    /**
     * load all departments
     *
     * @return list of DepartmentEntry objects wrapped with LiveData
     */
    @Query("Select * from departments where department_is_deleted = 0")
    LiveData<List<DepartmentEntry>> loadDepartments();

    @Query("select count(*) from tasks where tasks.task_is_completed = 1 And tasks.department_id = :depID")
    int getNumCompletedTasksDepartment(int depID);

    /**
     * load a department record by it's id
     *
     * @param departmentId : the department record's id
     * @return DepartmentEntry object wrapped with LiveData
     */
    @Query("SELECT * FROM departments WHERE department_id = :departmentId")
    LiveData<DepartmentEntry> loadDepartmentById(int departmentId);

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

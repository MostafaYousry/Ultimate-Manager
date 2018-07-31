package com.example.android.employeesmanagementapp.data.daos;

import com.example.android.employeesmanagementapp.data.DepartmentWithEmpsAndTasks;

import java.util.List;

import androidx.room.Dao;
import androidx.room.Query;

@Dao
public interface DepartmentWithEmpsAndTasksDao {

    @Query("SELECT * from departments where department_id = :depId")
    List<DepartmentWithEmpsAndTasks> getDepartmentWithEmpsAndTasks(int depId);
}

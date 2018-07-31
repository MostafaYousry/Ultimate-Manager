package com.example.android.employeesmanagementapp.data;

import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

import java.util.List;

import androidx.room.Embedded;
import androidx.room.Relation;

public class DepartmentWithEmpsAndTasks {

    @Embedded
    DepartmentEntry departmentEntry;

    @Relation(parentColumn = "department_id",
            entityColumn = "department_id")
    List<EmployeeEntry> departmentEmployees;

    @Relation(parentColumn = "department_id",
            entityColumn = "department_id")
    List<TaskEntry> departmentTasks;
}

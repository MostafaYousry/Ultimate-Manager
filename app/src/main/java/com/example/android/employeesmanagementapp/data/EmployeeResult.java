package com.example.android.employeesmanagementapp.data;


import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;

import java.util.Date;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;

public class EmployeeResult extends EmployeeEntry {

    @Embedded
    @ColumnInfo(name = "employee_id")
    public int employeeID;

    @Embedded
    @ColumnInfo(name = "employee_first_name")
    public String employeeFirstName;

    @Embedded
    @ColumnInfo(name = "employee_middle_name")
    public String employeeMiddleName;

    @Embedded
    @ColumnInfo(name = "employee_last_name")
    public String employeeLastName;

    @Embedded
    @ColumnInfo(name = "employee_image_uri")
    public String employeeImageUri;

    @ColumnInfo(name = "average_completed_task_rating")
    public Float employeeRating;

    @ColumnInfo(name = "incomplete_task_count")
    public int employeeNumRunningTasks;

    public EmployeeResult(int employeeID, int departmentId, String employeeFirstName, String employeeMiddleName, String employeeLastName, int employeeSalary, Date employeeHireDate, String employeeEmail, String employeePhone, String employeeNote, String employeeImageUri, boolean employeeIsDeleted) {
        super(employeeID, departmentId, employeeFirstName, employeeMiddleName, employeeLastName, employeeSalary, employeeHireDate, employeeEmail, employeePhone, employeeNote, employeeImageUri, employeeIsDeleted);
    }
}

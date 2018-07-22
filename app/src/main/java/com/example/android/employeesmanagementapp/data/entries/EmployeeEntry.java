package com.example.android.employeesmanagementapp.data.entries;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Employee Entry POJO
 * defines columns and keys in employees table
 * and constructors for RoomDatabase
 */
@Entity(tableName = "employees",
        foreignKeys = @ForeignKey(entity = DepartmentEntry.class,parentColumns = "department_id",childColumns = "department_id"))
public class EmployeeEntry {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "employee_id")
    private int employeeID;

    @ColumnInfo(name = "department_id")
    private int departmentId;

    @NonNull
    @ColumnInfo(name = "employee_name")
    private String employeeName;

    @NonNull
    @ColumnInfo(name = "employee_salary")
    private int employeeSalary;

    @ColumnInfo(name = "employee_hire_date")
    private Date employeeHireDate;

    //used when creating new EmployeeEntry object
    @Ignore
    public EmployeeEntry(int departmentId, @NonNull String employeeName, @NonNull int employeeSalary, Date employeeHireDate) {
        this.departmentId = departmentId;
        this.employeeName = employeeName;
        this.employeeSalary = employeeSalary;
        this.employeeHireDate = employeeHireDate;
    }

    //used by room when reading from database
    public EmployeeEntry(int employeeID, int departmentId, @NonNull String employeeName, @NonNull int employeeSalary, Date employeeHireDate) {
        this.employeeID = employeeID;
        this.departmentId = departmentId;
        this.employeeName = employeeName;
        this.employeeSalary = employeeSalary;
        this.employeeHireDate = employeeHireDate;
    }

    public int getEmployeeID() {
        return employeeID;
    }

    public void setEmployeeID(int employeeID) {
        this.employeeID = employeeID;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    @NonNull
    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(@NonNull String employeeName) {
        this.employeeName = employeeName;
    }

    @NonNull
    public int getEmployeeSalary() {
        return employeeSalary;
    }

    public void setEmployeeSalary(@NonNull int employeeSalary) {
        this.employeeSalary = employeeSalary;
    }

    public Date getEmployeeHireDate() {
        return employeeHireDate;
    }

    public void setEmployeeHireDate(Date employeeHireDate) {
        this.employeeHireDate = employeeHireDate;
    }

}

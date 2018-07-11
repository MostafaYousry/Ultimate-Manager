package com.example.android.employeesmanagementapp;

public class EmployeeData {
    private String employeeName;
    private int employeeImageSac;

    public EmployeeData(String employeeName, int eployeeImageSrc) {
        this.employeeName = employeeName;
        this.employeeImageSac = eployeeImageSrc;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public int getEmployeeImageSac() {
        return employeeImageSac;
    }
}

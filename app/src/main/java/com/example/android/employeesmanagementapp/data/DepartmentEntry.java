package com.example.android.employeesmanagementapp.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "departments")
public class DepartmentEntry {

    @ColumnInfo(name = "department_id")
    @PrimaryKey(autoGenerate = true)
    private int departmentId;

    @ColumnInfo(name = "department_name")
    private String departmentName;

    @Ignore
    public DepartmentEntry(String departmentName) {
        this.departmentName = departmentName;
    }

    public DepartmentEntry(int departmentId, String departmentName) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(String departmentName) {
        this.departmentName = departmentName;
    }
}

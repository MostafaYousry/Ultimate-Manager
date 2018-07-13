package com.example.android.employeesmanagementapp.data.entries;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "departments")
public class DepartmentEntry {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "department_id")
    private int departmentId;

    @NonNull
    @ColumnInfo(name = "department_name")
    private String departmentName;

    @Ignore
    public DepartmentEntry(@NonNull String departmentName) {
        this.departmentName = departmentName;
    }

    public DepartmentEntry(int departmentId, @NonNull String departmentName) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(int departmentId) {
        this.departmentId = departmentId;
    }

    @NonNull
    public String getDepartmentName() {
        return departmentName;
    }

    public void setDepartmentName(@NonNull String departmentName) {
        this.departmentName = departmentName;
    }

}

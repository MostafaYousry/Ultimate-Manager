package com.example.android.employeesmanagementapp.data.entries;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

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

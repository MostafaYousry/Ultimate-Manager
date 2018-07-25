package com.example.android.employeesmanagementapp.data.entries;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

/**
 * Department Entry POJO
 * defines columns and keys in departments table
 * and constructors for RoomDatabase
 */
@Entity(tableName = "departments")
public class DepartmentEntry {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "department_id")
    private int departmentId;

    @NonNull
    @ColumnInfo(name = "department_name")
    private String departmentName;

    //used when creating new DepartmentEntry object
    @Ignore
    public DepartmentEntry(@NonNull String departmentName) {
        this.departmentName = departmentName;
    }

    @Ignore
    public DepartmentEntry(int departmentId) {
        this.departmentId = departmentId;
    }

    //used by room when reading from database
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

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DepartmentEntry other = (DepartmentEntry) obj;
        if (departmentId == other.getDepartmentId() && departmentName.equals(other.departmentName))
            return true;
        return false;

    }
}

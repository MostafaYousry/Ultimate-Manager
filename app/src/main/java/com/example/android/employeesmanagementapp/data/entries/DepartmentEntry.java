package com.example.android.employeesmanagementapp.data.entries;

import java.util.Date;
import java.util.Objects;

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

    @NonNull
    @ColumnInfo(name = "department_date_created")
    private Date departmentDateCreated;

    @ColumnInfo(name = "department_image_uri")
    private String departmentImageUri;

    @NonNull
    @ColumnInfo(name = "department_is_deleted")
    private boolean departmentIsDeleted;

    //used when creating new DepartmentEntry object
    @Ignore
    public DepartmentEntry(String departmentName, Date departmentDateCreated, String departmentImageUri) {
        this.departmentName = departmentName;
        this.departmentDateCreated = departmentDateCreated;
        this.departmentImageUri = departmentImageUri;
    }

    //used with index of for department spinner
    @Ignore
    public DepartmentEntry(@NonNull int departmentId) {
        this.departmentId = departmentId;
    }

    //used by room when reading from database
    public DepartmentEntry(int departmentId, String departmentName, Date departmentDateCreated, String departmentImageUri, boolean departmentIsDeleted) {
        this.departmentId = departmentId;
        this.departmentName = departmentName;
        this.departmentDateCreated = departmentDateCreated;
        this.departmentImageUri = departmentImageUri;
        this.departmentIsDeleted = departmentIsDeleted;
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

    public Date getDepartmentDateCreated() {
        return departmentDateCreated;
    }

    public String getDepartmentImageUri() {
        return departmentImageUri;
    }

    public void setDepartmentImageUri(String departmentImageUri) {
        this.departmentImageUri = departmentImageUri;
    }

    public boolean isDepartmentIsDeleted() {
        return departmentIsDeleted;
    }

    public void setDepartmentIsDeleted(boolean departmentIsDeleted) {
        this.departmentIsDeleted = departmentIsDeleted;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof DepartmentEntry))
            return false;

        DepartmentEntry entry = (DepartmentEntry) obj;

        return Integer.compare(departmentId, entry.departmentId) == 0
                && departmentName.equals(entry.departmentName)
                && departmentDateCreated.equals(entry.departmentDateCreated)
                && departmentImageUri.equals(entry.departmentImageUri)
                && Boolean.compare(departmentIsDeleted, entry.departmentIsDeleted) == 0;

    }

    @Override
    public int hashCode() {
        return Objects.hash(departmentId, departmentName, departmentDateCreated, departmentImageUri, departmentIsDeleted);
    }
}

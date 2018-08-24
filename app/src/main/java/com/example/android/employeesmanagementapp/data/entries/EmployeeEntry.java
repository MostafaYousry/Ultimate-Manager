package com.example.android.employeesmanagementapp.data.entries;

import java.util.Calendar;
import java.util.Objects;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Employee Entry POJO
 * defines columns and keys in employees table
 * and constructors for RoomDatabase
 */
@Entity(tableName = "employees",
        indices = {@Index(value = {"department_id"})},
        foreignKeys = @ForeignKey(entity = DepartmentEntry.class, parentColumns = "department_id", childColumns = "department_id"))
public class EmployeeEntry {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "employee_id")
    private int employeeID;

    @ColumnInfo(name = "department_id")
    private int departmentId;

    @ColumnInfo(name = "employee_first_name")
    private String employeeFirstName;

    @ColumnInfo(name = "employee_middle_name")
    private String employeeMiddleName;

    @ColumnInfo(name = "employee_last_name")
    private String employeeLastName;

    @ColumnInfo(name = "employee_salary")
    private float employeeSalary;

    @ColumnInfo(name = "employee_hire_date")
    private Calendar employeeHireDate;

    @ColumnInfo(name = "employee_image_uri")
    private String employeeImageUri;

    @ColumnInfo(name = "employee_email")
    private String employeeEmail;

    @ColumnInfo(name = "employee_phone")
    private String employeePhone;

    @ColumnInfo(name = "employee_note")
    private String employeeNote;

    @ColumnInfo(name = "employee_is_deleted")
    private boolean employeeIsDeleted;

    @Ignore
    private boolean isChecked;


    //used when creating new EmployeeWithExtras object
    @Ignore
    public EmployeeEntry(int departmentId, String employeeFirstName, String employeeMiddleName, String employeeLastName, float employeeSalary, Calendar employeeHireDate, String employeeEmail, String employeePhone, String employeeNote, String employeeImageUri) {
        this.departmentId = departmentId;
        this.employeeFirstName = employeeFirstName;
        this.employeeMiddleName = employeeMiddleName;
        this.employeeLastName = employeeLastName;
        this.employeeSalary = employeeSalary;
        this.employeeHireDate = employeeHireDate;
        this.employeeImageUri = employeeImageUri;
        this.employeeEmail = employeeEmail;
        this.employeePhone = employeePhone;
        this.employeeNote = employeeNote;
    }

    //used by room when reading from database
    public EmployeeEntry(int employeeID, int departmentId, String employeeFirstName, String employeeMiddleName, String employeeLastName, float employeeSalary, Calendar employeeHireDate, String employeeEmail, String employeePhone, String employeeNote, String employeeImageUri, boolean employeeIsDeleted) {
        this.employeeID = employeeID;
        this.departmentId = departmentId;
        this.employeeFirstName = employeeFirstName;
        this.employeeMiddleName = employeeMiddleName;
        this.employeeLastName = employeeLastName;
        this.employeeSalary = employeeSalary;
        this.employeeHireDate = employeeHireDate;
        this.employeeImageUri = employeeImageUri;
        this.employeeEmail = employeeEmail;
        this.employeePhone = employeePhone;
        this.employeeNote = employeeNote;
        this.employeeIsDeleted = employeeIsDeleted;
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

    public String getEmployeeFirstName() {
        return employeeFirstName;
    }

    public void setEmployeeFirstName(String employeeFirstName) {
        this.employeeFirstName = employeeFirstName;
    }

    public String getEmployeeMiddleName() {
        if (employeeMiddleName == null)
            return "";
        return employeeMiddleName;
    }

    public void setEmployeeMiddleName(String employeeMiddleName) {
        this.employeeMiddleName = employeeMiddleName;
    }

    public String getEmployeeLastName() {
        if (employeeLastName == null)
            return "";
        return employeeLastName;
    }

    public void setEmployeeLastName(String employeeLastName) {
        this.employeeLastName = employeeLastName;
    }

    public float getEmployeeSalary() {
        return employeeSalary;
    }

    public void setEmployeeSalary(float employeeSalary) {
        this.employeeSalary = employeeSalary;
    }

    public Calendar getEmployeeHireDate() {
        return employeeHireDate;
    }

    public void setEmployeeHireDate(Calendar employeeHireDate) {
        this.employeeHireDate = employeeHireDate;
    }

    public String getEmployeeImageUri() {
        return employeeImageUri;
    }

    public void setEmployeeImageUri(String employeeImageUri) {
        this.employeeImageUri = employeeImageUri;
    }

    public String getEmployeeEmail() {
        return employeeEmail;
    }

    public void setEmployeeEmail(String employeeEmail) {
        this.employeeEmail = employeeEmail;
    }

    public String getEmployeePhone() {
        return employeePhone;
    }

    public void setEmployeePhone(String employeePhone) {
        this.employeePhone = employeePhone;
    }

    public String getEmployeeNote() {
        return employeeNote;
    }

    public void setEmployeeNote(String employeeNote) {
        this.employeeNote = employeeNote;
    }


    public boolean isEmployeeIsDeleted() {
        return employeeIsDeleted;
    }

    public void setEmployeeIsDeleted(boolean employeeIsDeleted) {
        this.employeeIsDeleted = employeeIsDeleted;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public String getFullName() {
        return getEmployeeFirstName() + " " + getEmployeeMiddleName() + " " + getEmployeeLastName();
    }


    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (!(obj instanceof EmployeeEntry))
            return false;

        EmployeeEntry entry = (EmployeeEntry) obj;

        return Integer.compare(employeeID, entry.employeeID) == 0
                && Integer.compare(departmentId, entry.departmentId) == 0
                && employeeFirstName.equals(entry.employeeFirstName)
                && employeeMiddleName.equals(entry.employeeMiddleName)
                && employeeLastName.equals(entry.employeeLastName)
                && Float.compare(employeeSalary, entry.employeeSalary) == 0
                && employeeHireDate.equals(entry.employeeHireDate)
                && employeeImageUri.equals(entry.employeeImageUri)
                && employeeEmail.equals(entry.employeeEmail)
                && employeePhone.equals(entry.employeePhone)
                && employeeNote.equals(entry.employeeNote)
                && Boolean.compare(employeeIsDeleted, entry.employeeIsDeleted) == 0
                && Boolean.compare(isChecked, entry.isChecked) == 0;
    }

    @Override
    public int hashCode() {

        return Objects.hash(employeeID, departmentId, employeeFirstName, employeeMiddleName, employeeLastName, employeeSalary, employeeHireDate, employeeImageUri, employeeEmail, employeePhone, employeeNote, employeeIsDeleted, isChecked);

    }
}

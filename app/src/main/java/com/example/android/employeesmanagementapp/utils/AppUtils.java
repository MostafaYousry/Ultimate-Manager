package com.example.android.employeesmanagementapp.utils;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * class for app utilities
 */
public final class AppUtils {

    public static List<Integer> employeeImages = new ArrayList<>();

    static {
        employeeImages.add(R.drawable.griezmann);
        employeeImages.add(R.drawable.salah);
        employeeImages.add(R.drawable.pogba);
        employeeImages.add(R.drawable.hazard);
        employeeImages.add(R.drawable.ronaldo);
        employeeImages.add(R.drawable.messi);
        employeeImages.add(R.drawable.dybala);
        employeeImages.add(R.drawable.kroos);
        employeeImages.add(R.drawable.mbappe);
    }

    public static int getRandomEmployeeImage() {

        return employeeImages.get((int) (Math.random() * employeeImages.size()));
    }

    /**
     * Temporary method for inserting fake data to RecyclerView's adapter.
     * tobe replaced with data from database
     */
    public static List<EmployeeEntry> getEmployeesFakeData() {
        List<EmployeeEntry> list = new ArrayList<>();

        EmployeeEntry employeeEntry1 = new EmployeeEntry(2, "Antoine Griezmann", 200, new Date(),false);
        EmployeeEntry employeeEntry2 = new EmployeeEntry(1, "Mohamed Salah", 200, new Date(),false);
        EmployeeEntry employeeEntry3 = new EmployeeEntry(2, "Paul Pogba", 200, new Date(),false);
        EmployeeEntry employeeEntry4 = new EmployeeEntry(3, "Eden Hazard", 200, new Date(),false);
        EmployeeEntry employeeEntry5 = new EmployeeEntry(0, "Cristiano Ronaldo", 200, new Date(),false);
        EmployeeEntry employeeEntry6 = new EmployeeEntry(2, "Lionel Messi", 200, new Date(),false);
        EmployeeEntry employeeEntry9 = new EmployeeEntry(2, "Paulo Dybala", 200, new Date(),false);
        EmployeeEntry employeeEntry7 = new EmployeeEntry(1, "Tony Kroos", 200, new Date(),false);
        EmployeeEntry employeeEntry8 = new EmployeeEntry(1, "Kylian Mbappe", 200, new Date(),false);

        list.add(employeeEntry1);
        list.add(employeeEntry2);
        list.add(employeeEntry3);
        list.add(employeeEntry4);
        list.add(employeeEntry5);
        list.add(employeeEntry6);
        list.add(employeeEntry7);
        list.add(employeeEntry8);
        list.add(employeeEntry9);
        list.add(employeeEntry1);
        list.add(employeeEntry2);
        list.add(employeeEntry3);
        list.add(employeeEntry4);
        list.add(employeeEntry1);
        list.add(employeeEntry2);
        list.add(employeeEntry3);
        list.add(employeeEntry4);
        return list;
    }

    /**
     * Temporary method for inserting fake data to RecyclerView's adapter.
     * tobe replaced with data from database
     */

    /**
     * Temporary method for inserting fake data to RecyclerView's adapter.
     * tobe replaced with data from database
     */
    public static List<DepartmentEntry> getDepartmentsFakeData() {
        DepartmentEntry departmentEntry1 = new DepartmentEntry("Production",false);
        DepartmentEntry departmentEntry2 = new DepartmentEntry("Research and Development",false);
        DepartmentEntry departmentEntry3 = new DepartmentEntry("Purchasing",false);
        DepartmentEntry departmentEntry4 = new DepartmentEntry("Marketing",false);
        DepartmentEntry departmentEntry5 = new DepartmentEntry("Human Resource Management",false);
        DepartmentEntry departmentEntry6 = new DepartmentEntry("Accounting and Finance",false);

        List<DepartmentEntry> list = new ArrayList<>();
        list.add(departmentEntry1);
        list.add(departmentEntry2);
        list.add(departmentEntry3);
        list.add(departmentEntry4);
        list.add(departmentEntry5);
        list.add(departmentEntry6);

        return list;
    }
}

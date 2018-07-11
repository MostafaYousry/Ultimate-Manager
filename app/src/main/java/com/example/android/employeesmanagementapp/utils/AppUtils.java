package com.example.android.employeesmanagementapp.utils;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.data.entries.DepartmentEntry;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.data.entries.TaskEntry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class AppUtils {

    public static List<Integer> employeeImages = new ArrayList<>();

    static {
        employeeImages.add(R.drawable.family_daughter);
        employeeImages.add(R.drawable.family_father);
        employeeImages.add(R.drawable.family_grandfather);
        employeeImages.add(R.drawable.family_grandmother);
        employeeImages.add(R.drawable.family_mother);
        employeeImages.add(R.drawable.family_older_brother);
        employeeImages.add(R.drawable.family_older_sister);
        employeeImages.add(R.drawable.family_son);
        employeeImages.add(R.drawable.family_younger_brother);
        employeeImages.add(R.drawable.family_younger_sister);
    }

    public static int getRandomEmployeeImage(){
        return employeeImages.get((int)(Math.random()*employeeImages.size()));
    }

    /**
     * Temporary method for inserting fake data to RecyclerView's adapter.
     * tobe replaced with data from database
     */
    public static List<EmployeeEntry> getEmployeesFakeData(){
        List<EmployeeEntry> list = new ArrayList<>();

        EmployeeEntry employeeEntry1 = new EmployeeEntry(2,"Robert Dawney",200,new Date());
        EmployeeEntry employeeEntry2 = new EmployeeEntry(1,"Emma Watson",200,new Date());
        EmployeeEntry employeeEntry3 = new EmployeeEntry(2,"Tom Holland",200,new Date());
        EmployeeEntry employeeEntry4 = new EmployeeEntry(3,"Elezabeth Olsen",200,new Date());
        EmployeeEntry employeeEntry5 = new EmployeeEntry(0,"Chris Evans",200,new Date());
        EmployeeEntry employeeEntry6 = new EmployeeEntry(2,"Robert Dawney",200,new Date());
        EmployeeEntry employeeEntry7 = new EmployeeEntry(1,"Emma Watson",200,new Date());
        EmployeeEntry employeeEntry8 = new EmployeeEntry(1,"Tom Holland",200,new Date());
        EmployeeEntry employeeEntry9 = new EmployeeEntry(2,"Elezabeth Olsen",200,new Date());
        EmployeeEntry employeeEntry10 = new EmployeeEntry(0,"Chris Evans",200,new Date());
        EmployeeEntry employeeEntry11 = new EmployeeEntry(1,"Robert Dawney",200,new Date());
        EmployeeEntry employeeEntry12 = new EmployeeEntry(3,"Emma Watson",200,new Date());
        EmployeeEntry employeeEntry13 = new EmployeeEntry(2,"Tom Holland",200,new Date());
        EmployeeEntry employeeEntry14 = new EmployeeEntry(1,"Elezabeth Olsen",200,new Date());
        EmployeeEntry employeeEntry15 = new EmployeeEntry(0,"Chris Evans",200,new Date());

        list.add(employeeEntry1);
        list.add(employeeEntry2);
        list.add(employeeEntry3);
        list.add(employeeEntry4);
        list.add(employeeEntry5);
        list.add(employeeEntry6);
        list.add(employeeEntry7);
        list.add(employeeEntry8);
        list.add(employeeEntry9);
        list.add(employeeEntry10);
        list.add(employeeEntry11);
        list.add(employeeEntry12);
        list.add(employeeEntry13);
        list.add(employeeEntry14);
        list.add(employeeEntry15);

        return list;
    }

    /**
     * Temporary method for inserting fake data to RecyclerView's adapter.
     * tobe replaced with data from database
     */
    public static List<TaskEntry> getTasksFakeData(){
        TaskEntry taskEntry1 = new TaskEntry(2,"App code refactor","wfjjwnfiwnfiwenf",new Date(),new Date());
        TaskEntry taskEntry2 = new TaskEntry(0,"Add new Feature","wfjjwnfiwnfiwenfasdasd",new Date(),new Date());
        TaskEntry taskEntry3 = new TaskEntry(3,"rererererererererererer","wfjjwnfiwnfiwenfsdasdasdadsad",new Date(),new Date());
        TaskEntry taskEntry4 = new TaskEntry(3,"el3ab baleee","wfjjwnfiwnfiwenfasdasd",new Date(),new Date());
        TaskEntry taskEntry5 = new TaskEntry(1,"skalob baneee","asdasdsdasdadsad",new Date(),new Date());
        TaskEntry taskEntry6 = new TaskEntry(4,"eboo msh baskoota","wfjjwnfiwnfiwenfasdasdasdsadasdasdsadasdsads",new Date(),new Date());

        List<TaskEntry> list = new ArrayList<>();
        list.add(taskEntry1);
        list.add(taskEntry2);
        list.add(taskEntry3);
        list.add(taskEntry4);
        list.add(taskEntry5);
        list.add(taskEntry6);

        return list;
    }

    /**
     * Temporary method for inserting fake data to RecyclerView's adapter.
     * tobe replaced with data from database
     */
    public static List<DepartmentEntry> getDepartmentsFakeData(){
        DepartmentEntry departmentEntry1 = new DepartmentEntry("Production" );
        DepartmentEntry departmentEntry2 = new DepartmentEntry("Research and Development");
        DepartmentEntry departmentEntry3 = new DepartmentEntry("Purchasing");
        DepartmentEntry departmentEntry4 = new DepartmentEntry("Marketing");
        DepartmentEntry departmentEntry5 = new DepartmentEntry("Human Resource Management");
        DepartmentEntry departmentEntry6 = new DepartmentEntry("Accounting and Finance");

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

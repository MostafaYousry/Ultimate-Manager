package com.example.android.employeesmanagementapp.utils;

import com.example.android.employeesmanagementapp.R;

import java.util.ArrayList;
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

}

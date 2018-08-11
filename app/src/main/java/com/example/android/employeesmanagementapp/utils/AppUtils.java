package com.example.android.employeesmanagementapp.utils;

import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.fragments.DatePickerFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;


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

    public static void showDatePicker(Context context, View view, boolean allowPastDates) {
        //create a bundle containing id of clicked text view (startDateTextView or dueDateTextView)
        Bundle bundle = new Bundle();
        bundle.putInt(DatePickerFragment.KEY_DISPLAY_VIEW_ID, view.getId());
        bundle.putBoolean(DatePickerFragment.KEY_ALLOW_PAST_DATES, allowPastDates);

        //instantiate a DatePickerFragment to show date picker dialog
        DialogFragment datePickerFragment = new DatePickerFragment();
        datePickerFragment.setArguments(bundle);

        //show th dialog
        datePickerFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "datePicker");
    }

    public static String getFriendlyDate(Date date) {
        return SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM).format(date);
    }

}

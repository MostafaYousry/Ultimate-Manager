package com.example.android.employeesmanagementapp.utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.activities.AddTaskActivity;
import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;
import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;
import com.example.android.employeesmanagementapp.fragments.ColorPickerDialogFragment;
import com.example.android.employeesmanagementapp.fragments.DatePickerDialogFragment;
import com.example.android.employeesmanagementapp.fragments.TimePickerFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;


/**
 * class for app utilities
 */
public final class AppUtils {
    /**
     * Converts dp to pixel
     */
    public static int dpToPx(Context context, int dp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, r.getDisplayMetrics()));
    }

    /**
     * Converts sp to pixel
     */
    public static int spToPx(Context context, int sp) {
        Resources r = context.getResources();
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, r.getDisplayMetrics()));
    }

    public static String getFriendlyDate(Date date) {
        return SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM).format(date);
    }

    public static String getFriendlyTime(Date date) {
        return SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT).format(date);
    }

    public static String getFullEmployeeName(EmployeeEntry employeeEntry) {
        return employeeEntry.getEmployeeFirstName() + " " + employeeEntry.getEmployeeMiddleName() + " " + employeeEntry.getEmployeeLastName();
    }

    public static void setNumOfChangedFiled(int changed) {
        numOfChangedFiled += changed;
        System.out.println("********************** change = " + numOfChangedFiled);
    }

    public static int getNumOfChangedFiled() {
        return numOfChangedFiled;
    }

    public static void clearOneFiledChanged() {
        numOfChangedFiled = 0;
    }

    public static void showDiscardChangesDialog(final Context context) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Discard changes");
        builder.setMessage("All changes will be discarded.");
        builder.setPositiveButton("DISCARD", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                AppUtils.clearOneFiledChanged();
                ((Activity) context).finish();
            }
        });

        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    public static void showTimePicker(AddTaskActivity context, View view) {
        //create a bundle containing id of clicked text view (startDateTextView or dueDateTextView)
        Bundle bundle = new Bundle();
        bundle.putInt(TimePickerFragment.KEY_DISPLAY_VIEW_ID, view.getId());
        if (view.getTag() != null)
            bundle.putLong(TimePickerFragment.KEY_DISPLAY_TIME, ((Date) view.getTag()).getTime());
        else
            bundle.putLong(TimePickerFragment.KEY_DISPLAY_TIME, new Date().getTime());

        //instantiate a DatePickerFragment to show date picker dialog
        DialogFragment timePickerFragment = new TimePickerFragment();
        timePickerFragment.setArguments(bundle);

        //show th dialog
        timePickerFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "timePicker");
    }
}

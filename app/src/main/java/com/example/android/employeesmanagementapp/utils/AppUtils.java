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


    private static int numOfChangedFiled = 0;

    public static void showDatePicker(Context context, View view) {
        //create a bundle containing id of clicked text view (startDateTextView or dueDateTextView)
        Bundle bundle = new Bundle();
        bundle.putInt(DatePickerDialogFragment.KEY_DISPLAY_VIEW_ID, view.getId());
        if (view.getTag() != null)
            bundle.putLong(DatePickerDialogFragment.KEY_DISPLAY_DATE, ((Date) view.getTag()).getTime());
        else
            bundle.putLong(DatePickerDialogFragment.KEY_DISPLAY_DATE, new Date().getTime());

        //instantiate a DatePickerDialogFragment to show date picker dialog
        DialogFragment datePickerFragment = new DatePickerDialogFragment();
        datePickerFragment.setArguments(bundle);

        //show th dialog
        datePickerFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "datePicker");
    }

    public static void showColorPicker(Context context, int taskId) {
        //create a bundle containing id of task
        Bundle bundle = new Bundle();
        bundle.putInt(ColorPickerDialogFragment.KEY_TASK_ID, taskId);

        //instantiate a ColorPickerDialogFragment
        DialogFragment colorPickerDialogFragment = new ColorPickerDialogFragment();
        colorPickerDialogFragment.setArguments(bundle);

        //show th dialog
        colorPickerDialogFragment.show(((AppCompatActivity) context).getSupportFragmentManager(), "colorPicker");
    }

    public static void showRateTaskDialog(final Context context, final int taskID) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Task done");
        builder.setMessage("Please rate task");

        View rateDialogView = LayoutInflater.from(context).inflate(R.layout.rating_bar, null, false);
        final RatingBar ratingBar = rateDialogView.findViewById(R.id.rating_bar);
//        ratingBar.setPaddingRelative(dpToPx(context ,16), 0, dpToPx(context,16), 0);
        builder.setView(rateDialogView);

        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(final DialogInterface dialog, int which) {
                AppExecutor.getInstance().diskIO().execute(new Runnable() {
                    @Override
                    public void run() {
                        AppDatabase.getInstance(context).tasksDao().rateTask(ratingBar.getRating(), taskID);
                        dialog.dismiss();
                    }
                });
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }


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

    public static Date getChosenDateAndTime(Date chosenDate, Date chosenTime) {
        Calendar dateAndTimeCalender = Calendar.getInstance();
        Calendar dateCalendar = Calendar.getInstance();
        Calendar timeCalendar = Calendar.getInstance();

        if (chosenDate != null) {
            dateCalendar.setTime(chosenDate);
            dateAndTimeCalender.set(Calendar.YEAR, dateCalendar.get(Calendar.YEAR));
            dateAndTimeCalender.set(Calendar.MONTH, dateCalendar.get(Calendar.MONTH));
            dateAndTimeCalender.set(Calendar.DAY_OF_MONTH, dateCalendar.get(Calendar.DAY_OF_MONTH));
        }

        if (chosenTime != null) {
            timeCalendar.setTime(chosenTime);
            dateAndTimeCalender.set(Calendar.HOUR_OF_DAY, timeCalendar.get(Calendar.HOUR_OF_DAY));
            dateAndTimeCalender.set(Calendar.MINUTE, timeCalendar.get(Calendar.MINUTE));
        } else {
            dateAndTimeCalender.set(Calendar.HOUR_OF_DAY, 0);
            dateAndTimeCalender.set(Calendar.MINUTE, 0);
        }

        return dateAndTimeCalender.getTime();
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

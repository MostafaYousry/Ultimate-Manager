package com.example.android.employeesmanagementapp.utils;

import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;

import com.example.android.employeesmanagementapp.R;
import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;
import com.example.android.employeesmanagementapp.fragments.ColorPickerDialogFragment;
import com.example.android.employeesmanagementapp.fragments.DatePickerDialogFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;


/**
 * class for app utilities
 */
public final class AppUtils {

    private static List<Integer> employeeImages = new ArrayList<>();
    private static int[] taskColorResources = new int[12];

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

        taskColorResources[0] = R.color.task_color_1;
        taskColorResources[1] = R.color.task_color_2;
        taskColorResources[2] = R.color.task_color_3;
        taskColorResources[3] = R.color.task_color_4;
        taskColorResources[4] = R.color.task_color_5;
        taskColorResources[5] = R.color.task_color_6;
        taskColorResources[6] = R.color.task_color_7;
        taskColorResources[7] = R.color.task_color_8;
        taskColorResources[8] = R.color.task_color_9;
        taskColorResources[9] = R.color.task_color_10;
        taskColorResources[10] = R.color.task_color_11;
        taskColorResources[11] = R.color.task_color_12;

    }

    public static int getRandomEmployeeImage() {

        return employeeImages.get((int) (Math.random() * employeeImages.size()));
    }

    public static void showDatePicker(Context context, View view, boolean allowPastDates) {
        //create a bundle containing id of clicked text view (startDateTextView or dueDateTextView)
        Bundle bundle = new Bundle();
        bundle.putInt(DatePickerDialogFragment.KEY_DISPLAY_VIEW_ID, view.getId());
        bundle.putBoolean(DatePickerDialogFragment.KEY_ALLOW_PAST_DATES, allowPastDates);

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

    public static String getFriendlyDate(Date date) {
        return SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM).format(date);
    }

    public static int[] getColorResources() {
        return taskColorResources;
    }
}

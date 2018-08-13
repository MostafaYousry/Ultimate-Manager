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
import java.util.Date;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;


/**
 * class for app utilities
 */
public final class AppUtils {

    private static int[] taskColorResources = new int[12];
    private static int[] accent100Values = new int[16];
    private static int[] accent700Values = new int[16];

    static {

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

        accent100Values[0] = R.color.A100_1;
        accent100Values[1] = R.color.A100_2;
        accent100Values[2] = R.color.A100_3;
        accent100Values[3] = R.color.A100_4;
        accent100Values[4] = R.color.A100_5;
        accent100Values[5] = R.color.A100_6;
        accent100Values[6] = R.color.A100_7;
        accent100Values[7] = R.color.A100_8;
        accent100Values[8] = R.color.A100_9;
        accent100Values[9] = R.color.A100_10;
        accent100Values[10] = R.color.A100_11;
        accent100Values[11] = R.color.A100_12;
        accent100Values[12] = R.color.A100_13;
        accent100Values[13] = R.color.A100_14;
        accent100Values[14] = R.color.A100_15;
        accent100Values[15] = R.color.A100_16;


        accent700Values[0] = R.color.A700_1;
        accent700Values[1] = R.color.A700_2;
        accent700Values[2] = R.color.A700_3;
        accent700Values[3] = R.color.A700_4;
        accent700Values[4] = R.color.A700_5;
        accent700Values[5] = R.color.A700_6;
        accent700Values[6] = R.color.A700_7;
        accent700Values[7] = R.color.A700_8;
        accent700Values[8] = R.color.A700_9;
        accent700Values[9] = R.color.A700_10;
        accent700Values[10] = R.color.A700_11;
        accent700Values[11] = R.color.A700_12;
        accent700Values[12] = R.color.A700_13;
        accent700Values[13] = R.color.A700_14;
        accent700Values[14] = R.color.A700_15;
        accent700Values[15] = R.color.A700_16;

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

    public static int[] getTaskColorResources() {
        return taskColorResources;
    }

    public static int getLetterColor(Object object) {
        return accent700Values[Math.abs(object.hashCode()) % accent700Values.length];
    }

    public static int getLetterBackgroundColor(Object object) {
        return accent100Values[Math.abs(object.hashCode()) % accent100Values.length];
    }

}

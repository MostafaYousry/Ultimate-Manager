package com.example.android.employeesmanagementapp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import com.example.android.employeesmanagementapp.data.entries.EmployeeEntry;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


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

}

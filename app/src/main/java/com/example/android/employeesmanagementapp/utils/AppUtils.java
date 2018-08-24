package com.example.android.employeesmanagementapp.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

import java.text.SimpleDateFormat;
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

    /**
     * @param date :Date object to be formatted
     * @return a date string formatted as AUG 21,2018
     */
    public static String getFriendlyDate(Date date) {
        return SimpleDateFormat.getDateInstance(SimpleDateFormat.MEDIUM).format(date);
    }

    /**
     * @param date :Date object to be formatted
     * @return a time string formatted as 11:56 PM
     */
    public static String getFriendlyTime(Date date) {
        return SimpleDateFormat.getTimeInstance(SimpleDateFormat.SHORT).format(date);
    }

}

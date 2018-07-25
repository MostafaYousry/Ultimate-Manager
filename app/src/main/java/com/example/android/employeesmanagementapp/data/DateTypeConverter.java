package com.example.android.employeesmanagementapp.data;


import java.util.Date;

import androidx.room.TypeConverter;

/**
 * Type Converter class
 * <p>
 * helps room to know how to convert a java Date object to a timestamp for SQLite
 * and vice versa.
 */
public class DateTypeConverter {

    @TypeConverter
    public static Date toDate(Long timestamp) {
        return timestamp == null ? null : new Date(timestamp);
    }

    @TypeConverter
    public static Long toTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }
}

package com.example.android.employeesmanagementapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;
import com.example.android.employeesmanagementapp.utils.NotificationUtils;

import java.util.Calendar;
import java.util.Date;
import java.util.List;


//define the BroadcastReceiver that will be executed by the alarm and will launch NotificationService
public class MyAlarmReceiver extends BroadcastReceiver {
    List<Integer> allTasksId;
    List<Calendar> allTasksDate;

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(final Context context, Intent intent) {

        /*to reset all the alarm manager after reboot :
         *after the reboot is completed the intent must has action : android.intent.action.BOOT_COMPLETED
         * if the intent has this action then restart all the alarm manager by their taskDueDate
         * if not, that mean the alarmManager time is ended and will call NotificationService
         */
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            AppExecutor.getInstance().diskIO().execute(() -> {
                allTasksId = AppDatabase.getInstance(context).tasksDao().getAllTasksId();
                allTasksDate = AppDatabase.getInstance(context).tasksDao().getAllTasksDueDate();
                NotificationUtils.resetAllAlarms(context, allTasksId, allTasksDate);
            });

        } else {
            Intent serviceIntent = new Intent(context, NotificationService.class);
            serviceIntent.putExtra("intent is sent from receiver", true);
            serviceIntent.putExtra("task id", intent.getExtras().getInt("task id"));
            context.startService(serviceIntent);
        }
    }
}
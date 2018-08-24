package com.example.android.employeesmanagementapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;

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
                createNewAlarm(context);
            });

        } else {
            Intent serviceIntent = new Intent(context, NotificationService.class);
            serviceIntent.putExtra("intent is sent from receiver", true);
            serviceIntent.putExtra("task id", intent.getExtras().getInt("task id"));
            context.startService(serviceIntent);
        }
    }

    //reset all the alarms
    private void createNewAlarm(Context context) {
        for (int i = 0; i < allTasksId.size(); i++) {
            //check if the due date of the task was during the time the boot take to complete or after that time
            if (allTasksDate.get(i).getTime().getTime() - new Date().getTime() >= -60 * 1000) {
                Intent intent = new Intent(context, MyAlarmReceiver.class);
                intent.putExtra("task id", allTasksId.get(i));
                final PendingIntent pIntent = PendingIntent.getBroadcast(context, allTasksId.get(i), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Calendar calendar = allTasksDate.get(i);

                //solve the problem of start the service for android8
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    alarm.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pIntent);
                } else
                    alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 0, pIntent);
            }
        }
    }
}
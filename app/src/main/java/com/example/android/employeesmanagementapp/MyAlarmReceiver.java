package com.example.android.employeesmanagementapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MyAlarmReceiver extends BroadcastReceiver {
    List<Integer> allTasksId;
    List<Date> allTasksDate;
    boolean restart = true;

    // Triggered by the Alarm periodically (starts the service to run task)
    @Override
    public void onReceive(final Context context, Intent intent) {
        System.out.println(intent.getAction());
        if("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            System.out.println("*********************** restart");
            AppExecutor.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    allTasksId = AppDatabase.getInstance(context).tasksDao().getAllTasksId();
                    allTasksDate= AppDatabase.getInstance(context).tasksDao().getAllTasksDueDate();
                    stopPreviousAlarm(context);
                    createNewAlarm(context);
                }
            });

        }
        else {
            System.out.println(intent.getExtras().getInt("task id"));
            System.out.println("*********************** not restart");
            Intent serviceIntent = new Intent(context, NotificationService.class);
            serviceIntent.putExtra("intent is sent from receiver", true);
            context.startService(serviceIntent);
        }
    }

    private void createNewAlarm(Context context) {
        for (int i = 0;i < allTasksId.size(); i++) {
            System.out.println(allTasksDate.get(i).compareTo(new Date()));
            if (allTasksDate.get(i).compareTo(new Date()) >= - 100 * 1000) {
                System.out.println("************************ task id = " + allTasksId.get(i));
                System.out.println("ok will restart");
                Intent intent = new Intent(context, MyAlarmReceiver.class);
                intent.putExtra("task id",allTasksId.get(i));
                final PendingIntent pIntent = PendingIntent.getBroadcast(context, allTasksId.get(i), intent, 0);
                AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                Calendar calendar = Calendar.getInstance();
                calendar.setTime(allTasksDate.get(i));
                alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 0, pIntent);
            }
        }
    }

    private void stopPreviousAlarm(Context context) {
        for (int i = 0; i < allTasksId.size(); i++) {
            try {
                Intent intent = new Intent(context, MyAlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, allTasksId.get(i), intent, 0);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
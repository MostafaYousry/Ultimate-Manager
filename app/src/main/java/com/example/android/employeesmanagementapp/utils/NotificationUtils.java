package com.example.android.employeesmanagementapp.utils;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.android.employeesmanagementapp.MyAlarmReceiver;
import com.example.android.employeesmanagementapp.NotificationService;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public final class NotificationUtils {

    /**
     * cancel the alarmManager of the empty tasks
     * @param emptyTasksId : list of integer of all the empty tasks id
     */
    public static void cancelEmptyTasksAlarm(Context context,List<Integer> emptyTasksId) {
        for (int i = 0; i < emptyTasksId.size(); i++) {
            try {
                Intent intent = new Intent(context, MyAlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(context, emptyTasksId.get(i), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
                alarmManager.cancel(pendingIntent);
                pendingIntent.cancel();
                Intent serviceIntent = new Intent(context, NotificationService.class);
                serviceIntent.putExtra("task id", emptyTasksId.get(i));
                context.startService(serviceIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * cancel the alarmManager for specific task
     * @param taskId : id of the task to stop its alarmManager
     */
    public static void cancelAlarmManager(Context context,int taskId) {
        try {
            Intent intent = new Intent(context, MyAlarmReceiver.class);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, taskId, intent, 0);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            Intent serviceIntent = new Intent(context, NotificationService.class);
            serviceIntent.putExtra("task id", taskId);
            context.startService(serviceIntent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * create new alarmManager for a task
     * @param taskId : id of the task to create its alarmManager
     * @param taskDueDateCalendar : Calendar object contains the task due date to create its alarmManager
     */
    public static void createNewAlarm(Context context,Calendar taskDueDateCalendar, int taskId) {
        Calendar calendar = taskDueDateCalendar;
        if (calendar.getTime().compareTo(new Date()) >= 0) {
            Intent intent = new Intent(context, MyAlarmReceiver.class);
            intent.putExtra("task id", taskId);
            PendingIntent pIntent = PendingIntent.getBroadcast(context, taskId, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarm = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            //solve the problem of start the service for android8
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarm.setAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() - 1000, pIntent);
            } else
                alarm.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis() - 1000, 0, pIntent);
        }
    }

    /**
     * reset all the alarmManagers after reboot
     * @param allTasksId : List of all the tasks id
     * @param allTasksDate: List of calender contains all the tasks due date
     */
    public static void resetAllAlarms(Context context, List<Integer> allTasksId, List<Calendar> allTasksDate) {
        for (int i = 0; i < allTasksId.size(); i++) {
            //check if the due date of the task was during the time the boot take to complete or after that time
            if (allTasksDate.get(i).getTime().getTime() - new Date().getTime() >= -120 * 1000) {
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

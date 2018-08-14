package com.example.android.employeesmanagementapp;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.IntentService;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Handler;
import android.util.Log;

import com.example.android.employeesmanagementapp.activities.MainActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class TimedNotificationService extends IntentService {
    private Context mContext;
    final Handler handler = new Handler();
    Timer timer;
    TimerTask timerTask;
    String TAG = "Timers";
    static int count = 0;
    int taskId;
    //int taskId;
    Long taskDueDate;
    static HashMap<Integer, Timer> mTaskTimer = new HashMap<>();

    public static void setCount(int count) {
        NotificationService.count = count;
    }

    public TimedNotificationService() {
        super("MyTimedNotificationService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    @Override
    protected void onHandleIntent(Intent intent) {
//        if (intent != null) {
//            taskId = intent.getExtras().getInt("task id");
//            taskDueDate = intent.getExtras().getLong("task due date");
//            startTimer();
//            LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
//        }
        Intent intent2 = new Intent(getApplicationContext(), MainActivity.class);
        intent2.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent2, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle(getResources().getText(R.string.app_name))
                .setContentText(++count + " tasks due date are met")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                // Set the intent that will fire when the user taps the notification
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setVisibility(1)
                .setDefaults(Notification.DEFAULT_ALL); //To control the level of detail visible in the notification from the lock screen
        if (count == 1)
            mBuilder.setContentText(count + " task due date is met");

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(0, mBuilder.build());
        setBadge(getApplicationContext(), count);
        cancelAlarm();
    }

    private void cancelAlarm() {

        Intent intent = new Intent(getApplicationContext(), MyAlarmReceiver.class);
        final PendingIntent pIntent = PendingIntent.getBroadcast(this, MyAlarmReceiver.REQUEST_CODE,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarm = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
        alarm.cancel(pIntent);
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        super.onTaskRemoved(rootIntent);
        startTimer();
        stopSelf();
    }

    private void startTimer() {
//        if (mTaskTimer.containsKey(taskId)) {
//            timer = mTaskTimer.get(taskId);
//            stopTimerTask();
//            mTaskTimer.remove(taskId);
//        }

        //set a new Timer
        timer = new Timer();
//        mTaskTimer.put(taskId, timer);

        //initialize the TimerTask's job
        initializeTimerTask();

        System.out.println("start timer");
        //schedule the timer, after the first 5000ms
        Log.d("intent output", "task Id = " + taskId);
        Log.d("intent output", "task due date = " + taskDueDate);

        timer.schedule(timerTask, 10000);
        //timer.schedule(timerTask, 5000,1000); //

    }

    private void stopTimerTask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    private void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                final boolean notification = handler.post(new Runnable() {
                    public void run() {

                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.mipmap.ic_launcher)
                                .setContentTitle(getResources().getText(R.string.app_name))
                                .setContentText(++count + " tasks due date are met")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                // Set the intent that will fire when the user taps the notification
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true)
                                .setVisibility(1)
                                .setDefaults(Notification.DEFAULT_ALL); //To control the level of detail visible in the notification from the lock screen
                        if (count == 1)
                            mBuilder.setContentText(count + " task due date is met");

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                        // notificationId is a unique int for each notification that you must define
                        notificationManager.notify(0, mBuilder.build());
                        setBadge(getApplicationContext(), count);

                    }
                });
            }
        };
    }

    public static void setBadge(Context context, int count) {
        String launcherClassName = getLauncherClassName(context);
        if (launcherClassName == null) {
            return;
        }
        Intent intent = new Intent("android.intent.action.BADGE_COUNT_UPDATE");
        intent.putExtra("badge_count", count);
        intent.putExtra("badge_count_package_name", context.getPackageName());
        intent.putExtra("badge_count_class_name", launcherClassName);
        context.sendBroadcast(intent);
    }

    public static String getLauncherClassName(Context context) {

        PackageManager pm = context.getPackageManager();

        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        for (ResolveInfo resolveInfo : resolveInfos) {
            String pkgName = resolveInfo.activityInfo.applicationInfo.packageName;
            if (pkgName.equalsIgnoreCase(context.getPackageName())) {
                String className = resolveInfo.activityInfo.name;
                return className;
            }
        }
        return null;
    }

}

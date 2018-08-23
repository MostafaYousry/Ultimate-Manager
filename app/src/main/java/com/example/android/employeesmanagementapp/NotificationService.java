package com.example.android.employeesmanagementapp;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.example.android.employeesmanagementapp.activities.MainActivity;
import com.example.android.employeesmanagementapp.data.AppDatabase;
import com.example.android.employeesmanagementapp.data.AppExecutor;
import com.example.android.employeesmanagementapp.fragments.TasksFragment;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;

public class NotificationService extends Service {
    //we are going to use a mHandler to be able to run in our TimerTask
    private String TAG = "Timers";
    private static int mTasksCount = 0;
    private static ArrayList<Integer> tasksSentNotifications = new ArrayList<>();


    public NotificationService() {

    }

    public static void setTasksCount(int tasksCount) {
        NotificationService.mTasksCount = tasksCount;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }


    /* this method is called to trigger the service work (send notification)
     * if a task sent a notification, its id will be saved and the tasks that sent a notification count will be increment
     * when editing the task due date, the task id is removed from the list so when the task send notification , its id will be saved and the count will be increment again
     * after marking a task as done or deleting it, its id is removed from the list
     * when the app is running the tasksCount is considered as the  umber of notifications are sent when the app is running and so on when the app is closed
     *  return START_STICKY to make sure the service is running in the background
     */

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        //check that the service was called after the alarmManager finished or not to send the notification
        if (intent != null && intent.getExtras().getBoolean("intent is sent from receiver")) {
            if (!tasksSentNotifications.contains(intent.getExtras().getInt("task id"))) {
                tasksSentNotifications.add(intent.getExtras().getInt("task id"));
            }
            mTasksCount = tasksSentNotifications.size();
            sendNotifications();
        } else if (intent != null) {
            if (tasksSentNotifications.contains(intent.getExtras().getInt("task id"))) {
                int index = tasksSentNotifications.indexOf(intent.getExtras().getInt("task id"));
                tasksSentNotifications.remove(index);
            }
            mTasksCount = tasksSentNotifications.size();
            setBadge(getApplicationContext(), mTasksCount);
        }
        return START_STICKY;
    }

    private void sendNotifications() {

        createNotificationChannel("22327");

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(), "22327")
                .setSmallIcon(R.drawable.ic_task_notification)
                .setContentTitle(getResources().getText(R.string.app_name))
                .setContentText(mTasksCount + " tasks due date are met")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent)  // Set the intent that will fire when the user taps the notification
                .setAutoCancel(true) // cancel the notification from the notifications bar after open the app
                .setVisibility(1)  //To control the level of detail visible in the notification from the lock screen
                .setDefaults(Notification.DEFAULT_ALL); //set the default of led, sound and vibration of the mobile when the notification is sent
        if (mTasksCount == 1)
            mBuilder.setContentText(mTasksCount + " task due date is met");

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
        notificationManager.notify(22327, mBuilder.build());

        setBadge(getApplicationContext(), mTasksCount);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    // create the channel of the notification
    private void createNotificationChannel(String CHANNEL_ID) {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.channel_name);
            String description = getString(R.string.channel_description);
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getApplicationContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    //set the badge of the app icon for the number of notifications the user doesn't check them
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
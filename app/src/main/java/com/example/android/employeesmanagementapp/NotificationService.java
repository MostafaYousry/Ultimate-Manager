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

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationService extends Service {
    //we are going to use a mHandler to be able to run in our TimerTask
    private final Handler mHandler = new Handler();
    private Timer mTimer;
    private TimerTask mTimerTask;
    private String TAG = "Timers";
    private static int mTasksCount = 0;
    private int mTaskId;
    private Long mTaskDueDate;
    private boolean appIsDestroyed = true;
    private static HashMap<Integer, Timer> mTaskTimer = new HashMap<>();


    public NotificationService() {
    }

    public static void setTasksCount(int tasksCount) {
        NotificationService.mTasksCount = tasksCount;
    }

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        if (intent != null && intent.hasExtra("task id") && intent.hasExtra("task due date")) {
            mTaskId = intent.getExtras().getInt("task id");
            mTaskDueDate = intent.getExtras().getLong("task due date");
            appIsDestroyed = intent.getExtras().getBoolean("app is destroyed");
        }
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        appIsDestroyed = true;
        startTimer();
        super.onDestroy();
    }

    public void startTimer() {

        if (!appIsDestroyed) {
            if (mTaskTimer.containsKey(mTaskId)) {
                mTimer = mTaskTimer.get(mTaskId);
                stopTimerTask();
                mTaskTimer.remove(mTaskId);
            }
            if (mTaskDueDate >= 0) {
                mTimer = new Timer();
                mTaskTimer.put(mTaskId, mTimer);
                initializeTimerTask();
                //mTimer.schedule(mTimerTask, 10000);
                mTimer.schedule(mTimerTask, mTaskDueDate);
            }
        } else {
            System.out.println("********************************************finally enter else");
            AppExecutor.getInstance().diskIO().execute(new Runnable() {
                @Override
                public void run() {
                    List<Date> allTasksDueDate = AppDatabase.getInstance(getApplicationContext()).tasksDao().getAllTasksDueDate();
                    List<Integer> allTasksId = AppDatabase.getInstance(getApplicationContext()).tasksDao().getAllTasksId();
                    for (int i = 0; i < allTasksDueDate.size(); i++) {
                        if (allTasksDueDate.get(i).compareTo(new Date()) >= 0) {
                            mTimer = new Timer();
                            mTaskTimer.put(allTasksId.get(i), mTimer);
                            initializeTimerTask();
                            //mTimer.schedule(mTimerTask, 10000 + i * 1000);
                            Log.i("tasks due date", " task number = " + allTasksId.get(i));
                            mTimer.schedule(mTimerTask, allTasksDueDate.get(i));
                        }
                    }
                }
            });

        }

        System.out.println("start mTimer");
    }

    public void stopTimerTask() {
        //stop the mTimer, if it's not already null
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
    }

    public void initializeTimerTask() {

        mTimerTask = new TimerTask() {
            public void run() {

                //use a mHandler to run a toast that shows the current timestamp
                final boolean notification = mHandler.post(new Runnable() {
                    public void run() {

                        createNotificationChannel("22327");
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext(),"22327")
                                .setSmallIcon(R.drawable.ic_task_notification)
                                .setContentTitle(getResources().getText(R.string.app_name))
                                .setContentText(++mTasksCount + " tasks due date are met")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                // Set the intent that will fire when the user taps the notification
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true)
                                .setVisibility(1)
                                .setDefaults(Notification.DEFAULT_ALL); //To control the level of detail visible in the notification from the lock screen
                        if (mTasksCount == 1)
                            mBuilder.setContentText(mTasksCount + " task due date is met");

                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());
                        // notificationId is a unique int for each notification that you must define
                        notificationManager.notify(22327, mBuilder.build());
                        setBadge(getApplicationContext(), mTasksCount);

                    }
                });
            }
        };
    }

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
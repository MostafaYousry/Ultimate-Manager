package com.example.android.employeesmanagementapp;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import com.example.android.employeesmanagementapp.activities.AddDepartmentActivity;
import com.example.android.employeesmanagementapp.activities.AddTaskActivity;

import java.util.Timer;
import java.util.TimerTask;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class NotificationService extends Service {
    //we are going to use a handler to be able to run in our TimerTask
    final Handler handler = new Handler();
    Timer timer;
    TimerTask timerTask;
    String TAG = "Timers";
    int taskId;
    //int taskId;
    Long taskDueDate;

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);

        taskId = intent.getExtras().getInt("task id");
        taskDueDate = intent.getExtras().getLong("task due date");
        startTimer();
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");


    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        stopTimerTask();
        super.onDestroy();
    }

    public void startTimer() {
        //set a new Timer
        timer = new Timer();

        //initialize the TimerTask's job
        initializeTimerTask();

        System.out.println("start timer");
        //schedule the timer, after the first 5000ms
        Log.d("intent output","task Id = " + taskId);
        Log.d("intent output","task due date = " + taskDueDate);

        timer.schedule(timerTask, taskDueDate);
        //timer.schedule(timerTask, 5000,1000); //
    }

    public void stopTimerTask() {
        //stop the timer, if it's not already null
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public void initializeTimerTask() {

        timerTask = new TimerTask() {
            public void run() {

                //use a handler to run a toast that shows the current timestamp
                final boolean notification = handler.post(new Runnable() {
                    public void run() {

                        Intent intent = new Intent(getApplicationContext(), AddTaskActivity.class);
                        intent.putExtra(AddTaskActivity.TASK_ID_KEY, taskId);
                        intent.putExtra(AddTaskActivity.TASK_ENABLE_VIEWS_KEY,true);

                        //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, intent, 0);

                        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(getApplicationContext())
                                .setSmallIcon(R.drawable.ic_tasks)
                                .setContentTitle("Tasks notification")
                                .setContentText("This task period is end")
                                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                                // Set the intent that will fire when the user taps the notification
                                .setContentIntent(pendingIntent)
                                .setAutoCancel(true)
                                .setVisibility(1); //To control the level of detail visible in the notification from the lock screen
                        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(getApplicationContext());

                        // notificationId is a unique int for each notification that you must define
                        notificationManager.notify(5, mBuilder.build());

                    }
                });
            }
        };
    }
}

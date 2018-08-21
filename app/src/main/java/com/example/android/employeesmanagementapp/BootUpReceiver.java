package com.example.android.employeesmanagementapp;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class BootUpReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        Intent i = new Intent(context, NotificationService.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        context.startService(i);
        Toast.makeText(context,"bibibibibibibibi",Toast.LENGTH_LONG).show();
    }
}
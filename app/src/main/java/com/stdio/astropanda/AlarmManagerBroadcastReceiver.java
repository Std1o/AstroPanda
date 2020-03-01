package com.stdio.astropanda;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

public class AlarmManagerBroadcastReceiver extends BroadcastReceiver {
    final public static String ONE_TIME="onetime";

    @Override
    public void onReceive(Context context, Intent intent){
        PowerManager pm=(PowerManager) context.getSystemService(Context.POWER_SERVICE);
        PowerManager.WakeLock wl= pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"YOUR TAG");
//Осуществляем блокировку
        wl.acquire();
        SharedPreferences prefs = context.getSharedPreferences("moneyPref", Context.MODE_PRIVATE);
        if (prefs.getInt("moneyCount", 0) != 1050) {
            sendNotification(context, context.getString(R.string.push));
        }

//Разблокируем поток.
        wl.release();
    }
    public void SetAlarm(Context context)
    {
        AlarmManager am=(AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent=new Intent(context, AlarmManagerBroadcastReceiver.class);
        intent.putExtra(ONE_TIME, Boolean.FALSE);//Задаем параметр интента
        PendingIntent pi= PendingIntent.getBroadcast(context,0, intent,0);
//Устанавливаем интервал срабатывания в 5 секунд.
        am.setRepeating(AlarmManager.RTC_WAKEUP,System.currentTimeMillis(),1000*60*60*24,pi);
    }


    public void sendNotification(Context context, String messageBody) {
        int notificationCode = 378;

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notificationCode, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        Uri defaultRingtone = null;
        defaultRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager =
                    (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channel = new NotificationChannel("dayPlanner",
                    "Channel name",
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("Channel description");
            notificationManager.createNotificationChannel(channel);
            NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(context, "dayPlanner")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setSound(defaultRingtone)
                    .setContentTitle(context.getResources().getString(R.string.app_name))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setContentText(messageBody);
            notificationManager.notify(notificationCode, notificationCompat.build());
        } else {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationCompat.Builder notificationCompat = new NotificationCompat.Builder(context, "dayPlanner")
                    .setSmallIcon(R.drawable.ic_launcher_foreground)
                    .setSound(defaultRingtone)
                    .setContentTitle(context.getResources().getString(R.string.app_name))
                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent)
                    .setContentText(messageBody);
            notificationManager.notify(notificationCode, notificationCompat.build());
        }

    }
}

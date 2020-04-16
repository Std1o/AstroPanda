package com.stdio.astropanda;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import java.text.ParseException;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MyService extends Service {
    public static final int DEFAULT_NOTIFICATION_ID = 140;
    NotificationManager manager;

    public void onCreate() {
        super.onCreate();
    }
    private void startMyOwnForeground(){
        String NOTIFICATION_CHANNEL_ID = "com.stdio.astropanda";
        String channelName = "My Background Service";
        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        assert manager != null;
        NotificationChannel chan = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            chan = new NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLightColor(Color.BLUE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            manager.createNotificationChannel(chan);
        }

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
        Notification notification = notificationBuilder.setOngoing(true)
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("App is running in background")
                .setPriority(NotificationManager.IMPORTANCE_MIN)
                .setCategory(Notification.CATEGORY_SERVICE)
                .build();
        startForeground(27, notification);
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAQQQQQQQQQQQQQQQQQ");

       startMyOwnForeground();

       final Context context = getApplication();
        final Timer myTimer = new Timer();
        myTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                try {
                    System.out.println(!isRunning(getApplicationContext()));
                    if (!isRunning(getApplicationContext())) {
                        ExcelCreator.createExcelFile(context, getString(R.string.app_name), null);
                        myTimer.cancel();
                        stopSelf();
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }

        }, 0, 1000);

        //return Service.START_STICKY;
        return START_REDELIVER_INTENT;
    }

    public static boolean isRunning(Context ctx) {
        ActivityManager activityManager = (ActivityManager) ctx.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

        for (ActivityManager.RunningTaskInfo task : tasks) {
            if (ctx.getPackageName().equalsIgnoreCase(task.baseActivity.getPackageName()))
                return true;
        }
        return false;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        //Removing any notifications
        manager.cancel(DEFAULT_NOTIFICATION_ID);

        //Disabling service
        stopSelf();
    }
}

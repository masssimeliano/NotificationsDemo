package com.example.notificationsdemo;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import java.util.Date;

public class MyService extends Service {

    private NotificationManager notificationManager;
    private static final int NOTIFY_ID = 1;
    private static final String CHANNEL_ID = "CHANNEL_ID";

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("Service", "Created");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d("Service", "Started Command");

        final long time = intent.getLongExtra("Time", startId);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Date date = new Date();

                int time_now = date.getHours()*60 + date.getMinutes();
                Log.d("Service", Integer.toString(time_now) + "VS" + Long.toString(time));

                while (time_now < time)
                {
                    date = new Date();
                    time_now = date.getHours()*60 + date.getMinutes();
                }
                Log.d("Service", Integer.toString(time_now) + " VS " + time);
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                PendingIntent pendingIntent = PendingIntent.getActivity(MyService.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round, options);

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setLargeIcon(bitmap)
                        .setContentTitle("Title")
                        .setContentText("Text")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_MAX);

                createChannelIfNeeded(notificationManager);

                notificationManager.notify(NOTIFY_ID, notificationBuilder.build());

                Log.d("Service", "Check");

                stopSelf();
            }
        }).start();

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("Service", "Destroyed");
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    private void createChannelIfNeeded(NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, "Channel Name", NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);

            Log.d("NotificationChannel", "Successfully Created NotificationChannel");
        }
        else
            Log.d("NotificationChannel", "Build Version Is Too Low For NotificationChannel");
    }
}

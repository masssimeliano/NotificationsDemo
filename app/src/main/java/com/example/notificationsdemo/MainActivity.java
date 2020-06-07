package com.example.notificationsdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private Button mBNotify;
    private TimePicker mTimePicker;

    private TimerTask timerTask;

    private NotificationManager notificationManager;
    private NotificationCompat.Builder notificationBuilder;

    private int count_id = 0;

    private static final String CHANNEL_ID = "CHANNEL_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBNotify = (Button) findViewById(R.id.mBNotify);
        mTimePicker = (TimePicker) findViewById(R.id.mTimePicker);

        mTimePicker.setIs24HourView(true);

        mBNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int time_timePicker = getTimeFromTimePicker();
                int time_now = getTimeNow();

                if (time_timePicker <= time_now)
                    Toast.makeText(MainActivity.this, "Wrong Schedule", Toast.LENGTH_LONG).show();
                else {
                    count_id = count_id + 1;

                    formNotification(count_id);
                    sendNotification(time_timePicker);

                    int hours_timePicker = time_timePicker/60;
                    int minutes_timePicker = time_timePicker - hours_timePicker*60;

                    Toast.makeText(MainActivity.this, "Set New Notification On " + getTimeInFormat(minutes_timePicker, hours_timePicker), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private int getTimeFromTimePicker() {
        int minutes_timePicker = mTimePicker.getMinute();
        int hours_timePicker = mTimePicker.getHour();
        int time_timePicker = hours_timePicker*60 + minutes_timePicker;

        Log.d("TimePicker", "Time (TimePicker) = " + getTimeInFormat(minutes_timePicker, hours_timePicker));

        return time_timePicker;
    }

    private int getTimeNow() {
        int minutes_now = Calendar.getInstance().get(Calendar.MINUTE);
        int hours_now = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int time_now = hours_now*60 + minutes_now;

        Log.d("TimePicker", "Time (Now) = " + getTimeInFormat(minutes_now, hours_now));

        return time_now;
    }

    private String getTimeInFormat(int minutes, int hours) {
        String time;

        if (minutes > 9)
            time = hours + ":" + minutes;
        else
            time = hours + ":" + "0" + minutes;

        return time;
    }

    private void formNotification(final int NOTIFICATION_ID) {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intentNotifiy = new Intent(getApplicationContext(), MainActivity.class);
        intentNotifiy.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intentNotifiy, PendingIntent.FLAG_UPDATE_CURRENT);

        BitmapFactory.Options options = new BitmapFactory.Options();
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round, options);

        notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                .setSmallIcon(R.mipmap.ic_launcher_round)
                .setLargeIcon(bitmap)
                .setContentTitle("Title")
                .setContentText("Text")
                .setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        createChannelIfNeeded(notificationManager);

        timerTask = new TimerTask() {
            @Override
            public void run() {
                notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build());
            }
        };
    }

    private void sendNotification(int time_notification) {
        Timer timer = new Timer();

        long delay = ((time_notification - Calendar.getInstance().get(Calendar.HOUR_OF_DAY)*60 - Calendar.getInstance().get(Calendar.MINUTE))*60 - Calendar.getInstance().get(Calendar.SECOND))*1000;

        Log.d("Delay", "Delay = " + delay/1000 + " Sec");

        timer.schedule(timerTask, delay);
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
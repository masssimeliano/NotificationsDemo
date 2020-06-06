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

public class MainActivity extends AppCompatActivity {

    private Button mBNotify;

    private NotificationManager notificationManager;
    private static final int NOTIFY_ID = 1;
    private static final String CHANNEL_ID = "CHANNEL_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBNotify = (Button) findViewById(R.id.mBNotify);

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        mBNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);

                PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher_round, options);

                NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
                        .setSmallIcon(R.mipmap.ic_launcher_round)
                        .setLargeIcon(bitmap)
                        .setContentTitle("Title")
                        .setContentText("Text")
                        .setContentIntent(pendingIntent)
                        .setAutoCancel(true)
                        .setWhen(System.currentTimeMillis())
                        .setPriority(NotificationCompat.PRIORITY_MAX);

                createChannelIfNeeded(notificationManager);

                notificationManager.notify(NOTIFY_ID, notificationBuilder.build());
            }
        });
    }

    private void createChannelIfNeeded(NotificationManager notificationManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_ID, NotificationManager.IMPORTANCE_DEFAULT);
            notificationManager.createNotificationChannel(notificationChannel);

            Log.d("NotificationChannel", "Successfully Created NotificationChannel");
        }
        else
            Log.d("NotificationChannel", "Build Version Is Too Low For NotificationChannel");
    }
}
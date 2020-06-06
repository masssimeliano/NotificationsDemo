package com.example.notificationsdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    private Button mBNotify;

    private TimePicker mTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBNotify = (Button) findViewById(R.id.mBNotify);
        mTimePicker = (TimePicker) findViewById(R.id.mTimePicker);

        mBNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent serviceIntent = new Intent(MainActivity.this, MyService.class);
                serviceIntent.putExtra("Time", getTimeFromTimePicker());
                startService(serviceIntent);
            }
        });
    }

    private long getTimeFromTimePicker() {
        int minute_timePicker = mTimePicker.getMinute();
        int hour_timePicker = mTimePicker.getHour();
        int time_timePicker = hour_timePicker*60 + minute_timePicker;

        Toast.makeText(MainActivity.this, "Notification Set On " + hour_timePicker + ":" + minute_timePicker, Toast.LENGTH_LONG).show();

        Log.d("TimePicker", "Notification Set On " + hour_timePicker + ":" + minute_timePicker);

        return time_timePicker;
    }
}
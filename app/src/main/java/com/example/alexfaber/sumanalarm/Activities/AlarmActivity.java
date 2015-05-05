package com.example.alexfaber.sumanalarm.Activities;

import android.app.Activity;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.alexfaber.sumanalarm.Alarm;
import com.example.alexfaber.sumanalarm.R;

import com.gimbal.android.Beacon;
import com.gimbal.android.BeaconEventListener;
import com.gimbal.android.BeaconManager;
import com.gimbal.android.BeaconSighting;

public class AlarmActivity extends ActionBarActivity{
    private Ringtone alarmTone;
    private int initialStrength;
    private boolean isInitialSighting;
    private BeaconManager beaconManager;
    private BeaconEventListener beaconSightingListener;
    private long startTime = 0;
    private long finishTime = 0;


    private static final String TAG = "AlarmActivity";

    protected void onCreate(Bundle savedInstanceState) {
        isInitialSighting = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        setupAlarmTone();


        beaconSightingListener = new BeaconEventListener() {
            @Override
            public void onBeaconSighting(BeaconSighting sighting) {
                Log.v("Gimbal Sighting", sighting.toString());
                boolean inRange = false;
                if(isInitialSighting){
                    initialStrength = sighting.getRSSI();
                    isInitialSighting = false;
                }
                if(sighting.getRSSI() > -50){
                    finishTime = System.currentTimeMillis();
                    toggleAlarm(findViewById(R.id.toggleButton));
                    beaconManager.stopListening();
                }
            }
        };
        beaconManager = new BeaconManager();
        beaconManager.addListener(beaconSightingListener);
        Log.v("AlarmActivity", "Starting listener");
        beaconManager.startListening();
        startTime = System.currentTimeMillis();
        toggleAlarmSound();
    }

    protected void onDestroy()
    {
        if (alarmTone.isPlaying())
            alarmTone.stop();
    }


    private void setupAlarmTone()
    {
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        if (notification == null)
        {
            notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            if (notification == null)
            {
                notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_RINGTONE);
            }
        }
        alarmTone = RingtoneManager.getRingtone(this, notification);
    }

    private void toggleAlarmSound()
    {
        if (alarmTone.isPlaying())
            alarmTone.stop();
        else
            alarmTone.play();
    }

    public void setSnooze(View view)
    {
        Log.v("AlarmActivity", "setSnooze() called at: " + System.currentTimeMillis());

        if (alarmTone.isPlaying())
            toggleAlarmSound();

        long time = 0;
        if (view.getId() == R.id.snooze5minutes)
        {
            time = 5 * Alarm.getAlarm().MINUTE;
        }
        else if (view.getId() == R.id.snooze10minutes)
        {
            time = 10 * Alarm.getAlarm().MINUTE;
        }
        else if (view.getId() == R.id.snooze30minutes)
        {
            time = 30 * Alarm.getAlarm().MINUTE;
        }
        else if (view.getId() == R.id.snooze60minutes)
        {
            time = 60 * Alarm.getAlarm().MINUTE;
        }
        Log.v("AlarmActivity", "button id: " + view.getId());
        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        Alarm.getAlarm().setSnooze(this, time);
        startActivity(mainActivityIntent);
    }

    public void toggleAlarm(View view)
    {
        Alarm.getAlarm().turnedOff();
        if (alarmTone.isPlaying())
            toggleAlarmSound();

        double timeElapsed = finishTime - startTime;
        double scoreWithoutStrength = 1 / timeElapsed;
        double score = scoreWithoutStrength * initialStrength * initialStrength * 100;
        Log.v("InitialStrength", String.valueOf(initialStrength));
        Log.v("TimeElapsed", String.valueOf(timeElapsed));
        Log.v("AlarmScore",  String.valueOf(score));
        Toast.makeText(this, "Your Morning Score: " + String.valueOf(score), Toast.LENGTH_LONG).show();
        Button button = (Button) findViewById(R.id.toggleButton);
        button.setEnabled(false);

        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
    }
}


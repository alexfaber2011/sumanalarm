package com.example.alexfaber.sumanalarm.Activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import com.example.alexfaber.sumanalarm.ApplicationController;
import com.example.alexfaber.sumanalarm.Models.Backend;
import com.example.alexfaber.sumanalarm.Models.Challenge;
import com.example.alexfaber.sumanalarm.Models.UserRESTClient;
import com.example.alexfaber.sumanalarm.R;

import com.gimbal.android.BeaconEventListener;
import com.gimbal.android.BeaconManager;
import com.gimbal.android.BeaconSighting;

public class AlarmActivity extends ActionBarActivity{
    private Ringtone alarmTone;
    private int initialStrength;
    private boolean isInitialSighting;
    private boolean foundBeacon = false;
    private BeaconManager beaconManager;
    private BeaconEventListener beaconSightingListener;
    private long startTime = 0;
    private long finishTime = 0;
    private SharedPreferences userPrefs;
    private String userId;
    private Context self;


    private static final String TAG = "AlarmActivity";

    protected void onCreate(Bundle savedInstanceState) {
        isInitialSighting = true;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        setupAlarmTone();

        self = this;


        beaconSightingListener = new BeaconEventListener() {
            @Override
            public void onBeaconSighting(BeaconSighting sighting) {
                Log.v("Gimbal Sighting", sighting.toString());
                foundBeacon = true;
                if(isInitialSighting){
                    Toast.makeText(self, "Gimbal Beacon Found", Toast.LENGTH_SHORT).show();
                    initialStrength = sighting.getRSSI();
                    isInitialSighting = false;
                }
                if(sighting.getRSSI() > -60){
                    finishTime = System.currentTimeMillis();
                    toggleAlarm(findViewById(R.id.toggleButton));
                }
            }
        };
        beaconManager = new BeaconManager();
        beaconManager.addListener(beaconSightingListener);
        Log.v("AlarmActivity", "Starting listener");
        beaconManager.startListening();
        startTime = System.currentTimeMillis();
        toggleAlarmSound();

        //Grab user preferences
        userPrefs = getSharedPreferences(ApplicationController.USER_SHARED_PREFS, Context.MODE_PRIVATE);
        userId = userPrefs.getString("_id", null);
        if(userId == null){
            Intent intent = new Intent(this, UserLoginActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
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

        beaconManager.stopListening();
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
        beaconManager.stopListening();
        if (alarmTone.isPlaying())
            toggleAlarmSound();

        double score = 1;
        if(finishTime > 0){
            //only set score if turned off by beacon
            double timeElapsed = finishTime - startTime;
            double scoreWithoutStrength = 1 / timeElapsed;
            score = scoreWithoutStrength * initialStrength * initialStrength * 100;
        } else if (!foundBeacon){
            //if beacon is not found tell the user and skip the score update
            Toast.makeText(this, "No beacon found. Score not calculated.", Toast.LENGTH_LONG).show();
            Intent mainActivityIntent = new Intent(this, MainActivity.class);
            startActivity(mainActivityIntent);
            return;
        }
        Log.v("AlarmScore",  String.valueOf(score));
        Toast.makeText(this, "Your Morning Score: " + String.valueOf(score), Toast.LENGTH_LONG).show();
        Button button = (Button) findViewById(R.id.toggleButton);
        button.setEnabled(false);

        //Update the user's score
        updateUserScore(score);

        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
    }

    private void updateUserScore(double score){
        UserRESTClient.updateAllScores(userId, score, new Backend.BackendCallback() {
            @Override
            public void onRequestCompleted(Object result) {
                Toast.makeText(self, "Successfully submitted score", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onRequestFailed(String errorCode) {
                switch (errorCode) {
                    //TODO Handle the servers error code when user tries to end a challenge that's not theirs.  (shouldn't happen, but it would be nice)
                    case "400":
                        Toast.makeText(self, errorCode + " : Bad Request Made to the Server", Toast.LENGTH_LONG).show();
                        break;
                    case "404":
                        Toast.makeText(self,"No Challenges Accepted or Invited To", Toast.LENGTH_LONG).show();
                        break;
                    default:
                        Toast.makeText(self, errorCode + " Error", Toast.LENGTH_LONG).show();
                        break;
                }
            }
        });
    }
}


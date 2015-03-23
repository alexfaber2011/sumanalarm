package com.example.alexfaber.sumanalarm.Activities;

import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.alexfaber.sumanalarm.R;

public class AlarmActivity extends ActionBarActivity{
    private Ringtone alarmTone;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        setupAlarmTone();

        // Turn alarm on
        toggleAlarmSound();
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

    public void toggleAlarm(View view)
    {
        toggleAlarmSound();
        Button button = (Button) findViewById(R.id.toggleButton);
        button.setEnabled(false);

        Intent mainActivityIntent = new Intent(this, MainActivity.class);
        startActivity(mainActivityIntent);
    }
}


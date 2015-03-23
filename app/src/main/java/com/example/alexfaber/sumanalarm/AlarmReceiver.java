package com.example.alexfaber.sumanalarm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import com.example.alexfaber.sumanalarm.Activities.AlarmActivity;

import java.util.Calendar;


public class AlarmReceiver extends WakefulBroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Intent alarmIntent = new Intent(context, AlarmActivity.class);
        alarmIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(alarmIntent);

        Log.v("AlarmReceiver", "in onReceive method at: " + System.currentTimeMillis());
    }
}


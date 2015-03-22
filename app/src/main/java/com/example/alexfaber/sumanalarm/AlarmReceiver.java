package com.example.alexfaber.sumanalarm;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

public class AlarmReceiver extends WakefulBroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "This is your alarm going off!",
                Toast.LENGTH_LONG).show();
        Log.v("AlarmReceiver", "in onReceive method at: " + System.currentTimeMillis());
    }
}


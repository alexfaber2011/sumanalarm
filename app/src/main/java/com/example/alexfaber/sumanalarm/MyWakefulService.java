package com.example.alexfaber.sumanalarm;

import android.app.IntentService;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;

/**
 * Created by Wes on 3/21/2015.
 */
public class MyWakefulService extends Service {


    public static final String INTENT_FILTER = "android.intent.action.MYWAKEFULSERVICE";

    protected void onHandleIntent(Intent intent)
    {
        Log.v("Wake", "Waking up at!" + Long.toString(Calendar.getInstance().getTimeInMillis()));
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.v("Bind", "We've been bound!" + Long.toString(Calendar.getInstance().getTimeInMillis()));
        Toast.makeText(this, "The alarm has been hit",
                        Toast.LENGTH_LONG).show();
        return null;
    }
}

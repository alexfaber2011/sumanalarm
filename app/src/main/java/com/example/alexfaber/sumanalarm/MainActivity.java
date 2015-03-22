package com.example.alexfaber.sumanalarm;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.provider.AlarmClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

import static android.support.v4.content.WakefulBroadcastReceiver.startWakefulService;

public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";
    TimePicker picker;
    AlarmClock alarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(TAG, "CREATED");
        picker = (TimePicker)findViewById(R.id.picker);

        Button confirmButton = (Button)findViewById(R.id.confirm_alarm);
        confirmButton.setOnClickListener(this);

        Intent intent2 = new Intent(getBaseContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getBaseContext(), 0, intent2, 0);
        Log.v("test", "In onCreate - Date before: " + Long.toString(System.currentTimeMillis()));
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (2 * 1000),
                pendingIntent);

//        WORKING!!
//        Intent intent2 = new Intent(this, AlarmReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 1, intent2, 0);
//        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
//                + (i * 1000), pendingIntent);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.options, menu);
        return(super.onCreateOptionsMenu(menu));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.set_alarm_button:
                Intent intent = new Intent(this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return(true);
            case R.id.settings_button:
                intent = new Intent(this, Settings.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return(true);
            case R.id.challenges_button:
                intent = new Intent(this, Challenges.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                return(true);
        }
        return(super.onOptionsItemSelected(item));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case  R.id.confirm_alarm: {
                Integer minute = picker.getCurrentMinute();
                Integer hour = picker.getCurrentHour();
                long millisecond = Calendar.getInstance().getTimeInMillis();
//                Log.v("test", "Date: " + Long.toString(millisecond));
//                Intent intent = new Intent(v.getContext(), MyWakefulService.class);
//                startService(intent);
////                bindService(intent, new ServiceConnection() {
////                    @Override
////                    public void onServiceConnected(ComponentName name, IBinder service) {
////
////                    }
////
////                    @Override
////                    public void onServiceDisconnected(ComponentName name) {
////
////                    }
////
////                }, BIND_AUTO_CREATE);
//
////                PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_NO_CREATE);
//
//                Log.v("test", "Date before: " + Long.toString(System.currentTimeMillis()));
//
//                AlarmManager alarmMgr = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//                PendingIntent alarmIntent = PendingIntent.getBroadcast(v.getContext(), 0, intent, 0);
//
//                alarmMgr.setExact(AlarmManager.RTC_WAKEUP,
//                        System.currentTimeMillis() +
//                                5 * 1000, alarmIntent);
//

                long alarmTime = System.currentTimeMillis()
                        + (2 * 1000);
                Intent intent2 = new Intent(this, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent2, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
                Toast.makeText(this, "Alarm set in " + 2 + " seconds",
                        Toast.LENGTH_LONG).show();
                Log.v("test", "Alarm Date after: " + Long.toString(System.currentTimeMillis()));

                Log.v("test", "Date after: " + Long.toString(Calendar.getInstance().getTimeInMillis()));
//
//                Intent intent2 = new Intent(this, MyWakefulService.class);
//                PendingIntent pendingIntent = PendingIntent.getBroadcast(
//                        this.getApplicationContext(), 234324243, intent2, 0);
//                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
//                alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
//                        + (5 * 1000), pendingIntent);
//                Toast.makeText(this, "Alarm set in " + 5 + " seconds",
//                        Toast.LENGTH_LONG).show();

                break;
            }
        }
    }
}

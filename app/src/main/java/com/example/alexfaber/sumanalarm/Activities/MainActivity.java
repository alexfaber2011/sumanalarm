package com.example.alexfaber.sumanalarm.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.alexfaber.sumanalarm.Alarm;
import com.example.alexfaber.sumanalarm.AlarmReceiver;
import com.example.alexfaber.sumanalarm.ApplicationController;
import com.example.alexfaber.sumanalarm.R;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";

    TimePicker picker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        picker = (TimePicker)findViewById(R.id.picker);

        Button confirmButton = (Button)findViewById(R.id.confirm_alarm);
        confirmButton.setOnClickListener(this);

        //Check to see if user is logged in (by checking only their userName... lawlz)
        SharedPreferences userPrefs = getSharedPreferences(ApplicationController.USER_SHARED_PREFS, Context.MODE_PRIVATE);
        String userName = userPrefs.getString("userName", null);
        Log.v(TAG, "userName: " + userName);
        if(userName == null){
            Intent intent = new Intent(this, UserLogin.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
        }
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
        Log.v("onClick", "clicked!");
        switch (v.getId()) {
            case  R.id.confirm_alarm: {
                Integer minutes = picker.getCurrentMinute();
                Integer hours = picker.getCurrentHour();

                Alarm.getAlarm().setAlarmAtTime(this, minutes, hours);

                break;
            }
        }
    }
}

package com.example.alexfaber.sumanalarm.Activities;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.alexfaber.sumanalarm.AlarmReceiver;
import com.example.alexfaber.sumanalarm.R;

import java.util.Calendar;
import java.util.GregorianCalendar;


public class MainActivity extends ActionBarActivity implements View.OnClickListener{

    private static final String TAG = "MainActivity";
    TimePicker picker;

    final long DAY = 86400000;
    final long HOUR = 3600000;
    final long MINUTE = 60000;
    final long SECOND = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.v(TAG, "CREATED");
        picker = (TimePicker)findViewById(R.id.picker);

        Button confirmButton = (Button)findViewById(R.id.confirm_alarm);
        confirmButton.setOnClickListener(this);
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

                if (!inTheFuture(hours, minutes))
                {
                    Toast.makeText(this, "Alarm is set to a time in the past, please pick a time in the future.",
                            Toast.LENGTH_LONG).show();
                    break;
                }

                long alarmTime = getAlarmTimeInMilliseconds(hours, minutes);

                Intent intent = new Intent(this, AlarmReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);

                // TODO: Pretty print the alarm time from now
                long difference = alarmTime - System.currentTimeMillis();

                String timeFromNow = prettyPrintTimeDifference(difference);

                Toast.makeText(this, "Alarm is set to go off " + timeFromNow + "from now",
                        Toast.LENGTH_SHORT).show();
                Log.v("test", "Set alarm at: " + Long.toString(System.currentTimeMillis()));

                break;
            }
        }
    }

    private long getAlarmTimeInMilliseconds(int hours, int minutes)
    {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int year = Calendar.getInstance().get(Calendar.YEAR);

        GregorianCalendar today = new GregorianCalendar(year, month, day);

        return today.getTimeInMillis() + (HOUR * hours) + (MINUTE * minutes);
    }

    private boolean inTheFuture(int alarmHour, int alarmMinute)
    {
        int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        int currentMinute = Calendar.getInstance().get(Calendar.MINUTE);

        boolean futureHour = alarmHour > currentHour;
        boolean futureMinuteThisHour = (alarmHour == currentHour) && (alarmMinute > currentMinute);

        return futureHour || futureMinuteThisHour;
    }

    private String prettyPrintTimeDifference(long timeDifference)
    {
        int days = (int)(timeDifference / DAY);
        timeDifference -= days * DAY;
        int hours = (int)(timeDifference / HOUR);
        timeDifference -= hours * HOUR;
        int minutes = (int)(timeDifference / MINUTE);
        timeDifference -= minutes * MINUTE;
        int seconds = (int)(timeDifference / SECOND);

        String output = "";
        if (days > 0)
            output += Integer.toString(days) + " days ";
        if (hours > 0)
            output += Integer.toString(hours) + " hours ";
        if (minutes > 0)
            output += Integer.toString(minutes) + " minutes ";
        if (seconds > 0)
            output += Integer.toString(seconds) + " seconds ";

        return output;
    }
}

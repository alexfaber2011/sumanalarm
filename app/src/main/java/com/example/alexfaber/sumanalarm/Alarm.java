package com.example.alexfaber.sumanalarm;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;


// This class a singleton so only one alarm can be set
public class Alarm {
    private static Alarm alarmSingleton = new Alarm();

    public final long DAY = 86400000;
    public final long HOUR = 3600000;
    public final long MINUTE = 60000;
    public final long SECOND = 1000;
    boolean alarmSet;

    private long setAlarmTime;

    public static Alarm getAlarm()
    {
        return alarmSingleton;
    }

    public String getSetAlarmTimePrettyPrinted()
    {
        if (isAlarmSet())
            return getAlarm().getAlarmDateTimePrettyPrinted(setAlarmTime);
        else
            return "";
    }

    public void setAlarmAtTime(Activity sendingActivity, Integer minutes, Integer hours)
    {
        if (isAlarmSet())
        {
            Toast.makeText(sendingActivity, "Alarm is already set.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        if (!inTheFuture(hours, minutes))
        {
            Toast.makeText(sendingActivity, "Alarm is set to a time in the past, please pick a time in the future.",
                    Toast.LENGTH_LONG).show();
            return;
        }

        long alarmTime = getAlarmTimeInMilliseconds(hours, minutes);

        setAlarm(sendingActivity, alarmTime);

        long difference = alarmTime - System.currentTimeMillis();
        String timeFromNow = prettyPrintTimeDifference(difference);

        Toast.makeText(sendingActivity, "Alarm is set to go off " + timeFromNow + "from now",
                Toast.LENGTH_SHORT).show();
        Log.v("test", "Set alarm at: " + Long.toString(System.currentTimeMillis()));

        alarmSet = true;
    }

    public void setSnooze(Activity sendingActivity, long snoozeTime)
    {
        alarmSet = false;
        long snoozeAlarmTime = System.currentTimeMillis() + snoozeTime;

        setAlarm(sendingActivity, snoozeAlarmTime);
        setAlarmTime = snoozeAlarmTime;

        String timePhrase = "";
        if (snoozeTime / MINUTE < 60)
            timePhrase = snoozeTime / MINUTE + " minutes";
        else
            timePhrase = snoozeTime / HOUR + " hour";

        Toast.makeText(sendingActivity, "Snoozing: Alarm is set to go off " + timePhrase + " from now",
                Toast.LENGTH_SHORT).show();
    }

    public void turnedOff()
    {
        alarmSet = false;
    }

    private void setAlarm(Activity sendingActivity, long alarmTime)
    {
        Intent intent = new Intent(sendingActivity, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(sendingActivity, 1, intent, 0);
        AlarmManager alarmManager = (AlarmManager) sendingActivity.getSystemService(sendingActivity.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmTime, pendingIntent);
        setAlarmTime = alarmTime;
    }

    private boolean isAlarmSet()
    {
        return alarmSet;
    }

    private long getAlarmTimeInMilliseconds(int hours, int minutes)
    {
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int month = Calendar.getInstance().get(Calendar.MONTH);
        int year = Calendar.getInstance().get(Calendar.YEAR);

        GregorianCalendar today = new GregorianCalendar(year, month, day);

        return today.getTimeInMillis() + (HOUR * hours) + (MINUTE * minutes);
    }

    private String getAlarmDateTimePrettyPrinted(long alarmTime)
    {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(alarmTime);
        int hour = calendar.get(Calendar.HOUR);
        hour = hour > 12 ? hour - 12 : hour;
        int minute = calendar.get(Calendar.MINUTE);
        int isAm = calendar.get(Calendar.AM_PM);

        Log.v("","" + calendar.toString());

        String dayOfWeekName = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.US);
        String am_pm = isAm == 0 ? "AM" : "PM";

        return String.format("%s %d:%d %s",dayOfWeekName, hour, minute, am_pm);
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

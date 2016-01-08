package com.example.ckenken.hw2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmService extends Service {

    final public static int MISSION_MAINSTART = 0;
    final public static int MISSION_TURNON = 1;
    final public static int MISSION_TURNOFF = 2;


    public static ArrayList<Alarm> alarms = new ArrayList<Alarm>();
    public static ArrayList<AlarmManager> alarmManagers= new ArrayList<AlarmManager>();

    public AlarmService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Bundle b = intent.getExtras();

        int id = -1;
        switch (b.getInt("mission_id")) {
            case MISSION_MAINSTART:

                for(int i = 0; i<alarms.size(); i++) {
                    if (alarms.get(i).a_on) {
                        turnOnAlarm(i);
                    }
                }

                break;
            case MISSION_TURNON:
                id = b.getInt("alarm_id");
                turnOnAlarm(id);
                break;
            case MISSION_TURNOFF:
                id = b.getInt("alarm_id");
                turnOffAlarm(id);
                break;
            default:
                break;
        }


        return START_REDELIVER_INTENT;
    }

    public void turnOnAlarm(int id)
    {
        Intent intent = new Intent(AlarmService.this, NotificationActivity.class);

        Calendar c = Calendar.getInstance();

        c.set(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH), alarms.get(id).hour, alarms.get(id).min, 0);

        if (checkBefore(c)) {
            c.add(Calendar.DATE, 1);
        }

        Log.d("turn_on, time:", Integer.toString(alarms.get(id).hour) + ":" + Integer.toString(alarms.get(id).min));
        PendingIntent pi = PendingIntent.getActivity(AlarmService.this, 0, intent, 0);

        alarms.get(id).setPendingPi(pi);

        alarmManagers.get(id).set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
    }

    public boolean checkBefore(Calendar c)
    {
        if (c.getTimeInMillis() <= Calendar.getInstance().getTimeInMillis()) {
            return true;
        }
        else {
            return false;
        }
    }

    public void turnOffAlarm(int id) {
        alarmManagers.get(id).cancel(alarms.get(id).pi);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}

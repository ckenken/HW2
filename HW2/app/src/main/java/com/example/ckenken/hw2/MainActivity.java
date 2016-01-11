package com.example.ckenken.hw2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final int REFRESH = 1;
    public static final int AM = 0;
    public static final int PM = 1;
    public static boolean ALARM_ON = true;
    public static boolean ALARM_OFF = false;
    public static final String NO_OLD_DATA= "__NO_SAVE";
    public static final String OLD_ALARMS_DATA = "com.ckenken.old.alarms.data";

    public static SimpleDateFormat sdf = new SimpleDateFormat("EEE, MM/dd hh:mm:ss aaa");

    private static TextView mLabel;

    private Button mSetAlarmButton;

    private TimeThread mRun1 = new TimeThread();

    private Handler mhandler = new Handler();

    Button testAlarmButton;
    Button testSavePreference;
    Button testRestorePreference;
    Button testRemovePreference;
    Button gogogoButton;
    TextView showPreference;

    private AlarmManager alarmManager;

    class TimeThread implements Runnable {

        private boolean isRunning = true;

        @Override
        public void run() {
            Calendar c = Calendar.getInstance();
            Date d = c.getTime();
            mLabel.setText(sdf.format(d));
            mhandler.postDelayed(this, 500);
        }

        public void stopThread() {
            this.isRunning = false;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        mSetAlarmButton = (Button)findViewById(R.id.button3);

        mLabel = (TextView)findViewById(R.id.textView);

        mSetAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, SetAlarmActivity.class);
                startActivity(intent);
            }
        });

        alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

        testAlarmButton = (Button)findViewById(R.id.button5);

        testAlarmButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);

                Calendar c = Calendar.getInstance();
                c.set(2016, 0, 7, 20, 40, 0);

                PendingIntent pi = PendingIntent.getActivity(MainActivity.this, 0, intent, 0);

                alarmManager.set(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pi);
            }
        });

        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        String output = sharedPref.getString(getString(R.string.old_alarms_data), NO_OLD_DATA);

        Log.d("OnCreate, old data:", output);

        if (!output.equals(NO_OLD_DATA) && AlarmService.alarms.size() == 0) {
            try {
                AlarmService.restoreAlarms(output);
                for(int i = 0; i<AlarmService.alarms.size(); i++) {
                    AlarmService.alarmManagers.add((AlarmManager) getSystemService(ALARM_SERVICE));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        Intent startIntent = new Intent(MainActivity.this, AlarmService.class);
        Bundle b = new Bundle();
        b.putInt("mission_id", AlarmService.MISSION_MAINSTART);
        startIntent.putExtras(b);
        startService(startIntent);

        ////////////////////  test block  //////////////////////

        testSavePreference = (Button) findViewById(R.id.button6);
        testRestorePreference = (Button) findViewById(R.id.button7);
        testRemovePreference = (Button) findViewById(R.id.button8);
        showPreference = (TextView) findViewById(R.id.textView10);

        testSavePreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.putString(getString(R.string.time_string), mLabel.getText().toString());
                editor.commit();
            }
        });

        testRestorePreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
            //    int defaultValue = getResources().getInteger(R.string.saved_high_score_default);
                String output = sharedPref.getString(getString(R.string.time_string), NO_OLD_DATA);
                showPreference.setText(output);
            }
        });

        testRemovePreference.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPref.edit();
                editor.remove(getString(R.string.time_string));
                editor.commit();
                editor.remove(getString(R.string.old_alarms_data));
                editor.commit();
                AlarmService.alarms = new ArrayList<Alarm>();
                System.gc();
            }
        });

        gogogoButton = (Button)findViewById(R.id.button9);

        gogogoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, TestActivity.class);
                startActivity(intent);
            }
        });

        gogogoButton.setVisibility(View.INVISIBLE);
        testSavePreference.setVisibility(View.INVISIBLE);
        testRestorePreference.setVisibility(View.INVISIBLE);
        showPreference.setVisibility(View.INVISIBLE);
        testAlarmButton.setVisibility(View.INVISIBLE);
        ////////////////////  test block  //////////////////////
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        mhandler.removeCallbacks(mRun1);

        try {
            saveAlarms(AlarmService.getSaveAlarmsString());
            Log.d("in_Check, saveString:", AlarmService.getSaveAlarmsString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        mhandler.postDelayed(mRun1, 500);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void saveAlarms(String jaString)
    {
        SharedPreferences sharedPref = MainActivity.this.getPreferences(Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        //editor.remove(getString(R.string.old_alarms_data));

        Log.d("In_SaveAlarms2, jaStr:", jaString);

        editor.putString(getString(R.string.old_alarms_data), jaString);
        editor.commit();
    }
}

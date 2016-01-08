package com.example.ckenken.hw2;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    public static final int REFRESH = 1;
    public static final int AM = 0;
    public static final int PM = 1;
    public static boolean ALARM_ON = true;
    public static boolean ALARM_OFF = false;

    public static SimpleDateFormat sdf = new SimpleDateFormat("EEE, MM/dd hh:mm:ss aaa");

    private static TextView mLabel;

    private Button mSetAlarmButton;

    private TimeThread mRun1 = new TimeThread();

    private Handler mhandler = new Handler();

    Button testAlarmButton;

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


        Intent startIntent = new Intent(MainActivity.this, AlarmService.class);
        Bundle b = new Bundle();
        b.putInt("mission_id", AlarmService.MISSION_MAINSTART);
        startIntent.putExtras(b);
        startService(startIntent);

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
}

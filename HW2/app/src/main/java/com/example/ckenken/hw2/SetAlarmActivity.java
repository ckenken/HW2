package com.example.ckenken.hw2;

import android.app.AlarmManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

public class SetAlarmActivity extends AppCompatActivity {

    private ListView mListView;

    private Button mNewAlarm;

    private AlarmAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_alarm);
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

        adapter = new AlarmAdapter(SetAlarmActivity.this, AlarmService.alarms);

        mListView = (ListView)findViewById(R.id.listView);
        mListView.setAdapter(adapter);
        mListView.setTextFilterEnabled(true);

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

//                Bundle bundle = new Bundle();
//
//                bundle.putString("title", top);
//                bundle.putString("stop", titles.get(row));
//
//                Intent intent = new Intent();
//                intent.setClass(StopList.this, BusDetail.class);
//
//                intent.putExtras(bundle);
//
//                startActivity(intent);
//                StopList.this.finish();

            }
        });

        mNewAlarm = (Button)findViewById(R.id.button);

        mNewAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Alarm newAlarm = new Alarm(AlarmService.alarms.size(), MainActivity.ALARM_OFF, 0, 0, MainActivity.AM);
                AlarmService.alarms.add(newAlarm);
                AlarmService.alarmManagers.add((AlarmManager) getSystemService(ALARM_SERVICE));

                Log.d("newAlarm size:", Integer.toString(AlarmService.alarms.size()));
                Log.d("counter:", Integer.toString(mListView.getCount()));
 //
                adapter.notifyDataSetChanged();
                mListView.invalidateViews();
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (AlarmService.alarms.size() == 0) {
            AlarmService.alarms.add(new Alarm(0, MainActivity.ALARM_OFF, 11, 21, MainActivity.AM));
            AlarmService.alarmManagers.add((AlarmManager) getSystemService(ALARM_SERVICE));
        }

        mListView.invalidateViews();
    }
}

package com.example.ckenken.hw2;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by ckenken on 2016/1/6.
 */
public class AlarmAdapter extends BaseAdapter {

    private Context mContext;

    private LayoutInflater inflater = null;

    ArrayList<Alarm> alarms = new ArrayList<Alarm>();

    private SimpleDateFormat adf = new SimpleDateFormat("hh:mm aaa");

    public AlarmAdapter(Context context, ArrayList<Alarm> data) {
        // TODO Auto-generated constructor stub
        inflater = LayoutInflater.from(context);

        mContext = context;

        alarms = data;   // when alarms update in SetAlarmActivity, this alarm arrayList will update too.

//        for(int i = 0; i<data.size(); i++) {
//            alarms.add(data.get(i).copy());
//        }

    }

    @Override
    public int getCount() {
        return alarms.size();
    }

    @Override
    public Object getItem(int position) {
        return alarms.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = inflater.inflate(R.layout.alarm_list, null);

        CheckBox a_on = (CheckBox) v.findViewById(R.id.checkBox);
        TextView time = (TextView) v.findViewById(R.id.textView2);
        TextView repeat = (TextView) v.findViewById(R.id.textView4);

        a_on.setChecked(AlarmService.alarms.get(position).a_on);
        a_on.setOnCheckedChangeListener(new CheckOnChangeListener(position));

        Calendar c = Calendar.getInstance();
        c.set(2016, 0, 6, alarms.get(position).hour, alarms.get(position).min);
//        Log.d("alarms.time:", Integer.toString(alarms.get(position).hour) + "," + Integer.toString(alarms.get(position).min));

        time.setOnClickListener(new SetTimeOnClickListener(position));
        repeat.setOnClickListener(new SetTimeOnClickListener(position));

        Date da = c.getTime();
        time.setText(adf.format(da));
        repeat.setText(alarms.get(position).displayRepeat());

        return v;
    }

    class SetTimeOnClickListener implements View.OnClickListener {
        private int id;

        public SetTimeOnClickListener(int inputId)
        {
            id = inputId;
        }

        @Override
        public void onClick(View v) {
            Intent startIntent = new Intent(mContext, InputAlarmTimeActivity.class);
            Bundle b = new Bundle();
            b.putInt("alarm_id", id);
            startIntent.putExtras(b);
            mContext.startActivity(startIntent);
        }
    }

    class CheckOnChangeListener implements CompoundButton.OnCheckedChangeListener {
        private int id;
        public  CheckOnChangeListener(int inputId)
        {
            id = inputId;
        }

        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if (isChecked) {
                Log.d("on:", Integer.toString(id));

                Intent startIntent = new Intent(mContext, AlarmService.class);
                Bundle b = new Bundle();
                b.putInt("mission_id", AlarmService.MISSION_TURNON);
                b.putInt("alarm_id", id);
                startIntent.putExtras(b);
                mContext.startService(startIntent);
                AlarmService.alarms.get(id).a_on = MainActivity.ALARM_ON;
            }
            else {

                Log.d("off:", Integer.toString(id));

                Intent startIntent = new Intent(mContext, AlarmService.class);
                Bundle b = new Bundle();
                b.putInt("mission_id", AlarmService.MISSION_TURNOFF);
                b.putInt("alarm_id", id);
                startIntent.putExtras(b);
                mContext.startService(startIntent);
                AlarmService.alarms.get(id).a_on = MainActivity.ALARM_OFF;
            }
        }
    }

}

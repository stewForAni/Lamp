package com.lamps.lamps.activity;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;

import com.lamps.lamps.R;
import com.lamps.lamps.util.CommandUtils;
import com.lamps.lamps.util.ControlLight;

import java.util.Calendar;


/**
 * Created by stew on 16/9/6.
 * mail: stewforani@gmail.com
 */
public class TimingActivity extends BaseActivity implements View.OnClickListener {
    private TextView startAPM;
    private TextView endAPM;
    private TextView startTimeTv;
    private TextView endTimeTv;
    private Boolean switchOpen = false;
    private String[] time = {"0", "0", "0", "0"};
    private SharedPreferences.Editor editor;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timing);
        initView();
    }

    private void initView() {

        /**
         * 保存设置
         */
        SharedPreferences sharedPreferences = getSharedPreferences("timing", MODE_PRIVATE);
        editor = sharedPreferences.edit();

        ImageView back = (ImageView) findViewById(R.id.setting_back);
        TextView save = (TextView) findViewById(R.id.save);
        Switch aSwitch = (Switch) findViewById(R.id.switch_timing);
        startAPM = (TextView) findViewById(R.id.start_apm);
        endAPM = (TextView) findViewById(R.id.end_apm);
        startTimeTv = (TextView) findViewById(R.id.start_time_tv);
        endTimeTv = (TextView) findViewById(R.id.end_time_tv);
        LinearLayout startTime = (LinearLayout) findViewById(R.id.start_time);
        LinearLayout endTime = (LinearLayout) findViewById(R.id.end_time);

        if (back == null || save == null || startTime == null || endTime == null || aSwitch == null) {
            return;
        }

        back.setOnClickListener(this);
        save.setOnClickListener(this);
        startTime.setOnClickListener(this);
        endTime.setOnClickListener(this);

        if (sharedPreferences.getBoolean("checkState", false)) {
            aSwitch.setChecked(true);
            switchOpen = true;
        }

        aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                switchOpen = isChecked;
            }
        });


        setStartTimeTv(Integer.parseInt(sharedPreferences.getString("startH", "00")), Integer.parseInt(sharedPreferences.getString("startM", "00")));
        setEndTimeTv(Integer.parseInt(sharedPreferences.getString("endH", "00")), Integer.parseInt(sharedPreferences.getString("endM", "00")));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_back: {

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(getResources().getString(R.string.remind));
                builder.setMessage(getResources().getString(R.string.remind_save));
                builder.setPositiveButton(getString(R.string.sure), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });

                builder.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                builder.show();

            }
            break;

            case R.id.save: {
                sendTime();
            }
            break;

            case R.id.start_time: {
                showStartTimePickerDialog();
            }
            break;

            case R.id.end_time: {
                showEndTimePickerDialog();
            }
            break;
        }

    }

    private void sendTime() {
        if (switchOpen) {

            final AlertDialog dialog = new AlertDialog.Builder(this).create();
            dialog.setTitle(getResources().getString(R.string.remind));
            dialog.setMessage(getResources().getString(R.string.wait));
            dialog.show();

            Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int min = c.get(Calendar.MINUTE);

            String hourStr = hour + "";
            String minStr = min + "";

            if (hour < 10) {
                hourStr = "0" + hour + "";
            }

            if (min < 10) {
                minStr = "0" + min + "";
            }

            ControlLight.sendTime(CommandUtils.TIMING_CURRENT, hourStr, minStr);
            startAPM.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ControlLight.sendTime(CommandUtils.TIMING_ON_1, time[0], time[1]);

                    startAPM.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ControlLight.sendTime(CommandUtils.TIMING_ON_2, time[2], time[3]);
                            dialog.cancel();
                            finishThis();
                        }
                    }, 1000);

                }
            }, 1000);


        } else {
            ControlLight.sendOrder(CommandUtils.TIMING_OFF);

            finishThis();
        }

    }

    private void finishThis() {
        editor.putString("startH", time[0]);
        editor.putString("startM", time[1]);
        editor.putString("endH", time[2]);
        editor.putString("endM", time[3]);
        editor.putBoolean("checkState", switchOpen);
        editor.apply();
        finish();
    }

    private void showStartTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                setStartTimeTv(hourOfDay, minute);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();
    }


    private void showEndTimePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {


                setEndTimeTv(hourOfDay, minute);
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true).show();

    }

    private void setStartTimeTv(int hourOfDay, int minute) {

        if (hourOfDay >= 12) {
            startAPM.setText("PM");
        } else {
            startAPM.setText("AM");
        }

        String newH = null;
        String newM = null;
        if (hourOfDay < 10) {
            newH = "0" + hourOfDay;
        } else {
            newH = hourOfDay + "";
        }
        if (minute < 10) {
            newM = "0" + minute;
        } else {
            newM = minute + "";
        }

        String tvH = null;
        if (hourOfDay > 12) {
            tvH = (hourOfDay - 12) + "";
            if ((hourOfDay - 12) < 10) {
                tvH = "0" + (hourOfDay - 12) + "";
            }

        } else {
            tvH = (hourOfDay) + "";
            if (hourOfDay < 10) {
                tvH = "0" + (hourOfDay) + "";
            }
        }

        startTimeTv.setText(tvH + ":" + newM);
        time[0] = newH;
        time[1] = newM;
    }


    private void setEndTimeTv(int hourOfDay, int minute) {
        if (hourOfDay >= 12) {
            endAPM.setText("PM");
        } else {
            endAPM.setText("AM");
        }

        String newH = null;
        String newM = null;
        if (hourOfDay < 10) {
            newH = "0" + hourOfDay;
        } else {
            newH = hourOfDay + "";
        }
        if (minute < 10) {
            newM = "0" + minute;
        } else {
            newM = minute + "";
        }

        String tvH = null;
        if (hourOfDay > 12) {
            tvH = (hourOfDay - 12) + "";
            if ((hourOfDay - 12) < 10) {
                tvH = "0" + (hourOfDay - 12) + "";
            }

        } else {
            tvH = (hourOfDay) + "";
            if (hourOfDay < 10) {
                tvH = "0" + (hourOfDay) + "";
            }
        }

        endTimeTv.setText(tvH + ":" + newM);
        time[2] = newH;
        time[3] = newM;
    }


}

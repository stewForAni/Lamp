package com.lamps.lamps.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.lamps.lamps.R;
import com.lamps.lamps.util.CommandUtils;
import com.lamps.lamps.util.ControlLight;

/**
 * Created by stew on 16/9/4.
 * mail: stewforani@gmail.com
 */
public class SettingActivity extends BaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        initView();
    }

    private void initView() {

        ImageView back = (ImageView) findViewById(R.id.setting_back);
        TextView tv1 = (TextView) findViewById(R.id.change_ssid);
        TextView tv2 = (TextView) findViewById(R.id.change_wifi_psd);
        TextView tv3 = (TextView) findViewById(R.id.change_language);
        Button restart = (Button) findViewById(R.id.restart);
        if (back == null || tv1 == null || tv2 == null || tv3 == null||restart==null) {
            return;
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv1.setOnClickListener(this);
        tv2.setOnClickListener(this);
        tv3.setOnClickListener(this);
        restart.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.change_ssid: {
                Intent intent = new Intent(SettingActivity.this, ChangeSSIDActivity.class);
                startActivity(intent);
            }
            break;


            case R.id.change_wifi_psd: {
                Intent intent = new Intent(SettingActivity.this, ChangeWifiPsdActivity.class);
                startActivity(intent);
            }
            break;


            case R.id.change_language: {
                Intent intent = new Intent(SettingActivity.this, ChangeLanguageActivity.class);
                startActivity(intent);
            }
            break;

            case R.id.restart: {
                ControlLight.sendOrder(CommandUtils.RESTART);
            }
            break;

        }
    }

}

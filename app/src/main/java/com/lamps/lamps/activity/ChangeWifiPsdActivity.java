package com.lamps.lamps.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.lamps.lamps.R;
import com.lamps.lamps.util.ControlLight;

/**
 * Created by stew on 16/9/4.
 * mail: stewforani@gmail.com
 */
public class ChangeWifiPsdActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_wifi_psd);
        initView();
    }

    private void initView() {

        ImageView back = (ImageView) findViewById(R.id.setting_back);
        final EditText ssid = (EditText) findViewById(R.id.edit_psd);
        Button btn = (Button) findViewById(R.id.btn_reset);

        if (back == null || ssid == null || btn == null) {
            return;
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ssid.getText().toString().isEmpty()) {
                    Toast.makeText(ChangeWifiPsdActivity.this, R.string.psd_null, Toast.LENGTH_SHORT).show();
                    return;
                }

                if (ssid.getText().toString().length() > 12 || ssid.getText().toString().length() < 8) {
                    Toast.makeText(ChangeWifiPsdActivity.this, R.string.psd_8_12, Toast.LENGTH_SHORT).show();
                    return;
                }

                ControlLight.sendPassword(ssid.getText().toString());
                Toast.makeText(ChangeWifiPsdActivity.this, R.string.psd_change_success, Toast.LENGTH_SHORT).show();
                finish();

            }
        });

    }
}

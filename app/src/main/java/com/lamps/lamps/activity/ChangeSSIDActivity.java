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
public class ChangeSSIDActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chnage_ssid);
        initView();
    }


    private void initView() {

        ImageView back = (ImageView) findViewById(R.id.setting_back);
        final EditText ssid = (EditText) findViewById(R.id.edit_ssid);
        Button btn = (Button) findViewById(R.id.button_send);

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
                    Toast.makeText(ChangeSSIDActivity.this, R.string.ssid_can_not_be_null, Toast.LENGTH_SHORT).show();
                    return;
                }

                ControlLight.sendSSID(ssid.getText().toString());
                finish();

            }
        });


    }

}

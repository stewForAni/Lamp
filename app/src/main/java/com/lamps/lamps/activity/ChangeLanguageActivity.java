package com.lamps.lamps.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lamps.lamps.R;

import java.util.Locale;

/**
 * Created by stew on 16/9/4.
 * mail: stewforani@gmail.com
 */
public class ChangeLanguageActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_language);
        initView();
    }

    private void initView() {
        /**
         * 保存设置
         */
        SharedPreferences sharedPreferences = getSharedPreferences("set", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        /**
         * 获取系统配置
         */
        final Resources resources = getResources();
        final DisplayMetrics dm = resources.getDisplayMetrics();
        final Configuration config = resources.getConfiguration();

        ImageView back = (ImageView) findViewById(R.id.setting_back);
        TextView CN = (TextView) findViewById(R.id.language_cn);
        TextView EN = (TextView) findViewById(R.id.language_en);

        if (back == null || CN == null || EN == null) {
            return;
        }

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        CN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                config.locale = Locale.SIMPLIFIED_CHINESE;
                resources.updateConfiguration(config, dm);

                editor.putString("language","0");
                editor.apply();

                Intent intent = new Intent(ChangeLanguageActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


            }
        });


        EN.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                config.locale = Locale.ENGLISH;
                resources.updateConfiguration(config, dm);

                editor.putString("language","1");
                editor.apply();

                Intent intent = new Intent(ChangeLanguageActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });



    }

}

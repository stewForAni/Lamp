package com.lamps.lamps.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.lamps.lamps.R;
import com.lamps.lamps.util.ControlLight;

import java.util.List;
import java.util.Locale;

/**
 * Created by stew on 16/8/30.
 * mail: stewforani@gmail.com
 */
public class WifiConnectActivity extends BaseActivity {
    public static final String TAG = "WifiConnectActivity";
    public static final String LANGUAGE_CN = "0";
    public static final String LANGUAGE_EN = "1";
    private static final int PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION = 0;
    private ImageView wifiConnectIcon;
    private LinearLayout wifiStateLL;
    private TextView wifiName;
    private Button enter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate: ");

        setContentView(R.layout.activity_wifi_state);
        checkLanguage();
        initView();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION);
            //After this point you wait for callback in onRequestPermissionsResult(int, String[], int[]) overriden method

        } else {
            initWifi();
            //do something, permission was previously granted; or legacy device
        }

    }

    private void initWifi() {
        WifiManager wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (!wifiManager.isWifiEnabled()) {
            Toast.makeText(this, "wifi未打开！", Toast.LENGTH_SHORT).show();
            return;
        }
        List<ScanResult> list = wifiManager.getScanResults();
        ListView listView = (ListView) findViewById(R.id.wifi_list);

        if (listView == null) {
            return;
        }

        if (list.size() == 0) {
            Toast.makeText(this, "未搜索到wifi信号！", Toast.LENGTH_SHORT).show();

        } else {
            listView.setAdapter(new MyAdapter(this, list));
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
                startActivity(intent);

            }
        });

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_CODE_ACCESS_COARSE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // Do something with granted permission
            initWifi();
        }
    }

    private void checkLanguage() {

        SharedPreferences sharedPreferences = this.getSharedPreferences("set", MODE_PRIVATE);
        /**
         * 获取系统配置
         */
        Resources resources = getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        Configuration config = resources.getConfiguration();


        if (LANGUAGE_CN.equals(sharedPreferences.getString("language", "0"))) {

            config.locale = Locale.SIMPLIFIED_CHINESE;
            resources.updateConfiguration(config, dm);
        } else if (LANGUAGE_EN.equals(sharedPreferences.getString("language", "0"))) {

            config.locale = Locale.ENGLISH;
            resources.updateConfiguration(config, dm);
        } else {

            config.locale = Locale.SIMPLIFIED_CHINESE;
            resources.updateConfiguration(config, dm);
        }

    }

    private void initView() {
        wifiConnectIcon = (ImageView) findViewById(R.id.wifi_connect_icon);
        wifiStateLL = (LinearLayout) findViewById(R.id.wifi_state_ll);
        wifiName = (TextView) findViewById(R.id.wifi_name);
        enter = (Button) findViewById(R.id.button_enter);

        if (enter == null) {
            return;
        }

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WifiConnectActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.d(TAG, "onResume: ");
        /**
         * 在onPause()中判断wifi连接状态
         *暂时不做wifi状态监听（wifi确认链接后返回页面，或者重启app，才会生效）
         */

        ControlLight.setOnConnectStateListener(new ControlLight.ConnectStateListener() {
            @Override
            public void connect() {
                Log.d(TAG, "connect: ");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        connectY();
                    }
                });

            }

            @Override
            public void connectFailed() {
                Log.d(TAG, "connectFailed: ");

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        connectN();
                    }
                });

            }
        });


        ControlLight.connectLight();
    }


    public void connectY() {
        /**
         * 连接成功
         */

        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        wifiConnectIcon.setImageResource(R.drawable.wifi_connect_y);
        wifiStateLL.setVisibility(View.INVISIBLE);
        wifiName.setVisibility(View.VISIBLE);
        enter.setVisibility(View.VISIBLE);
        wifiName.setText(wifiInfo.getSSID());
    }

    public void connectN() {
        /**
         *连接失败
         */

        wifiConnectIcon.setImageResource(R.drawable.wifi_connect_n);
        wifiStateLL.setVisibility(View.VISIBLE);
        wifiName.setVisibility(View.INVISIBLE);
        enter.setVisibility(View.VISIBLE);
    }

    private class MyAdapter extends BaseAdapter {

        LayoutInflater inflater;
        List<ScanResult> list;

        public MyAdapter(Context context, List<ScanResult> list) {
            this.inflater = LayoutInflater.from(context);
            this.list = list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;

            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.wifi_list, null);
                holder.wifiName = (TextView) convertView.findViewById(R.id.wifi_name);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            ScanResult scanResult = list.get(position);
            holder.wifiName.setText(scanResult.SSID);
            return convertView;
        }


        public final class ViewHolder {
            public TextView wifiName;
        }

    }


    @Override
    protected void onDestroy() {
        Log.d(TAG, "onDestroy: ");
        ControlLight.setOnConnectStateListener(null);
        super.onDestroy();
    }
}

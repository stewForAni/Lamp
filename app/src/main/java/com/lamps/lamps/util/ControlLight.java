package com.lamps.lamps.util;

import android.util.Log;

/**
 * Created by stew on 16/9/4.
 * mail: stewforani@gmail.com
 */

public class ControlLight {
    private static final String IP = "192.168.1.1";
    private static final int PORT = 5000;
    private static TcpClient tcpClient = null;
    private static ConnectStateListener listener;

    public static TcpClient newInstance() {
        if (null == tcpClient) {
            tcpClient = new TcpClient() {

                @Override
                public void onConnect(SocketTransceiver transceiver) {
                    listener.connect();
                }

                @Override
                public void onConnectFailed() {
                    listener.connectFailed();
                }

                @Override
                public void onReceive(SocketTransceiver transceiver, String s) {

                }

                @Override
                public void onDisconnect(SocketTransceiver transceiver) {

                }
            };
        }

        return tcpClient;
    }


    public static void connectLight() {
        try {
            ControlLight.newInstance().connect(IP, PORT);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
    }


    public interface ConnectStateListener {
        void connect();

        void connectFailed();
    }

    public static void setOnConnectStateListener(ConnectStateListener listener) {
        ControlLight.listener = listener;
    }

    /**
     * 发送指令（修改灯的效果）
     */

    public static void sendOrder(String order) {

        Log.d("MainActivity", order + "");

        try {
            byte b = (byte) Integer.parseInt(("0x" + order).substring(2), 16);
            ControlLight.newInstance().getTransceiver().send(b);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送指令（修改SSID）
     */
    public static void sendSSID(String ssid) {
        try {

            byte b = (byte) Integer.parseInt(("0xE1").substring(2), 16);
            int count = ssid.getBytes().length;
            byte[] c = ssid.getBytes();
            byte[] a = new byte[count + 1];
            a[0] = b;
            for (int i = 0; i < count; i++) {
                a[i + 1] = c[i];
            }

            ControlLight.newInstance().getTransceiver().send(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 发送指令（修改密码）
     */
    public static void sendPassword(String ssid) {
        try {

            byte b = (byte) Integer.parseInt(("0xE2").substring(2), 16);
            int count = ssid.getBytes().length;
            byte[] c = ssid.getBytes();
            byte[] a = new byte[count + 1];
            a[0] = b;
            for (int i = 0; i < count; i++) {
                a[i + 1] = c[i];
            }

            ControlLight.newInstance().getTransceiver().send(a);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 发送指令（定时）
     */
    public static void sendTime(String header, String time1, String time2) {
        try {
            byte a = (byte) Integer.parseInt(header, 16);
            byte b = (byte) Integer.parseInt(time1, 16);
            byte c = (byte) Integer.parseInt(time2, 16);
            byte[] d = {a, b, c};
            ControlLight.newInstance().getTransceiver().send(d);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

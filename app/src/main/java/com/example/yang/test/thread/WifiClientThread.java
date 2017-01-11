package com.example.yang.test.thread;

import android.content.Context;
import android.net.DhcpInfo;
import android.os.Environment;

import com.example.yang.test.util.LogUtils;
import com.example.yang.test.util.WifiManageUtils;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * Created by Administrator on 2017/1/10.
 */

public class WifiClientThread extends Thread {
    public Socket socket;
    public Context context;
    public static final int SERVERPORT = 8191;

    public OutputStream os;
    public InputStream in;

    public WifiClientThread(Context context) {
        this.context = context;
    }

    public void run() {
        try {
            DhcpInfo dhcp = new WifiManageUtils(context).getDhcpInfo();
            int ipInt = dhcp.gateway;
            String serverip = String.valueOf(new StringBuilder()
                    .append((ipInt & 0xff)).append('.').append((ipInt >> 8) & 0xff)
                    .append('.').append((ipInt >> 16) & 0xff).append('.')
                    .append(((ipInt >> 24) & 0xff)).toString()

            );
            LogUtils.d("mtest","serverip--"+serverip);
            socket = new Socket(serverip, SERVERPORT);

            new Thread(new Runnable() {

                @Override
                public void run() {
                    if (socket == null) {
                        return;
                    }

                    try {
                        String path = Environment.getExternalStorageDirectory()
                                .getAbsolutePath() + "/CHFS/000000000000";
                        if (android.os.Build.MODEL.contains("8812")) {
                            path += "/camera/" + "camera_temp_name.jpg";
                        } else {
                            path += "/ARChon-v1.1-x86_64.zip";
                        }
                        DataInputStream read = new DataInputStream(
                                new FileInputStream(new File(path)));
                        String fileName = path.substring(path.lastIndexOf("/") + 1);// 获得文件名加类型

                        os = socket.getOutputStream();
                        in = socket.getInputStream();
                        os.write((fileName + ";" + read.available())
                                .getBytes("utf-8"));// 将文件名和文件大小传给接收端
                        os.flush();
                        byte[] data = new byte[1024];

                        int len = in.read(data);

                        String start = new String(data, 0, len);

                        int sendCountLen = 0;

                        if (start.equals("start")) {

                            while ((len = read.read(data)) != -1) {
                                os.write(data, 0, len);
                                sendCountLen += len;
                            }
                            os.flush();
                            os.close();
                            read.close();
                        }

                    } catch (Exception e) {

                        e.printStackTrace();
                    } finally {

                        try {
                            socket.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }

                }
            }).start();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
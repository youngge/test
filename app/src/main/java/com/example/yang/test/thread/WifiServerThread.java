package com.example.yang.test.thread;

import android.content.Context;
import android.os.Environment;
import android.os.Handler;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Administrator on 2017/1/10.
 */

public class WifiServerThread extends Thread {
    public ServerSocket mserverSocket;
    public Socket socket;
    public Context context;
    public static final int SERVERPORT = 8191;
    public Boolean isrun = true;
    public Handler handler;

    public WifiServerThread(Context context, Handler handler) {
        this.context = context;
        this.handler = handler;
    }

    public void run() {
        try {
            mserverSocket = new ServerSocket(SERVERPORT);
            while (isrun) {
                socket = mserverSocket.accept();
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        byte[] buffer = new byte[1024];
                        int bytes;
                        InputStream mmInStream = null;

                        try {
                            mmInStream = socket.getInputStream();
                        } catch (IOException e1) {
                            e1.printStackTrace();
                        }
                        System.out.println("server");

                        try {
                            InputStream in = socket.getInputStream();
                            OutputStream os = socket.getOutputStream();

                            byte[] data = new byte[1024];
                            while (in.available() <= 0)
                                ;// 同步

                            int len = in.read(data);

                            String[] str = new String(data, 0, len, "utf-8")
                                    .split(";");

                            String path = Environment
                                    .getExternalStorageDirectory()
                                    .getAbsolutePath()
                                    + "/CHFS/000000000000" + "/";
                            if (len != -1) {
                                path += "socket_" + str[0];// str[0]是文件名加类型
                            }
                            handler.obtainMessage(10, (Object) str[0])
                                    .sendToTarget();

                            System.out.println(path);

                            os.write("start".getBytes());

                            os.flush();

                            File file = new File(path);

                            DataOutputStream out = new DataOutputStream(
                                    new FileOutputStream(file));

                            System.out.println("开始接收.....");
                            int countSize = 0;

                            while ((len = in.read(data)) != -1) {
                                out.write(data, 0, len);
                                countSize += len;
                            }

                            os.close();
                            out.flush();
                            out.close();
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        } finally {
                            try {
                                socket.close();
                                System.out.println("关闭....");
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            handler.obtainMessage(10, (Object) "接受 完成")
                                    .sendToTarget();
                        }

                    }
                }).start();
            }
            if (mserverSocket != null) {
                try {
                    mserverSocket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
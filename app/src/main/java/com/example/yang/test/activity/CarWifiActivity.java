package com.example.yang.test.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.test.R;
import com.example.yang.test.application.BaseActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class CarWifiActivity extends BaseActivity {
    private Button startButton;
    private EditText IPText;
    private Button ForWard;
    private Button BackWard;
    private Button Stop;
    private Button TurnLeft;
    private Button TurnRight;
    private TextView recvText;
    private Context mContext;
    private boolean isConnecting = false;
    private Thread mThreadClient = null;
    private Socket mSocketClient = null;
    static BufferedReader mBufferedReaderServer = null;
    static PrintWriter mPrintWriterServer = null;
    static BufferedReader mBufferedReaderClient = null;
    static PrintWriter mPrintWriterClient = null;
    private String recvMessageClient = "";
    private String recvMessageServer = "";

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                recvText.append("Server: " + recvMessageServer); //
            } else if (msg.what == 1) {
                recvText.append("Client: " + recvMessageClient); //
            }
        }
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cardwifi);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        mContext = this;
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .detectDiskReads().detectDiskWrites().detectNetwork().penaltyLog().build());
        StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectLeakedSqlLiteObjects()

                .penaltyLog().penaltyDeath().build());
        IPText = (EditText) findViewById(R.id.editIP);
        IPText.setText("192.168.1.110");
        startButton = (Button) findViewById(R.id.StartConnect);
        startButton.setOnClickListener(StartClickListener);
        ForWard = (Button) findViewById(R.id.ForWard);
        ForWard.setOnClickListener(ForWardClickListener);
        Stop = (Button) findViewById(R.id.Stop);
        Stop.setOnClickListener(StopClickListener);
        TurnLeft = (Button) findViewById(R.id.TurnLeft);
        TurnLeft.setOnClickListener(TurnLeftClickListener);
        TurnRight = (Button) findViewById(R.id.TurnRight);
        TurnRight.setOnClickListener(TurnRightClickListener);
        BackWard = (Button) findViewById(R.id.BackWard);
        BackWard.setOnClickListener(BackWardClickListener);
        recvText = (TextView) findViewById(R.id.RecvText);
        recvText.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    private OnClickListener StartClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            if (isConnecting) {
                isConnecting = false;
                try {
                    if (mSocketClient != null) {

                        mSocketClient.close();
                        mSocketClient = null;
                        mPrintWriterClient.close();
                        mPrintWriterClient = null;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                mThreadClient.interrupt();
                startButton.setText("开始连接");
                IPText.setEnabled(true);
                recvText.setText("ok:\n");
            } else {
                isConnecting = true;
                startButton.setText("停止连接");
                IPText.setEnabled(false);
                mThreadClient = new Thread(mRunnable);
                mThreadClient.start();
            }
        }
    };
    private OnClickListener ForWardClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) { // TODO Auto-generated method stub
            if (isConnecting && mSocketClient != null) {
                String PWM1 = "PWM 1 100 50";
                String PWM2 = "PWM 2 100 50";
                String OUTPUT = "OUTPUT 0";
                try {
                    mPrintWriterClient.print(PWM1);//发送 PWM1 信息 给路由
                    mPrintWriterClient.flush();

                    mPrintWriterClient.print(PWM2);//发送 PWM2 信息 给路由
                    mPrintWriterClient.flush();
                    Thread.sleep(600);
                    mPrintWriterClient.print(OUTPUT);//发送方向给路 由
                    mPrintWriterClient.flush();
                } catch (Exception e) { // TODO: handle exception
                    Toast.makeText(mContext, "发送异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, "没有连接", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private OnClickListener StopClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            if (isConnecting && mSocketClient != null) {
                String PWM1 = "PWM 1 100 0";
                String PWM2 = "PWM 2 100 0";
                String OUTPUT = "OUTPUT 0";
                try {
                    mPrintWriterClient.print(PWM1);//发送 PWM1 信息 给路由
                    mPrintWriterClient.flush();
                    mPrintWriterClient.print(PWM2);//发送 PWM2 信息 给路由
                    mPrintWriterClient.flush();
                    Thread.sleep(600);
                    mPrintWriterClient.print(OUTPUT);//发送方向给路 由
                    mPrintWriterClient.flush();

                } catch (Exception e) {
                    Toast.makeText(mContext, "发送异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, "没有连接", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private OnClickListener TurnLeftClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            if (isConnecting && mSocketClient != null) {
                String PWM1 = "PWM 1 100 0";
                String PWM2 = "PWM 2 100 50";
                String OUTPUT = "OUTPUT 0";
                try {
                    mPrintWriterClient.print(PWM1);//发送 PWM1 信息 给路由
                    mPrintWriterClient.flush();
                    mPrintWriterClient.print(PWM2);//发送 PWM2 信息 给路由
                    mPrintWriterClient.flush();
                    Thread.sleep(600);
                    mPrintWriterClient.print(OUTPUT);//发送方向给路 由
                    mPrintWriterClient.flush();
                } catch (Exception e) {
                    Toast.makeText(mContext, "发送异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }

            } else {
                Toast.makeText(mContext, "没有连接", Toast.LENGTH_SHORT).show();
            }
        }
    };
    private OnClickListener TurnRightClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            if (isConnecting && mSocketClient != null) {
                String PWM1 = "PWM 1 100 50";
                String PWM2 = "PWM 2 100 0";
                String OUTPUT = "OUTPUT 0";
                try {
                    mPrintWriterClient.print(PWM1);//发送 PWM1 信息
                    mPrintWriterClient.flush();
                    mPrintWriterClient.print(PWM2);//发送 PWM2 信息
                    mPrintWriterClient.flush();
                    Thread.sleep(600);
                    mPrintWriterClient.print(OUTPUT);//发送方向给路 由
                    mPrintWriterClient.flush();
                } catch (Exception e) {
                    Toast.makeText(mContext, "发送异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, "没有连接",

                        Toast.LENGTH_SHORT).show();
            }
        }
    };

    private OnClickListener BackWardClickListener = new OnClickListener() {
        @Override
        public void onClick(View arg0) {
            if (isConnecting && mSocketClient != null) {
                String PWM1 = "PWM 1 100 50";
                String PWM2 = "PWM 2 100 50";
                String OUTPUT = "OUTPUT 15";
                try {
                    mPrintWriterClient.print(PWM1);//发送 PWM1 信息
                    mPrintWriterClient.flush();
                    mPrintWriterClient.print(PWM2);//发送 PWM2 信息
                    mPrintWriterClient.flush();
                    Thread.sleep(600);
                    mPrintWriterClient.print(OUTPUT);//发送方向给路 由
                    mPrintWriterClient.flush();
                } catch (Exception e) {
                    Toast.makeText(mContext, "发送异常：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, "没有连接", Toast.LENGTH_SHORT).show();
            }
        }
    };

    //线程:监听路由发来的消息
    private Runnable mRunnable = new Runnable() {
        public void run() {
            String msgText = IPText.getText().toString();
            if (msgText.length() <= 0) {
                Toast.makeText(mContext, "IP 不能为空！", Toast.LENGTH_SHORT).show();
                recvMessageClient = "IP 不能为空！\n";//消息换行
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
                return;
            }
            int start = msgText.indexOf(":");
            if ((start == -1) || (start + 1 >= msgText.length())) {
                recvMessageClient = "IP 地址不合法\n";//消息换行
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
                return;
            }
            String sIP = msgText.substring(0, start);
            String sPort = msgText.substring(start + 1);
            int port = Integer.parseInt(sPort);
            Log.d("gjz", "IP:" + sIP + ":" + port);
            try {
                //连接路由
                mSocketClient = new Socket(sIP, port); //portnum //取得输入、输出流
                mBufferedReaderClient = new BufferedReader(new InputStreamReader(mSocketClient.getInputStream()));
                mPrintWriterClient = new PrintWriter(mSocketClient.getOutputStream(), true);
                recvMessageClient = "已经连接 server!\n";//消息换行
                Message msg = new Message();

                msg.what = 1;
                mHandler.sendMessage(msg);
            } catch (Exception e) {
                recvMessageClient = "连接 IP 异常:" + e.toString() + e.getMessage() + "\n";//消息换行
                Message msg = new Message();
                msg.what = 1;
                mHandler.sendMessage(msg);
                return;
            }
            char[] buffer = new char[256];
            int count = 0;
            while (isConnecting) {
                try {
                    if ((recvMessageClient = mBufferedReaderClient.readLine()) != null)
                        if ((count = mBufferedReaderClient.read(buffer)) > 0) {
                            recvMessageClient = getInfoBuff(buffer, count) + "\n";//消息换行
                            Message msg = new Message();
                            msg.what = 1;
                            mHandler.sendMessage(msg);
                        }
                } catch (Exception e) {
                    recvMessageClient = "接收异常:" + e.getMessage() + "\n";//消息换行
                    Message msg = new Message();
                    msg.what = 1;
                    mHandler.sendMessage(msg);
                }
            }
        }
    };

    private String getInfoBuff(char[] buff, int count) {
        char[] temp = new char[count];
        for (int i = 0; i < count; i++) {
            temp[i] = buff[i];
        }
        return new String(temp);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (isConnecting) {
            isConnecting = false;
            try {
                if (mSocketClient != null) {
                    mSocketClient.close();
                    mSocketClient = null;
                    mPrintWriterClient.close();
                    mPrintWriterClient = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            mThreadClient.interrupt();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
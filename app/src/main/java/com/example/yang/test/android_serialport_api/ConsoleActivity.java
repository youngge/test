package com.example.yang.test.android_serialport_api;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yang.test.R;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class ConsoleActivity extends SerialPortActivity {
    Button btnSend;
    EditText mReception;
    EditText mEmission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_console);
        //    setTitle("Loopback test");
        mReception = (EditText) findViewById(R.id.EditTextReception);
        mEmission = (EditText) findViewById(R.id.EditTextEmission);
        btnSend = (Button) findViewById(R.id.btnSend);
        btnSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mEmission.getText().toString();
                try {
                    mOutputStream.write(new String(text).getBytes());
                    mOutputStream.write('\n');
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        //发送指令到斑马打印机
        mEmission.setText("^XA^A0N,40,30^FO50,150^FDHELLO WORLD^FS^XZ");

   /*二维码指令 　　　　　　
   ^XA 　　　　　　
   ^PMY 　　　　　　
   ^FO200,200^BQ,2,10 　　　　　　
   ^FDD03040C,LA,012345678912AABBqrcode^FS 　　　　　　
   ^XZ 　　　　
   */
    }

    @Override
    protected void onDataReceived(final byte[] buffer, final int size) {
        runOnUiThread(new Runnable() {
            public void run() {
                if (mReception != null) {
                    mReception.append(new String(buffer, 0, size));
                }
                Toast.makeText(mApplication, "收到消息：" +
                        new String(buffer) + "  size = " + size, Toast.LENGTH_SHORT).show();
            }
        });
    }

//    最后别忘了一个操作权限的问题，很多设备直接操作串口，会提示无权限 read/write 的问题，
//     需要java层去提权，方法如下：使用下面的方法执行指令： chmod 777 /dev/ttyS2

    public void exeShell(String cmd) {
        try {
            Process p = Runtime.getRuntime().exec(cmd);
            BufferedReader in = new BufferedReader(
                    new InputStreamReader(
                            p.getInputStream()));
            String line = null;
            while ((line = in.readLine()) != null) {
                Log.i("exeShell", line);
            }
        } catch (Throwable t) {
            t.printStackTrace();
        }

    }

    //手动解决方法：打开cmd，进入  adb shell，执行：chmod 777 /dev/ttyS2
}
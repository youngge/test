package com.example.yang.test.activity;

import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.yang.test.R;
import com.example.yang.test.util.DateUtils;
import com.example.yang.test.view.ClockView;
import com.example.yang.test.view.CustomTopBar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

public class CustomViewActivity extends AppCompatActivity {

    @ViewInject(R.id.topbar)
    private CustomTopBar topbar;
    @ViewInject(R.id.clockview)
    private ClockView clockview;

    private Timer timer = new Timer();
    private Handler handler = new Handler(){

        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                    Calendar instance = Calendar.getInstance();
                    clockview.updateTime(instance.get(Calendar.HOUR_OF_DAY),
                            instance.get(Calendar.MINUTE)%60,instance.get(Calendar.SECOND)%60);
                    break;
            }
            super.handleMessage(msg);
        }

    };

    private TimerTask task = new TimerTask(){

        public void run() {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
        ViewUtils.inject(this);

        topbar.setOnTopbarClickListener(new CustomTopBar.OnTopbarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                Snackbar.make(topbar,"more",Snackbar.LENGTH_SHORT).show();
            }
        });

        //每隔一秒刷新时钟
        timer.schedule(task, 0,1000);
    }
}

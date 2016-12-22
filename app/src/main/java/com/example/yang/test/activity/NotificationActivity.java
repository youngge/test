package com.example.yang.test.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.NotificationCompat;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.example.yang.test.R;
import com.example.yang.test.application.BaseActivity;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class NotificationActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.button_01)
    private Button button_01;
    @ViewInject(R.id.button_02)
    private Button button_02;
    @ViewInject(R.id.button_03)
    private Button button_03;
    @ViewInject(R.id.button_04)
    private Button button_04;

    private NotificationManager manager;
    private PendingIntent pIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ViewUtils.inject(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        button_01.setOnClickListener(this);
        button_02.setOnClickListener(this);
        button_03.setOnClickListener(this);
        button_04.setOnClickListener(this);

        manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        Intent intent = new Intent(this, MainActivity.class);
        pIntent = PendingIntent.getActivity(this, 0, intent, 0);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_01:
                Notification notification_01 = new NotificationCompat.Builder(this).setContentTitle("This is content title")
                        .setContentText("This is content text! This is content text! This is content text!")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setContentIntent(pIntent)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .build();
                manager.notify(1, notification_01);
                break;

            case R.id.button_02:
                Notification notification_02 = new NotificationCompat.Builder(this).setContentTitle("This is content title")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true)
                        .setStyle(new NotificationCompat.BigTextStyle().bigText("This is content text! This is content text! This is content text!"))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setContentIntent(pIntent)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .build();
                manager.notify(2, notification_02);
                break;

            case R.id.button_03:
                Notification notification_03 = new NotificationCompat.Builder(this).setContentTitle("This is content title")
                        .setContentText("This is content text! This is content text! This is content text!")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true)
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher)))
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setContentIntent(pIntent)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .build();
                manager.notify(3, notification_03);
                break;

            case R.id.button_04:
                Notification notification_04 = new NotificationCompat.Builder(this).setContentTitle("This is content title")
                        .setContentText("This is content text! This is content text! This is content text!")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_MAX)
                        .setDefaults(NotificationCompat.DEFAULT_ALL)
                        .setContentIntent(pIntent)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher))
                        .build();
                manager.notify(4, notification_04);
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

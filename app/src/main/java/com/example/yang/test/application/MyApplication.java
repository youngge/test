package com.example.yang.test.application;

import android.app.Application;

import com.example.yang.test.util.LogUtils;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;

/**
 * Created by Administrator on 2016/12/9.
 */

public class MyApplication extends Application {

    private static MyApplication instance;

    @Override
    public void onCreate() {
        LogUtils.d("mtest",SpeechConstant.APPID +"=586dda6a");
        SpeechUtility.createUtility(MyApplication.this, "appid=" +"586dda6a");
        super.onCreate();
    }

    public static MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }
        return instance;
    }
}

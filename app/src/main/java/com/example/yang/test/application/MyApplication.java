package com.example.yang.test.application;

import android.app.Application;

/**
 * Created by Administrator on 2016/12/9.
 */

public class MyApplication extends Application {

    private static MyApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    public static MyApplication getInstance() {
        if (null == instance) {
            instance = new MyApplication();
        }
        return instance;
    }
}

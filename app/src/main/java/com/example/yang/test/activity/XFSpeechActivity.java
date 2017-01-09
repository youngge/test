package com.example.yang.test.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yang.test.R;
import com.example.yang.test.application.BaseActivity;
import com.example.yang.test.util.LogUtils;
import com.example.yang.test.util.XunfeiUtil;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.sunflower.FlowerCollector;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class XFSpeechActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.btn_start)
    private Button btn_start;
    @ViewInject(R.id.et_content)
    private EditText et_content;

    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private SpeechRecognizer sRecognizer;
    private XunfeiUtil mXunfeiUtil;

    private static final String TAG = "XFSpeechActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xfspeech);
        ViewUtils.inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        btn_start.setOnClickListener(this);

        initIflytek();
    }


    private void initIflytek() {
        mXunfeiUtil = new XunfeiUtil(XFSpeechActivity.this, mEngineType);//这就是我的语音转换初始化和事件的封装类
        sRecognizer = mXunfeiUtil.initIflytek();//语音识别器,OnDestroy(),和OnResume()事件中需要用到
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                mXunfeiUtil.doIflytek(et_content, mEngineType);//这里来触发语音识别事件
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时释放连接
        sRecognizer.cancel();
        sRecognizer.destroy();
    }

    @Override
    protected void onResume() {
        // 开放统计 移动数据统计分析
        FlowerCollector.onResume(XFSpeechActivity.this);
        FlowerCollector.onPageStart(TAG);
        super.onResume();
    }

    @Override
    protected void onPause() {
        // 开放统计 移动数据统计分析
        FlowerCollector.onPageEnd(TAG);
        FlowerCollector.onPause(XFSpeechActivity.this);
        super.onPause();
    }
}

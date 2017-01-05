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

public class XunfeiActivity02 extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.btn_start)
    private Button btn_start;
    @ViewInject(R.id.tv_content)
    private TextView tv_content;
    @ViewInject(R.id.et_content)
    private EditText et_content;

    private StringBuilder mStringBuilder = new StringBuilder();

    private String mEngineType = SpeechConstant.TYPE_CLOUD;
    private SpeechRecognizer sRecognizer;
    private String mAbnormalAudio; // 录音
    private int m_total_count = 200; // 默认最长输入200个字符
    private XunfeiUtil mXunfeiUtil;

    private static final String TAG = "XunfeiActivity02";

    //听写监听器
    private RecognizerListener mRecoListener = new RecognizerListener() {
        @Override
        public void onVolumeChanged(int i, byte[] bytes) {

        }

        @Override
        public void onBeginOfSpeech() {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onResult(com.iflytek.cloud.RecognizerResult recognizerResult, boolean b) {
            LogUtils.d("mtest", recognizerResult.getResultString());
            mStringBuilder.append("go");
            mStringBuilder.append(recognizerResult.getResultString());
            tv_content.setText(mStringBuilder.toString());
        }

        @Override
        public void onError(SpeechError speechError) {

        }

        @Override
        public void onEvent(int i, int i1, int i2, Bundle bundle) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xunfei02);
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
        mXunfeiUtil = new XunfeiUtil(XunfeiActivity02.this, mEngineType);//这就是我的语音转换初始化和事件的封装类
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
        FlowerCollector.onResume(XunfeiActivity02.this);
        FlowerCollector.onPageStart(TAG);
        super.onResume();
    }

    @Override
    protected void onPause() {
        // 开放统计 移动数据统计分析
        FlowerCollector.onPageEnd(TAG);
        FlowerCollector.onPause(XunfeiActivity02.this);
        super.onPause();
    }
}

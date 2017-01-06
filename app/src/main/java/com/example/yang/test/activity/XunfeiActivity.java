package com.example.yang.test.activity;

import android.app.Service;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.test.R;
import com.example.yang.test.application.BaseActivity;
import com.example.yang.test.util.LogUtils;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

public class XunfeiActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.btn_start)
    private Button btn_start;
    @ViewInject(R.id.tv_content)
    private TextView tv_content;

    private StringBuilder mStringBuilder = new StringBuilder();


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
        setContentView(R.layout.activity_xunfei);
        ViewUtils.inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        btn_start.setOnClickListener(this);

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

    private void transform() {
        InitListener mInitListener = new InitListener() {
            @Override
            public void onInit(int i) {
                LogUtils.d("mtest", i+"---i");
            }
        };
        //1.创建SpeechRecognizer对象，第二个参数：本地听写时传InitListener
        RecognizerDialog    iatDialog = new RecognizerDialog(this,mInitListener);
        //2.设置听写参数，同上节
        //3.设置回调接口
        iatDialog.setListener(new RecognizerDialogListener() {
            @Override
            public void onResult(com.iflytek.cloud.RecognizerResult recognizerResult, boolean b) {
                LogUtils.d("mtest", recognizerResult.getResultString());
                mStringBuilder.append("go");
                mStringBuilder.append(recognizerResult.getResultString());
                if (b) {
                    //话已经说完了
                    String finalResult = mStringBuilder.toString();
                    tv_content.setText(finalResult);

                }
            }

            @Override
            public void onError(SpeechError speechError) {
                LogUtils.d("mtest", speechError.toString());
            }
        });
        //4.开始听写
        iatDialog.show();
//        //1.创建SpeechRecognizer对象，第二个参数：本地听写时传InitListener
//        SpeechRecognizer mIat = SpeechRecognizer.createRecognizer(this, null);
//        //2.设置听写参数，详见《科大讯飞MSC API手册(Android)》SpeechConstant类
//        mIat.setParameter(SpeechConstant.DOMAIN, "iat");
//        mIat.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
//        mIat.setParameter(SpeechConstant.ACCENT, "mandarin ");
//        //3.开始听写
//        mIat.startListening(mRecoListener);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
//                btnVoice();
                transform();
                break;
        }
    }

    //TODO 开始说话：
    private void btnVoice() {
        RecognizerDialog dialog = new RecognizerDialog(this,null);
        dialog.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
        dialog.setParameter(SpeechConstant.ACCENT, "mandarin");

        dialog.setListener(new RecognizerDialogListener() {
                               @Override
                               public void onResult(com.iflytek.cloud.RecognizerResult recognizerResult, boolean b) {
                                   printResult(recognizerResult);
                               }

                               @Override
                               public void onError(SpeechError speechError) {

                               }
                           });
        dialog.show();
        Toast.makeText(this, "请开始说话", Toast.LENGTH_SHORT).show();
    }

    //回调结果：
    private void printResult(com.iflytek.cloud.RecognizerResult results) {
        String text = parseIatResult(results.getResultString());
        // 自动填写地址
        mStringBuilder.append(text);
        tv_content.setText(mStringBuilder.toString());
    }

    public static String parseIatResult(String json) {
        StringBuffer ret = new StringBuffer();
        try {
            JSONTokener tokener = new JSONTokener(json);
            JSONObject joResult = new JSONObject(tokener);

            JSONArray words = joResult.getJSONArray("ws");
            for (int i = 0; i < words.length(); i++) {
                // 转写结果词，默认使用第一个结果
                JSONArray items = words.getJSONObject(i).getJSONArray("cw");
                JSONObject obj = items.getJSONObject(0);
                ret.append(obj.getString("w"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret.toString();
    }
}

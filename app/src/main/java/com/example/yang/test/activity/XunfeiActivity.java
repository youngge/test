package com.example.yang.test.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.test.R;
import com.example.yang.test.baseactivity.BaseActivity;
import com.example.yang.test.util.LogUtils;
import com.example.yang.test.view.ClearEditText;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUnderstander;
import com.iflytek.cloud.SpeechUnderstanderListener;
import com.iflytek.cloud.TextUnderstander;
import com.iflytek.cloud.TextUnderstanderListener;
import com.iflytek.cloud.UnderstanderResult;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONObject;

public class XunfeiActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.btn_start)
    private Button btn_start;
    @ViewInject(R.id.btn01)
    private Button btn01;
    @ViewInject(R.id.btn02)
    private Button btn02;
    @ViewInject(R.id.tv_content)
    private TextView tv_content;
    @ViewInject(R.id.et_content)
    private ClearEditText et_content;


    // 语义理解对象（语音到语义）。
    private SpeechUnderstander mSpeechUnderstander;
    // 语义理解对象（文本到语义）。
    private TextUnderstander mTextUnderstander;
    private Toast mToast;

    int ret = 0;// 函数调用返回值
    private StringBuilder mStringBuilder = new StringBuilder();


    /**
     * 初始化监听器（语音到语义）。
     */
    private InitListener mSpeechUdrInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败,错误码：" + code);
            }
        }
    };

    /**
     * 初始化监听器（文本到语义）。
     */
    private InitListener mTextUdrInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            if (code != ErrorCode.SUCCESS) {
                showTip("初始化失败,错误码：" + code);
            }
        }
    };

    private TextUnderstanderListener mTextUnderstanderListener = new TextUnderstanderListener() {

        @Override
        public void onResult(final UnderstanderResult result) {
            if (null != result) {
                // 显示
                String text = result.getResultString();
                LogUtils.d("mtest", text);
                if (!TextUtils.isEmpty(text)) {
                    tv_content.setText(parseIatResult(text));
                    if (!TextUtils.isEmpty(tv_content.getText().toString())){
                        tv_content.setVisibility(View.VISIBLE);
                    }
                } else {
                    showTip("识别结果不正确。");
                }
            }
        }

        @Override
        public void onError(SpeechError error) {
            // 文本语义不能使用回调错误码14002，请确认您下载sdk时是否勾选语义场景和私有语义的发布
            showTip("onError Code：" + error.getErrorCode());

        }
    };


    /**
     * 语义理解回调。
     */
    private SpeechUnderstanderListener mSpeechUnderstanderListener = new SpeechUnderstanderListener() {

        @Override
        public void onResult(final UnderstanderResult result) {
            if (null != result) {
                // 显示
                String text = result.getResultString();
                LogUtils.d("mtest", text);
                if (!TextUtils.isEmpty(text)) {
                    tv_content.setText(parseIatResult(text));
                    if (!TextUtils.isEmpty(tv_content.getText().toString())){
                        tv_content.setVisibility(View.VISIBLE);
                    }
                }
            } else {
                showTip("识别结果不正确。");
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            showTip("当前正在说话，音量大小：" + volume);
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            showTip("结束说话");
        }

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            showTip("开始说话");
        }

        @Override
        public void onError(SpeechError error) {
            showTip(error.getPlainDescription(true));
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            //	if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            //		String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            //		Log.d(TAG, "session id =" + sid);
            //	}
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

        // 初始化对象
        mSpeechUnderstander = SpeechUnderstander.createUnderstander(XunfeiActivity.this, mSpeechUdrInitListener);
        mTextUnderstander = TextUnderstander.createTextUnderstander(XunfeiActivity.this, mTextUdrInitListener);

        mToast = Toast.makeText(XunfeiActivity.this, "", Toast.LENGTH_SHORT);

        btn_start.setOnClickListener(this);
        btn01.setOnClickListener(this);
        btn02.setOnClickListener(this);

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
                break;

            // 开始文本理解
            case R.id.btn01:
                tv_content.setText("");
                tv_content.setVisibility(View.GONE);
//                String text = "广州明天的天气怎么样？";
                String text = et_content.getText().toString();
                if (TextUtils.isEmpty(text)) {
                    et_content.setShakeAnimation();
                    return;
                }

                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_content.getWindowToken(), 0);

                if (mTextUnderstander.isUnderstanding()) {
                    mTextUnderstander.cancel();
                    showTip("取消");
                } else {
                    ret = mTextUnderstander.understandText(text, mTextUnderstanderListener);
                    if (ret != 0) {
                        showTip("语义理解失败,错误码:" + ret);
                    }
                }
                break;

            // 开始语音理解
            case R.id.btn02:
                tv_content.setText("");
                tv_content.setVisibility(View.GONE);

                InputMethodManager imm2 =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm2.hideSoftInputFromWindow(et_content.getWindowToken(), 0);

                // 设置参数
                setParam();

                if (mSpeechUnderstander.isUnderstanding()) {// 开始前检查状态
                    mSpeechUnderstander.stopUnderstanding();
                    showTip("停止录音");
                } else {
                    ret = mSpeechUnderstander.startUnderstanding(mSpeechUnderstanderListener);
                    if (ret != 0) {
                        showTip("语义理解失败,错误码:" + ret);
                    } else {
                        showTip("请开始说话…");
                    }
                }
                break;
        }
    }

    public static String parseIatResult(String json) {
        StringBuilder builder = new StringBuilder();
        try {
            JSONObject joResult = new JSONObject(json);
            JSONObject answer = joResult.getJSONObject("answer");
            if (answer == null) {
                return "";
            }
            String text = answer.getString("text");
            String question = joResult.getString("text");
            if (text != null && question != null) {
                builder.append("问：" + question + "\n\n");
                builder.append("答：" + text);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return builder.toString();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 退出时释放连接
        mSpeechUnderstander.cancel();
        mSpeechUnderstander.destroy();
        if (mTextUnderstander.isUnderstanding())
            mTextUnderstander.cancel();
        mTextUnderstander.destroy();
    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    /**
     * 参数设置
     *
     * @return
     */
    public void setParam() {
        String lang = "mandarin";
        if (lang.equals("en_us")) {
            // 设置语言
            mSpeechUnderstander.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            mSpeechUnderstander.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            mSpeechUnderstander.setParameter(SpeechConstant.ACCENT, lang);
        }
        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        mSpeechUnderstander.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        mSpeechUnderstander.setParameter(SpeechConstant.VAD_EOS, "1000");

        // 设置标点符号，默认：1（有标点）
        mSpeechUnderstander.setParameter(SpeechConstant.ASR_PTT, "1");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        mSpeechUnderstander.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        mSpeechUnderstander.setParameter(SpeechConstant.ASR_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/sud.wav");
    }

}

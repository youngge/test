package com.example.yang.test.activity;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.example.yang.test.R;
import com.example.yang.test.adapter.ConversationAdapter;
import com.example.yang.test.application.BaseActivity;
import com.example.yang.test.bean.ConversationBean;
import com.example.yang.test.minterface.ItemClickListener;
import com.example.yang.test.util.DBManager;
import com.example.yang.test.util.LogUtils;
import com.example.yang.test.util.ToastUtil;
import com.example.yang.test.view.ClearEditText;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SpeechUnderstander;
import com.iflytek.cloud.SynthesizerListener;
import com.iflytek.cloud.TextUnderstander;
import com.iflytek.cloud.TextUnderstanderListener;
import com.iflytek.cloud.UnderstanderResult;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 讯飞问答列表
 */
public class XunfeiListActivity extends BaseActivity implements View.OnClickListener,View.OnLayoutChangeListener {

    @ViewInject(R.id.mRecyclerView)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.btn_send)
    private Button btn_send;
    @ViewInject(R.id.et_content)
    private ClearEditText et_content;


    // 语义理解对象（语音到语义）。
    private SpeechUnderstander mSpeechUnderstander;
    // 语义理解对象（文本到语义）。
    private TextUnderstander mTextUnderstander;
    private Toast mToast;

    private int ret = 0;// 函数调用返回值

    private String text = "";

    private List<ConversationBean> list = new ArrayList<>();
    private ConversationAdapter mRecyclerAdapter;

    private DBManager dbManager;

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
                    String parseResult = parseIatResult(text);
                    if (!parseResult.equals("")) {
                        ConversationBean bean = new ConversationBean(parseResult, 0);
                        list.add(bean);
                        mRecyclerAdapter.notifyItemInserted(list.size() - 1);
                        mRecyclerView.scrollToPosition(list.size() - 1);

                        dbManager.add(bean);
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


    //合成监听器
    private SynthesizerListener mSynListener = new SynthesizerListener() {

        //缓冲进度回调
        //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            ToastUtil.showToast(XunfeiListActivity.this, "缓冲进度--" + percent + "+%");
        }

        //开始播放
        public void onSpeakBegin() {
            ToastUtil.showToast(XunfeiListActivity.this, "开始播放");
        }

        //暂停播放
        public void onSpeakPaused() {
            ToastUtil.showToast(XunfeiListActivity.this, "暂停播放");
        }

        //播放进度回调
        //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            ToastUtil.showToast(XunfeiListActivity.this, "播放进度--" + percent + "%");
        }

        //会话结束回调接口，没有错误时，speechError为null
        @Override
        public void onCompleted(SpeechError speechError) {
            ToastUtil.showToast(XunfeiListActivity.this, "会话结束");
        }

        //恢复播放回调接口
        public void onSpeakResumed() {
            ToastUtil.showToast(XunfeiListActivity.this, "恢复播放");
        }


        //会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
            ToastUtil.showToast(XunfeiListActivity.this, "onEvent");
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xunfei_list);
        ViewUtils.inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // 初始化对象
        mSpeechUnderstander = SpeechUnderstander.createUnderstander(XunfeiListActivity.this, mSpeechUdrInitListener);
        mTextUnderstander = TextUnderstander.createTextUnderstander(XunfeiListActivity.this, mTextUdrInitListener);

        mToast = Toast.makeText(XunfeiListActivity.this, "", Toast.LENGTH_SHORT);

        btn_send.setOnClickListener(this);
        mRecyclerView.addOnLayoutChangeListener(this);

        //初始化DBManager
        dbManager = new DBManager(this);

        query();

        initList();

    }

    private void initList() {
        mRecyclerAdapter = new ConversationAdapter(list, new ItemClickListener() {
            @Override
            public void onItemClick(int position) {

            }

            @Override
            public void onLongClick(int position) {
                initIflytek(list.get(position).getContent());
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mRecyclerAdapter);
        mRecyclerView.scrollToPosition(list.size()-1);
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
            // 开始文本理解
            case R.id.btn_send:
                text = et_content.getText().toString();
                et_content.setText("");
                if (TextUtils.isEmpty(text)) {
                    et_content.setShakeAnimation();
                    return;
                }
                ConversationBean bean = new ConversationBean(text, 1);
                list.add(bean);
                mRecyclerAdapter.notifyItemInserted(list.size() - 1);
                mRecyclerView.scrollToPosition(list.size() - 1);

                dbManager.add(bean);

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

        }
    }

    public String parseIatResult(String json) {
        StringBuilder builder = new StringBuilder();
        try {
            JSONObject joResult = new JSONObject(json);
            JSONObject answer = joResult.getJSONObject("answer");
            if (answer == null) {
                builder.append("");
            }else{
                String text = answer.getString("text");
                if (text != null) {
                    builder.append(text);
                }
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
        dbManager.closeDB();
    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

    private void initIflytek(String text) {
        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(XunfeiListActivity.this, null);

        /**
         2.合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
         *
         */

        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);

        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
        mTts.setParameter(SpeechConstant.VOICE_NAME,"aismengchun");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "50");//设置语速
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, "50");
        mTts.setParameter(SpeechConstant.VOLUME, "30");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
//        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
//        boolean isSuccess = mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts2.wav");
//        Toast.makeText(MainActivity.this, "语音合成 保存音频到本地：\n" + isSuccess, Toast.LENGTH_LONG).show();
        //3.开始合成
        int code = mTts.startSpeaking(text, mSynListener);

        if (code != ErrorCode.SUCCESS) {
            if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
                //上面的语音配置对象为初始化时：
                Toast.makeText(XunfeiListActivity.this, "语音组件未安装", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(XunfeiListActivity.this, "语音合成失败,错误码: " + code, Toast.LENGTH_LONG).show();
            }
        }
    }

    @Override
    public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
        mRecyclerView.scrollToPosition(list.size()-1);
    }

    public void query() {
        List<ConversationBean> query = dbManager.query();
        list.addAll(query);
    }
}

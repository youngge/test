package com.example.yang.test.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yang.test.R;
import com.example.yang.test.baseactivity.BaseActivity;
import com.example.yang.test.minterface.IPermissionListener;
import com.example.yang.test.speech.SpeechManager;
import com.example.yang.test.util.IOUtilsY;
import com.example.yang.test.util.ToastUtil;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechSynthesizer;
import com.iflytek.cloud.SynthesizerListener;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import cn.qqtheme.framework.picker.FilePicker;

/**
 * 文字转语音
 */
public class TextToSpeechActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.btn_start)
    private Button btn_start;
    @ViewInject(R.id.btn_choose)
    private Button btn_choose;
    @ViewInject(R.id.et_content)
    private EditText et_content;

    private SpeechSynthesizer mTts;

    //合成监听器
    private SynthesizerListener mSynListener = new SynthesizerListener() {

        //缓冲进度回调
        //percent为缓冲进度0~100，beginPos为缓冲音频在文本中开始位置，endPos表示缓冲音频在文本中结束位置，info为附加信息。
        public void onBufferProgress(int percent, int beginPos, int endPos, String info) {
            ToastUtil.showToast(TextToSpeechActivity.this, "缓冲进度--" + percent + "+%");
        }

        //开始播放
        public void onSpeakBegin() {
            ToastUtil.showToast(TextToSpeechActivity.this, "开始播放");
        }

        //暂停播放
        public void onSpeakPaused() {
        }

        //播放进度回调
        //percent为播放进度0~100,beginPos为播放音频在文本中开始位置，endPos表示播放音频在文本中结束位置.
        public void onSpeakProgress(int percent, int beginPos, int endPos) {
            ToastUtil.showToast(TextToSpeechActivity.this, "播放进度--" + percent + "%");
        }

        //会话结束回调接口，没有错误时，speechError为null
        @Override
        public void onCompleted(SpeechError speechError) {
            ToastUtil.showToast(TextToSpeechActivity.this, "播放结束");
        }

        //恢复播放回调接口
        public void onSpeakResumed() {
            ToastUtil.showToast(TextToSpeechActivity.this, "恢复播放");
        }


        //会话事件回调接口
        public void onEvent(int arg0, int arg1, int arg2, Bundle arg3) {
            ToastUtil.showToast(TextToSpeechActivity.this, "onEvent");
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_speech);
        ViewUtils.inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mTts = SpeechManager.getSpeechSynthesizer(this);

        btn_start.setOnClickListener(this);
        btn_choose.setOnClickListener(this);
    }


    private void startSpeak(String text) {
        //3.开始合成
        int code = mTts.startSpeaking(text, mSynListener);

        if (code != ErrorCode.SUCCESS) {
            if (code == ErrorCode.ERROR_COMPONENT_NOT_INSTALLED) {
                //上面的语音配置对象为初始化时：
                Toast.makeText(TextToSpeechActivity.this, "语音组件未安装", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(TextToSpeechActivity.this, "语音合成失败,错误码: " + code, Toast.LENGTH_LONG).show();
            }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_start:
                if (!et_content.getText().toString().trim().equals("")) {
                    startSpeak(et_content.getText().toString());
                }
                break;
            case R.id.btn_choose:
                showFileChooser();
                break;
        }
    }

    private void showFileChooser() {
        String[] per = new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        mRequestPermissions(per, new IPermissionListener() {
            @Override
            public void granted() {
                try {
                    FilePicker picker = new FilePicker(TextToSpeechActivity.this, FilePicker.FILE);
                    picker.setShowHideDir(false);
                    picker.setTitleText("请选择文本文件");
//                    picker.setRootPath(StorageUtils.getRootPath(this) + "Download/");
//                    picker.setAllowExtensions(new String[]{".apk"});
                    picker.setOnFilePickListener(new FilePicker.OnFilePickListener() {
                        @Override
                        public void onFilePicked(String currentPath) {
                            String read = IOUtilsY.read(currentPath);
                            startSpeak(read);
                        }
                    });
                    picker.show();
                } catch (SecurityException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void denied() {

            }
        });

    }

    @Override
    protected void onDestroy() {
        mTts.stopSpeaking();
        mTts.destroy();
        super.onDestroy();
    }
}

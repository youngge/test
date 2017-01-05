package com.example.yang.test.util;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.InitListener;
import com.iflytek.cloud.RecognizerListener;
import com.iflytek.cloud.RecognizerResult;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechRecognizer;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.ui.RecognizerDialog;
import com.iflytek.cloud.ui.RecognizerDialogListener;
import com.iflytek.sunflower.FlowerCollector;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.LinkedHashMap;

/**
 * Created by Administrator on 2017/1/5.
 */

public class XunfeiUtil {
    private Context context;
    private SpeechRecognizer speechRecognizer;//
    private String mEngineType;// 转译类型
    private static String TAG = "SpeechService_";

    public XunfeiUtil() {
    }

    ;

    /**
     * 录音转译功能封装类
     *
     * @param mEngineType 转译模式类型（（云端）在线，本地，混合）
     */
    public XunfeiUtil(Context context, String mEngineType) {
        this.context = context;
        this.mEngineType = mEngineType;
    }

    ;

    private RecognizerDialog speechRecognizerDialog;
//    private SharedPreferences mSharedPreferences;

    /**
     * 返回语音转换对象，
     *
     * @return SpeechRecognizer
     * 返回语音识别器对象，在当前Activity的生命周期中需要用到，进行销毁暂停等操作
     */
    public SpeechRecognizer initIflytek() {
        // 注册
        // 这里替换为你自己的Appid
        SpeechUtility.createUtility(context, SpeechConstant.APPID
                + "=586d0554");
        // 使用SpeechRecognizer对象，可根据回调消息自定义界面；
        speechRecognizer = SpeechRecognizer.createRecognizer(context,
                mInitListener);
        // 初始化听写Dialog，如果只使用有UI听写功能，无需创建SpeechRecognizer
        // 使用UI听写功能，请根据sdk文件目录下的notice.txt,放置布局文件和图片资源
        speechRecognizerDialog = new RecognizerDialog(context, mInitListener);

//        mSharedPreferences = context.getSharedPreferences(
//                IatSettings.PREFER_NAME, Activity.MODE_PRIVATE);
        // mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        // et_remark = ((EditText) findViewById(R.id.iat_text));
        // mInstaller = new ApkInstaller(context);
        // Activity activity = (Activity) context;

        return speechRecognizer;
    }

    /**
     * 初始化监听器。
     */
    private InitListener mInitListener = new InitListener() {

        @Override
        public void onInit(int code) {
            Log.d(TAG + "初始化监听器", "SpeechRecognizer init() code = " + code);
            if (code != ErrorCode.SUCCESS) {
                Toast.makeText(context, "初始化失败，错误码：" + code, Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };
    // 用HashMap存储听写结果
    private HashMap<String, String> speechRecognizerResults = new LinkedHashMap<String, String>();

    private void printResult(RecognizerResult results) {
//        String text = JsonParser.parseIatResult(results.getResultString());
        String text = results.getResultString();

        String sn = null;
        // 读取json结果中的sn字段
        try {
            JSONObject resultJson = new JSONObject(results.getResultString());
            sn = resultJson.optString("sn");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        speechRecognizerResults.put(sn, text);

        StringBuffer resultBuffer = new StringBuffer();
        for (String key : speechRecognizerResults.keySet()) {
            resultBuffer.append(speechRecognizerResults.get(key));
        }
        currentEditText.setText(resultBuffer.toString());
        currentEditText.setSelection(currentEditText.length());
        // switch (voiceType) {
        // case 1:// 今日小结
        // mEdResult.setText(resultBuffer.toString());
        // mEdResult.setSelection(mEdResult.length());
        // break;
        // case 2:// 补充说明
        // mEdSuppleExplan.setText(resultBuffer.toString());
        // mEdSuppleExplan.setSelection(mEdSuppleExplan.length());
        // break;
        // }
    }

    private EditText currentEditText;// 当前需要赋值的EditText

    /**
     * 返回录音文件的路径用于播放
     *
     * @param mEngineType 语音识别模式 云端（在线）、本地、混合，此处传该参数是用于，刷新可能被修改的语音识别模式
     * @param editText    目标输入框，语音转译后生成文本的目标控件
     * @return mAudioPath 返回录音文件路径，用于录音文件处理
     */
    public String doIflytek(EditText editText, String mEngineType) {
        this.mEngineType = mEngineType;
        this.currentEditText = editText;
        // 移动数据分析，收集开始听写事件
        FlowerCollector.onEvent(context, "iat_recognize");
        editText.setText(null);// 清空显示内容
        speechRecognizerResults.clear();
        // 设置参数
        setParam();
//        boolean isShowDialog = mSharedPreferences.getBoolean(
//                context.getString(R.string.pref_key_iat_show), true);
        boolean isShowDialog = false;
        if (isShowDialog) {
            // 显示听写对话框
            speechRecognizerDialog.setListener(mRecognizerDialogListener);
            speechRecognizerDialog.show();
            // showMessage(context.getString(R.string.text_begin));
            Toast.makeText(context, "开始识别",
                    Toast.LENGTH_SHORT).show();
        } else {
            // 不显示听写对话框
            int ret = speechRecognizer.startListening(mRecognizerListener);
            if (ret != ErrorCode.SUCCESS) {
                Toast.makeText(context, "听写失败,错误码：" + ret, Toast.LENGTH_SHORT)
                        .show();
            } else {
                Toast.makeText(context, "开始识别",
                        Toast.LENGTH_SHORT).show();
            }
        }
        return mAudioPath;
    }

    private String mAudioPath; // 录音

    public void setParam() {
        // 清空参数
        speechRecognizer.setParameter(SpeechConstant.PARAMS, null);
        // 设置听写引擎
        speechRecognizer.setParameter(SpeechConstant.ENGINE_TYPE, mEngineType);
        // 设置返回结果格式
        speechRecognizer.setParameter(SpeechConstant.RESULT_TYPE, "json");

//        String lag = mSharedPreferences.getString("iat_language_preference",
//                "mandarin");
        String lag = "mandarin";
        if (lag.equals("en_us")) {
            // 设置语言
            speechRecognizer.setParameter(SpeechConstant.LANGUAGE, "en_us");
        } else {
            // 设置语言
            speechRecognizer.setParameter(SpeechConstant.LANGUAGE, "zh_cn");
            // 设置语言区域
            speechRecognizer.setParameter(SpeechConstant.ACCENT, lag);
        }

        // 设置语音前端点:静音超时时间，即用户多长时间不说话则当做超时处理
        speechRecognizer.setParameter(SpeechConstant.VAD_BOS, "4000");

        // 设置语音后端点:后端点静音检测时间，即用户停止说话多长时间内即认为不再输入， 自动停止录音
        speechRecognizer.setParameter(SpeechConstant.VAD_EOS, "1800");

        // 设置标点符号,设置为"0"返回结果无标点,设置为"1"返回结果有标点
        speechRecognizer.setParameter(SpeechConstant.ASR_PTT, "1");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
        speechRecognizer.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
        // mAudioPath = Environment.getExternalStorageDirectory()
        // + "/ydsanbu/msc/" + DateUtil.getCurrentTime() + ".wav";
        mAudioPath = context.getExternalFilesDir(null) + File.separator
                + "logReport.wav";
        speechRecognizer
                .setParameter(SpeechConstant.ASR_AUDIO_PATH, mAudioPath);
    }

    /**
     * 听写监听器。
     */
    private RecognizerListener mRecognizerListener = new RecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            // 此回调表示：sdk内部录音机已经准备好了，用户可以开始语音输入
            Toast.makeText(context, "开始说话", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(SpeechError error) {
            // Tips：
            // 错误码：10118(您没有说话)，可能是录音机权限被禁，需要提示用户打开应用的录音权限。
            // 如果使用本地功能（语记）需要提示用户开启语记的录音权限。
            Toast.makeText(context, error.getPlainDescription(true),
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onEndOfSpeech() {
            // 此回调表示：检测到了语音的尾端点，已经进入识别过程，不再接受语音输入
            Toast.makeText(context, "结束说话", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onResult(RecognizerResult results, boolean isLast) {
            Log.d(TAG + "onResult", results.getResultString());
            printResult(results);
            if (isLast) {
                // TODO 最后的结果
                // File file=new File(mAudioPath);
                // if(file!=null){
                // // mAudioPath = pVisitVO.getOpinionAudio();
                // int time = Util.getSeconds(mAudioPath);
                // rl_audio.setVisibility(View.VISIBLE);
                // tv_audio.setCompoundDrawablesWithIntrinsicBounds(
                // R.drawable.chatto_voice_playing, 0, 0, 0);
                // tv_audio.setCompoundDrawablePadding(5);
                // tv_audio.setText(String.valueOf(time));
                // tv_audio.setGravity(Gravity.CENTER_VERTICAL);
                // }
            }
        }

        @Override
        public void onVolumeChanged(int volume, byte[] data) {
            // showMessage("当前正在说话，音量大小：" + volume);
            Log.d("mtest", "返回音频数据：" + data.length);
        }

        @Override
        public void onEvent(int eventType, int arg1, int arg2, Bundle obj) {
            // 以下代码用于获取与云端的会话id，当业务出错时将会话id提供给技术支持人员，可用于查询会话日志，定位出错原因
            // 若使用本地能力，会话id为null
            // if (SpeechEvent.EVENT_SESSION_ID == eventType) {
            // String sid = obj.getString(SpeechEvent.KEY_EVENT_SESSION_ID);
            // Log.d(TAG, "session id =" + sid);
            // }
        }
    };

    /**
     * 听写UI监听器
     */
    private RecognizerDialogListener mRecognizerDialogListener = new RecognizerDialogListener() {
        public void onResult(RecognizerResult results, boolean isLast) {
            printResult(results);
            if (isLast) {
                // TODO 最后的结果
                // File file=new File(mAudioPath);
                // if(file!=null){
                // // mAudioPath = pVisitVO.getOpinionAudio();
                // int time = Util.getSeconds(mAudioPath);
                // rl_audio.setVisibility(View.VISIBLE);
                // tv_audio.setCompoundDrawablesWithIntrinsicBounds(
                // R.drawable.chatto_voice_playing, 0, 0, 0);
                // tv_audio.setCompoundDrawablePadding(5);
                // tv_audio.setText(String.valueOf(time));
                // tv_audio.setGravity(Gravity.CENTER_VERTICAL);
                // }
            }
        }

        /**
         * 识别回调错误.
         */
        public void onError(SpeechError error) {
            Toast.makeText(context, error.getPlainDescription(true),
                    Toast.LENGTH_SHORT).show();
        }
    };
}
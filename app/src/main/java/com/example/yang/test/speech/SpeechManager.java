package com.example.yang.test.speech;

import android.content.Context;
import android.os.Environment;
import android.widget.Toast;

import com.example.yang.test.activity.MainActivity;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechSynthesizer;

/**
 * Created by Administrator on 2017/1/19.
 */

public class SpeechManager {

    /**
     * 语音合成
     * @param context
     * @return
     */
    public static SpeechSynthesizer getSpeechSynthesizer(Context context) {
        //1.创建SpeechSynthesizer对象, 第二个参数：本地合成时传InitListener
        SpeechSynthesizer mTts = SpeechSynthesizer.createSynthesizer(context, null);

        /**
         * 合成参数设置，详见《科大讯飞MSC API手册(Android)》SpeechSynthesizer 类
         */

        // 清空参数
        mTts.setParameter(SpeechConstant.PARAMS, null);

        mTts.setParameter(SpeechConstant.ENGINE_TYPE, SpeechConstant.TYPE_CLOUD); //设置云端
//        mTts.setParameter(SpeechConstant.VOICE_NAME, "xiaoyan");//设置发音人
        mTts.setParameter(SpeechConstant.VOICE_NAME, "nannan");//设置发音人
        mTts.setParameter(SpeechConstant.SPEED, "60");//设置语速
        //设置合成音调
        mTts.setParameter(SpeechConstant.PITCH, "50");
        mTts.setParameter(SpeechConstant.VOLUME, "50");//设置音量，范围0~100
        mTts.setParameter(SpeechConstant.STREAM_TYPE, "3");
        // 设置播放合成音频打断音乐播放，默认为true
        mTts.setParameter(SpeechConstant.KEY_REQUEST_FOCUS, "true");

        // 设置音频保存路径，保存音频格式支持pcm、wav，设置路径为sd卡请注意WRITE_EXTERNAL_STORAGE权限
        // 注：AUDIO_FORMAT参数语记需要更新版本才能生效
//        mTts.setParameter(SpeechConstant.AUDIO_FORMAT, "wav");
//        boolean isSuccess = mTts.setParameter(SpeechConstant.TTS_AUDIO_PATH, Environment.getExternalStorageDirectory() + "/msc/tts2.wav");
//        Toast.makeText(context, "语音合成 保存音频到本地：\n" + isSuccess, Toast.LENGTH_LONG).show();
        return mTts;
    }
}

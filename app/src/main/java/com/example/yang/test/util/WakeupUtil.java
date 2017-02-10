package com.example.yang.test.util;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;
import com.iflytek.cloud.VoiceWakeuper;
import com.iflytek.cloud.WakeuperListener;
import com.iflytek.cloud.WakeuperResult;
import com.iflytek.cloud.util.ResourceUtil;
import com.iflytek.cloud.util.ResourceUtil.RESOURCE_TYPE;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Administrator on 2017/2/10.
 */

public abstract class WakeupUtil {
    /**
     * 唤醒的回调
     */
    public abstract void kqwWake();

    // 上下文
    private Context mContext;
    // 语音唤醒对象
    private VoiceWakeuper mIvw;
    private String resultString = "";

    /*
     * TODO 设置门限值 ： 门限值越低越容易被唤醒，需要自己反复测试，根据不同的使用场景，设置一个比较合适的唤醒门限
     */
    // private final static int MAX = 60;
    // private final static int MIN = -20;
    private int curThresh = 40;

    public WakeupUtil(Context context) {
        mContext = context;

        // 加载识唤醒地资源，resPath为本地识别资源路径
        StringBuffer param = new StringBuffer();
        String resPath = ResourceUtil.generateResourcePath(context, RESOURCE_TYPE.assets, "ivw/586db7d6.jet");
        param.append(ResourceUtil.IVW_RES_PATH + "=" + resPath);
        param.append("," + ResourceUtil.ENGINE_START + "=" + SpeechConstant.ENG_IVW);
        boolean ret = SpeechUtility.getUtility().setParameter(ResourceUtil.ENGINE_START, param.toString());
        if (!ret) {
            LogUtils.d("启动本地引擎失败！");
        }
        // 初始化唤醒对象
        mIvw = VoiceWakeuper.createWakeuper(context, null);

    }

    ;

    /**
     * 唤醒
     */
    public void wake() {
        // 非空判断，防止因空指针使程序崩溃
        mIvw = VoiceWakeuper.getWakeuper();
        if (mIvw != null) {
            // 清空参数
            mIvw.setParameter(SpeechConstant.PARAMS, null);
            // 唤醒门限值，根据资源携带的唤醒词个数按照“id:门限;id:门限”的格式传入
            mIvw.setParameter(SpeechConstant.IVW_THRESHOLD, "0:" + curThresh);
            // 设置唤醒模式
            mIvw.setParameter(SpeechConstant.IVW_SST, "wakeup");
            // 设置持续进行唤醒
            mIvw.setParameter(SpeechConstant.KEEP_ALIVE, "1");
            mIvw.startListening(mWakeuperListener);
        } else {
            ToastUtil.showToast(mContext,"唤醒未初始化");
        }
    }

    public void stopWake() {
        mIvw = VoiceWakeuper.getWakeuper();
        if (mIvw != null) {
            mIvw.stopListening();
        } else {
            ToastUtil.showToast(mContext,"唤醒未初始化");
        }
    }

    private WakeuperListener mWakeuperListener = new WakeuperListener() {

        @Override
        public void onResult(WakeuperResult result) {
            LogUtils.d("onResult");
            try {
                String text = result.getResultString();
                JSONObject object;
                object = new JSONObject(text);
                StringBuffer buffer = new StringBuffer();
                buffer.append("【RAW】 " + text);
                buffer.append("\n");
                buffer.append("【操作类型】" + object.optString("sst"));
                buffer.append("\n");
                buffer.append("【唤醒词id】" + object.optString("id"));
                buffer.append("\n");
                buffer.append("【得分】" + object.optString("score"));
                buffer.append("\n");
                buffer.append("【前端点】" + object.optString("bos"));
                buffer.append("\n");
                buffer.append("【尾端点】" + object.optString("eos"));
                resultString = buffer.toString();
                stopWake();
                kqwWake();
            } catch (JSONException e) {
                resultString = "结果解析出错";
                e.printStackTrace();
            }
        }

        @Override
        public void onError(SpeechError error) {
            LogUtils.d("onError--"+error.getPlainDescription(true));
        }

        @Override
        public void onBeginOfSpeech() {
            LogUtils.d("onBeginOfSpeech");
        }

        @Override
        public void onEvent(int eventType, int isLast, int arg2, Bundle obj) {
            LogUtils.d("onEvent");
        }

        @Override
        public void onVolumeChanged(int i) {
            LogUtils.d("onVolumeChanged");
        }
    };
}

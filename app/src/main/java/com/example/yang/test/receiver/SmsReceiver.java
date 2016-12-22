package com.example.yang.test.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.telephony.SmsMessage;

import com.example.yang.test.util.DateUtils;
import com.example.yang.test.util.FileUtils;
import com.example.yang.test.util.LogUtils;
import com.example.yang.test.util.ToastUtil;

public class SmsReceiver extends BroadcastReceiver {
    private static final String ACTION_SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";
    private static final String TAG = "SmsReceiver";

    public SmsReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        ToastUtil.showToast(context,"onReceive");
        String action = intent.getAction();
        if (ACTION_SMS_RECEIVED.equals(action)) {
            handReceivedSMS(context, intent);
        }
    }

    private boolean handReceivedSMS(Context context, Intent intent) {
        boolean result = false;
        final SmsMessage[] msgs = getMessages(intent);
        if (null != msgs) {
            for (SmsMessage smsMessage : msgs) {
                handleSmsMessage(context, smsMessage);
            }
        }
        return result;
    }

    private final static SmsMessage[] getMessages(final Intent intent) {
        final Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = new SmsMessage[]{};
        if (null != bundle) {
            msgs = getMessagesFromIntent(intent);
        }
        return msgs;
    }

    public static SmsMessage[] getMessagesFromIntent(final Intent intent) {
        final Object[] messages = (Object[]) intent.getSerializableExtra("pdus");
        final byte[][] pduObjs = new byte[messages.length][];

        for (int i = 0; i < messages.length; i++) {
            pduObjs[i] = (byte[]) messages[i];
        }
        final byte[][] pdus = new byte[pduObjs.length][];
        final int pduCount = pdus.length;
        final SmsMessage[] msgs = new SmsMessage[pduCount];
        for (int i = 0; i < pduCount; i++) {
            pdus[i] = pduObjs[i];
            msgs[i] = SmsMessage.createFromPdu(pdus[i]);
        }
        return msgs;
    }

    private void handleSmsMessage(Context context, SmsMessage msg) {
        final String number = msg.getOriginatingAddress();
        final String smsContent = msg.getDisplayMessageBody();
        long date = System.currentTimeMillis();
        String mdate = DateUtils.formatDate(date, DateUtils.DATE_FORMAT_2);
        String content = "—————-" + "\n"
                + "Number:" + number + "\n"
                + "Content:" + smsContent + "\n"
                + "Time:" + mdate + "\n"
                + "—————-";
        LogUtils.d(TAG,
                content);
        ToastUtil.showToast(context,"收到来自"+number+"的短信");
        if (number.equals("10086")) {
            LogUtils.d(TAG, "拦截");
            FileUtils.write2File(Environment.getExternalStorageDirectory().getPath() + "/test.txt", content, true);
        }
    }
}

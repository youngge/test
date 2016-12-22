package com.example.yang.test.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.util.Log;


import java.io.IOException;


/**
 * Created by Administrator on 2016/12/7.
 */

public class MusicService extends Service implements MediaPlayer.OnCompletionListener {

    private  Messenger messenger;
    private static  MediaPlayer mediaPlayer;
    private int size;
    private int index;
    private String artist,title;

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer=new MediaPlayer();

        mediaPlayer.setOnCompletionListener(this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("thread", Thread.currentThread().getName());
        String option = intent.getStringExtra("option");
        title = intent.getStringExtra("title");
        artist = intent.getStringExtra("artist");
        index = intent.getIntExtra("index",0);
        size = intent.getIntExtra("size",0);
        if (messenger == null) {
            messenger = (Messenger) intent.getExtras().get("messenger");
        }
        if ("开始".equals(option)) {
            start(intent.getStringExtra("path"));
        } else if ("暂停".equals(option)) {
            pause();
        } else if ("继续".equals(option)) {
            continuePlay();
        } else if ("停止".equals(option)) {
            stop();
        } else if ("跳转".equals(option)) {
            seekPlay(intent.getIntExtra("progress", -1));
        }
        return super.onStartCommand(intent, flags, startId);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            //释放资源
            mediaPlayer.release();
        }
        super.onDestroy();
    }

    //开始
    public void start(String path) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(path);
            try {
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
//            mediaPlayer= MediaPlayer.create(this, R.raw.music);
            if (mediaPlayer.isPlaying()){
                mediaPlayer.stop();
            }
            mediaPlayer.start();
//            MediaUtils.currentState= Constants.PLAY_START;
            new Thread() {
                @Override
                public void run() {
                    while (true) {
                        SystemClock.sleep(200);
                        Message message= Message.obtain();
                        Bundle bundle = new Bundle();
                        bundle.putInt("cPosition",mediaPlayer.getCurrentPosition());
                        bundle.putInt("duration",mediaPlayer.getDuration());
                        bundle.putInt("index",index);
                        bundle.putString("artist",artist);
                        bundle.putString("title",title);
                        bundle.putInt("size",size);
                        bundle.putInt("type",1);
//                        message.arg1=mediaPlayer.getCurrentPosition();
//                        message.arg2=mediaPlayer.getDuration();
//                        message.what=1;
                        message.setData(bundle);
                        try {
                            messenger.send(message);
                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    //暂停
    public void pause (){
        if (mediaPlayer!=null&&mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
//            MediaUtils.currentState=Constants.PLAY_PAUSE;
        }
    }
    //继续播放
    public void continuePlay() {
        if (mediaPlayer!=null&&!mediaPlayer.isPlaying()) {
            mediaPlayer.start();
//            MediaUtils.currentState= Constants.PLAY_START;
        }
    }
    //停止播放
    public void stop() {
        if (mediaPlayer!=null) {
            mediaPlayer.stop();
//            MediaUtils.currentState=Constants.PLAY_STOP;
        }
    }

    public void seekPlay(int progress){
        Log.d("test",progress+"---progress");
        if (progress==-1){
            return;
        }
        mediaPlayer.seekTo(progress);
    }

    public static boolean isPlaying(){
        if (mediaPlayer!=null&&mediaPlayer.isPlaying()){
            return true;
        }
        return false;
    }

    public static boolean getInstance(){
        if (mediaPlayer!=null){
            return true;
        }
        return false;
    }

    @Override
    public void onCompletion(MediaPlayer mp) {

    }
}

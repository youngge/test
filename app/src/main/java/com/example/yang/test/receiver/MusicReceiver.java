package com.example.yang.test.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Messenger;
import android.provider.MediaStore;

import com.example.yang.test.bean.MusicBean;
import com.example.yang.test.service.MusicService;
import com.example.yang.test.util.LogUtils;

import java.util.ArrayList;
import java.util.List;


public class MusicReceiver extends BroadcastReceiver {
    private int playedMusicTime;
    private boolean changeSong;
    private int durationMusic;
    private Context mContext;
    private static int index;

    private List<MusicBean> musicBeanList = new ArrayList<>();
    private  Messenger messenger;

    public MusicReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtils.d("mtest","onReceive");
        mContext = context;
        if (musicBeanList.size()==0) {
            LogUtils.d("mtest","musicBeanList.size()==0");
            getSongList(context);
        }
        String action = intent.getAction();
        if (messenger == null) {
            messenger = (Messenger) intent.getExtras().get("messenger");
        }
        if (action.equals("action.play")){
            startMusicService("开始",musicBeanList.get(index).getData());
        } else if (action.equals("action.stop")) {
            startMusicService("暂停");
        } else if (action.equals("action.continue")) {
            startMusicService("继续");
        }else if (action.equals("action.seek")) {
            startMusicService("跳转", intent.getIntExtra("progress",0));
        }else if (action.equals("action.previous")) {
                if (index == 0) {
                    index = musicBeanList.size() - 1;
                    startMusicService("开始", musicBeanList.get(index).getData());
                } else {
                    index--;
                    startMusicService("开始", musicBeanList.get(index).getData());
                }
        }else if (action.equals("action.next")) {
            if (index == musicBeanList.size() - 1) {
                    index = 0;
                    startMusicService("开始", musicBeanList.get(index).getData());
                } else {
                    index++;
                    startMusicService("开始", musicBeanList.get(index).getData());
                }
        }
    }

    public void getSongList(Context context) {
        musicBeanList.clear();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = context.getContentResolver().query(uri,
                new String[]{
                        MediaStore.Audio.Media.TITLE,
                        MediaStore.Audio.Media.ARTIST,
                        MediaStore.Audio.Media.DATA}, null, null, null);
        while (cursor.moveToNext()) {
            musicBeanList.add(new MusicBean(
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)),
                    cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA))));
        }

    }

    private void startMusicService(String option, String path) {
        Intent intentService = new Intent(mContext, MusicService.class);
        intentService.putExtra("option", option);
        intentService.putExtra("messenger",messenger);
        intentService.putExtra("path", path);
        intentService.putExtra("index", index);
        intentService.putExtra("artist", musicBeanList.get(index).getArtist());
        intentService.putExtra("title", musicBeanList.get(index).getTitle());
        intentService.putExtra("size", musicBeanList.size());
        mContext.startService(intentService);
    }

    private void startMusicService(String option) {
        Intent intentService = new Intent(mContext, MusicService.class);
        intentService.putExtra("option", option);
        intentService.putExtra("messenger",messenger);
        intentService.putExtra("index", index);
        intentService.putExtra("artist", musicBeanList.get(index).getArtist());
        intentService.putExtra("title", musicBeanList.get(index).getTitle());
        intentService.putExtra("size", musicBeanList.size());
        mContext.startService(intentService);
    }

    private void startMusicService(String option, int progress) {
        Intent intentService = new Intent(mContext, MusicService.class);
        intentService.putExtra("option", option);
        intentService.putExtra("progress", progress);
        intentService.putExtra("messenger",messenger);
        intentService.putExtra("index", index);
        intentService.putExtra("artist", musicBeanList.get(index).getArtist());
        intentService.putExtra("title", musicBeanList.get(index).getTitle());
        intentService.putExtra("size", musicBeanList.size());
        mContext.startService(intentService);
    }

}

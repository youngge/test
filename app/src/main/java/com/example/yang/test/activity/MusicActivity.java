package com.example.yang.test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.yang.test.R;
import com.example.yang.test.baseactivity.BaseActivity;
import com.example.yang.test.util.DateUtils;
import com.example.yang.test.view.MusicPlayView;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class MusicActivity extends BaseActivity {

    @ViewInject(R.id.seekbar02)
    private SeekBar seekBar02;
    @ViewInject(R.id.customview)
    private MusicPlayView customview;
    @ViewInject(R.id.tv_duration)
    private TextView tv_duration;
    @ViewInject(R.id.tv_data)
    private TextView tv_data;
    @ViewInject(R.id.btn_previous)
    private Button btn_previous;
    @ViewInject(R.id.btn_play)
    private Button btn_play;
    @ViewInject(R.id.btn_next)
    private Button btn_next;

    public int current = 0;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
           int type = bundle.getInt("type");
            switch (type) {
                case 1:
                    int curduration = bundle.getInt("cPosition");
                    int totalduration = bundle.getInt("duration");
                    int index = bundle.getInt("index");
                    String artist = bundle.getString("artist");
                    String title = bundle.getString("title");
                    int size = bundle.getInt("size");
                    current = curduration;
                    customview.setMaxNum(totalduration);
                    customview.setCurrentNum(curduration);
                    seekBar02.setMax(totalduration);
                    seekBar02.setProgress(curduration);
                    tv_duration.setText(DateUtils.number2minute(curduration / 1000)
                            + "/" + DateUtils.number2minute(totalduration / 1000));
                    tv_data.setText("(" + (index + 1) + "/" + size + ")" + title
                            + "--" + artist);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music);
        ViewUtils.inject(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_play.getText().toString().equals("play")) {
                    if (current == 0) {
                        Intent intent = new Intent("action.play");
                        intent.putExtra("messenger", new Messenger(handler));
                        sendBroadcast(intent);
                        btn_play.setText("stop");
                    } else {
                        Intent intent = new Intent("action.continue");
                        intent.putExtra("messenger", new Messenger(handler));
                        sendBroadcast(intent);
                        btn_play.setText("stop");
                    }
                } else {
                    Intent intent = new Intent("action.stop");
                    intent.putExtra("messenger", new Messenger(handler));
                    sendBroadcast(intent);
                    btn_play.setText("play");
                }
            }
        });

        btn_previous.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("action.previous");
                intent.putExtra("messenger", new Messenger(handler));
                sendBroadcast(intent);
                btn_play.setText("stop");
            }
        });

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("action.next");
                intent.putExtra("messenger", new Messenger(handler));
                sendBroadcast(intent);
                btn_play.setText("stop");
            }
        });

        seekBar02.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                Intent intent = new Intent("action.seek");
                intent.putExtra("progress",seekBar.getProgress());
                sendBroadcast(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}

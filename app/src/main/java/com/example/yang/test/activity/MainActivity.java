package com.example.yang.test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.yang.test.R;
import com.example.yang.test.activity.xunfei.SpeechActivity;
import com.example.yang.test.adapter.common.BaseRecyclerAdapter;
import com.example.yang.test.adapter.common.BaseRecyclerHolder;
import com.example.yang.test.android_serialport_api.ConsoleActivity;
import com.example.yang.test.baseactivity.BaseActivity;
import com.example.yang.test.util.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @ViewInject(R.id.mRecyclerView)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.swipe_refresh_layout)
    private SwipeRefreshLayout mSwipeRefreshLayout;
    @ViewInject(R.id.drawerlayout)
    private DrawerLayout mDrawerlayout;
    @ViewInject(R.id.nav_view)
    private NavigationView mNavigationView;
    @ViewInject(R.id.fab)
    private FloatingActionButton mFab;

    private List<String> dataList = new ArrayList<>();
    private BaseRecyclerAdapter mRecyclerAdapter;

    private final static String TEXT_ZHIMAFEN = "芝麻分";
    private final static String TEXT_MUSIC = "播放器";
    private final static String TEXT_NOTIFICATION = "通知";
    private final static String TEXT_GAME = "2048小游戏";
    private final static String TEXT_RUSSIANSQUARE = "俄罗斯方块";
    private final static String TEXT_CUSTOMVIEW = "自定义控件";
    private final static String TEXT_ANIMATION = "动画";
    private final static String TEXT_PERMISSION = "运行时权限";
    private final static String TEXT_CAMERA = "监控";
    private final static String TEXT_SPEECHDEMO = "讯飞语音示例";
    private final static String TEXT_XUNFEI = "讯飞语音识别(问答)";
    private final static String TEXT_XUNFEI_LIST = "讯飞问答";
    private final static String TEXT_SPEECHTOTEXT = "讯飞语音转文字";
    private final static String TEXT_TEXTTOSPEECH = "讯飞文字转语音";
    private final static String TEXT_WIFI = "wifi操作";
    private final static String TEXT_CARDWIFI = "wifi操作玩具车";
    private final static String TEXT_SERVER = "读取本地服务器数据";
    private final static String TEXT_BLUETOOTH = "蓝牙读取连接";
    private final static String TEXT_DATASHOW = "数据实时显示";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ViewUtils.inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.mipmap.icon_menu);
        }

        //设置刷新时动画的颜色，可以设置4个
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setProgressViewOffset(true, 0, 100);
//            mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
//            mSwipeRefreshLayout.setProgressBackgroundColor(android.R.color.holo_blue_light);
            mSwipeRefreshLayout.setColorSchemeResources(R.color.colorPrimary);
            mSwipeRefreshLayout.setOnRefreshListener(this);
        }

        mSwipeRefreshLayout.setRefreshing(true);
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        Snackbar.make(mRecyclerView, "刷新完毕", Snackbar.LENGTH_SHORT).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtil.showToast(MainActivity.this, "OK");
                            }
                        }).show();
                    }
                });
            }
        }.start();

        initData();

        mRecyclerAdapter = new BaseRecyclerAdapter<String>(this, dataList, R.layout.item_home) {
            @Override
            public void convert(BaseRecyclerHolder holder, String item, int position, boolean isScrolling) {
                holder.setText(R.id.tv, item);
            }
        };

        mRecyclerAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position) {
                Intent intent = new Intent();
                String mtext = dataList.get(position);
                if (mtext.equals(TEXT_ZHIMAFEN)) {//芝麻分
                    intent.setClass(MainActivity.this, ZhimafenActivity.class);
                    startActivity(intent);
                } else if (mtext.equals(TEXT_MUSIC)) {//音乐播放器
                    intent.setClass(MainActivity.this, MusicActivity.class);
                    startActivity(intent);
                } else if (mtext.equals(TEXT_NOTIFICATION)) {//通知
                    intent.setClass(MainActivity.this, NotificationActivity.class);
                    startActivity(intent);
                } else if (mtext.equals(TEXT_GAME)) {//2048游戏
                    intent.setClass(MainActivity.this, GameActivity.class);
                    startActivity(intent);
                } else if (mtext.equals(TEXT_RUSSIANSQUARE)) {//俄罗斯方块
                    intent.setClass(MainActivity.this, RussianSquareActivity.class);
                    startActivity(intent);
                } else if (mtext.equals(TEXT_CUSTOMVIEW)) {//自定义控件
                    intent.setClass(MainActivity.this, CustomViewActivity.class);
                    startActivity(intent);
                } else if (mtext.equals(TEXT_ANIMATION)) {//动画
                    intent.setClass(MainActivity.this, AnimationActivity.class);
                    startActivity(intent);
                } else if (mtext.equals(TEXT_PERMISSION)) {//运行时权限
                    intent.setClass(MainActivity.this, PermissionActivity.class);
                    startActivity(intent);
                } else if (mtext.equals(TEXT_CAMERA)) {//监控
                    intent.setClass(MainActivity.this, SocketCameraActivity.class);
                    startActivity(intent);
                } else if (mtext.equals(TEXT_SPEECHDEMO)) {//讯飞语音示例
                    intent.setClass(MainActivity.this, SpeechActivity.class);
                    startActivity(intent);
                } else if (mtext.equals(TEXT_XUNFEI)) {//讯飞语音识别(问答)
                    intent.setClass(MainActivity.this, XunfeiActivity.class);
                    startActivity(intent);
                } else if (mtext.equals(TEXT_XUNFEI_LIST)) {//讯飞问答
                    intent.setClass(MainActivity.this, XunfeiListActivity.class);
                    startActivity(intent);
                } else if (mtext.equals(TEXT_SPEECHTOTEXT)) {//讯飞语音转文字
                    intent.setClass(MainActivity.this, SpeechToTextActivity.class);
                    startActivity(intent);
                } else if (mtext.equals(TEXT_TEXTTOSPEECH)) {//讯飞文字转语音
                    intent.setClass(MainActivity.this, TextToSpeechActivity.class);
                    startActivity(intent);
                } else if (mtext.equals(TEXT_WIFI)) {//wifi操作
//                    intent.setClass(MainActivity.this, WifiActivity.class);
//                    startActivity(intent);
                    intent.setClass(MainActivity.this, ConsoleActivity.class);
                    startActivity(intent);
                } else if (mtext.equals(TEXT_CARDWIFI)) {//wifi操作玩具车
//                    intent.setClass(MainActivity.this, CarWifiActivity.class);
//                    startActivity(intent);
                } else if (mtext.equals(TEXT_SERVER)) {//读取本地服务器数据
                    intent.setClass(MainActivity.this, ServerActivity.class);
                    startActivity(intent);
                } else if (mtext.equals(TEXT_BLUETOOTH)) {//蓝牙读取连接
                    intent.setClass(MainActivity.this, BlueToothActivity.class);
                    startActivity(intent);
                } else if (mtext.equals(TEXT_DATASHOW)) {//数据实时显示
                    intent.setClass(MainActivity.this, DataShowActivity.class);
                    startActivity(intent);
                }
            }
        });


        mRecyclerAdapter.setOnItemLongClickListener(new BaseRecyclerAdapter.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(RecyclerView parent, View view, int position) {
                dataList.add(position, "--添加项--");
                mRecyclerAdapter.notifyItemInserted(position);
                return true;
            }
        });


        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        mNavigationView.setCheckedItem(R.id.nav_call);
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                mDrawerlayout.closeDrawers();
                return true;
            }
        });

        mFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, "more", Snackbar.LENGTH_SHORT).show();
            }
        });


    }

    private void initData() {
        dataList.clear();
        dataList.add(TEXT_ZHIMAFEN);
        dataList.add(TEXT_MUSIC);
        dataList.add(TEXT_NOTIFICATION);
        dataList.add(TEXT_GAME);
//        dataList.add(TEXT_RUSSIANSQUARE);
        dataList.add(TEXT_CUSTOMVIEW);
        dataList.add(TEXT_ANIMATION);
        dataList.add(TEXT_PERMISSION);
        dataList.add(TEXT_CAMERA);
        dataList.add(TEXT_SPEECHDEMO);
        dataList.add(TEXT_XUNFEI);
        dataList.add(TEXT_XUNFEI_LIST);
        dataList.add(TEXT_SPEECHTOTEXT);
        dataList.add(TEXT_TEXTTOSPEECH);
        dataList.add(TEXT_WIFI);
//        dataList.add(TEXT_CARDWIFI);
        dataList.add(TEXT_SERVER);
        dataList.add(TEXT_BLUETOOTH);
        dataList.add(TEXT_DATASHOW);
        dataList.add("空");
        dataList.add("空");
    }

    @Override
    public void onRefresh() {
        new Thread() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //切换回主线程
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        initData();
                        mRecyclerAdapter.notifyDataSetChanged();
                        Snackbar.make(mRecyclerView, "刷新完毕", Snackbar.LENGTH_SHORT).setAction("OK", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ToastUtil.showToast(MainActivity.this, "OK");
                            }
                        }).show();
                    }
                });
            }
        }.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerlayout.openDrawer(GravityCompat.START);
                break;

            case R.id.menu_camera:
                ToastUtil.showToast(this, "camera");
                break;

            case R.id.menu_voice:
                ToastUtil.showToast(this, "voice");
                break;

            case R.id.menu_setting:
                ToastUtil.showToast(this, "setting");
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        if (mDrawerlayout.isDrawerOpen(GravityCompat.START)) {
            mDrawerlayout.closeDrawers();
            return;
        }
        super.onBackPressed();
    }
}

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
import com.example.yang.test.adapter.HomeAdapter;
import com.example.yang.test.application.BaseActivity;
import com.example.yang.test.minterface.ItemClickListener;
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
    private HomeAdapter mRecyclerAdapter;

    private final static int CORE_ZHIMAFEN = 0;
    private final static int CORE_MUSIC = 1;
    private final static int CORE_NOTIFICATION = 2;
    private final static int CORE_GAME = 3;
    private final static int CORE_RUSSIANSQUARE = 4;
    private final static int CORE_CUSTOMVIEW = 5;
    private final static int CORE_ANIMATION = 6;
    private final static int CORE_PERMISSION = 7;
    private final static int CORE_XUNFEI = 8;
    private final static int CORE_XUNFEI02 = 9;
    private final static int CORE_WIFI = 10;
    private final static int CORE_CARDWIFI = 11;
    private final static int CORE_SPEECHDEMO = 12;

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
                        Snackbar.make(mRecyclerView, "刷新完毕", Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
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

//        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerAdapter = new HomeAdapter(dataList, new ItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent();
                switch (position){
                    case CORE_ZHIMAFEN:
                        intent.setClass(MainActivity.this,ZhimafenActivity.class);
                        startActivity(intent);
                        break;
                    case CORE_MUSIC:
                        intent.setClass(MainActivity.this,MusicActivity.class);
                        startActivity(intent);
                        break;
                    case CORE_NOTIFICATION:
                        intent.setClass(MainActivity.this,NotificationActivity.class);
                        startActivity(intent);
                        break;
                    case CORE_GAME:
                        intent.setClass(MainActivity.this,GameActivity.class);
                        startActivity(intent);
                        break;
                    case CORE_RUSSIANSQUARE:
                        intent.setClass(MainActivity.this,RussianSquareActivity.class);
                        startActivity(intent);
                        break;
                    case CORE_CUSTOMVIEW:
                        intent.setClass(MainActivity.this,CustomViewActivity.class);
                        startActivity(intent);
                        break;
                    case CORE_ANIMATION:
                        intent.setClass(MainActivity.this,AnimationActivity.class);
                        startActivity(intent);
                        break;
                    case CORE_PERMISSION:
                        intent.setClass(MainActivity.this,PermissionActivity.class);
                        startActivity(intent);
                        break;
                    case CORE_XUNFEI:
                        intent.setClass(MainActivity.this,XunfeiActivity.class);
                        startActivity(intent);
                        break;
                    case CORE_XUNFEI02:
                        intent.setClass(MainActivity.this,XFSpeechActivity.class);
                        startActivity(intent);
                        break;
                    case CORE_WIFI:
                        intent.setClass(MainActivity.this,WifiActivity.class);
                        startActivity(intent);
                        break;
                    case CORE_CARDWIFI:
                        intent.setClass(MainActivity.this,CarWifiActivity.class);
                        startActivity(intent);
                        break;
                    case CORE_SPEECHDEMO:
                        intent.setClass(MainActivity.this,SpeechActivity.class);
                        startActivity(intent);
                        break;
                }
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
        dataList.add("芝麻分");
        dataList.add("播放器");
        dataList.add("通知");
        dataList.add("2048小游戏");
        dataList.add("俄罗斯方块");
        dataList.add("自定义控件");
        dataList.add("动画");
        dataList.add("运行时权限");
        dataList.add("讯飞语音识别(问答)");
        dataList.add("讯飞语音转文字");
        dataList.add("wifi操作");
        dataList.add("wifi操作玩具车");
        dataList.add("讯飞语音示例");
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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mSwipeRefreshLayout.setRefreshing(false);
                        initData();
                        mRecyclerAdapter.notifyDataSetChanged();
                        Snackbar.make(mRecyclerView, "刷新完毕", Snackbar.LENGTH_LONG).setAction("OK", new View.OnClickListener() {
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

            case R.id.menu_share:
                ToastUtil.showToast(this, "share");
                break;

            case R.id.menu_delete:
                ToastUtil.showToast(this, "delete");
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

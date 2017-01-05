package com.example.yang.test.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.example.yang.test.R;
import com.example.yang.test.adapter.HomeAdapter;
import com.example.yang.test.application.BaseActivity;
import com.example.yang.test.minterface.IHomeClickListener;
import com.example.yang.test.util.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

public class WifiActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @ViewInject(R.id.mRecyclerView)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.swipe_refresh_layout)
    private SwipeRefreshLayout mSwipeRefreshLayout;

    private List<String> dataList = new ArrayList<>();
    private HomeAdapter mRecyclerAdapter;

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
            mSwipeRefreshLayout.setProgressViewOffset(false, 0, 100);
//            mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
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
                                ToastUtil.showToast(WifiActivity.this, "OK");
                            }
                        }).show();
                    }
                });
            }
        }.start();

        initData();

        mRecyclerAdapter = new HomeAdapter(dataList, new IHomeClickListener() {
            @Override
            public void onItemClick(int position) {
                Intent intent = new Intent();
                switch (position) {
                }
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mRecyclerAdapter);

    }

    private void initData() {
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
                    }
                });
            }
        }.start();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

        }
        return true;
    }

}

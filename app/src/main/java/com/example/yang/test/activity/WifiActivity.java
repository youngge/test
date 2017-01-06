package com.example.yang.test.activity;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yang.test.R;
import com.example.yang.test.adapter.HomeAdapter;
import com.example.yang.test.adapter.WifiAdapter;
import com.example.yang.test.application.BaseActivity;
import com.example.yang.test.minterface.ItemClickListener;
import com.example.yang.test.util.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.ArrayList;
import java.util.List;

import static android.R.id.list;

public class WifiActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    @ViewInject(R.id.mRecyclerView)
    private RecyclerView mRecyclerView;
    @ViewInject(R.id.swipe_refresh_layout)
    private SwipeRefreshLayout mSwipeRefreshLayout;
    @ViewInject(R.id.tv_speed)
    private TextView tv_speed;

    private List<ScanResult> dataList = new ArrayList<>();
    private WifiAdapter mRecyclerAdapter;
    private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        ViewUtils.inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
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
                wifiManager.startScan();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        initData();
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

    }

    private void initData() {
        if (dataList!=null&&dataList.size()>0){
            dataList.clear();
        }
        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        WifiInfo connectionInfo = wifiManager.getConnectionInfo();
        tv_speed.setText(connectionInfo.getLinkSpeed()+"--");
        openWifi();
        dataList = wifiManager.getScanResults();
        if (dataList == null) {
            Toast.makeText(this, "wifi未打开！", Toast.LENGTH_LONG).show();
        }else {
            mRecyclerAdapter = new WifiAdapter(dataList, new ItemClickListener() {
                @Override
                public void onItemClick(int position) {
                    ToastUtil.showToast(WifiActivity.this,"点击的是"+position);
                    List<WifiConfiguration> configuredNetworks = wifiManager.getConfiguredNetworks();
                    wifiManager.enableNetwork(configuredNetworks.get(position).networkId,true);
                }
            });
            LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            mRecyclerView.setLayoutManager(layoutManager);
            mRecyclerView.setAdapter(mRecyclerAdapter);
        }
    }

    private void openWifi() {
        if (!wifiManager.isWifiEnabled()) {
            wifiManager.setWifiEnabled(true);
        }

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

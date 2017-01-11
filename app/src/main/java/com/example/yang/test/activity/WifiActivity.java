package com.example.yang.test.activity;

import android.content.Context;
import android.net.DhcpInfo;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.yang.test.R;
import com.example.yang.test.application.BaseActivity;
import com.example.yang.test.thread.WifiClientThread;
import com.example.yang.test.util.ToastUtil;
import com.example.yang.test.util.WifiManageUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class WifiActivity extends BaseActivity {

    @ViewInject(R.id.tv_speed)
    private TextView tv_speed;
    @ViewInject(R.id.btn_click)
    private Button btn_click;

    private WifiManager wifiManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);
        ViewUtils.inject(this);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        wifiManager = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiManageUtils wifimanageutils = new WifiManageUtils(WifiActivity.this);
                WifiConfiguration netConfig = wifimanageutils
                        .getCustomeWifiClientConfiguration("CJL1", "wty61082988", 3);

                int wcgID = wifiManager.addNetwork(netConfig);
                boolean b = wifiManager.enableNetwork(wcgID, true);

                Boolean iptoready = false;
                if (!b) {
                    ToastUtil.showToast(WifiActivity.this, "wifi 连接配置不可用");
                    return;
                }
                while (!iptoready) {
                    try {
                        // 为了避免程序一直while循环，让它睡个100毫秒在检测……
                        Thread.currentThread();
                        Thread.sleep(100);
                    } catch (InterruptedException ie) {
                    }

                    DhcpInfo dhcp = new WifiManageUtils(WifiActivity.this).getDhcpInfo();
                    int ipInt = dhcp.gateway;
                    if (ipInt != 0) {
                        iptoready = true;
                    }
                }
//                wifiLock.acquire();
                WifiClientThread clientThread = new WifiClientThread(WifiActivity.this);
                clientThread.start();
            }
        });

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

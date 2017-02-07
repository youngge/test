package com.example.yang.test.activity;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.yang.test.R;
import com.example.yang.test.baseactivity.BaseActivity;
import com.example.yang.test.util.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class BlueToothActivity extends BaseActivity implements View.OnClickListener {

    @ViewInject(R.id.textView)
    private TextView textView;
    @ViewInject(R.id.btn_click01)
    private Button btn_click01;     //搜索
    @ViewInject(R.id.btn_click02)
    private Button btn_click02;     //传输
    @ViewInject(R.id.btn_click03)
    private Button btn_click03;     //结束

    private BluetoothAdapter bluetoothAdapter;
    private BlueToothReceiver mReceiver;
    private BluetoothDevice device;
    private List<Map> arrayList = new ArrayList<>();
    private BluetoothSocket socket;
    private BufferedOutputStream bos;
    private BufferedInputStream bis;

    private static final String myUuid = "00001101-0000-1000-8000-00805F9B34FB";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bluetooth);
        ViewUtils.inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        btn_click01.setOnClickListener(this);

        btn_click02.setOnClickListener(this);

        btn_click03.setOnClickListener(this);

        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (bluetoothAdapter == null) {
            ToastUtil.showToast(this, "不支持Bluetooth");
            finish();
        } else if (!bluetoothAdapter.isEnabled()) {
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, 1);
            ToastUtil.showToast(this, "启动Bluetooth中");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //打开蓝牙设备后
        if (resultCode == RESULT_OK && requestCode == 1) {
            if (bluetoothAdapter.isDiscovering()) {
                bluetoothAdapter.cancelDiscovery();
            }
            textView.setText("搜索中...");
            bluetoothAdapter.startDiscovery();

            mReceiver = new BlueToothReceiver();
            IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
            registerReceiver(mReceiver, intentFilter);
        }
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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_click01:
                if (!bluetoothAdapter.isEnabled()) {
                    Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(intent, 1);
                    return;
                }
                if (bluetoothAdapter.isDiscovering()) {
                    bluetoothAdapter.cancelDiscovery();
                }
                textView.setText("搜索中...");
                bluetoothAdapter.startDiscovery();

                mReceiver = new BlueToothReceiver();
                IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
                registerReceiver(mReceiver, intentFilter);

                break;

            case R.id.btn_click02:
                UUID uuid = UUID.fromString(myUuid);
                BluetoothDevice mDevice = bluetoothAdapter.getRemoteDevice(arrayList.get(0).
                        get("arrayAddress").toString());
                try {
                    socket = mDevice.createRfcommSocketToServiceRecord(uuid);
                    socket.connect();
                    bos = new BufferedOutputStream(socket.getOutputStream());
                    bis = new BufferedInputStream(socket.getInputStream());
                    bos.write("hello".getBytes());
                    bos.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;

            case R.id.btn_click03:
                finish();
                break;
        }
    }

    public class BlueToothReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (textView.getText().toString().contains("搜索中")) {
                textView.setText("");
            }
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if (device.getBondState() != BluetoothDevice.BOND_BONDED) {
                    short rssi = intent.getExtras().getShort(BluetoothDevice.EXTRA_RSSI);
                    textView.append("名字：" + device.getName() + "\n远程蓝牙信号强弱：" + rssi + "\n地址："
                            + device.getAddress() + "\n\n");
                }
                HashMap<String, String> map = new HashMap<>();
                map.put("arrayName", device.getName());
                map.put("arrayAddress", device.getAddress());
                arrayList.add(map);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mReceiver != null) {
            unregisterReceiver(mReceiver);
        }
        try {
            bos.close();
            bis.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

package com.example.yang.test.baseactivity;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;

import com.example.yang.test.minterface.IPermissionListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/12/7.
 */

public class BaseActivity extends AppCompatActivity {

    private IPermissionListener iPermissionListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    protected void mRequestPermissions(String[] permissions, IPermissionListener iPermissionListener) {
        this.iPermissionListener = iPermissionListener;
        if (permissions.length == 0) {
            return;
        }
        List<String> mList = new ArrayList<>();
        for (int i = 0; i < permissions.length; i++) {
            if (ActivityCompat.checkSelfPermission(this, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                mList.add(permissions[i]);
            }
        }
        if (mList.isEmpty()) {
            iPermissionListener.granted();
            return;
        }
        ActivityCompat.requestPermissions(this, mList.toArray(new String[mList.size()]), 1);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                        iPermissionListener.denied();
                        return;
                    }
                }
                iPermissionListener.granted();
                }
        }
    }
}

package com.example.yang.test.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yang.test.R;
import com.example.yang.test.application.BaseActivity;
import com.example.yang.test.net.IRequestCallback;
import com.example.yang.test.net.IRequestManager;
import com.example.yang.test.net.RequestFactory;
import com.example.yang.test.util.LogUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class ServerActivity extends BaseActivity {

    @ViewInject(R.id.tv_speed)
    private TextView tv_speed;
    @ViewInject(R.id.et_content)
    private EditText et_content;
    @ViewInject(R.id.btn_click)
    private Button btn_click;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        ViewUtils.inject(this);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        btn_click.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getdata();
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

    private void getdata() {
        String url = "http://192.168.1.110:1478/" + et_content.getText().toString();
        //这里发起请求依赖的是IReqauestManager接口
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.get(url, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtils.d("mtest", response);
                tv_speed.setText(response);
            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
                LogUtils.d("mtest", "请求失败" + throwable.toString());
                tv_speed.setText(throwable.toString());
            }
        });
    }


}

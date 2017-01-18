package com.example.yang.test.activity;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.yang.test.R;
import com.example.yang.test.application.BaseActivity;
import com.example.yang.test.bean.UserBean;
import com.example.yang.test.net.IRequestCallback;
import com.example.yang.test.net.IRequestManager;
import com.example.yang.test.net.RequestFactory;
import com.example.yang.test.util.LogUtils;
import com.google.gson.Gson;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

import java.util.List;

public class ServerActivity extends BaseActivity {

    @ViewInject(R.id.tv_speed)
    private TextView tv_speed;
    @ViewInject(R.id.et_content)
    private EditText et_content;
    @ViewInject(R.id.btn_click)
    private Button btn_click;
    @ViewInject(R.id.image)
    private ImageView image;

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
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(et_content.getWindowToken(), 0);

                String url = "";
                if (et_content.getText().toString().equals("")) {
                    url = "http://192.168.1.142:2323/index.html";
//                    url = "http://www.campus100.cn/App/Home/Apineed/needlist";
                } else {
                    url = "http://192.168.1.142:1478/" + et_content.getText().toString();
                }
                getdata(url);
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

    private void getdata(String url) {
        //这里发起请求依赖的是IReqauestManager接口
        IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.get(url, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtils.d("mtest", "onSuccess--"+response);
                StringBuilder builder = new StringBuilder();
                Gson gson = new Gson();
                UserBean userBean = gson.fromJson(response, UserBean.class);
                List<UserBean.ResultBean> result = userBean.getResult();
                for (UserBean.ResultBean mResult : result){
                    builder.append(mResult.toString());
                    builder.append("\n");
                }
                tv_speed.setText(builder.toString());
            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
                LogUtils.d("mtest", "onFailure--" + throwable.toString());
                tv_speed.setText(throwable.toString());
            }
        });
    }

}

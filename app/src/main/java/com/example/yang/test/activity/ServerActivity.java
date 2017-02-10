package com.example.yang.test.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.yang.test.R;
import com.example.yang.test.adapter.common.BaseRecyclerAdapter;
import com.example.yang.test.adapter.common.BaseRecyclerHolder;
import com.example.yang.test.baseactivity.BaseActivity;
import com.example.yang.test.bean.UserBean;
import com.example.yang.test.net.IRequestCallback;
import com.example.yang.test.net.IRequestManager;
import com.example.yang.test.net.RequestFactory;
import com.example.yang.test.util.LogUtils;
import com.example.yang.test.util.ToastUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ServerActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.et_content)
    EditText etContent;
    @BindView(R.id.btn_click)
    Button btnClick;
    @BindView(R.id.image)
    ImageView image;
    @BindView(R.id.tv_speed)
    TextView tvSpeed;
    @BindView(R.id.mRecyclerView)
    RecyclerView mRecyclerView;

    private UserBean userBean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server);
        ButterKnife.bind(this);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        btnClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager imm =
                        (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(etContent.getWindowToken(), 0);

                String url = "";
                if (etContent.getText().toString().equals("")) {
//                    url = "http://192.168.1.108:2323/index.html";
                    url = "http://nc16237326.imwork.net:5858/cjl/user";
//                    url = "http://www.cjlgo.top:10190/index.html";
                } else {
//                    url = "http://192.168.1.108:1478/" + etContent.getText().toString();
                    url = "http://nc16237326.imwork.net:5858/cjl/user?limit=" + etContent.getText().toString();
//                    url = "http://www.cjlgo.top:10190/" + etContent.getText().toString();
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
        final IRequestManager requestManager = RequestFactory.getRequestManager();
        requestManager.get(url, new IRequestCallback() {
            @Override
            public void onSuccess(String response) {
                LogUtils.d("mtest", "onSuccess--" + response);
                StringBuilder builder = new StringBuilder();
                Gson gson = new Gson();
                userBean = gson.fromJson(response, UserBean.class);
                if (userBean != null) {
                    List<UserBean.ResultBean> result = userBean.getResult();
                    for (UserBean.ResultBean mResult : result) {
                        builder.append(mResult.toString());
                        builder.append("\n");
                    }
                }
//                tvSpeed.setText(builder.toString());
                initList();

                Glide.with(ServerActivity.this)
                        .load("http://nc16237326.imwork.net:8080/yang/zhou.png")
                        .asBitmap().into(image);
                ToastUtil.showToast(ServerActivity.this, "over!");

            }

            @Override
            public void onFailure(Throwable throwable) {
                throwable.printStackTrace();
                LogUtils.d("mtest", "onFailure--" + throwable.toString());
                tvSpeed.setText(throwable.toString());

                Glide.with(ServerActivity.this)
                        .load("http://nc16237326.imwork.net:8080/yang/zhou.png")
                        .asBitmap().into(image);
                ToastUtil.showToast(ServerActivity.this, "over!");
            }
        });
    }


    private void initList() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL, false));

        mRecyclerView.setAdapter(new BaseRecyclerAdapter<UserBean.ResultBean>(this,
                userBean.getResult(), R.layout.item_server) {
            @Override
            public void convert(BaseRecyclerHolder holder, UserBean.ResultBean item,
                                int position, boolean isScrolling) {
                holder.setText(R.id.tv_id, item.get_id());
                holder.setText(R.id.tv_name, item.getName());
                holder.setText(R.id.tv_sex, item.getSex());
                holder.setText(R.id.tv_age, item.getAge());
                holder.setText(R.id.tv_phone, item.getPhone());
                holder.setText(R.id.tv_job, item.getJob());
                holder.setText(R.id.tv_address, item.getAddress());
                holder.setText(R.id.tv_school, item.getSchool());
            }

        });

    }


}

package com.example.yang.test.activity;

import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.example.yang.test.R;
import com.example.yang.test.view.CustomTopBar;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class CustomViewActivity extends AppCompatActivity {

    @ViewInject(R.id.topbar)
    private CustomTopBar topbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom_view);
        ViewUtils.inject(this);

        topbar.setOnTopbarClickListener(new CustomTopBar.OnTopbarClickListener() {
            @Override
            public void onLeftClick() {
                finish();
            }

            @Override
            public void onRightClick() {
                Snackbar.make(topbar,"more",Snackbar.LENGTH_SHORT).show();
            }
        });
    }
}

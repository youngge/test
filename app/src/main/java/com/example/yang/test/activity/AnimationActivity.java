package com.example.yang.test.activity;

import android.animation.ObjectAnimator;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.yang.test.R;
import com.example.yang.test.baseactivity.BaseActivity;
import com.example.yang.test.util.ToastUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;

public class AnimationActivity extends BaseActivity implements View.OnClickListener {
    @ViewInject(R.id.image)
    private ImageView mImage;
    @ViewInject(R.id.btn_top)
    private Button btn_top;
    @ViewInject(R.id.btn_bottom)
    private Button btn_bottom;
    @ViewInject(R.id.btn_left)
    private Button btn_left;
    @ViewInject(R.id.btn_right)
    private Button btn_right;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animation);
        ViewUtils.inject(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        mImage.setOnClickListener(this);
        btn_top.setOnClickListener(this);
        btn_bottom.setOnClickListener(this);
        btn_left.setOnClickListener(this);
        btn_right.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image:
                ToastUtil.showToast(this, "image");
                break;

            case R.id.btn_top:
                ObjectAnimator animatorT = ObjectAnimator.ofFloat(mImage, "translationY", -300);
                animatorT.setDuration(1000);
                animatorT.start();
                break;

            case R.id.btn_bottom:
                ObjectAnimator animatorB = ObjectAnimator.ofFloat(mImage, "translationY", 300);
                animatorB.setDuration(1000);
                animatorB.start();
                break;

            case R.id.btn_left:
                ObjectAnimator animatorL = ObjectAnimator.ofFloat(mImage, "translationX", -300);
                animatorL.setDuration(1000);
                animatorL.start();
                break;

            case R.id.btn_right:
                ObjectAnimator animatorR = ObjectAnimator.ofFloat(mImage, "translationX", 300);
                animatorR.setDuration(1000);
                animatorR.start();
                break;
        }
    }

}

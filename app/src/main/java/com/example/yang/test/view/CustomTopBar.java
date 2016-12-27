package com.example.yang.test.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yang.test.R;
import com.example.yang.test.util.DisplayUtil;
import com.example.yang.test.util.LogUtils;

import static android.R.attr.padding;

/**
 * Created by Administrator on 2016/12/25.
 */

public class CustomTopBar extends RelativeLayout {
    private  String leftText;
    private  int leftTextColor;
    private Drawable leftBackground;
    private String title;
    private float titleSize;
    private int titleColor;
    private String rightText;
    private int rightTextColor;
    private Drawable rightBackground;

    private  Button btn_left;
    private Button btn_right;
    private TextView tv_title;

    private OnTopbarClickListener onTopbarClickListener;

    public interface OnTopbarClickListener{
        void onLeftClick();
        void onRightClick();
    }

    public CustomTopBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTopBar);
        leftText = typedArray.getString(R.styleable.CustomTopBar_leftText);
        leftTextColor = typedArray.getColor(R.styleable.CustomTopBar_leftTextColor, 0);
        leftBackground = typedArray.getDrawable(R.styleable.CustomTopBar_leftBackground);
        title = typedArray.getString(R.styleable.CustomTopBar_title);
        titleSize = typedArray.getDimension(R.styleable.CustomTopBar_titleSize, DisplayUtil.sp2px(context,18));
        titleColor = typedArray.getColor(R.styleable.CustomTopBar_titleColor, 0);
        rightText = typedArray.getString(R.styleable.CustomTopBar_rightText);
        rightTextColor = typedArray.getColor(R.styleable.CustomTopBar_rightTextColor, 0);
        rightBackground = typedArray.getDrawable(R.styleable.CustomTopBar_rightBackground);
        typedArray.recycle();

        //标题
        tv_title = new TextView(context);
        //左按钮
        btn_left = new Button(context);
        //右按钮
        btn_right = new Button(context);

        tv_title.setTextColor(titleColor);
        LogUtils.d("mtest",DisplayUtil.px2sp(context,titleSize)+"---"+titleSize);
        tv_title.setTextSize(DisplayUtil.px2sp(context,titleSize));
        tv_title.setText(title);
        btn_left.setBackground(leftBackground);
        btn_left.setTextColor(leftTextColor);
        btn_left.setText(leftText);
        btn_right.setText(rightText);
        btn_right.setBackground(rightBackground);
        btn_right.setTextColor(rightTextColor);

        LayoutParams leftLP = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        leftLP.addRule(RelativeLayout.ALIGN_PARENT_LEFT, TRUE);
        btn_left.setLayoutParams(leftLP);

        LayoutParams titleLP = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        titleLP.addRule(RelativeLayout.CENTER_IN_PARENT, TRUE);
        tv_title.setLayoutParams(titleLP);

        LayoutParams rightLP = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        rightLP.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,TRUE);
        btn_right.setLayoutParams(rightLP);

//        requestLayout();
        this.addView(btn_left);
        this.addView(tv_title);
        this.addView(btn_right);

        btn_left.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onTopbarClickListener.onLeftClick();
            }
        });

        btn_right.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                onTopbarClickListener.onRightClick();
            }
        });
    }

        public CustomTopBar(Context context){
            this(context,null);
        }

        public CustomTopBar(Context context, AttributeSet attrs) {
            this(context, attrs,0);
    }


    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed,l,t,r,b);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
}

    public void setOnTopbarClickListener(OnTopbarClickListener listener){
        this.onTopbarClickListener = listener;
    }

}

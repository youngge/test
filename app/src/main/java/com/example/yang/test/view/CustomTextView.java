package com.example.yang.test.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import com.example.yang.test.R;

/**
 * Created by Administrator on 2016/12/25.
 */

public class CustomTextView extends TextView {

    Paint paint01,paint02;

    public CustomTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        paint01 = new Paint();
        paint01.setColor(getResources().getColor(R.color.colorPrimary));
        paint01.setStyle(Paint.Style.FILL);
        paint02 = new Paint();
        paint02.setColor(getResources().getColor(R.color.colorAccent));
        paint02.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.save();
        canvas.drawRect(0,0,getMeasuredWidth(),getMeasuredHeight(),paint01);
        canvas.drawRect(10,10,getMeasuredWidth()-10,getMeasuredHeight()-10,paint02);
        canvas.save();
        canvas.translate(10,0);
        super.onDraw(canvas);
        canvas.restore();
    }
}

package com.example.yang.test.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Administrator on 2016/12/26.
 */

public class ClockView extends View {

    private int mWidth = 500;
    private int mHeight = 500;
    private int mRadius = 200;

    private Paint paintHour;
    private Paint paintMinues;

    public ClockView(Context context) {
        this(context, null);
    }

    public ClockView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ClockView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        paintHour = new Paint();
        paintHour.setStyle(Paint.Style.FILL);
        paintHour.setColor(Color.BLACK);
        paintHour.setAntiAlias(true);
        paintHour.setStrokeWidth(8);

        paintMinues = new Paint();
        paintMinues.setStyle(Paint.Style.FILL);
        paintMinues.setColor(Color.BLACK);
        paintMinues.setAntiAlias(true);
        paintMinues.setStrokeWidth(5);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int wSize = MeasureSpec.getSize(widthMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hSize = MeasureSpec.getSize(heightMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);

        if (wMode == MeasureSpec.EXACTLY) {
            mWidth = wSize;
        }

        if (hMode == MeasureSpec.EXACTLY) {
            mHeight = hSize;
        }

        mRadius = Math.min(mWidth,mHeight)/2-10;

        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画外圆
        drawCircle(canvas);
        //画刻度和数字
        darwScale(canvas);
        //画指针
        darwIndicator(canvas,3,10);

    }

    private void darwIndicator(Canvas canvas,int hour ,int minute) {
        canvas.save();
        canvas.translate(mWidth/2,mHeight/2);
        //计算时针的角度
        float hourDegree = hour/12.0f*360+minute/60.0f*(5/60.0f*360);
        canvas.rotate(hourDegree);
        canvas.drawLine(0,0,0,mRadius/3*2-mHeight/2,paintHour);
//        canvas.restore();
//        canvas.save();
        canvas.rotate(minute/60.0f*360-hourDegree);
        canvas.drawLine(0,0,0,mRadius/2-mHeight/2,paintMinues);
        canvas.restore();
    }

    private void darwScale(Canvas canvas) {
        canvas.save();
        Paint paintScale = new Paint();
        paintScale.setStyle(Paint.Style.FILL);
        paintScale.setColor(Color.BLACK);
        paintScale.setStrokeWidth(2);
        for (int i = 0; i < 60; i++) {
            if (i % 5 == 0) {
                paintScale.setStrokeWidth(3);
                paintScale.setColor(Color.BLACK);
                paintScale.setTextSize(15);
                canvas.drawLine(mWidth/2, mHeight/2 - mRadius, mWidth/2, mHeight/2 - mRadius + 20, paintScale);
                String num = String.valueOf(i/5);
                if ("0".equals(num)){
                    num = "12";
                }
                canvas.drawText(num,mWidth/2-paintScale.measureText(num)/2,mHeight/2-mRadius+40,paintScale);
            } else {
                paintScale.setStrokeWidth(2);
                paintScale.setColor(Color.GRAY);
                canvas.drawLine(mWidth/2, mHeight/2 - mRadius, mWidth/2, mHeight/2 - mRadius + 10, paintScale);
            }
            canvas.rotate(6, mWidth/2, mHeight/2);
        }
        canvas.restore();
    }

    private void drawCircle(Canvas canvas) {
        canvas.save();
        Paint paintCircle = new Paint();
        paintCircle.setStyle(Paint.Style.STROKE);
        paintCircle.setAntiAlias(true);
        paintCircle.setStrokeWidth(5);
        canvas.drawCircle(mWidth/2, mHeight/2, mRadius, paintCircle);
        canvas.restore();
    }
}

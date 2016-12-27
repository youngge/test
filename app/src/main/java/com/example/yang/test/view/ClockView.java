package com.example.yang.test.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by czy on 2016/12/26.
 */

public class ClockView extends View {

    private int mWidth = 500;
    private int mHeight = 500;
    private int mRadius = 200;

    private int hour = 0;
    private int minute= 0;
    private int second= 0;

    private Paint paintHour;
    private Paint paintMinute;
    private Paint paintSecond;

    private final static String AM="AM";
    private final static String PM="PM";

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

        paintMinute = new Paint();
        paintMinute.setStyle(Paint.Style.FILL);
        paintMinute.setColor(Color.BLACK);
        paintMinute.setAntiAlias(true);
        paintMinute.setStrokeWidth(5);

        paintSecond = new Paint();
        paintSecond.setStyle(Paint.Style.FILL);
        paintSecond.setColor(Color.RED);
        paintSecond.setAntiAlias(true);
        paintSecond.setStrokeWidth(3);
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
        drawScale(canvas);
        //画am或pm
        drawAMPM(canvas);
        //画指针
        drawIndicator(canvas,hour,minute,second);
        //画圆心
        drawPoint(canvas);
    }

    private void drawAMPM(Canvas canvas) {
        Paint paintAM= new Paint();
        paintAM.setColor(Color.BLACK);
        paintAM.setTextSize(30);
        canvas.save();
        if (hour<12) {
            canvas.drawText(AM,mWidth/2-paintAM.measureText(AM)/2,mHeight/2-mRadius/3*2,paintAM);
        }else{
            canvas.drawText(PM,mWidth/2-paintAM.measureText(PM)/2,mHeight/2-mRadius/3*2,paintAM);
        }
    }

    private void drawPoint(Canvas canvas) {
        canvas.save();
        Paint paintPoint = new Paint();
        paintPoint.setStyle(Paint.Style.FILL);
        paintPoint.setColor(Color.BLACK);
        canvas.drawCircle(mWidth/2,mHeight/2,10,paintPoint);
        canvas.restore();
    }

    private void drawIndicator(Canvas canvas,int hour ,int minute,int second) {
        canvas.save();
        canvas.translate(mWidth/2,mHeight/2);
        //计算秒针的角度
        float secondDegree = second / 60.0f*360;
        canvas.rotate(secondDegree);
        canvas.drawLine(0,0,0,mRadius/3-mHeight/2,paintSecond);
        //计算分针的角度
        float minuesDegree = minute/60.0f*360+second/60.0f*1/60.0f*360;
        canvas.rotate(minuesDegree-secondDegree);
        canvas.drawLine(0,0,0,mRadius/2-mHeight/2,paintMinute);
        //计算时针的角度
        float hourDegree = hour/12.0f*360+minute/60.0f*(5/60.0f*360);
        canvas.rotate(hourDegree-minuesDegree);
        canvas.drawLine(0,0,0,mRadius/3*2-mHeight/2,paintHour);
        canvas.restore();
    }

    private void drawScale(Canvas canvas) {
        canvas.save();
        Paint paintScale = new Paint();
        paintScale.setStyle(Paint.Style.FILL);
        paintScale.setColor(Color.BLACK);
        paintScale.setStrokeWidth(2);
        for (int i = 0; i < 60; i++) {
            if (i % 5 == 0) {
                paintScale.setStrokeWidth(3);
                paintScale.setColor(Color.BLACK);
                paintScale.setTextSize(25);
                canvas.drawLine(mWidth/2, mHeight/2 - mRadius, mWidth/2, mHeight/2 - mRadius + 30, paintScale);
                String num = String.valueOf(i/5);
                if ("0".equals(num)){
                    num = "12";
                }
                canvas.drawText(num,mWidth/2-paintScale.measureText(num)/2,mHeight/2-mRadius+50,paintScale);
            } else {
                paintScale.setStrokeWidth(2);
                paintScale.setColor(Color.GRAY);
                canvas.drawLine(mWidth/2, mHeight/2 - mRadius, mWidth/2, mHeight/2 - mRadius + 20, paintScale);
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

    public void updateTime(int hour,int minute,int second){
        this.hour=hour;
        this.minute=minute;
        this.second=second;
        invalidate();
    }
}

package com.example.yang.test.view;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.view.View;

import com.example.yang.test.R;

/**
 * 音乐播放按钮
 * Created by Administrator on 2016/12/3.
 */

public class MusicPlayView extends View {

    private Context context;
    //最大值
    private int maxNum;
    //起始角度
    private int startAngle;
    //扫过的角度
    private int sweepAngle;
    //外圆的宽度
    private int sweepOutWidth = 20;
    //画笔
    private Paint paint;
    private Paint paint_2;
    private Paint paint_3;
    private Paint paint_4;

    //宽度
    private int mWidth;
    //高度
    private int mHeight;
    //半径
    private int radius;
    //当前值
    private int currentNum;

    private int[] indicatorColor = {0xff456445,0xff456445,0xff456445};

    public MusicPlayView(Context context) {
        this(context, null);
    }

    public MusicPlayView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MusicPlayView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;

        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ZhifuView);
        maxNum = typedArray.getInt(R.styleable.ZhifuView_maxNum, 500);
        startAngle = typedArray.getInt(R.styleable.ZhifuView_startAngle, 160);
        sweepAngle = typedArray.getInt(R.styleable.ZhifuView_sweepAngle, 220);
        radius = typedArray.getInt(R.styleable.ZhifuView_radius, 100);
        typedArray.recycle();

        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setDither(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(0xffffffff);

        paint_2 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_3 = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint_4 = new Paint(Paint.ANTI_ALIAS_FLAG);

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
        } else {
            mWidth = dp2px(300);
        }

        if (hMode == MeasureSpec.EXACTLY) {
            mHeight = hSize;
        } else {
            mHeight = dp2px(400);
        }

        setMeasuredDimension(mWidth, mHeight);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
//        radius = getMeasuredWidth() / 4;
        canvas.save();
        canvas.translate(mWidth / 2, mWidth / 2);

        drawRound(canvas);
        drawIndicator(canvas);
    }

    /**
     * 画圆
     *
     * @param canvas
     */
    private void drawRound(Canvas canvas) {
        canvas.save();
        //外圆
        paint.setStrokeWidth(sweepOutWidth);
        int w = dp2px(10);
        RectF rectf2 = new RectF(-radius - w, -radius - w, radius + w, radius + w);
        canvas.drawArc(rectf2, startAngle, sweepAngle, false, paint);

        canvas.restore();
    }


    /**
     * 画指示器
     *
     * @param canvas
     */
    private void drawIndicator(Canvas canvas) {
        canvas.save();
        paint_2.setStyle(Paint.Style.STROKE);
        int sweep;
        if (currentNum <= maxNum) {
            sweep = (int) ((float) currentNum / (float) maxNum * sweepAngle);
        } else {
            sweep = sweepAngle;
        }
        paint_2.setStrokeWidth(sweepOutWidth);
        Shader shader = new SweepGradient(0, 0, indicatorColor, null);
        paint_2.setShader(shader);

        int w = dp2px(10);
        RectF rectf = new RectF(-radius - w, -radius - w, radius + w, radius + w);
        canvas.drawArc(rectf, startAngle, sweep, false, paint_2);

        float x = (float) ((radius + dp2px(10)) * Math.cos(Math.toRadians(startAngle + sweep)));
        float y = (float) ((radius + dp2px(10)) * Math.sin(Math.toRadians(startAngle + sweep)));
        paint_3.setStyle(Paint.Style.FILL);
        paint_3.setColor(0xff456445);
        paint_3.setMaskFilter(new BlurMaskFilter(dp2px(3), BlurMaskFilter.Blur.SOLID));
        canvas.drawCircle(x, y, dp2px(3), paint_3);

        canvas.restore();
    }


    private int dp2px(float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    public void setSweepAngle(int sweepAngle) {
        this.sweepAngle = sweepAngle;
        invalidate();
    }

    public void setMaxNum(int maxNum) {
        this.maxNum = maxNum;
        invalidate();
    }

    public void setCurrentNum(int currentNum) {
        this.currentNum = currentNum;
        invalidate();
    }

    /**
     * 设置数值并添加动画
     *
     * @param num
     */
    public void setCurrentAnim(int num) {
        float duration = (float) Math.abs(num - currentNum) / maxNum * 1500 + 500;
        final ObjectAnimator anim = ObjectAnimator.ofInt(this, "currentNum", num);
        anim.setDuration((long) Math.min(duration, 2000));
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int value = (int) animation.getAnimatedValue();
                int color = calculateColor(value);
                setBackgroundColor(color);
            }
        });
        anim.start();
    }

    /**
     * 获取颜色
     * @param value
     * @return
     */
    private int calculateColor(int value) {
        ArgbEvaluator argbEvaluator = new ArgbEvaluator();
        float fraction = 0;
        int color = 0;
        if (value <= maxNum / 2) {
            fraction = (float) value / (maxNum / 2);
            color = (int) argbEvaluator.evaluate(fraction, 0xffff6347, 0xffff8c00);
        } else {
            fraction = ((float) value - maxNum / 2) / (maxNum / 2);
            color = (int) argbEvaluator.evaluate(fraction, 0xffff8c00, 0xff00ced1);
        }
        return color;
    }

}











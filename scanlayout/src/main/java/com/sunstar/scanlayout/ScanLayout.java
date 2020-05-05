package com.sunstar.scanlayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Shader;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

/**
 * @Description: ${}<>
 * @author: 孙浩
 * @data: 2018/6/1 08:31
 * @version: V1.0
 */
public class ScanLayout extends FrameLayout {

    public static final int  ALWAYS = 4;
    public static final int  MANUAL = 5;
    View childView;
    float childTop, childLeft, childWidth, childHeight, childBottom, childRight;
    Paint linePaint, bgPaint,vPaint,hPaint,scanSqurePaint;
    private int bgColor, borderColor, timeSecond, scanDirection, scanMode,timeCont;
    private float distancePreSecond;
    private Handler handler = new Handler();
    private boolean isStart = true;
    private boolean shouldScanning;
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            switch (scanDirection) {
                case 0:
                    if (childLeft <= childRight) {
                        childLeft += distancePreSecond;
                    } else {
                        childLeft = childView.getLeft();
                    }

                    break;
                case 1:
                    if (childTop <= childBottom) {
                        childTop += distancePreSecond;
                    } else {
                        childTop = childView.getTop();
                    }
                    break;
                case 2:
                    if (childRight >= childLeft) {
                        childRight -= distancePreSecond;
                    } else {
                        childRight = childView.getRight();
                    }
                    break;
                case 3:
                    if (childBottom >= childTop) {
                        childBottom -= distancePreSecond;
                    } else {
                        childBottom = childView.getBottom();
                    }
                    break;
            }
            invalidate();
        }
    };

    public ScanLayout(@NonNull Context context) {
        this(context, null);
    }

    public ScanLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScanLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScanLayout);
        initProperty(typedArray);
    }

    private void initProperty(TypedArray typedArray) {
        bgColor = typedArray.getColor(R.styleable.ScanLayout_bgColor, Color.parseColor("#4D000000"));
        borderColor = typedArray.getColor(R.styleable.ScanLayout_borderColor, Color.GREEN);
        timeSecond = typedArray.getInt(R.styleable.ScanLayout_timecount, 1);
        scanDirection = typedArray.getInteger(R.styleable.ScanLayout_scanDirection, 1);
        scanMode = typedArray.getInteger(R.styleable.ScanLayout_scan_mode, 1);
        if(scanMode == ALWAYS){
            shouldScanning = true;
        } else {
            shouldScanning = false;
        }
        typedArray.recycle();
    }


    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() > 1) {
            throw new IllegalStateException("扫描控件中只允许有一个子控件");
        }
        childView = getChildAt(0);
        initPaint();
    }


    private void initPaint() {
        bgPaint = new Paint();
        linePaint = new Paint();
        vPaint = new Paint();
        hPaint = new Paint();
        linePaint.setColor(borderColor);
        vPaint.setColor(borderColor);
        hPaint.setColor(borderColor);
        linePaint.setStrokeWidth(4);
        bgPaint.setColor(bgColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {

        if(childHeight == 0){
            childTop = child.getTop();
            childLeft = child.getLeft();
            childBottom = child.getBottom();
            childRight = child.getRight();
            childWidth = child.getWidth();
            childHeight = child.getHeight();
            initDistance();
        }

        Path myPath = new Path();
        initBgPath(myPath,child);
        canvas.drawPath(myPath, bgPaint);
        if(scanMode != ALWAYS && !shouldScanning){
            return super.drawChild(canvas, child, drawingTime);
        }
        switch (scanDirection) {
            case 0:
                canvas.drawLine(childLeft, childTop, childLeft, childBottom, linePaint);

                for (float i = child.getLeft()+30; i < childLeft; i+=30) {
                    vPaint.setAlpha(100);
                    canvas.drawLine(i, childTop, i, childBottom, vPaint);
                }
                for (float i = child.getTop()+30; i < childBottom; i+=30) {
                    hPaint.setAlpha(100);
                    canvas.drawLine(child.getLeft(), i, childLeft, i, hPaint);
                }

                break;
            case 1:
                canvas.drawLine(childLeft, childTop, childRight, childTop, linePaint);
                for (float i = child.getLeft()+30; i < child.getRight(); i+=30) {
                    vPaint.setAlpha(100);
                    canvas.drawLine(i, childTop, i, child.getTop(), vPaint);
                }
                for (float i = child.getTop()+30; i < childTop; i+=30) {
                    hPaint.setAlpha(100);
                    canvas.drawLine(childLeft, i, child.getRight(), i, hPaint);
                }
                break;
            case 2:
                canvas.drawLine(childRight, childTop, childRight, childBottom, linePaint);
                for (float i = childRight+30; i < child.getRight(); i+=30) {
                    vPaint.setAlpha(100);
                    canvas.drawLine(i, childTop, i, childBottom, vPaint);
                }
                for (float i = childTop+30; i < child.getBottom(); i+=30) {
                    hPaint.setAlpha(100);
                    canvas.drawLine(childRight, i, child.getRight(), i, hPaint);
                }
                break;
//            case 3:
//                canvas.drawLine(childLeft, childBottom, childRight, childBottom, linePaint);
//
//                for (float i = child.getLeft()+30; i < child.getRight(); i+=30) {
//                    vPaint.setAlpha(100);
//                    canvas.drawLine(i, childBottom, i, child.getBottom(), vPaint);
//                }
//                for (float i = childBottom+30; i < childView.getBottom(); i+=30) {
//                    hPaint.setAlpha(100);
//                    canvas.drawLine(childLeft, i, childRight,i, hPaint);
//                }
//                break;
        }
        handler.postDelayed(runnable, 10);
        return super.drawChild(canvas, child, drawingTime);
    }

    public void startScan(){
        if(!shouldScanning && scanMode == MANUAL){
            shouldScanning = true;
            handler.postDelayed(runnable, 10);
        }
    }

    public void stopScan(){
        if(shouldScanning && scanMode == MANUAL){
            shouldScanning = false;
            invalidate();
            handler.removeCallbacksAndMessages(null);
        }
    }

    private void initDistance() {
        timeCont = timeSecond * 1000;
        int scanTimes = timeCont / 10;
        switch (scanDirection) {
            case 0:
                distancePreSecond = childWidth / scanTimes;
                break;
            case 1:
                distancePreSecond = childHeight / scanTimes;
                break;
            case 2:
                distancePreSecond = childWidth / scanTimes;
                break;
            case 3:
                distancePreSecond = childHeight / scanTimes;
                break;
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
    }

    private void initBgPath(Path myPath,View childView) {
        myPath.moveTo(getTop(), 0);
        myPath.lineTo(childView.getLeft(), 0);
        myPath.lineTo(childView.getLeft(), childView.getBottom());
        myPath.lineTo(childView.getRight(), childView.getBottom());
        myPath.lineTo(childView.getRight(), childView.getTop());
        myPath.lineTo(childView.getLeft(), childView.getTop());
        myPath.lineTo(childView.getLeft(), getTop());
        myPath.lineTo(getRight(), getTop());
        myPath.lineTo(getRight(), getBottom());
        myPath.lineTo(getLeft(), getBottom());
        myPath.close();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }
}

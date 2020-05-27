package com.king.superdemo.custom;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.BatteryManager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.king.superdemo.R;

/**
 * Created by wangqiang on 2017/11/28.
 */

public class BatteryChargingView extends View {

    public static final String TAG = "wq";

    private boolean isCharging;
    private boolean isDrawing = false;
    private int centerX = 0;
    private int centerY = 0;
    private int mPowerLevel = 0;
    private int mAutoIncrementWidth = 0;

    private static int INTERVAL = 1000;
    private static int WIDTH_STROKE = 2;
    private static int WIDTH_BATTERY_HEAD = 2;
    private static int WIDTH_BATTERY_EDGE = 2;
    private static int WIDTH_INCREASMENT = 1;


    private int mBatteryWidth = 0;
    private int mBatteryHeight = 0;

    //exclude battery head, battery edge.
    private int mDrawBatteryWidth = 0;

    private Context mContext;
    private Paint  mChargingPaint;
    private Rect mBatteryVolume;
    private Bitmap mBackgroundBitmap;

    private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int batterLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
            int batteryMax = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100);
            int batteryStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0);
            int power = batterLevel * 100 / batteryMax;

            if (batteryStatus == BatteryManager.BATTERY_STATUS_CHARGING) {
                setCharging(true);
                setPowerLevel(batterLevel);
            } else {
                setCharging(false);
                setPowerLevel(batterLevel);
            }

        }
    };

    public BatteryChargingView(Context context) {
        this(context, null);
    }

    public BatteryChargingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public BatteryChargingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public BatteryChargingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mContext = context;
        mBackgroundBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.battery_bg);
        mBatteryWidth = mBackgroundBitmap.getWidth();
        mBatteryHeight = mBackgroundBitmap.getHeight();
        mDrawBatteryWidth = mBatteryWidth - 2 * WIDTH_BATTERY_EDGE - WIDTH_BATTERY_HEAD;
        Log.v("wq", "backgroundWidth="+  mBackgroundBitmap.getWidth() + " ,backgroundHeight="+ mBackgroundBitmap.getHeight() +
                " ,batteryWidth="+ mBatteryWidth + " ,batteryHeight="+ mBatteryHeight);
        initPaint();
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED);
        mContext.registerReceiver(batteryReceiver, intentFilter);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        mContext.unregisterReceiver(batteryReceiver);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int measuredWitth = getMeasuredWidth() / 2;
        int measuredHeight = getMeasuredHeight() / 2;
        Log.v("wq", "onSizeChanged measuredWitth="+ measuredWitth + " ,measuredHeight="+ measuredHeight);
        mBatteryVolume = new Rect();
        mBatteryVolume.left = WIDTH_BATTERY_EDGE + WIDTH_BATTERY_HEAD ;
        mBatteryVolume.top = WIDTH_BATTERY_EDGE ;
        mBatteryVolume.bottom = mBatteryHeight - WIDTH_BATTERY_EDGE;

    }

    private void initPaint() {
        mChargingPaint = new Paint();
        mChargingPaint.setColor(Color.GREEN);
        mChargingPaint.setAntiAlias(true);
        mChargingPaint.setStrokeWidth(WIDTH_STROKE);
        mChargingPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawBitmap(mBackgroundBitmap, new Matrix(), new Paint());

        float powerPercent = mPowerLevel / 100.0f;
        mBatteryVolume.right = mBatteryVolume.left + getDynamicBatteryVolume(powerPercent * mDrawBatteryWidth);
        canvas.drawRect(mBatteryVolume, mChargingPaint);
    }

    private int getDynamicBatteryVolume(float width) {
        if (isCharging) {
            mAutoIncrementWidth += WIDTH_INCREASMENT;
        } else {
            mAutoIncrementWidth = 0;
        }
        //when increment more than width, then again from zero.
        if (mAutoIncrementWidth >= (mDrawBatteryWidth - width)) {
            mAutoIncrementWidth = 0;
        }
        return (int) (width + mAutoIncrementWidth);
    }

    public void setCharging(boolean charging) {
        this.isCharging = charging;
    }

    public void setPowerLevel(int powerLevel) {
        if (powerLevel < 0) mPowerLevel  = 0;
        if (powerLevel > 100) mPowerLevel = 100;
        mPowerLevel = powerLevel;
        if (isCharging) {
            //if has no drawing, draw it.
            if (!isDrawing) {
                drawBatteryCharging();
                isDrawing = true;
            }
        } else {
            invalidate();
            isDrawing = false;
        }
    }

    private void drawBatteryCharging() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                postInvalidate();
                drawBatteryCharging();
            }
        }, INTERVAL);
    }
}


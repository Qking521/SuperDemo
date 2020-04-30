package com.king.superdemo.custom;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;

/**
 * reference https://blog.csdn.net/dongzhong1990/article/details/80512706
 */
public class FloatView extends AppCompatImageView {

    WindowManager mWindowManager;
    WindowManager.LayoutParams layoutParams;
    float mStartX;
    float mStartY;
    float mTouchX;
    float mTouchY;
    int mParamsX = 0;
    int mParamsY = 0;

    private Context mContext;
    private OnClickListener mClickListener;
    private Toast mToast;

    public FloatView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        initWindowManager();
        registerClickListener();
    }

    private void registerClickListener() {
        mClickListener = new OnClickListener() {
            @Override
            public void onClick(View v) {
                Bitmap b = screenShot();

            }
        };
        setOnClickListener(mClickListener);
    }

    private Bitmap screenShot() {

        return null;
    }

    private void initWindowManager() {
        layoutParams = new WindowManager.LayoutParams();
        layoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.x = 0;
        layoutParams.y = 0;
        mWindowManager.addView(this, layoutParams);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        mTouchX = event.getRawX();
        mTouchY = event.getRawY();

        int motion = event.getAction();
        switch (motion) {
            case MotionEvent.ACTION_DOWN:
                mStartX = event.getRawX();
                mStartY = event.getRawY();
                mParamsX = layoutParams.x;
                mParamsY = layoutParams.y;
                break;
            case MotionEvent.ACTION_MOVE:
                updateWindow();
                break;
            case MotionEvent.ACTION_UP:
                if (needUpdateWindow()) {
                    updateWindow();
                } else {
                    performClick();
                }
                break;
        }
        return true;
    }

    private boolean needUpdateWindow() {
        int dx = (int) (mTouchX - mStartX);
        int dy = (int) (mTouchY - mStartY);
        return (Math.abs(dx) > 5 && Math.abs(dy) > 5);
    }

    private void updateWindow() {
        layoutParams.x = mParamsX + (int) (mTouchX - mStartX);
        layoutParams.y = mParamsY + (int) (mTouchY - mStartY);
        mWindowManager.updateViewLayout(this, layoutParams);
    }

    public void finish() {
        mWindowManager.removeView(this);
    }
}
package com.king.superdemo;

import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

/** 相当于一个标识,所有继承此类的Activity会自动在入口MainActivity展示
 * reference: https://blog.csdn.net/c10WTiybQ1Ye3/article/details/78098763
 * */
public class BaseActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int uiOption = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // content布局占据全屏,但是状态栏会显示在界面上层
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION ; // content布局占据全屏,但是导航栏会显示在界面上层
        decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | uiOption);
        setStatusBar(getActionbarBg(), false);
    }


    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
        //setFitsSystemWindows 这个属性的作用就是通过设置View的padding值来填充system window的位置，所以padding就是system window的高度
        ViewGroup rootView = (ViewGroup) ((ViewGroup) findViewById(android.R.id.content)).getChildAt(0);
        rootView.setFitsSystemWindows(true);
    }

    public void hideActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

    public void setStatusBarTransparent(boolean light) {
        setStatusBar(Color.TRANSPARENT, light);
    }

    /**
     * @param color
     * @param light //设置透明状态栏,如果设置后底色是白色看不清状态栏,需要设置light模式
     */
    public void setStatusBar(int color, boolean light) {
        getWindow().setStatusBarColor(color);
        if (light) {
            int uiOption = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(decorView.getSystemUiVisibility() | uiOption);
        }
    }

    private int getActionbarBg() {
        int[] actionbarAttr = new int[]{android.R.attr.background};
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.actionBarStyle, typedValue, true);
        TypedArray ta = getTheme().obtainStyledAttributes(typedValue.resourceId, actionbarAttr);
        int backaground;
        try {
            backaground = ta.getColor(0, Color.RED);
        } catch (Exception e) {
            backaground = Color.RED;
        }
        return backaground;
    }
}

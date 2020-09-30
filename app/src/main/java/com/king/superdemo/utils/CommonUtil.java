package com.king.superdemo.utils;

import android.content.Context;

public class CommonUtil {

    public static int getStatusBarHeight(Context context) {
        int statusBarHeight = 0;
        int resID = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resID > 0)
            statusBarHeight = context.getResources().getDimensionPixelSize(resID);
        return statusBarHeight;
    }
}

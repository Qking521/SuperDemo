package com.king.superdemo.utils

import android.content.Context

object CommonUtil {
    fun getStatusBarHeight(context: Context): Int {
        var statusBarHeight = 0
        val resID = context.resources.getIdentifier("status_bar_height", "dimen", "android")
        if (resID > 0) statusBarHeight = context.resources.getDimensionPixelSize(resID)
        return statusBarHeight
    }
}
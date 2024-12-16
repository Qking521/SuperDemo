package com.king.superdemo.extension

import android.R
import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.util.TypedValue

//获取actionbar的背景色
fun getActionBarBackground(context: Context, defaultColor: Int = Color.WHITE): Int {
    assert(context is Activity){
        "Context must be Activity"
    }
    val typedValue = TypedValue()
    context.theme.resolveAttribute(R.attr.actionBarStyle, typedValue, true)
    val ta = context.theme.obtainStyledAttributes(typedValue.resourceId, intArrayOf(R.attr.background))
    val background = try {
        ta.getColor(0, defaultColor)
    } catch (e: Exception) {
        defaultColor
    }finally {
        ta.recycle()
    }
    return background
}
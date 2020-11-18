package com.king.superdemo.kotlin

import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.king.superdemo.activities.BaseActivity

/**
 * 各种类的扩展函数集合
 */
fun View.toast(msg: String) {
    Toast.makeText(context, msg, Toast.LENGTH_LONG).show()
}
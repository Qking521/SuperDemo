package com.king.superdemo.utils

import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.Toast
import com.king.permission.PermissionBean
import com.king.permission.PermissionUtil

/**
 * 各种类的扩展函数集合
 */
fun View.toast(msg: String) = Toast.makeText(context, msg, Toast.LENGTH_LONG).show()

fun Activity.toast(msg: String, length: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, msg, length).show()
fun Activity.requestPermission(activity: Activity, vararg permissionBeans: PermissionBean) = PermissionUtil.requestPermission(this, *permissionBeans)

fun logThread(tag: String) = Log.v("tag", "logThread: name="+ Thread.currentThread().name)
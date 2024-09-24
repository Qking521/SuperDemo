package com.king.superdemo.extension

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.util.Log
import android.view.View
import android.widget.Toast
import com.king.permission.PermissionBean
import com.king.permission.PermissionUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay

fun logThreadName(tag: String = "wq") = Log.v(tag, "logThread: name="+ Thread.currentThread().name)

fun Context.registerBroadcastExt(intentFilter: IntentFilter, block: (intent: Intent?) -> Unit) {
    this.registerReceiver(object : BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            block.invoke(intent)
        }
    }, intentFilter)
}

/**
 * 各种类的扩展函数集合
 */
fun View.toast(msg: String) = Toast.makeText(context, msg, Toast.LENGTH_LONG).show()

fun Activity.toast(msg: String, length: Int = Toast.LENGTH_SHORT) = Toast.makeText(this, msg, length).show()
fun Activity.requestPermission(vararg permissionBeans: PermissionBean) = PermissionUtil.requestPermission(this, *permissionBeans)


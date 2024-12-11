package com.king.superdemo.extension

import android.util.Log
import org.json.JSONObject

private const val DEFAULT_TAG = "wq"
private const val DEBUG = true

// 2. 扩展函数使用顶层函数
fun Any.logD(message: String, tag: String = DEFAULT_TAG) {
    if (DEBUG) Log.d(tag, "$this: $message")
}

fun Any.logE(message: String, tag: String = DEFAULT_TAG) {
    if (DEBUG) Log.e(tag, "$this: $message")
}

// 记录方法调用耗时
fun Any.logTime(block: () -> Unit, tag: String = DEFAULT_TAG) {
    val startTime = System.currentTimeMillis()
    block()
    val endTime = System.currentTimeMillis()
    Log.d(tag, "$this: 执行耗时: ${endTime - startTime}ms")
}

// 记录JSON格式数据
fun Any.logJson(json: String, tag: String = DEFAULT_TAG) {
    if (DEBUG) {
        try {
            val jsonObject = JSONObject(json)
            Log.d(tag, "$this: ${jsonObject.toString(4)}")
        } catch (e: Exception) {
            Log.e(tag, "$this: Invalid JSON format", e)
        }
    }
}

// 记录列表数据
fun <T> Any.logList(list: List<T>, tag: String = DEFAULT_TAG) {
    if (DEBUG) {
        val sb = StringBuilder()
        list.forEachIndexed { index, item ->
            sb.append("[$index]: $item\n")
        }
        Log.d(tag, "$this: List内容:\n$sb")
    }
}
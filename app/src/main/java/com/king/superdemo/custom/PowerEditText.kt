package com.king.superdemo.custom

import android.content.Context
import android.util.AttributeSet
import android.util.Log
import android.util.Range
import androidx.appcompat.widget.AppCompatEditText;
import com.king.superdemo.extension.toast
import java.util.regex.Pattern


open class PowerEditText : AppCompatEditText {

    val EMAIL_REG = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
    val MOBILE_REG = "^((13[0-9])|(14[5,7,9])|(15[^4])|(18[0-9])|(17[0,1,3,5,6,7,8]))\\d{8}$"

    init {
        Log.i("wq", "init: ");
    }

    constructor(context: Context) : super(context){
        Log.i("wq", "constructor:1 ");
    }

    constructor(context: Context, attributes: AttributeSet) : super(context, attributes){

    }

    /**
     * 检查输入的字符串是否超过限制
     * @param string 需要check的字符串
     * @param custom 优先回调自定义函数, 不传函数参数时,默认为null
     * @param minLength 最小限制值
     * @param maxLength 最大限制值
     */
    fun checkLengthLimit(string: String, custom : () -> Unit = { null }, minLength : Int = 6, maxLength : Int = 12){
        if (string.length !in Range<Int>(minLength, maxLength)) {
            custom?.invoke();
        }
    }

    /**
     * 检查输入的字符串是否超过限制
     * @param string 需要check的字符串
     * @param custom 当有自定义的操作,优先使用自定义操作
     */
    fun checkUppercase(string: String, custom : () -> Unit = { null }) {
        if (string.toCharArray().any { it.isUpperCase() }) {
            custom?.invoke()
        }
    }

    fun checkEmailFormat(string: String) {
        if (Pattern.compile(EMAIL_REG).matcher(string).matches()) {
            toast("email format is unavaible")
        }
    }

    fun checkMobileFormat(string: String) {
        if (Pattern.compile(MOBILE_REG).matcher(string).matches()) {
            toast("mobile format is unavaible")
        }
    }
}
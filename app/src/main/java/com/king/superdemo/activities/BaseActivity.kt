package com.king.superdemo.activities

import android.R
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceScreen

/** 相当于一个标识,所有继承此类的Activity会自动在入口MainActivity展示
 * reference: https://blog.csdn.net/c10WTiybQ1Ye3/article/details/78098763
 */
open class BaseActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val decorView = window.decorView
        val uiOption = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // content布局占据全屏,但是状态栏会显示在界面上层
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION) // content布局占据全屏,但是导航栏会显示在界面上层
        decorView.systemUiVisibility = decorView.systemUiVisibility or uiOption
        setStatusBar(actionbarBg, false)

    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        //setFitsSystemWindows 这个属性的作用就是通过设置View的padding值来填充system window的位置，所以padding就是system window的高度
        val rootView = (findViewById<View>(R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
        rootView.fitsSystemWindows = true
    }

    fun hideActionBar() {
        val actionBar = supportActionBar
        actionBar?.hide()
    }

    fun setStatusBarTransparent(light: Boolean) {
        setStatusBar(Color.TRANSPARENT, light)
    }

    /**
     * @param color
     * @param light //设置透明状态栏,如果设置后底色是白色看不清状态栏,需要设置light模式
     */
    fun setStatusBar(color: Int, light: Boolean) {
        window.statusBarColor = color
        if (light) {
            val uiOption = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            val decorView = window.decorView
            decorView.systemUiVisibility = decorView.systemUiVisibility or uiOption
        }
    }

    //获取ActionBar的背景色
    private val actionbarBg: Int
        private get() {
            val actionbarAttr = intArrayOf(R.attr.background)
            val typedValue = TypedValue()
            theme.resolveAttribute(R.attr.actionBarStyle, typedValue, true)
            val ta = theme.obtainStyledAttributes(typedValue.resourceId, actionbarAttr)
            val backaground = try {
                ta.getColor(0, Color.RED)
            } catch (e: Exception) {
                Color.RED
            }
            return backaground
        }
}
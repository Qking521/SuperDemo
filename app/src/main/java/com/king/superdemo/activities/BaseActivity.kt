package com.king.superdemo.activities

import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceScreen
import com.king.superdemo.extension.getActionBarBackground

/** 相当于一个标识,所有继承此类的Activity会自动在入口MainActivity展示
 * reference: https://blog.csdn.net/c10WTiybQ1Ye3/article/details/78098763
 */
open class BaseActivity : AppCompatActivity() {

    data class ActivityConfig (
        val useCommonColor: Boolean = true, //actionbar and statusbar use common color
        val hideActionBar: Boolean = false,
        val panoramaMode: Boolean = false) //一般用于全屏显示图片的界面,使得状态栏和导航栏显示在应用界面上面，达到全景效果

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        configActivity(ActivityConfig())
    }

    private fun configActivity(activityConfig: ActivityConfig) {
        with(activityConfig) {
            if (useCommonColor) setStatusBar(getActionBarBackground(this@BaseActivity))
            if (hideActionBar) hideActionBar()
            if (panoramaMode) panoramaMode()
        }
    }

    override fun setContentView(layoutResID: Int) {
        super.setContentView(layoutResID)
        //setFitsSystemWindows 这个属性的作用就是通过设置View的padding值来填充system window的位置，所以padding就是system window的高度
        val rootView = (findViewById<View>(android.R.id.content) as ViewGroup).getChildAt(0) as ViewGroup
        rootView.fitsSystemWindows = true
    }

    //全景效果模式，一般用于activity整个界面显示整张图片
    fun panoramaMode() {
        val decorView = window.decorView
        val uiOption = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN // content布局占据全屏,状态栏显示在应用界面上层
                or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION) // content布局占据全屏,导航栏显示在应用界面上层
        decorView.systemUiVisibility = decorView.systemUiVisibility or uiOption
    }

    fun hideActionBar() {
        val actionBar = supportActionBar
        actionBar?.hide()
    }

    /**
     * @param color
     */
    fun setStatusBar(color: Int) {
        window.statusBarColor = color
        if (color == Color.WHITE) {
            setLightStatusBar()
        }
    }

    //如果设置后底色是白色看不清状态栏,需要设置light模式-黑底白字
    private fun setLightStatusBar() {
        val uiOption = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        val decorView = window.decorView
        decorView.systemUiVisibility = decorView.systemUiVisibility or uiOption
    }
}
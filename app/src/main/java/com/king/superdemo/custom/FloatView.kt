package com.king.superdemo.custom

import android.content.Context
import android.graphics.Bitmap
import android.graphics.PixelFormat
import android.util.AttributeSet
import android.view.Gravity
import android.view.MotionEvent
import android.view.View.OnClickListener
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.widget.AppCompatImageView

/**
 * reference https://blog.csdn.net/dongzhong1990/article/details/80512706
 */
class FloatView(private val mContext: Context, attrs: AttributeSet?) : AppCompatImageView(mContext, attrs) {
    var mWindowManager: WindowManager
    var layoutParams: WindowManager.LayoutParams? = null
    var mStartX = 0f
    var mStartY = 0f
    var mTouchX = 0f
    var mTouchY = 0f
    var mParamsX = 0
    var mParamsY = 0
    private var mClickListener: OnClickListener? = null
    private val mToast: Toast? = null
    private fun registerClickListener() {
        mClickListener = OnClickListener { val b = screenShot() }
        setOnClickListener(mClickListener)
    }

    private fun screenShot(): Bitmap? {
        return null
    }

    private fun initWindowManager() {
        layoutParams = WindowManager.LayoutParams()
        layoutParams!!.width = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams!!.height = WindowManager.LayoutParams.WRAP_CONTENT
        layoutParams!!.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
        layoutParams!!.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
        layoutParams!!.gravity = Gravity.LEFT or Gravity.TOP
        layoutParams!!.format = PixelFormat.RGBA_8888
        layoutParams!!.x = 0
        layoutParams!!.y = 0
        mWindowManager.addView(this, layoutParams)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        mTouchX = event.rawX
        mTouchY = event.rawY
        val motion = event.action
        when (motion) {
            MotionEvent.ACTION_DOWN -> {
                mStartX = event.rawX
                mStartY = event.rawY
                mParamsX = layoutParams!!.x
                mParamsY = layoutParams!!.y
            }
            MotionEvent.ACTION_MOVE -> updateWindow()
            MotionEvent.ACTION_UP -> if (needUpdateWindow()) {
                updateWindow()
            } else {
                performClick()
            }
        }
        return true
    }

    private fun needUpdateWindow(): Boolean {
        val dx = (mTouchX - mStartX).toInt()
        val dy = (mTouchY - mStartY).toInt()
        return Math.abs(dx) > 5 && Math.abs(dy) > 5
    }

    private fun updateWindow() {
        layoutParams!!.x = mParamsX + (mTouchX - mStartX).toInt()
        layoutParams!!.y = mParamsY + (mTouchY - mStartY).toInt()
        mWindowManager.updateViewLayout(this, layoutParams)
    }

    fun finish() {
        mWindowManager.removeView(this)
    }

    init {
        mWindowManager = mContext.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        initWindowManager()
        registerClickListener()
    }
}
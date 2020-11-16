package com.king.superdemo.custom

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.*
import android.os.BatteryManager
import android.util.AttributeSet
import android.util.Log
import android.view.View
import com.king.superdemo.R

/**
 * Created by wangqiang on 2017/11/28.
 */
class BatteryChargingView @JvmOverloads constructor(private val mContext: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0, defStyleRes: Int = 0) : View(mContext, attrs, defStyleAttr, defStyleRes) {
    private var isCharging = false
    private var isDrawing = false
    private val centerX = 0
    private val centerY = 0
    private var mPowerLevel = 0
    private var mAutoIncrementWidth = 0
    private var mBatteryWidth = 0
    private var mBatteryHeight = 0

    //exclude battery head, battery edge.
    private var mDrawBatteryWidth = 0
    private var mChargingPaint: Paint? = null
    private var mBatteryVolume: Rect? = null
    private val mBackgroundBitmap: Bitmap
    private val batteryReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val batterLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0)
            val batteryMax = intent.getIntExtra(BatteryManager.EXTRA_SCALE, 100)
            val batteryStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, 0)
            val power = batterLevel * 100 / batteryMax
            if (batteryStatus == BatteryManager.BATTERY_STATUS_CHARGING) {
                setCharging(true)
                setPowerLevel(batterLevel)
            } else {
                setCharging(false)
                setPowerLevel(batterLevel)
            }
        }
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        val intentFilter = IntentFilter()
        intentFilter.addAction(Intent.ACTION_BATTERY_CHANGED)
        mContext.registerReceiver(batteryReceiver, intentFilter)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mContext.unregisterReceiver(batteryReceiver)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        val measuredWitth = measuredWidth / 2
        val measuredHeight = measuredHeight / 2
        Log.v("wq", "onSizeChanged measuredWitth=$measuredWitth ,measuredHeight=$measuredHeight")
        mBatteryVolume = Rect()
        mBatteryVolume!!.left = WIDTH_BATTERY_EDGE + WIDTH_BATTERY_HEAD
        mBatteryVolume!!.top = WIDTH_BATTERY_EDGE
        mBatteryVolume!!.bottom = mBatteryHeight - WIDTH_BATTERY_EDGE
    }

    private fun initPaint() {
        mChargingPaint = Paint()
        mChargingPaint!!.color = Color.GREEN
        mChargingPaint!!.isAntiAlias = true
        mChargingPaint!!.strokeWidth = WIDTH_STROKE.toFloat()
        mChargingPaint!!.style = Paint.Style.FILL
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(mBackgroundBitmap, Matrix(), Paint())
        val powerPercent = mPowerLevel / 100.0f
        mBatteryVolume!!.right = mBatteryVolume!!.left + getDynamicBatteryVolume(powerPercent * mDrawBatteryWidth)
        canvas.drawRect(mBatteryVolume as Rect, mChargingPaint as Paint)
    }

    private fun getDynamicBatteryVolume(width: Float): Int {
        if (isCharging) {
            mAutoIncrementWidth += WIDTH_INCREASMENT
        } else {
            mAutoIncrementWidth = 0
        }
        //when increment more than width, then again from zero.
        if (mAutoIncrementWidth >= mDrawBatteryWidth - width) {
            mAutoIncrementWidth = 0
        }
        return (width + mAutoIncrementWidth).toInt()
    }

    fun setCharging(charging: Boolean) {
        isCharging = charging
    }

    fun setPowerLevel(powerLevel: Int) {
        if (powerLevel < 0) mPowerLevel = 0
        if (powerLevel > 100) mPowerLevel = 100
        mPowerLevel = powerLevel
        if (isCharging) {
            //if has no drawing, draw it.
            if (!isDrawing) {
                drawBatteryCharging()
                isDrawing = true
            }
        } else {
            invalidate()
            isDrawing = false
        }
    }

    private fun drawBatteryCharging() {
        postDelayed({
            postInvalidate()
            drawBatteryCharging()
        }, INTERVAL.toLong())
    }

    companion object {
        const val TAG = "wq"
        private const val INTERVAL = 1000
        private const val WIDTH_STROKE = 2
        private const val WIDTH_BATTERY_HEAD = 2
        private const val WIDTH_BATTERY_EDGE = 2
        private const val WIDTH_INCREASMENT = 1
    }

    init {
        mBackgroundBitmap = BitmapFactory.decodeResource(mContext.resources, R.drawable.battery_bg)
        mBatteryWidth = mBackgroundBitmap.width
        mBatteryHeight = mBackgroundBitmap.height
        mDrawBatteryWidth = mBatteryWidth - 2 * WIDTH_BATTERY_EDGE - WIDTH_BATTERY_HEAD
        Log.v("wq", "backgroundWidth=" + mBackgroundBitmap.width + " ,backgroundHeight=" + mBackgroundBitmap.height +
                " ,batteryWidth=" + mBatteryWidth + " ,batteryHeight=" + mBatteryHeight)
        initPaint()
    }
}

package com.king.superdemo.activities

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import com.king.superdemo.R

class VibrateActivity : BaseActivity() {

    val VIBRATE_TIME:Long = 200


    lateinit var vibrator: Vibrator
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_vibrate)
        vibrator = getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    }


    fun vibrateLegacyDefault(view: View) {
        //震动指定时间 ，数据类型long，单位为毫秒，一毫秒为1/1000秒
        vibrator.vibrate(VIBRATE_TIME)
    }

    fun vibrateLegacyMode(view: View) {
        //第一个参数为等待指定时间后开始震动，震动时间为第二个参数。后边的参数依次为等待震动和震动的时
        //第二个参数为重复次数，-1为不重复，0为一直震动
        vibrator.vibrate(longArrayOf(100,10,100,1000), -1)
    }

    fun vibrateDefaultAmplitude(view: View) {
        vibrator.vibrate(VibrationEffect.createOneShot(VIBRATE_TIME, VibrationEffect.DEFAULT_AMPLITUDE))
    }

    fun vibrateEffectTick(view: View) {
        vibrator.vibrate(VibrationEffect.createOneShot(VIBRATE_TIME, VibrationEffect.EFFECT_TICK))
    }

    fun vibrateEffectClick(view: View) {
        vibrator.vibrate(VibrationEffect.createOneShot(VIBRATE_TIME, VibrationEffect.EFFECT_CLICK))
    }

    fun vibrateEffectDoubleClick(view: View) {
        vibrator.vibrate(VibrationEffect.createOneShot(VIBRATE_TIME, VibrationEffect.EFFECT_DOUBLE_CLICK))
    }

    fun vibrateEffectHeavyClick(view: View) {
        vibrator.vibrate(VibrationEffect.createOneShot(VIBRATE_TIME, VibrationEffect.EFFECT_HEAVY_CLICK))
    }
}
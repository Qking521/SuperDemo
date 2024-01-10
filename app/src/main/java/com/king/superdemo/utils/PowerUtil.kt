package com.king.superdemo.utils

import android.app.admin.DeviceAdminReceiver
import android.app.admin.DevicePolicyManager
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.BatteryManager
import android.os.PowerManager
import android.os.SystemClock
import androidx.core.content.ContextCompat
import java.lang.reflect.InvocationTargetException


object PowerUtil {
    /**
     * 主动获取当前电池是否在充电 , 即数据线是否插在手机上
     * @return
     */
    fun isBatteryCharging(context: Context): Boolean {
        var isBatteryCharging = false
        // 主动发送包含是否正在充电状态的广播 , 该广播会持续发送
        val intentFilter = IntentFilter(Intent.ACTION_BATTERY_CHANGED)
        // 注册广播接受者
        val intent = context.registerReceiver(null, intentFilter)

        // 获取充电状态
        val batteryChargeState = intent!!.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1)

        // 判定是否是 AC 交流电充电
        val isAc = batteryChargeState == BatteryManager.BATTERY_PLUGGED_AC
        // 判断是否是 USB 充电
        val isUsb = batteryChargeState == BatteryManager.BATTERY_PLUGGED_USB
        // 判断是否是 无线充电
        val isWireless = batteryChargeState == BatteryManager.BATTERY_PLUGGED_WIRELESS

        // 如何上述任意一种为 true , 说明当前正在充电
        isBatteryCharging = isAc || isUsb || isWireless
        return isBatteryCharging
    }

    fun goToSleep(context: Context) {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        try {
            powerManager.javaClass.getMethod(
                "goToSleep",
                *arrayOf<Class<*>?>(Long::class.javaPrimitiveType)
            ).invoke(powerManager, SystemClock.uptimeMillis())
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        }
    }

    /**
     * 唤醒屏幕
     * @param context
     */
    fun wakeUp(context: Context) {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        try {
            powerManager.javaClass.getMethod(
                "wakeUp",
                *arrayOf<Class<*>?>(Long::class.javaPrimitiveType)
            ).invoke(powerManager, SystemClock.uptimeMillis())
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        }
    }


    /**
     * 锁屏
     * @param onlyCheck 是否只做检查
     *
     */
    fun lockScreen(context: Context, onlyCheck: Boolean = false) {
        val componentName =
            ComponentName(context.applicationContext, DeviceAdminReceiver::class.java)
        val dpm: DevicePolicyManager =
            context.getSystemService(Context.DEVICE_POLICY_SERVICE) as DevicePolicyManager
        if (dpm.isAdminActive(componentName)) {
            if (!onlyCheck) {
                dpm.lockNow()
            }
        } else {
            Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN).let {
                it.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName)
                it.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "一键锁屏")
                it.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }.run {
                ContextCompat.startActivity(context, this, null)
            }
        }
    }
}
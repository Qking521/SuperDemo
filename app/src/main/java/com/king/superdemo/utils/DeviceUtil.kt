package com.king.superdemo.utils

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.util.Log
import com.king.superdemo.BuildConfig
import java.net.NetworkInterface

object DeviceUtil {

    /**
     * 获取设备ID
     */
    fun getDeviceId(context: Context): String {
        val deviceKey = "device_id"
        val sp = context.getSharedPreferences("data", Context.MODE_PRIVATE)
        var deviceId = sp.getString(deviceKey, "")
        if (deviceId == null || "" == deviceId) {
            deviceId = getMacAddress()
            sp.edit().run { putString(deviceKey, deviceId) }.apply()
        }
        //todo 临时调试，发布之前删除
        if(BuildConfig.DEBUG){
            deviceId="a0fb831afabb";
        }
        return deviceId
    }

    /**
     * 获取设备IMEI
     * 需要动态申请Manifest.permission.READ_PHONE_STATE权限
     */
    @SuppressLint("HardwareIds")
    fun getImei(context: Context): String? {
        val manger: TelephonyManager =
            context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manger.imei
        } else {
            manger.deviceId
        }
    }

    @SuppressLint("HardwareIds")
    fun getSn(context: Context): String? {
        val manager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        return manager.simSerialNumber
    }

    @SuppressLint("HardwareIds")
    fun getAndroidId(context: Context): String? {
        return Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
    }

    /**
     * 获取设备MAC地址，需要权限：android.permission.ACCESS_WIFI_STATE
     */
    fun getMacAddress(): String {
        return NetworkInterface.getByName("wlan0").run {
            StringBuffer().apply {
                for (byte in hardwareAddress) {
                    append(String.format("%02x", byte))
                }
            }.toString()
        }
    }

    fun getMac2(context: Context) {
        val wlan = NetworkInterface.getByName("wlan0")
        val mb = wlan.hardwareAddress
        val macBuild = StringBuilder()
        mb.forEach { macBuild.append(String.format("%02X", it)) }
        Log.d("wq","直接获取MAC地址：${macBuild}")

        val networkList = NetworkInterface.getNetworkInterfaces()
        for (net in networkList) {
            if (net.name != "wlan0") {
                continue
            }
            val macBytes = net.hardwareAddress
            Log.d("wq","NetWorkName:${net.name},disName:${net.displayName},isVir:${net.isVirtual},obj:${net.isPointToPoint},index:${net.index}")
            if (macBytes != null) {
                val macStrBuilder = StringBuilder()
                for (macByte in macBytes) {
                    macStrBuilder.append(String.format("%02X", macByte))
                }
                Log.d("wq","获取到的Mac地址：$macStrBuilder")

            }
        }
    }
}
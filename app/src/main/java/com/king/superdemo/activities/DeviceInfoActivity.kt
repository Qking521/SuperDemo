package com.king.superdemo.activities

import android.content.Context
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.storage.StorageManager
import android.telephony.TelephonyManager
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.TextView
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.king.permission.PermissionBean
import com.king.permission.PermissionCallback
import com.king.permission.PermissionUtil
import com.king.superdemo.R
import com.king.superdemo.utils.CommonUtil
import com.king.superdemo.utils.requestPermission
import java.lang.reflect.InvocationTargetException

class DeviceInfoActivity : BaseActivity() {
    private var deviceInfo: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.device_info)
        initUI()
        XXPermissions.with(this).permission(Permission.RECORD_AUDIO).request(object : OnPermissionCallback{
            override fun onGranted(permissions: MutableList<String>, allGranted: Boolean) {
                TODO("Not yet implemented")
            }

            override fun onDenied(permissions: MutableList<String>, doNotAskAgain: Boolean) {
                super.onDenied(permissions, doNotAskAgain)
            }
        })
        XXPermissions.with(this).permission(Permission.READ_PHONE_STATE).request { permissions, allGranted ->  }
//        requestPermission(
//                PermissionBean(PermissionUtil.PERMISSION_READ_PHONE_STATE, PermissionCallback { phoneStateGranted: Boolean -> if (phoneStateGranted) deviceInfo() }),
//                PermissionBean(PermissionUtil.PERMISSION_WRITE_EXTERNAL_STORAGE, PermissionCallback { storageGranted: Boolean -> if (storageGranted) deviceInfo() }))
    }

    private fun initUI() {
        deviceInfo = findViewById<View>(R.id.device_info) as TextView
    }

    private fun deviceInfo() {
        val sb = StringBuilder()
        displayInfo(sb)
        buildInfo(sb)
        storageInfo(sb)
        telephonyInfo(sb)
        val ssb = SpannableStringBuilder(sb.toString())
        //high light text
        highLightString(ssb, sb.toString(), highLightedStrings)
        deviceInfo!!.text = ssb
    }

    //highlight given string
    private fun highLightString(ssb: SpannableStringBuilder, text: String, highLightedStrings: Array<String>) {
        for (i in highLightedStrings.indices) {
            val start = text.indexOf(highLightedStrings[i])
            val end = start + highLightedStrings[i].length
            ssb.setSpan(ForegroundColorSpan(Color.RED), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

    /**
     * now the external path have emulated indicate that the external storage is a part of internal storage
     * if a phone have 16 GB size, that is internal storage size.
     * 4 GB used to system contain system image and so on
     * the left is external storage but it's emulated by internal storage
     *
     */
    private fun storageInfo(sb: StringBuilder) {
        val manager = getSystemService(Context.STORAGE_SERVICE) as StorageManager
        sb.append(STORAGE_TITLE).append(ENTER)
        try {
            val paramClasses = arrayOf<Class<*>>()
            val getVolumePaths = manager.javaClass.getMethod("getVolumePaths", *paramClasses)
            getVolumePaths.isAccessible = true
            val obj = getVolumePaths.invoke(manager, *arrayOf())
            val path = obj as Array<String>
            for (i in path.indices) {
                sb.append(path[i]).append(ENTER)
            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
            Log.v("wq", "NoSuchMethodException")
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
            Log.v("wq", "InvocationTargetException")
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
            Log.v("wq", "IllegalAccessException")
        }
        sb.append("---------------Environment------------------").append(ENTER)
                .append("getDataDirectory: ").append(Environment.getDataDirectory().absolutePath).append(ENTER)
                .append("getDownloadCacheDirectory: ").append(Environment.getDownloadCacheDirectory().absolutePath).append(ENTER)
                .append("getExternalStorageDirectory: ").append(Environment.getExternalStorageDirectory().path).append(ENTER)
                .append("getRootDirectory: ").append(Environment.getRootDirectory().absolutePath).append(ENTER)
                .append("isExternalStorageEmulated： ").append(Environment.isExternalStorageEmulated().toString()).append(ENTER)
        sb.append("---------------application------------------").append(ENTER)
                .append("getExternalCacheDir: ").append(externalCacheDir!!.absolutePath).append(ENTER)
                .append("getFilesDir: ").append(filesDir.absolutePath).append(ENTER)
                .append("getCacheDir: ").append(cacheDir.absolutePath).append(ENTER)
                .append("getDataDir: ").append(dataDir.absolutePath).append(ENTER)
                .append("getObbDir: ").append(obbDir.absolutePath).append(ENTER)
                .append("getCodeCacheDir: ").append(codeCacheDir.absolutePath).append(ENTER)
                .append("getExternalCacheDir: ").append(externalCacheDir!!.absolutePath).append(ENTER)
                .append("getNoBackupFilesDir: ").append(noBackupFilesDir.absolutePath).append(ENTER)
        deviceInfo!!.text = sb
    }

    private fun telephonyInfo(sb: StringBuilder) {
        val tm = this.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        sb.append(TELEPHONE_TITLE).append(ENTER) //                .append("device id: ").append(tm.getDeviceId()).append(ENTER)
                //                .append("device soft version: ").append(tm.getDeviceSoftwareVersion()).append(ENTER)
                //                .append("groupId level: ").append(tm.getGroupIdLevel1()).append(ENTER)
                //                .append("line number: ").append(tm.getLine1Number()).append(ENTER)
                .append("mmsUA prof url: ").append(tm.mmsUAProfUrl).append(ENTER)
                .append("mmsUserAgent: ").append(tm.mmsUserAgent).append(ENTER)
                .append("network country ISO: ").append(tm.networkCountryIso).append(ENTER)
                .append("network operator: ").append(tm.networkOperator).append(ENTER)
                .append("network operator name: ").append(tm.networkOperatorName).append(ENTER)
                .append("sim country iso: ").append(tm.simCountryIso).append(ENTER)
                .append("sim operator: ").append(tm.simOperator).append(ENTER)
                .append("sim operator name: ").append(tm.simOperatorName).append(ENTER) //                .append("sim serial number: ").append(tm.getSimSerialNumber()).append(ENTER)
                //                .append("subscriber id: ").append(tm.getSubscriberId()).append(ENTER)
                .append("sim state: ").append(tm.simState).append(ENTER)
    }

    private fun buildInfo(sb: StringBuilder) {
        sb.append(BUILD_TITLE).append(ENTER)
                .append("board: ").append(Build.BOARD).append(ENTER)
                .append("boot load: ").append(Build.BOOTLOADER).append(ENTER)
                .append("brand: ").append(Build.BRAND).append(ENTER)
                .append("device: ").append(Build.DEVICE).append(ENTER)
                .append("display: ").append(Build.DISPLAY).append(ENTER)
                .append("fingerPrint: ").append(Build.FINGERPRINT).append(ENTER)
                .append("hard: ").append(Build.HARDWARE).append(ENTER)
                .append("host: ").append(Build.HOST).append(ENTER)
                .append("id: ").append(Build.ID).append(ENTER)
                .append("manufacturer: ").append(Build.MANUFACTURER).append(ENTER)
                .append("model: ").append(Build.MODEL).append(ENTER)
                .append("product: ").append(Build.PRODUCT).append(ENTER)
                .append("serial: ").append(Build.SERIAL).append(ENTER)
                .append("time: ").append(Build.TIME).append(ENTER)
                .append("type: ").append(Build.TYPE).append(ENTER)
                .append("user: ").append(Build.USER).append(ENTER)
                .append("version: ").append(Build.VERSION.RELEASE).append(ENTER)
                .append("tag: ").append(Build.TAGS).append(ENTER)
                .append("radio version: ").append(Build.getRadioVersion()).append(ENTER)
    }

    private fun displayInfo(sb: StringBuilder) {
        val dm = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(dm)
        sb.append(DISPLAY_TITLE).append(ENTER)
                .append("width: ").append(dm.widthPixels).append(ENTER)
                .append("height: ").append(dm.heightPixels).append(ENTER)
                .append("scale: ").append(dm.scaledDensity).append(ENTER)
                .append("density: ").append(dm.density).append(ENTER)
                .append("statusBarHeight: ").append(CommonUtil.getStatusBarHeight(this)).append(ENTER)
    }

    companion object {
        private const val ENTER = "\n"
        const val TELEPHONE_TITLE = "telephone info:"
        const val DISPLAY_TITLE = "display info:"
        const val STORAGE_TITLE = "storage info:"
        const val BUILD_TITLE = "build info:"
        private val highLightedStrings = arrayOf(
                TELEPHONE_TITLE, DISPLAY_TITLE, STORAGE_TITLE, BUILD_TITLE)
    }
}
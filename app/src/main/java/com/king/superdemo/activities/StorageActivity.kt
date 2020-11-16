package com.king.superdemo.activities

import android.app.usage.StorageStatsManager
import android.content.Context
import android.os.Bundle
import android.os.Environment
import android.os.StatFs
import android.os.storage.StorageManager
import android.util.Log
import android.view.View
import android.widget.TextView
import com.king.superdemo.R
import java.io.File
import java.io.IOException
import java.lang.reflect.InvocationTargetException
import java.util.*

class StorageActivity : BaseActivity() {
    private var mStorageManager: StorageManager? = null
    private var mStorageStatsManager: StorageStatsManager? = null
    private var storageInfo: TextView? = null
    private var stringBuilder: StringBuilder? = null
    private val units = arrayOf("B", "KB", "MB", "GB", "TB")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_storage)
        storageInfo = findViewById<View>(R.id.storage_info) as TextView
        stringBuilder = StringBuilder()
        mStorageManager = getSystemService(Context.STORAGE_SERVICE) as StorageManager
        mStorageStatsManager = getSystemService(Context.STORAGE_STATS_SERVICE) as StorageStatsManager
        queryStorageOfPath(Environment.getExternalStorageDirectory().path, 1000)
        queryStorageOfPath(Environment.getExternalStorageDirectory().path, 1024)
        queryStorageOfPath(Environment.getDataDirectory().path, 1000)
        queryStorageOfPath(Environment.getDataDirectory().path, 1024)
        queryStorageOfAll(1000)
        queryStorageOfAll(1024)
        storageInfo!!.text = stringBuilder.toString()
    }

    /**
     * 查询内置sd卡的大小,不包含系统所占的空间,比如16GB大小,这里会得到11.79GB
     */
    private fun queryStorageOfPath(path: String, unit: Int) {
        stringBuilder!!.append("以" + unit + "为单位").append("\n")
        val statFs = StatFs(path)
        //存储块总数量
        val blockCount = statFs.blockCountLong
        //块大小
        val blockSize = statFs.blockSizeLong
        //可用块数量
        val availableCount = statFs.availableBlocksLong
        //剩余块数量，注：这个包含保留块（including reserved blocks）即应用无法使用的空间
        val freeBlocks = statFs.freeBlocksLong
        //这两个方法是直接输出总内存和可用空间，也有getFreeBytes
        //API level 18（JELLY_BEAN_MR2）引入
        val totalSize = statFs.totalBytes
        val availableSize = statFs.availableBytes
        val freeSize = freeBlocks * blockSize
        val usedSize = totalSize - freeSize
        stringBuilder!!.append("路径: $path").append("\n")
                .append("总存储大小: ").append(getUnit(totalSize.toFloat(), unit)).append("\n")
                .append("可用存储大小: ").append(getUnit(availableSize.toFloat(), unit)).append("\n")
                .append("空闲存储大小: ").append(getUnit(freeSize.toFloat(), unit)).append("\n")
                .append("已用存储大小: ").append(getUnit(usedSize.toFloat(), unit)).append("\n")
    }

    /**
     * 查询总共容量大小，包括系统大小
     */
    fun queryStorageOfAll(unit: Int) {
        stringBuilder!!.append("以" + unit + "为单位").append("\n")
        try {
            val getVolumeInfo = StorageManager::class.java.getDeclaredMethod("getVolumes")
            getVolumeInfo.isAccessible = true
            val volumeInfoList = getVolumeInfo.invoke(mStorageManager) as List<Any>
            var SDTotalSize = 0L
            var usedSize = 0L
            var freeSize = 0L
            var systemSize = 0L
            var totalSize = 0L
            for (obj in volumeInfoList) {
                val getType = obj.javaClass.getField("type")
                getType.isAccessible = true
                val type = getType.getInt(obj)
                if (type == 1) { //TYPE_PRIVATE
                    val getFsUuid = obj.javaClass.getDeclaredMethod("getFsUuid")
                    val fsUuid = getFsUuid.invoke(obj) as String
                    SDTotalSize = getTotalSize(fsUuid) //8.0 以后使用,内置SD卡总大小(包含系统占用大小)
                    val isMountedReadable = obj.javaClass.getDeclaredMethod("isMountedReadable")
                    val readable = isMountedReadable.invoke(obj) as Boolean
                    if (readable) {
                        val file = obj.javaClass.getDeclaredMethod("getPath")
                        val f = file.invoke(obj) as File
                        //f.getTotalSpace()  内置SD卡出去系统占用的大小
                        systemSize = SDTotalSize - f.totalSpace
                        totalSize = f.totalSpace
                        freeSize = f.freeSpace
                        usedSize = totalSize - f.freeSpace
                        stringBuilder!!.append("内置SD卡总存储大小" + getUnit(SDTotalSize.toFloat(), unit)).append("\n")
                                .append("内置SD卡文件路径:" + f.absolutePath).append("\n")
                                .append("总存储大小(除去system占用)：" + getUnit(totalSize.toFloat(), unit)).append("\n")
                                .append("空闲存储大小：" + getUnit(freeSize.toFloat(), unit)).append("\n")
                                .append("已用存储大小：" + getUnit(usedSize.toFloat(), unit)).append("\n")
                                .append("系统占用存储大小" + getUnit(systemSize.toFloat(), unit)).append("\n")
                    }
                } else if (type == 0) { //TYPE_PUBLIC
                    //外置存储
                    val isMountedReadable = obj.javaClass.getDeclaredMethod("isMountedReadable")
                    val readable = isMountedReadable.invoke(obj) as Boolean
                    if (readable) {
                        val file = obj.javaClass.getDeclaredMethod("getPath")
                        val f = file.invoke(obj) as File
                        freeSize = f.freeSpace
                        usedSize = f.totalSpace - f.freeSpace
                        totalSize = f.totalSpace
                        stringBuilder!!.append("文件路径:" + f.absolutePath).append("\n")
                                .append("总存储大小：" + getUnit(totalSize.toFloat(), unit)).append("\n")
                                .append("可用存储大小：" + getUnit(freeSize.toFloat(), unit)).append("\n")
                                .append("已用存储大小：" + getUnit(usedSize.toFloat(), unit)).append("\n")
                    }
                }
            }
        } catch (e: NoSuchMethodException) {
            Log.i("wq", "queryStorageOfAll: e=" + e.message)
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            Log.i("wq", "queryStorageOfAll: e=" + e.message)
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            Log.i("wq", "queryStorageOfAll: e=" + e.message)
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            Log.i("wq", "queryStorageOfAll: e=" + e.message)
            e.printStackTrace()
        }
    }

    /**
     * API 26 android O
     * 获取总共容量大小，包括系统大小
     */
    fun getTotalSize(fsUuid: String?): Long {
        return try {
            val id: UUID
            id = if (fsUuid == null) {
                StorageManager.UUID_DEFAULT
            } else {
                UUID.fromString(fsUuid)
            }
            val stats = getSystemService(StorageStatsManager::class.java)
            stats.getTotalBytes(id)
        } catch (e: NoSuchFieldError) {
            e.printStackTrace()
            0
        } catch (e: NoClassDefFoundError) {
            e.printStackTrace()
            0
        } catch (e: NullPointerException) {
            e.printStackTrace()
            0
        } catch (e: IOException) {
            e.printStackTrace()
            0
        }
    }

    /**
     * 单位转换
     */
    private fun getUnit(size: Float): String {
        var size = size
        var index = 0
        while (size > 1024 && index < 4) {
            size = size / 1024
            index++
        }
        return String.format(Locale.getDefault(), " %.2f %s", size, units[index])
    }

    private fun getUnit(size: Float, unit: Int): String {
        var size = size
        var index = 0
        while (size > unit && index < 4) {
            size = size / unit
            index++
        }
        return String.format(Locale.getDefault(), " %.2f %s", size, units[index])
    }
}
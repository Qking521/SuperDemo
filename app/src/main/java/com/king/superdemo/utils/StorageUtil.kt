package com.king.superdemo.utils

import android.content.Context
import android.os.storage.StorageManager
import android.os.storage.StorageVolume
import android.util.Log
import java.lang.reflect.InvocationTargetException

object StorageUtil {
    fun getExternalStoragePath(context: Context): String {
        val storageManager = context.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        try {
            val getStorageVolumes = storageManager.javaClass.getDeclaredMethod("getStorageVolumes")
            getStorageVolumes.isAccessible = true
            val storageVolumes = getStorageVolumes.invoke(storageManager) as List<StorageVolume>
            Log.i("wq", "getExternalStoragePath: storageVolumes size=" + storageVolumes.size)
            for (storageVolume in storageVolumes) {
                Log.i("wq", "getExternalStoragePath: " + storageVolume.getDescription(context) + storageVolume.uuid)
            }
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        }
        return ""
    }
}
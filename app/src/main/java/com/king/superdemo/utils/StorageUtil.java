package com.king.superdemo.utils;

import android.content.Context;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.util.Log;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class StorageUtil {

    public static String getExternalStoragePath(Context context){
        StorageManager storageManager = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
        try {
            Method getStorageVolumes = storageManager.getClass().getDeclaredMethod("getStorageVolumes");
            getStorageVolumes.setAccessible(true);
           List<StorageVolume> storageVolumes = (List<StorageVolume>) getStorageVolumes.invoke(storageManager);
            Log.i("wq", "getExternalStoragePath: storageVolumes size="+ storageVolumes.size());
            for (StorageVolume storageVolume : storageVolumes) {
                Log.i("wq", "getExternalStoragePath: "+ storageVolume.getDescription(context) + storageVolume.getUuid());
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return "";
    }

}

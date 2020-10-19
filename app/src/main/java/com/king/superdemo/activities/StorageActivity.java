package com.king.superdemo.activities;

import android.app.usage.StorageStatsManager;
import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.os.storage.StorageVolume;
import android.text.format.Formatter;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.king.superdemo.R;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class StorageActivity extends BaseActivity {


    private StorageManager mStorageManager;
    private StorageStatsManager mStorageStatsManager;
    private TextView storageInfo;
    private StringBuilder stringBuilder;

    private String[] units = {"B", "KB", "MB", "GB", "TB"};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        storageInfo = (TextView) findViewById(R.id.storage_info);
        stringBuilder = new StringBuilder();
        mStorageManager = (StorageManager) getSystemService(Context.STORAGE_SERVICE);
        mStorageStatsManager = (StorageStatsManager) getSystemService(Context.STORAGE_STATS_SERVICE);
        queryStorageOfPath(Environment.getExternalStorageDirectory().getPath(), 1000);
        queryStorageOfPath(Environment.getExternalStorageDirectory().getPath(), 1024);
        queryStorageOfPath(Environment.getDataDirectory().getPath(), 1000);
        queryStorageOfPath(Environment.getDataDirectory().getPath(), 1024);
        queryStorageOfAll(1000);
        queryStorageOfAll(1024);

        storageInfo.setText(stringBuilder.toString());
    }

    /**
     * 查询内置sd卡的大小,不包含系统所占的空间,比如16GB大小,这里会得到11.79GB
     */
    private void queryStorageOfPath(String path, int unit) {
        stringBuilder.append("以" + unit + "为单位").append("\n");
        StatFs statFs = new StatFs(path);
        //存储块总数量
        long blockCount = statFs.getBlockCountLong();
        //块大小
        long blockSize = statFs.getBlockSizeLong();
        //可用块数量
        long availableCount = statFs.getAvailableBlocksLong();
        //剩余块数量，注：这个包含保留块（including reserved blocks）即应用无法使用的空间
        long freeBlocks = statFs.getFreeBlocksLong();
        //这两个方法是直接输出总内存和可用空间，也有getFreeBytes
        //API level 18（JELLY_BEAN_MR2）引入
        long totalSize = statFs.getTotalBytes();
        long availableSize = statFs.getAvailableBytes();
        long freeSize = freeBlocks * blockSize;
        long usedSize = totalSize - freeSize;
        stringBuilder.append("路径: " + path).append("\n")
                .append("总存储大小: ").append(getUnit(totalSize, unit)).append("\n")
                .append("可用存储大小: ").append(getUnit(availableSize, unit)).append("\n")
                .append("空闲存储大小: ").append(getUnit(freeSize, unit)).append("\n")
                .append("已用存储大小: ").append(getUnit(usedSize, unit)).append("\n");
    }



    /**
     * 查询总共容量大小，包括系统大小
     */
    public void queryStorageOfAll(int unit) {
        stringBuilder.append("以" + unit + "为单位").append("\n");
        try {
            Method getVolumeInfo = mStorageManager.getClass().getDeclaredMethod("getVolumes");
            getVolumeInfo.setAccessible(true);
            List<Object> volumeInfoList = (List<Object>) getVolumeInfo.invoke(mStorageManager);
            long SDTotalSize = 0L, usedSize = 0L, freeSize = 0L, systemSize = 0L, totalSize = 0L;
            for (Object obj : volumeInfoList) {
                Field getType = obj.getClass().getField("type");
                getType.setAccessible(true);
                int type = getType.getInt(obj);
                if (type == 1) {//TYPE_PRIVATE

                    Method getFsUuid = obj.getClass().getDeclaredMethod("getFsUuid");
                    String fsUuid = (String) getFsUuid.invoke(obj);
                    SDTotalSize = getTotalSize(fsUuid);//8.0 以后使用,内置SD卡总大小(包含系统占用大小)
                    Method isMountedReadable = obj.getClass().getDeclaredMethod("isMountedReadable");
                    boolean readable = (boolean) isMountedReadable.invoke(obj);
                    if (readable) {
                        Method file = obj.getClass().getDeclaredMethod("getPath");
                        File f = (File) file.invoke(obj);
                        //f.getTotalSpace()  内置SD卡出去系统占用的大小
                        systemSize = SDTotalSize - f.getTotalSpace();
                        totalSize = f.getTotalSpace();
                        freeSize = f.getFreeSpace();
                        usedSize = totalSize - f.getFreeSpace();

                        stringBuilder.append( "内置SD卡总存储大小" + getUnit(SDTotalSize, unit)).append("\n")
                                .append(  "内置SD卡文件路径:" + f.getAbsolutePath()).append("\n")
                                .append(  "总存储大小(除去system占用)：" + getUnit(totalSize, unit)).append("\n")
                                .append( "空闲存储大小：" + getUnit(freeSize, unit)).append("\n")
                                .append( "已用存储大小：" + getUnit(usedSize, unit)).append("\n")
                                .append( "系统占用存储大小" + getUnit(systemSize, unit)).append("\n");
                    }
                }else if (type == 0) {//TYPE_PUBLIC
                    //外置存储
                    Method isMountedReadable = obj.getClass().getDeclaredMethod("isMountedReadable");
                    boolean readable = (boolean) isMountedReadable.invoke(obj);
                    if (readable) {
                        Method file = obj.getClass().getDeclaredMethod("getPath");
                        File f = (File) file.invoke(obj);
                        freeSize = f.getFreeSpace();
                        usedSize = f.getTotalSpace() - f.getFreeSpace();
                        totalSize = f.getTotalSpace();
                        stringBuilder.append("文件路径:" + f.getAbsolutePath()).append("\n")
                                .append("总存储大小：" + getUnit(totalSize, unit)).append("\n")
                                .append("可用存储大小：" + getUnit(freeSize, unit)).append("\n")
                                .append("已用存储大小：" + getUnit(usedSize, unit)).append("\n");
                    }
                }
            }
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NoSuchFieldException e) {
            Log.i("wq", "queryStorageOfAll: e="+ e.getMessage());
            e.printStackTrace();
        }

    }

    /**
     * API 26 android O
     * 获取总共容量大小，包括系统大小
     */
    public long getTotalSize(String fsUuid) {
        try {
            UUID id;
            if (fsUuid == null) {
                id = StorageManager.UUID_DEFAULT;
            } else {
                id = UUID.fromString(fsUuid);
            }
            StorageStatsManager stats = getSystemService(StorageStatsManager.class);
            return stats.getTotalBytes(id);
        } catch (NoSuchFieldError | NoClassDefFoundError | NullPointerException | IOException e) {
            e.printStackTrace();
            return 0;
        }
    }

    /**
     * 单位转换
     */
    private String getUnit(float size) {
        int index = 0;
        while (size > 1024 && index < 4) {
            size = size / 1024;
            index++;
        }
        return String.format(Locale.getDefault(), " %.2f %s", size, units[index]);
    }

    private String getUnit(float size, int unit) {
        int index = 0;
        while (size > unit && index < 4) {
            size = size / unit;
            index++;
        }
        return String.format(Locale.getDefault(), " %.2f %s", size, units[index]);
    }
}

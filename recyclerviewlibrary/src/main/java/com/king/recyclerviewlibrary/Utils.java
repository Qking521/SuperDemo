package com.king.recyclerviewlibrary;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

/**
 * 默认的转换工具, 若要显示非默认的数据, 可通过setCommonItemList显示设定的数据
 */
public class Utils {

    private Executors executors = (Executors) Executors.newFixedThreadPool(5);

    public static CommonItem getCommonItem(Context context, Object object) {
        if (object instanceof File) return getCommonItemOfFileType(context, object);
        if (object instanceof String)  return getCommonItemOfStringType(context, object);
        if (object instanceof PackageInfo)  return getCommonItemOfPackageInfoType(context, object);
        if (object instanceof ResolveInfo)  return getCommonItemOfResolveInfoType(context, object);
        return null;
    }

    private static CommonItem getCommonItemOfFileType(Context context, Object object) {
        File file = (File)object;
        CommonItem commonItem = new CommonItem();
        commonItem.title = file.getName();
        commonItem.file = file;
        return commonItem;
    }

    private static CommonItem getCommonItemOfStringType(Context context, Object object) {
        String s = (String)object;
        CommonItem commonItem = new CommonItem();
        commonItem.title = s;
        return commonItem;
    }

    private static CommonItem getCommonItemOfPackageInfoType(Context context, Object object) {
        PackageInfo packageInfo = (PackageInfo) object;
        PackageManager packageManager = context.getPackageManager();
        CommonItem commonItem = new CommonItem();
        commonItem.title = packageInfo.applicationInfo.loadLabel(packageManager).toString();
        commonItem.summary = packageInfo.packageName;
        commonItem.iconDrawable = packageInfo.applicationInfo.loadIcon(packageManager);
        return commonItem;
    }

    private static CommonItem getCommonItemOfResolveInfoType(Context context, Object object) {
        PackageManager packageManager = context.getPackageManager();
        ResolveInfo resolveInfo = (ResolveInfo) object;
        CommonItem commonItem = new CommonItem();
        commonItem.title = resolveInfo.loadLabel(packageManager).toString();
        commonItem.summary = resolveInfo.activityInfo.packageName;
        commonItem.iconDrawable = resolveInfo.activityInfo.loadIcon(packageManager);
        return commonItem;
    }
}

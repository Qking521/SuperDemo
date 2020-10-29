package com.king.superdemo.activities;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.storage.StorageManager;
import android.telephony.TelephonyManager;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.widget.TextView;

import com.king.permission.PermissionBean;
import com.king.permission.PermissionCallback;
import com.king.permission.PermissionUtil;

import java.io.InputStreamReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

import com.king.superdemo.R;
import com.king.superdemo.utils.CommonUtil;

import static android.graphics.Color.*;

public class DeviceInfoActivity extends BaseActivity {

    private static final String ENTER = "\n";
    public static final String TELEPHONE_TITLE = "telephone info:";
    public static final String DISPLAY_TITLE = "display info:";
    public static final String STORAGE_TITLE = "storage info:";
    public static final String BUILD_TITLE = "build info:";
    private static final String[] highLightedStrings = new String[]{
            TELEPHONE_TITLE, DISPLAY_TITLE, STORAGE_TITLE, BUILD_TITLE};

    private TextView deviceInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.device_info);
        initUI();
        requestPermission(
                        new PermissionBean(PermissionUtil.PERMISSION_READ_PHONE_STATE, phoneStateGranted ->  {if (phoneStateGranted) deviceInfo();}),
                        new PermissionBean(PermissionUtil.PERMISSION_WRITE_EXTERNAL_STORAGE, storageGranted -> {if (storageGranted) deviceInfo();}));
    }

    private void initUI() {
        deviceInfo = (TextView) findViewById(R.id.device_info);
    }

    private void deviceInfo() {
        StringBuilder sb = new StringBuilder();
        displayInfo(sb);
        buildInfo(sb);
        storageInfo(sb);
        telephonyInfo(sb);

        SpannableStringBuilder ssb = new SpannableStringBuilder(sb.toString());
        //high light text
        highLightString(ssb, sb.toString(), highLightedStrings);
        deviceInfo.setText(ssb);
    }

    //highlight given string
    private void highLightString(SpannableStringBuilder ssb, String text, String[] highLightedStrings) {
        for (int i = 0; i < highLightedStrings.length; i++) {
            int start = text.indexOf(highLightedStrings[i]);
            int end = start + highLightedStrings[i].length();
            ssb.setSpan(new ForegroundColorSpan(RED), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    /**
     * now the external path have emulated indicate that the external storage is a part of internal storage
     * if a phone have 16 GB size, that is internal storage size.
     * 4 GB used to system contain system image and so on
     * the left is external storage but it's emulated by internal storage
     *
     */
    private void storageInfo(StringBuilder sb) {
        StorageManager manager = (StorageManager)getSystemService(Context.STORAGE_SERVICE);
        sb.append(STORAGE_TITLE).append(ENTER);
        try {
            Class<?>[] paramClasses = {};
            Method getVolumePaths = manager.getClass().getMethod("getVolumePaths", paramClasses);
            getVolumePaths.setAccessible(true);
            Object obj = getVolumePaths.invoke(manager, new Object[]{});
            String[] path = (String[])obj;
            for (int i = 0; i < path.length; i++) {
                sb.append(path[i]).append(ENTER);
            }
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            Log.v("wq", "NoSuchMethodException");
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            Log.v("wq", "InvocationTargetException");
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            Log.v("wq", "IllegalAccessException");
        }

        sb.append("---------------Environment------------------").append(ENTER)
                .append("getDataDirectory: ").append(Environment.getDataDirectory().getAbsolutePath()).append(ENTER)
                .append("getDownloadCacheDirectory: ").append(Environment.getDownloadCacheDirectory().getAbsolutePath()).append(ENTER)
                .append("getExternalStorageDirectory: ").append(Environment.getExternalStorageDirectory().getPath()).append(ENTER)
                .append("getRootDirectory: ").append(Environment.getRootDirectory().getAbsolutePath()).append(ENTER)
                .append("isExternalStorageEmulated： ").append(String.valueOf(Environment.isExternalStorageEmulated())).append(ENTER);
        sb.append("---------------application------------------").append(ENTER)
                .append("getExternalCacheDir: ").append(getExternalCacheDir().getAbsolutePath()).append(ENTER)
                .append("getFilesDir: ").append(getFilesDir().getAbsolutePath()).append(ENTER)
                .append("getCacheDir: ").append(getCacheDir().getAbsolutePath()).append(ENTER)
                .append("getDataDir: ").append(getDataDir().getAbsolutePath()).append(ENTER)
                .append("getObbDir: ").append(getObbDir().getAbsolutePath()).append(ENTER)
                .append("getCodeCacheDir: ").append(getCodeCacheDir().getAbsolutePath()).append(ENTER)
                .append("getExternalCacheDir: ").append(getExternalCacheDir().getAbsolutePath()).append(ENTER)
                .append("getNoBackupFilesDir: ").append(getNoBackupFilesDir().getAbsolutePath()).append(ENTER);
        deviceInfo.setText(sb);
    }

    private void telephonyInfo(StringBuilder sb) {
        TelephonyManager tm = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
        sb.append(TELEPHONE_TITLE).append(ENTER)
//                .append("device id: ").append(tm.getDeviceId()).append(ENTER)
//                .append("device soft version: ").append(tm.getDeviceSoftwareVersion()).append(ENTER)
//                .append("groupId level: ").append(tm.getGroupIdLevel1()).append(ENTER)
//                .append("line number: ").append(tm.getLine1Number()).append(ENTER)
                .append("mmsUA prof url: ").append(tm.getMmsUAProfUrl()).append(ENTER)
                .append("mmsUserAgent: ").append(tm.getMmsUserAgent()).append(ENTER)
                .append("network country ISO: ").append(tm.getNetworkCountryIso()).append(ENTER)
                .append("network operator: ").append(tm.getNetworkOperator()).append(ENTER)
                .append("network operator name: ").append(tm.getNetworkOperatorName()).append(ENTER)
                .append("sim country iso: ").append(tm.getSimCountryIso()).append(ENTER)
                .append("sim operator: ").append(tm.getSimOperator()).append(ENTER)
                .append("sim operator name: ").append(tm.getSimOperatorName()).append(ENTER)
//                .append("sim serial number: ").append(tm.getSimSerialNumber()).append(ENTER)
//                .append("subscriber id: ").append(tm.getSubscriberId()).append(ENTER)
                .append("sim state: ").append(tm.getSimState()).append(ENTER);
    }

    private void buildInfo(StringBuilder sb) {
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
                .append("radio version: ").append(Build.getRadioVersion()).append(ENTER);
    }

    private void displayInfo(StringBuilder sb) {
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);

        sb.append(DISPLAY_TITLE).append(ENTER)
                .append("width: ").append(dm.widthPixels).append(ENTER)
                .append("height: ").append(dm.heightPixels).append(ENTER)
                .append("scale: ").append(dm.scaledDensity).append(ENTER)
                .append("density: ").append(dm.density).append(ENTER)
                .append("statusBarHeight: ").append(CommonUtil.getStatusBarHeight(this)).append(ENTER);
    }


}

package com.king.superdemo.utils;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;

import androidx.core.app.ActivityCompat;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wangqiang on 2017/3/20.
 */

public class PermissionUtil {

    public interface PermissionCallback{
        void permissionResult(boolean result);
    }

    static Map<String, Boolean> sPermissionResultMap = new HashMap<>();
    static Map<String, PermissionCallback> sPermissionCallbackMap = new HashMap<>();

    public static void setPermissionCallbacks(String[] permissions, PermissionCallback[] callbacks) {
        int length = permissions.length;
        for (int i = 0; i < length; i++) {
            sPermissionCallbackMap.put(permissions[i], callbacks[i]);
        }
    }

    public static final int PERMISSION_REQUESTCODE_CALENDAR = 0;
    public static final int PERMISSION_REQUESTCODE_CAMERA = 1;
    public static final int PERMISSION_REQUESTCODE_CONTACTS = 2;
    public static final int PERMISSION_REQUESTCODE_LOCATION = 3;
    public static final int PERMISSION_REQUESTCODE_MICROPHONE = 4;
    public static final int PERMISSION_REQUESTCODE_PHONE = 5;
    public static final int PERMISSION_REQUESTCODE_SENSORS = 6;
    public static final int PERMISSION_REQUESTCODE_SMS = 7;
    public static final int PERMISSION_REQUESTCODE_STORAGE = 8;
    public static final int PERMISSION_REQUESTCODE_MULTI = 100;

    public static final String PERMISSION_READ_CALENDAR = Manifest.permission.READ_CALENDAR;
    public static final String PERMISSION_WRITE_CALENDAR = Manifest.permission.WRITE_CALENDAR;
    public static final String PERMISSION_CAMERA = Manifest.permission.CAMERA;
    public static final String PERMISSION_READ_CONTACTS = Manifest.permission.READ_CONTACTS;
    public static final String PERMISSION_WRITE_CONTACTS = Manifest.permission.WRITE_CONTACTS;
    public static final String PERMISSION_GET_ACCOUNTS = Manifest.permission.GET_ACCOUNTS;
    public static final String PERMISSION_ACCESS_FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    public static final String PERMISSION_ACCESS_COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    public static final String PERMISSION_RECORD_AUDIO = Manifest.permission.RECORD_AUDIO;
    public static final String PERMISSION_READ_PHONE_STATE = Manifest.permission.READ_PHONE_STATE;
    public static final String PERMISSION_CALL_PHONE = Manifest.permission.CALL_PHONE;
    public static final String PERMISSION_READ_CALL_LOG = Manifest.permission.READ_CALL_LOG;
    public static final String PERMISSION_WRITE_CALL_LOG = Manifest.permission.WRITE_CALL_LOG;
    public static final String PERMISSION_ADD_VOICEMAIL = Manifest.permission.ADD_VOICEMAIL;
    public static final String PERMISSION_USE_SIP = Manifest.permission.USE_SIP;
    public static final String PERMISSION_PROCESS_OUTGOING_CALLS = Manifest.permission.PROCESS_OUTGOING_CALLS;
    public static final String PERMISSION_BODY_SENSORS = Manifest.permission.BODY_SENSORS;
    public static final String PERMISSION_SEND_SMS = Manifest.permission.SEND_SMS;
    public static final String PERMISSION_RECEIVE_SMS = Manifest.permission.RECEIVE_SMS;
    public static final String PERMISSION_READ_SMS = Manifest.permission.READ_SMS;
    public static final String PERMISSION_RECEIVE_MMS = Manifest.permission.RECEIVE_MMS;
    public static final String PERMISSION_RECEIVE_WAP_PUSH = Manifest.permission.RECEIVE_WAP_PUSH;
    public static final String PERMISSION_READ_EXTERNAL_STORAGE = Manifest.permission.READ_EXTERNAL_STORAGE;
    public static final String PERMISSION_WRITE_EXTERNAL_STORAGE = Manifest.permission.WRITE_EXTERNAL_STORAGE;

    public static boolean isPermissionGranted(Context context, String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    private static String getRationale(String permission) {
        switch (permission) {
            case Manifest.permission.READ_CALENDAR:
            case Manifest.permission.WRITE_CALENDAR:
                return "CALENDAR";
            case Manifest.permission.CAMERA:
                return "CAMERA";
            case Manifest.permission.READ_CONTACTS:
            case Manifest.permission.WRITE_CONTACTS:
            case Manifest.permission.GET_ACCOUNTS:
                return "CONTACTS";
            case Manifest.permission.ACCESS_FINE_LOCATION:
            case Manifest.permission.ACCESS_COARSE_LOCATION:
                return "LOCATION";
            case Manifest.permission.RECORD_AUDIO:
                return "MICROPHONE";
            case Manifest.permission.READ_PHONE_STATE:
            case Manifest.permission.CALL_PHONE:
            case Manifest.permission.READ_CALL_LOG:
            case Manifest.permission.WRITE_CALL_LOG:
            case Manifest.permission.ADD_VOICEMAIL:
            case Manifest.permission.USE_SIP:
            case Manifest.permission.PROCESS_OUTGOING_CALLS:
                return "PHONE";
            case Manifest.permission.BODY_SENSORS:
                return "SENSORS";
            case Manifest.permission.SEND_SMS:
            case Manifest.permission.RECEIVE_SMS:
            case Manifest.permission.READ_SMS:
            case Manifest.permission.RECEIVE_WAP_PUSH:
            case Manifest.permission.RECEIVE_MMS:
                return "SMS";
            case Manifest.permission.READ_EXTERNAL_STORAGE:
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                return "STORAGE";
            default:
                return null;
        }
    }

    public static void shouldShowRequestPermissionRationale(final Activity activity, String[] permissions){
        for (String permission : permissions) {
            shouldShowRequestPermissionRationale(activity, permission, getRationale(permission));
        }
    }

    public static void shouldShowRequestPermissionRationale(final Activity activity, String permission, String rationale){
        if (activity.shouldShowRequestPermissionRationale(permission)) {
            ShowRequestPermissionRationaleDialog(activity, "rationale", rationale,
                    (dialog, which) -> requestPermission(activity, new String[]{permission}),
                   null);
        } else {
            if (!isPermissionGranted(activity, permission)) {
                showDialog(activity, "goto settings", "need open form settings", (dialog, which) -> gotoSettings(activity), null);
            }
        }
    }

    public static void gotoSettings(Activity activity) {
        final Intent i = new Intent();
        i.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        i.addCategory(Intent.CATEGORY_DEFAULT);
        i.setData(Uri.parse("package:" + activity.getPackageName()));
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        i.addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
        activity.startActivity(i);
    }

    public static void requestPermission(final Activity activity, final String[] permissions) {
        activity.requestPermissions(permissions, getRequestCode(permissions));
    }

    private static int getRequestCode(String[] permissions) {
        if (permissions.length > 0) {
            return PERMISSION_REQUESTCODE_MULTI;
        }
        String permission = permissions[0];
        switch (permission) {
            case Manifest.permission.READ_CALENDAR:
            case Manifest.permission.WRITE_CALENDAR:
                return PERMISSION_REQUESTCODE_CALENDAR;
            case Manifest.permission.CAMERA:
                return PERMISSION_REQUESTCODE_CAMERA;
            case Manifest.permission.READ_CONTACTS:
            case Manifest.permission.WRITE_CONTACTS:
            case Manifest.permission.GET_ACCOUNTS:
                return PERMISSION_REQUESTCODE_CONTACTS;
            case Manifest.permission.ACCESS_FINE_LOCATION:
            case Manifest.permission.ACCESS_COARSE_LOCATION:
                return PERMISSION_REQUESTCODE_LOCATION;
            case Manifest.permission.RECORD_AUDIO:
                return PERMISSION_REQUESTCODE_MICROPHONE;
            case Manifest.permission.READ_PHONE_STATE:
            case Manifest.permission.CALL_PHONE:
            case Manifest.permission.READ_CALL_LOG:
            case Manifest.permission.WRITE_CALL_LOG:
            case Manifest.permission.ADD_VOICEMAIL:
            case Manifest.permission.USE_SIP:
            case Manifest.permission.PROCESS_OUTGOING_CALLS:
                return PERMISSION_REQUESTCODE_PHONE;
            case Manifest.permission.BODY_SENSORS:
                return PERMISSION_REQUESTCODE_SENSORS;
            case Manifest.permission.SEND_SMS:
            case Manifest.permission.RECEIVE_SMS:
            case Manifest.permission.READ_SMS:
            case Manifest.permission.RECEIVE_WAP_PUSH:
            case Manifest.permission.RECEIVE_MMS:
                return PERMISSION_REQUESTCODE_SMS;
            case Manifest.permission.READ_EXTERNAL_STORAGE:
            case Manifest.permission.WRITE_EXTERNAL_STORAGE:
                return PERMISSION_REQUESTCODE_STORAGE;
            default:
                return -1;
        }
    }

    private static void ShowRequestPermissionRationaleDialog(Context context, String title, String msg,
                                                             DialogInterface.OnClickListener okClickListener,
                                                             DialogInterface.OnClickListener cancelClickListener) {
        showDialog(context, title, msg, okClickListener, cancelClickListener);
    }

    private static void showDialog(Context context, String title, String msg, DialogInterface.OnClickListener okClickListener, DialogInterface.OnClickListener cancelClickListener) {
        new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(android.R.string.ok, okClickListener)
                .setNegativeButton(android.R.string.cancel, cancelClickListener)
                .create()
                .show();
    }

    public static void onRequestPermissionResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode != PERMISSION_REQUESTCODE_MULTI) {
            sPermissionResultMap.put(permissions[0], grantResults[0] == PackageManager.PERMISSION_GRANTED);
        }else {
            for (int i = 0; i < permissions.length; i++) {
                sPermissionResultMap.put(permissions[i], grantResults[i] == PackageManager.PERMISSION_GRANTED);
            }
        }
    }

    public static void callback(String[] permissions)  {
        for (String permission : permissions) {
            if (sPermissionResultMap.containsKey(permission)) {
                boolean result = sPermissionResultMap.get(permission);
                sPermissionCallbackMap.get(permission).permissionResult(result);
            }
        }
    }
}


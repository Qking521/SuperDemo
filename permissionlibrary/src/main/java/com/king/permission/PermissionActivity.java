package com.king.permission;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

public class PermissionActivity extends AppCompatActivity {
    public boolean isPermissionGranted(Context context, String permission) {
        return PermissionUtil.isPermissionGranted(context, permission);
    }

    public void requestPermission(final AppCompatActivity activity, final String[] permissions, PermissionUtil.PermissionCallback[] callbacks) {
        PermissionUtil.setPermissionCallbacks(permissions, callbacks);
        PermissionUtil.requestPermission(activity, permissions);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.onRequestPermissionResult(requestCode, permissions, grantResults);
        PermissionUtil.shouldShowRequestPermissionRationale(this, permissions);
        PermissionUtil.callback(permissions);
    }
}

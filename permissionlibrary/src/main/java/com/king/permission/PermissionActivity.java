package com.king.permission;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.List;

public class PermissionActivity extends AppCompatActivity {
    public boolean isPermissionGranted(Context context, String permission) {
        return PermissionUtil.isPermissionGranted(context, permission);
    }

    public void requestPermission(final String[] permissions, PermissionCallback[] callbacks) {
        PermissionUtil.setPermissionCallbacks(permissions, callbacks);
        PermissionUtil.requestPermission(this, permissions);
    }

    public void requestPermission(PermissionBean... permissionBean) {
        int size = permissionBean.length;
        String[] requestPermissions = new String[size];
        PermissionCallback[] permissionCallbacks = new PermissionCallback[size];
        for (int i = 0; i < size; i++) {
            requestPermissions[i] = permissionBean[i].requestPermission;
            permissionCallbacks[i] = permissionBean[i].permissionCallback;
        }
        requestPermission(requestPermissions, permissionCallbacks);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionUtil.onRequestPermissionResult(requestCode, permissions, grantResults);
        PermissionUtil.shouldShowRequestPermissionRationale(this, permissions);
        PermissionUtil.callback(permissions);
    }
}

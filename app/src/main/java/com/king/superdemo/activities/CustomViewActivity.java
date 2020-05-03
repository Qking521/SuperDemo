package com.king.superdemo.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.king.superdemo.BaseActivity;
import com.king.superdemo.R;
import com.king.superdemo.service.FloatViewService;

public class CustomViewActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
    }

    public void floatView(View view) {
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "no permission", Toast.LENGTH_SHORT).show();
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 1);
        }else {
            String tag = (String)view.getTag();
            if (tag.equals("start")) {
                startService(new Intent(this, FloatViewService.class));
                view.setTag("stop");
            } else {
                stopService(new Intent(this, FloatViewService.class));
                view.setTag("start");
            }
        }
    }

    public void setVisible(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

}

package com.king.superdemo.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.view.LayoutInflater;

import com.king.superdemo.R;
import com.king.superdemo.custom.FloatView;

public class FloatViewService extends Service {
    FloatView mFloatView;
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mFloatView = (FloatView) LayoutInflater.from(this).inflate(R.layout.float_view, null);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFloatView.finish();
    }
}

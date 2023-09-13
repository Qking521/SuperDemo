package com.king.superdemo.service

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import com.king.superdemo.R
import com.king.superdemo.custom.FloatView

class FloatViewService : Service() {
    var mFloatView: FloatView? = null
    override fun onBind(intent: Intent): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        mFloatView = LayoutInflater.from(this).inflate(R.layout.float_view, null) as FloatView

    }

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        registerReceiver(object : BroadcastReceiver(){
            override fun onReceive(context: Context?, intent: Intent?) {
                Log.v("wq", "onReceive: action="+ intent?.action)
            }

        }, IntentFilter("com.king.action.bb"))
        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        super.onDestroy()
        mFloatView!!.finish()
    }
}
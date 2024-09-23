package com.king.systemlibrary

import android.app.IActivityManager
import android.app.IProcessObserver
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.ServiceManager
import android.util.Log

class SystemMainService: Service() {

    val activityService = IActivityManager.Stub.asInterface(ServiceManager.getService(Context.ACTIVITY_SERVICE))

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun onCreate() {
        super.onCreate()
        Log.d("wq", "CommonService onCreate: ")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        activityService.registerProcessObserver(processObserver)
        return START_STICKY
    }

    private val processObserver =  object : IProcessObserver.Stub() {
        override fun onForegroundActivitiesChanged(
            pid: Int,
            uid: Int,
            foregroundActivities: Boolean
        ) {
            Log.d("wq", "onForegroundActivitiesChanged: ")
            packageManager.getPackagesForUid(uid)?.forEach {
                Log.d("wq", "onForegroundActivitiesChanged: packageName=$it")
            }
        }

        override fun onForegroundServicesChanged(pid: Int, uid: Int, serviceTypes: Int) {
            Log.d("wq", "onForegroundServicesChanged: ")
        }

        override fun onProcessDied(pid: Int, uid: Int) {
            Log.d("wq", "onProcessDied: ")
        }
    }

    override fun onDestroy() {
        activityService.unregisterProcessObserver(processObserver)
        super.onDestroy()

    }


}
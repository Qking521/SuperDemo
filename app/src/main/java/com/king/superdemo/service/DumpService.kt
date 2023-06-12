package com.king.superdemo.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import java.io.FileDescriptor
import java.io.PrintWriter

class DumpService : Service() {

    //start:adb shell am startservice -n com.king.superdemo/.service.DumpService
    //dump:adb shell dumpsys activity service DumpService

    override fun onBind(intent: Intent?): IBinder? {
        TODO("Not yet implemented")
    }

    override fun dump(fd: FileDescriptor?, writer: PrintWriter?, args: Array<out String>?) {
        super.dump(fd, writer, args)
        writer?.println(this.packageName)
    }


}
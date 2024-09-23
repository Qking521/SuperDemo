package com.king.systemlibrary

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View

class SystemMainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.system_activity_main)
        Log.d("wq", "onCreate: systemlibrary")
    }

    fun registerProcessChange(view: View): Unit {
        val intent = Intent(this, SystemMainService::class.java)
        startService(intent)
    }
}
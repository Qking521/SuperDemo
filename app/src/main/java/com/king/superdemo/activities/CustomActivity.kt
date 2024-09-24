package com.king.superdemo.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import com.imuxuan.floatingview.FloatingView
import com.king.superdemo.R
import com.king.superdemo.fragments.CityPickerFragment
import com.king.superdemo.fragments.TimePickerFragment
import com.king.superdemo.service.FloatViewService
import com.king.systemlibrary.SystemMainActivity

/**
 * 展示自定义View和显示第三方控件的Activity
 */
class CustomActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_custom)
    }

    fun floatView(view: View) {
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "no permission", Toast.LENGTH_SHORT).show()
            startActivityForResult(Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName")), 1)
        } else {
            val tag = view.tag as String
            if (tag == "start") {
                startService(Intent(this, FloatViewService::class.java))
                FloatingView.get().add();
                view.tag = "stop"
            } else {
                stopService(Intent(this, FloatViewService::class.java))
                view.tag = "start"
                FloatingView.get().remove();
            }
        }

    }

    override fun onStart() {
        super.onStart()
        FloatingView.get().attach(this)
    }

    override fun onStop() {
        super.onStop()
        FloatingView.get().detach(this)
    }

    fun setVisible(view: View) {
        if (view.visibility == View.VISIBLE) {
            view.visibility = View.GONE
        } else {
            view.visibility = View.VISIBLE
        }
    }

    fun showCityPicker(view: View?) {
        val fragmentTransaction = supportFragmentManager.beginTransaction()
        val cityPickerFragment = CityPickerFragment()
        fragmentTransaction.replace(R.id.custom_fragment, cityPickerFragment)
        fragmentTransaction.commit()
    }

    fun showTimePicker(view: View) {
        val fragmentTransaction = supportFragmentManager.beginTransaction();
        val timePickerFragment = TimePickerFragment()
        fragmentTransaction.replace(R.id.custom_fragment, timePickerFragment)
        fragmentTransaction.commit()
    }

    fun systemInterface(view: View) {
        val intent = Intent(this, SystemMainActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        startActivity(intent)
    }
}
package com.king.superdemo.activities

import android.annotation.SuppressLint
import android.app.StatusBarManager
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import com.king.superdemo.R
import com.king.superdemo.fragments.AppLeftFragment
import com.king.superdemo.fragments.AppLeftFragment.CallBack
import com.king.superdemo.fragments.AppRightFragment

class AppManagerActivity : BaseActivity() {

    lateinit var leftFragment: AppLeftFragment
    lateinit var rightFragment: AppRightFragment

    @RequiresApi(Build.VERSION_CODES.Q)
    @SuppressLint("WrongConstant")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_manager_layout)
        leftFragment = supportFragmentManager.findFragmentById(R.id.app_left_fragment) as AppLeftFragment
        rightFragment = supportFragmentManager.findFragmentById(R.id.app_right_fragment) as AppRightFragment
        leftFragment!!.setOnItemClickListener {position : Int -> update(position) }
        val statusBarManager: StatusBarManager = getSystemService(Context.STATUS_BAR_SERVICE) as StatusBarManager

    }

    private fun update(position: Int) {
        val bundle = Bundle()
        bundle.putInt("position", position)
        rightFragment!!.arguments = bundle
        Log.i("wq", "onItemClick: position=$position")
    }
}
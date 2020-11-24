package com.king.superdemo.activities

import android.os.Bundle
import android.util.Log
import com.king.superdemo.R
import com.king.superdemo.fragments.AppLeftFragment
import com.king.superdemo.fragments.AppLeftFragment.CallBack
import com.king.superdemo.fragments.AppRightFragment

class AppManagerActivity : BaseActivity() {
    var leftFragment: AppLeftFragment? = null
    var rightFragment: AppRightFragment? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.app_manager_layout)
        leftFragment = supportFragmentManager.findFragmentById(R.id.app_left_fragment) as AppLeftFragment?
        rightFragment = supportFragmentManager.findFragmentById(R.id.app_right_fragment) as AppRightFragment?
        leftFragment!!.setOnItemClickListener {position : Int -> update(position) }
    }

    private fun update(position: Int) {
        val bundle = Bundle()
        bundle.putInt("position", position)
        rightFragment!!.arguments = bundle
        Log.i("wq", "onItemClick: position=$position")
    }
}
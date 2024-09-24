package com.king.superdemo.activities

import android.content.ComponentName
import android.content.Intent
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.king.superdemo.R
import java.util.*

class MainActivity : AppCompatActivity() {
    var mListView: ListView? = null
    var mActivityInfoList: MutableList<ActivityInfo> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initData()
        initView()
    }

    private fun initData() {
        try {
            val packageInfo = packageManager.getPackageInfo(packageName, PackageManager.GET_ACTIVITIES)
            if (packageInfo.activities != null) {
                for (activity in packageInfo.activities) {
                    //判断Activity是否是传入的activity的父类
                    if (BaseActivity::class.java.isAssignableFrom(Class.forName(activity.name))) {
                        mActivityInfoList.add(activity)
                    }
                }
            }
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        } catch (e: ClassNotFoundException) {
            e.printStackTrace()
        }
    }

    private fun initView() {
        mListView = findViewById(R.id.main_listview)
        mListView?.onItemClickListener = OnItemClickListener { _, _, position, _ ->
            Log.i("wq", "onItemClick: position=$position")
            val intent = Intent()
            var clazz: Class<*>? = null
            try {
                clazz = Class.forName(mActivityInfoList[position].name)
            } catch (e: ClassNotFoundException) {
                e.printStackTrace()
            }
            if (clazz != null) {
                val componentName = ComponentName(applicationContext, clazz)
                intent.component = componentName
                startActivity(intent)
            }
        }
        var adapter = ArrayAdapter(
                this,
                android.R.layout.simple_list_item_1,
                mActivityInfoList.map { x -> if (x.labelRes > 0) x.loadLabel(packageManager) else x.name })
        mListView?.setAdapter(adapter)
    }
}
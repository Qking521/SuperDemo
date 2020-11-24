package com.king.superdemo.fragments

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.king.recyclerviewlibrary.CommonAdapter
import com.king.recyclerviewlibrary.CommonItem
import com.king.recyclerviewlibrary.CommonRecyclerView
import com.king.superdemo.R
import kotlinx.coroutines.*
import java.util.stream.Collectors
import kotlin.coroutines.CoroutineContext

class AppRightFragment : BaseFragment() {
    private var mRecyclerView: CommonRecyclerView? = null
    private var mCommonAdapter: CommonAdapter? = null
    private var mContext: Context? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        mContext = context
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun setArguments(args: Bundle?) {
        super.setArguments(args)
        val bundle = arguments
        if (bundle != null) {
            val position = bundle.getInt("position")
            val itemList = getItemList(position)
            mCommonAdapter!!.setCommonItemList(itemList)
            mCommonAdapter!!.notifyDataSetChanged()
        }
    }

    private fun getItemList(position: Int): List<CommonItem>? {
        return when (position) {
            0 -> allApps
            1 -> systemApps
            2 -> launcherApps
            else -> null
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.app_right_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mRecyclerView = view.findViewById(R.id.app_right_fragment_recycler_view)
        mRecyclerView!!.setLayoutManager(LinearLayoutManager(view.context))
        mCommonAdapter = CommonAdapter(view.context)
        //使用协程获取数据,否则再UI线程中获取数据会阻塞UI线程,当数据量较大时,会有明显的卡顿
        GlobalScope.launch {
            mCommonAdapter!!.setCommonItemList(allApps)
            //使用withContext切换到主线程更新UI
            withContext(Dispatchers.Main) {mRecyclerView!!.setAdapter(mCommonAdapter)}
        }
    }

    //获取所有应用
    private val allApps: List<CommonItem>
        private get() {
            val packageManager = mContext!!.packageManager
            val packageInfoList = packageManager.getInstalledPackages(0)
            return mCommonAdapter!!.covertToCommonHolder(packageInfoList)
        }

    //获取系统应用
    private val systemApps: List<CommonItem>
        private get() {
            val packageManager = mContext!!.packageManager
            val packageInfoList = packageManager.getInstalledPackages(0)
                    .stream()
                    .filter { it.applicationInfo.flags and ApplicationInfo.FLAG_SYSTEM == ApplicationInfo.FLAG_SYSTEM }
                    .collect(Collectors.toList())
            return mCommonAdapter!!.covertToCommonHolder(packageInfoList)
        }

    //获取桌面上的应用
    private val launcherApps: List<CommonItem>
        private get() {
            val packageManager = mContext!!.packageManager
            val intent = Intent()
            intent.addCategory(Intent.CATEGORY_LAUNCHER)
            intent.action = Intent.ACTION_MAIN
            val resolveInfoList = packageManager.queryIntentActivities(intent, 0)
            return mCommonAdapter!!.covertToCommonHolder(resolveInfoList)
        }
}
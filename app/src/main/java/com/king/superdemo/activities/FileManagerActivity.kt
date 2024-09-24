package com.king.superdemo.activities

import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.KeyEvent
import android.view.Menu
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import com.king.permission.PermissionBean
import com.king.permission.PermissionCallback
import com.king.permission.PermissionUtil
import com.king.recyclerviewlibrary.CommonAdapter
import com.king.recyclerviewlibrary.CommonRecyclerView
import com.king.superdemo.R
import com.king.superdemo.extension.requestPermission
import com.king.superdemo.utils.FileUtil
import java.io.File
import java.util.*

class FileManagerActivity : BaseActivity() {
    private var mCurrentFile: File? = null

    var mFilePathList: MutableList<File?>? = ArrayList()
    var mParentFileList: MutableList<File>? = ArrayList()
    var mCommonRecyclerView: CommonRecyclerView? = null
    var mCommonAdapter: CommonAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_file_manager)
        mCommonRecyclerView = findViewById<View>(R.id.file_recyclerview) as CommonRecyclerView
        requestPermission(PermissionBean(PermissionUtil.PERMISSION_READ_EXTERNAL_STORAGE, PermissionCallback { if (it) fileManager() }))
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.file_manager_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    fun fileManager() {
        if (Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED) {
            val rootFile = ROOT_FILE
            mCommonAdapter = CommonAdapter(this)
            mCommonAdapter!!.covertToCommonHolder(mFilePathList)
            mCommonRecyclerView!!.adapter = mCommonAdapter
            mCommonRecyclerView!!.setOnItemClickListener { _, _, position: Int, _ -> operateFile(mParentFileList!![position]) }
            scanFile(rootFile)
        } else {
            Toast.makeText(this, "no external storage", Toast.LENGTH_SHORT).show()
        }
    }

    private fun operateFile(file: File) {
        if (file.isDirectory) {
            scanFile(file)
        } else {
            viewFile(file)
        }
    }

    private fun viewFile(file: File) {
        startActivity(FileUtil.getFileIntent(this, file))
    }

    private fun scanFile(file: File) {
        mCurrentFile = file
        Log.v("wq", "scanFile file name =" + file.name + " ,file path=" + file.path)
        if (mFilePathList != null && !mFilePathList!!.isEmpty()) mFilePathList!!.clear()
        if (mParentFileList != null && !mParentFileList!!.isEmpty()) {
            mParentFileList!!.clear()
        }
        val files = file.listFiles()
        Log.i("wq", "scanFile: isDirectory=" + file.isDirectory)
        if (files != null && files.size > 0) {
            for (childFile in files) {
                mParentFileList!!.add(childFile)
                mFilePathList!!.add(childFile)
            }
        } else {
            Log.i("wq", "scanFile: file == null")
        }
        refresh()
    }

    private fun refresh() {
        mCommonAdapter!!.covertToCommonHolder(mFilePathList)
        mCommonAdapter!!.notifyDataSetChanged()
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        if (keyCode == KeyEvent.KEYCODE_BACK && mCurrentFile != null && mCurrentFile != ROOT_FILE) {
            scanFile(mCurrentFile!!.parentFile)
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    companion object {
        val ROOT_FILE = Environment.getExternalStorageDirectory()
    }
}
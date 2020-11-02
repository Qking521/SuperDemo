package com.king.superdemo.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.king.permission.PermissionBean;
import com.king.permission.PermissionUtil;
import com.king.recyclerviewlibrary.CommonAdapter;
import com.king.recyclerviewlibrary.CommonRecyclerView;
import com.king.superdemo.R;
import com.king.superdemo.utils.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class FileManagerActivity extends BaseActivity {

    public static final File ROOT_FILE = Environment.getExternalStorageDirectory();
    private File mCurrentFile;
    //file manager
    List<String> mFilePathList = new ArrayList<String>();
    List<File> mParentFileList = new ArrayList<File>();

    CommonRecyclerView mFileListView;
    CommonAdapter mCommonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);
        mFileListView = (CommonRecyclerView)findViewById(R.id.file_recyclerview);
        mFileListView.setLayoutManager(new LinearLayoutManager(this));
        requestPermission(
                new PermissionBean(PermissionUtil.PERMISSION_READ_EXTERNAL_STORAGE, storage -> {  if (storage) fileManager();}));
    }

    public void fileManager() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File externalFile = ROOT_FILE;
            mCommonAdapter = new CommonAdapter(mFilePathList, CommonAdapter.CONVERT_TYPE_TITLE);
            mFileListView.setAdapter(mCommonAdapter);
            mFileListView.setOnItemClickListener((parent, view, position, id) -> operateFile(mParentFileList.get(position)));
            scanFile(externalFile);
        }else {
            Toast.makeText(this, "no external storage", Toast.LENGTH_SHORT).show();
        }
    }

    private void operateFile(File file) {
        if (file.isDirectory()){
            scanFile(file);
        }else {
            viewFile(file);
        }
    }

    private void viewFile(File file) {
        startActivity(FileUtil.getFileIntent(this, file));
    }

    private void scanFile(File parentFile) {
        mCurrentFile = parentFile;
        Log.v("wq", "scanFile file name =" + parentFile.getName() + " ,file path=" + parentFile.getPath());
        if (mFilePathList != null && !mFilePathList.isEmpty())
            mFilePathList.clear();
        if (mParentFileList != null && !mParentFileList.isEmpty()) {
            mParentFileList.clear();
        }
        File[] files = parentFile.listFiles();
        Log.i("wq", "scanFile: isDirectory="+ parentFile.isDirectory());
        if (files != null && files.length > 0) {
            for (File file : files) {
                mParentFileList.add(file);
                mFilePathList.add(file.getName());
            }
        } else {
            Log.i("wq", "scanFile: file == null");
        }
        refresh();
    }

    private void refresh() {
        mCommonAdapter.covertToCommonHolder(mFilePathList, 1);
        mCommonAdapter.notifyDataSetChanged();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mCurrentFile != null && !mCurrentFile.equals(ROOT_FILE)){
            scanFile(mCurrentFile.getParentFile());
            return  true;
        }
        return super.onKeyDown(keyCode, event);
    }
}

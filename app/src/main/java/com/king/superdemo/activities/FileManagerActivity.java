package com.king.superdemo.activities;

import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.king.permission.PermissionBean;
import com.king.permission.PermissionUtil;
import com.king.recyclerviewlibrary.CommonAdapter;
import com.king.recyclerviewlibrary.CommonItem;
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
    List<File> mFilePathList = new ArrayList<File>();
    List<File> mParentFileList = new ArrayList<File>();
    List<CommonItem> mCommonItemList = new ArrayList<>();

    CommonRecyclerView mCommonRecyclerView;
    CommonAdapter mCommonAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);
        mCommonRecyclerView = (CommonRecyclerView)findViewById(R.id.file_recyclerview);
        requestPermission(
                new PermissionBean(PermissionUtil.PERMISSION_READ_EXTERNAL_STORAGE, storage -> {  if (storage) fileManager();}));
    }

    public void fileManager() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File rootFile = ROOT_FILE;
            mCommonAdapter = new CommonAdapter(this);
            mCommonAdapter.covertToCommonHolder(mFilePathList);
            mCommonRecyclerView.setAdapter(mCommonAdapter);
            mCommonRecyclerView.setOnItemClickListener((parent, view, position, id) -> operateFile(mParentFileList.get(position)));
            scanFile(rootFile);
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

    private void scanFile(File file) {
        mCurrentFile = file;
        Log.v("wq", "scanFile file name =" + file.getName() + " ,file path=" + file.getPath());
        if (mFilePathList != null && !mFilePathList.isEmpty())
            mFilePathList.clear();
        if (mParentFileList != null && !mParentFileList.isEmpty()) {
            mParentFileList.clear();
        }
        File[] files = file.listFiles();
        Log.i("wq", "scanFile: isDirectory="+ file.isDirectory());
        if (files != null && files.length > 0) {
            for (File childFile : files) {
                mParentFileList.add(childFile);
                mFilePathList.add(childFile);
            }
        } else {
            Log.i("wq", "scanFile: file == null");
        }
        refresh();
    }

    private void refresh() {
        mCommonAdapter.covertToCommonHolder(mFilePathList);
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

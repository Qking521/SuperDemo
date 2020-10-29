package com.king.superdemo.activities;

import android.Manifest;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.king.permission.PermissionBean;
import com.king.permission.PermissionCallback;
import com.king.permission.PermissionUtil;
import com.king.superdemo.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;

public class FileManagerActivity extends BaseActivity {

    public static String TYPE_UNKNOW = "*/*";//未知类型
    public static String TYPE_PICTURE = "image/*";//代表图片类型
    public static String TYPE_VIDEO = "video/*";//代表视频
    public static String TYPE_AUDIO = "audio/*";//代表音频
    public static String TYPE_TEXT = "text/*";//代表文档

    //file manager
    List<String> mFilePathList = new ArrayList<String>();
    List<File> mParentFileList = new ArrayList<File>();

    ListView mFileListView;
    ArrayAdapter mFileAdapter;
    File mCurrentFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_manager);
        mFileListView = (ListView)findViewById(R.id.file_listview);
        requestPermission(
                new PermissionBean(PermissionUtil.PERMISSION_READ_EXTERNAL_STORAGE, storage -> {  if (storage) fileManager();}),
                new PermissionBean(PermissionUtil.PERMISSION_ACCESS_FINE_LOCATION,  isContact -> Log.i("wq", "onCreate: contact="+ isContact)));
    }

    public void fileManager() {
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            File externalFile = Environment.getExternalStorageDirectory();

            mFileAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mFilePathList);
            mFileListView.setAdapter(mFileAdapter);
            mFileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    File parentFile = mParentFileList.get(position);
                    if (parentFile.isDirectory()){
                        Log.v("wq", "onItemClick file name =" + parentFile.getName());
                        mFileListView.setAdapter(mFileAdapter);
                        scanFile(parentFile);
                    }else {
                        Intent viewIntent = new Intent(Intent.ACTION_VIEW);
                        viewIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        String type = getMIMEType(parentFile);
                        viewIntent.setDataAndType(Uri.fromFile(parentFile), type);
                        FileManagerActivity.this.startActivity(viewIntent);
                    }
                }
            });
            scanFile(externalFile);
        }else {
            Toast.makeText(this, "no external storage", Toast.LENGTH_SHORT).show();
        }

    }


    //根据文件后缀名,返回文件类型
    private String getMIMEType(File file){
        String fileName = file.getName();
        if(fileName.endsWith(".gif")||fileName.endsWith(".jpg")||fileName.endsWith(".png")
                ||fileName.endsWith(".GIF")||fileName.endsWith(".JPG")||fileName.endsWith(".PNG"))
            return TYPE_PICTURE;
        if(fileName.endsWith(".mp3"))
            return TYPE_AUDIO;
        if(fileName.endsWith(".mp4")||fileName.endsWith(".rmvb")||fileName.endsWith(".rm"))
            return TYPE_VIDEO;
        if(fileName.endsWith(".txt")||fileName.endsWith(".html"))
            return TYPE_TEXT;
        return TYPE_UNKNOW;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && mCurrentFile != null && !mCurrentFile.equals(Environment.getExternalStorageDirectory())){
            scanFile(mCurrentFile.getParentFile());
            return  true;
        }else {
            return super.onKeyDown(keyCode, event);
        }

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
        if (files != null) {
            for (File file : files) {
                mParentFileList.add(file);
                mFilePathList.add(file.getName());
                mFileAdapter.notifyDataSetChanged();
            }
        } else {
            Log.i("wq", "scanFile: file == null");
        }
//        Optional.ofNullable(files).ifPresent(files1 -> {
//            Stream.of(files1).forEach(file -> {
//                mParentFileList.add(file);
//                mFilePathList.add(file.getName());});
//            mFileAdapter.notifyDataSetChanged();
//        });
    }

}

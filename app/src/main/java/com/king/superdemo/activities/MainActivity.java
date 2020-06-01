package com.king.superdemo.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.king.superdemo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {


    ListView mListView;
    List<ActivityInfo> mActivityInfoList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();
        initView();
    }

    private void initData() {
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_ACTIVITIES);
            if (packageInfo.activities != null) {
                for (ActivityInfo activity : packageInfo.activities) {
                    //判断Activity是否是传入的activity的父类
                    if (BaseActivity.class.isAssignableFrom(Class.forName(activity.name))) {
                        mActivityInfoList.add(activity);
                    }
                }
            }
        } catch (PackageManager.NameNotFoundException | ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void initView() {
        mListView = findViewById(R.id.main_listview);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("wq", "onItemClick: position=" + position);
                Intent intent = new Intent();
                Class clazz = null;
                try {
                    clazz = Class.forName(mActivityInfoList.get(position).name);
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                if (clazz != null) {
                    ComponentName componentName = new ComponentName(getApplicationContext(), clazz);
                    intent.setComponent(componentName);
                    startActivity(intent);
                }
            }
        });
        mListView.setAdapter(new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                mActivityInfoList.stream()
                        .map(x -> x.labelRes > 0 ? x.loadLabel(getPackageManager()) : x.name)
                        .collect(Collectors.toList())));
    }
}

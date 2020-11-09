package com.king.superdemo.activities;

import android.os.Bundle;
import android.util.Log;
import android.widget.AdapterView;

import androidx.annotation.Nullable;

import com.king.superdemo.R;
import com.king.superdemo.fragments.AppLeftFragment;
import com.king.superdemo.fragments.AppRightFragment;

public class AppManagerActivity extends BaseActivity {

    AppLeftFragment leftFragment;
    AppRightFragment rightFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_manager_layout);
        leftFragment = (AppLeftFragment)getSupportFragmentManager().findFragmentById(R.id.app_left_fragment);
        rightFragment = (AppRightFragment)getSupportFragmentManager().findFragmentById(R.id.app_right_fragment);
        leftFragment.setOnItemClickListener(position -> update(position));
    }

    private void update(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("position", position);
        rightFragment.setArguments(bundle);
        Log.i("wq", "onItemClick: position="+ position);
    }
}

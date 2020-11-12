package com.king.superdemo.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.king.superdemo.R;
import com.king.superdemo.fragments.CityPickerFragment;
import com.king.superdemo.service.FloatViewService;
import com.lljjcoder.Interface.OnCityItemClickListener;
import com.lljjcoder.bean.CityBean;
import com.lljjcoder.bean.DistrictBean;
import com.lljjcoder.bean.ProvinceBean;
import com.lljjcoder.citywheel.CityConfig;
import com.lljjcoder.style.cityjd.JDCityConfig;
import com.lljjcoder.style.cityjd.JDCityPicker;
import com.lljjcoder.style.citylist.CityListSelectActivity;
import com.lljjcoder.style.citylist.bean.CityInfoBean;
import com.lljjcoder.style.citylist.utils.CityListLoader;
import com.lljjcoder.style.citypickerview.CityPickerView;
import com.lljjcoder.style.citythreelist.ProvinceActivity;

import java.util.HashMap;

import kotlin.reflect.KFunction;

/**
 * 展示自定义View和显示第三方控件的Activity
 */
public class CustomActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_custom);
    }

    public void floatView(View view) {
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "no permission", Toast.LENGTH_SHORT).show();
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName())), 1);
        }else {
            String tag = (String)view.getTag();
            if (tag.equals("start")) {
                startService(new Intent(this, FloatViewService.class));
                view.setTag("stop");
            } else {
                stopService(new Intent(this, FloatViewService.class));
                view.setTag("start");
            }
        }
    }

    public void setVisible(View view) {
        if (view.getVisibility() == View.VISIBLE) {
            view.setVisibility(View.GONE);
        } else {
            view.setVisibility(View.VISIBLE);
        }
    }

    public void showCityPicker(View view) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        CityPickerFragment cityPickerFragment = new CityPickerFragment();
        fragmentTransaction.replace(R.id.custom_fragment, cityPickerFragment);
        fragmentTransaction.commit();
    }

}

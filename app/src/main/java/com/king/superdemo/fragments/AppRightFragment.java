package com.king.superdemo.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.king.recyclerviewlibrary.CommonAdapter;
import com.king.recyclerviewlibrary.CommonItem;
import com.king.recyclerviewlibrary.CommonRecyclerView;
import com.king.superdemo.R;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class AppRightFragment extends BaseFragment{

    private CommonRecyclerView mRecyclerView;
    private CommonAdapter mCommonAdapter;
    private Context mContext;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void setArguments(@Nullable Bundle args) {
        super.setArguments(args);
        Bundle bundle = getArguments();
        if (bundle != null) {
            int position = bundle.getInt("position");
            List<CommonItem> itemList = getItemList(position);
            mCommonAdapter.setCommonItemList(itemList);
            mCommonAdapter.notifyDataSetChanged();
        }

    }

    private List<CommonItem> getItemList(int position) {
        switch (position) {
            case 0 : return  getAllApps();
            case 1 : return getSystemApps();
            case 2 : return getLauncherApps();
            default: return null;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.app_right_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.app_right_fragment_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        mCommonAdapter = new CommonAdapter(view.getContext());
        mCommonAdapter.setCommonItemList(getAllApps());
        mRecyclerView.setAdapter(mCommonAdapter);
    }

    //获取所有应用
    private  List<CommonItem>  getAllApps() {
        PackageManager packageManager = mContext.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0);
        List<CommonItem> commonItemList = mCommonAdapter.covertToCommonHolder(packageInfoList);
        return commonItemList;
    }

    //获取系统应用
    private List<CommonItem> getSystemApps() {
        PackageManager packageManager = mContext.getPackageManager();
        List<PackageInfo> packageInfoList = packageManager.getInstalledPackages(0)
                .stream()
                .filter(packageInfo -> (packageInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM)
                .collect(Collectors.toList());
        List<CommonItem> commonItemList = mCommonAdapter.covertToCommonHolder(packageInfoList);
        return commonItemList;
    }

    //获取桌面上的应用
    private List<CommonItem> getLauncherApps() {
        PackageManager packageManager = mContext.getPackageManager();
        Intent intent = new Intent();
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setAction(Intent.ACTION_MAIN);
        List<ResolveInfo> resolveInfoList = packageManager.queryIntentActivities(intent, 0);

        List<CommonItem> commonItemList = mCommonAdapter.covertToCommonHolder(resolveInfoList);
        return commonItemList;
    }
}

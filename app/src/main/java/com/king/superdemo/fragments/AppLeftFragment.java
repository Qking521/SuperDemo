package com.king.superdemo.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.king.superdemo.R;

public class AppLeftFragment extends BaseFragment {

    public interface CallBack {
        void onItemClick(int position);
    }

    private ListView mListView;
    public static String[] datas = new String[]{"已安装所有应用", "已安装系统应用", "桌面上的所有应用"};
    private CallBack callBack;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.app_left_fragment, container, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mListView = view.findViewById(R.id.app_left_fragment_lv);
        mListView.setAdapter(new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1,datas));
        mListView.setOnItemClickListener((parent, view1, position, id) -> callBack.onItemClick(position));
    }

    public void setOnItemClickListener(CallBack callBack) {
        this.callBack = callBack;
    }
}

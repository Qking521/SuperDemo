package com.king.recyclerviewlibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

public class CommonRecyclerView extends RecyclerView {

    private AdapterView.OnItemClickListener itemClickListener;
    private AdapterView.OnItemLongClickListener itemLongClickListener;

    public CommonRecyclerView(@NonNull Context context) {
        super(context);
    }

    public CommonRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CommonRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
        CommonAdapter commonAdapter = getCommonAdapter();
        if (commonAdapter != null) {
            commonAdapter.setOnItemClickListener(itemClickListener);
        }
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
        CommonAdapter commonAdapter = getCommonAdapter();
        if (commonAdapter != null) {
            commonAdapter.setOnItemLongClickListener(itemLongClickListener);
        }
    }

    private CommonAdapter getCommonAdapter() {
        Adapter adapter = getAdapter();
        CommonAdapter commonAdapter = null;
        if (adapter != null) {
            if (adapter instanceof CommonAdapter) {
                commonAdapter = (CommonAdapter) adapter;
            }
        }
        return commonAdapter;
    }
}

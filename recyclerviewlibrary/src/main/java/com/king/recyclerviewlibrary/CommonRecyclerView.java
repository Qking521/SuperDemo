package com.king.recyclerviewlibrary;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class CommonRecyclerView extends RecyclerView {

    private AdapterView.OnItemClickListener itemClickListener;
    private AdapterView.OnItemLongClickListener itemLongClickListener;

    public CommonRecyclerView(@NonNull Context context) {
        super(context);
    }

    public CommonRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CommonRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setDefaultConfig(context);
    }

    /**
     * recyclerView的默认设置
     * @param context
     */
    private void setDefaultConfig(Context context) {
        setLayoutManager(new LinearLayoutManager(context));
        addItemDecoration(new DividerItemDecoration(context, DividerItemDecoration.VERTICAL));
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

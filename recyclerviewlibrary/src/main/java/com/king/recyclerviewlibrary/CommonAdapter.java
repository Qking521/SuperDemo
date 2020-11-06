package com.king.recyclerviewlibrary;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;


import com.bumptech.glide.Glide;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class CommonAdapter extends RecyclerView.Adapter<ViewHolder> {

    private Context mContext;
    private static List<CommonItem> mItemList = new ArrayList<>();;
    private AdapterView.OnItemClickListener itemClickListener;
    private AdapterView.OnItemLongClickListener itemLongClickListener;

    public static final int VIEW_TYPE_EMPTY = 1;
    public static final int VIEW_TYPE_NORMAL = 2;

    private RecyclerView.AdapterDataObserver adapterDataObserver = new RecyclerView.AdapterDataObserver() {
        @Override
        public void onChanged() {
            super.onChanged();

        }
    };

    public CommonAdapter(Context context, List<CommonItem> itemList) {
        mContext = context;
        mItemList = itemList;
    }

    public static List<CommonItem>  covertToCommonHolder(List<?> dataList) {
        mItemList.clear();
        for (Object o : dataList) {
            mItemList.add(Utils.getCommonItem(o));
        }
        return mItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ViewHolder viewHolder;
        if (viewType == VIEW_TYPE_EMPTY) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_empty_layout, parent, false);
            viewHolder = new EmptyViewHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recyclerview_item_layout, null);
            viewHolder = new CommonViewHolder(view);
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Log.i("wq", "onBindViewHolder: ");
        if (holder instanceof CommonViewHolder) {
            CommonViewHolder commonViewHolder = (CommonViewHolder)holder;
            CommonItem item = mItemList.get(position);
            setItemIcon(commonViewHolder.icon, item.file);
            commonViewHolder.title.setText(item.title);
            commonViewHolder.summary.setText(item.summary);
            commonViewHolder.arrow.setImageDrawable(item.arrowDrawable);
            commonViewHolder.itemView.setOnClickListener(v -> itemClickListener.onItemClick(null, commonViewHolder.itemView, position, -1));
            commonViewHolder.itemView.setOnLongClickListener(v -> itemLongClickListener.onItemLongClick(null, commonViewHolder.itemView, position, -1));
        } else if (holder instanceof EmptyViewHolder) {
            EmptyViewHolder emptyViewHolder = (EmptyViewHolder) holder;
        }

    }

    private void setItemIcon(ImageView src, File file) {
        Log.i("wq", "setImageDrawable: ");
        Glide.with(mContext)
                .load(file)
                .into(src);
    }


    @Override
    public int getItemCount() {
        //mItemList.size()为0的话，只引入一个布局，就是emptyView, 那么这个recyclerView的itemCount为1
        if (mItemList.size() == 0) {
            return 1;
        }
        return mItemList.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (mItemList.size() == 0) {
            return VIEW_TYPE_EMPTY;
        } else {
            return VIEW_TYPE_NORMAL;
        }
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener itemLongClickListener) {
        this.itemLongClickListener = itemLongClickListener;
    }
}
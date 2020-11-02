package com.king.recyclerviewlibrary;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CommonViewHolder extends RecyclerView.ViewHolder {

    public ImageView icon;
    public TextView title;
    public TextView summary;
    public ImageView arrow;

    public CommonViewHolder(@NonNull View itemView) {
        super(itemView);
        icon = (ImageView) itemView.findViewById(R.id.recycler_item_icon);
        title = (TextView) itemView.findViewById(R.id.recycler_item_title);
        summary = (TextView) itemView.findViewById(R.id.recycler_item_summary);
        arrow = (ImageView) itemView.findViewById(R.id.recycler_item_arrow);
    }
}

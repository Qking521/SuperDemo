package com.king.recyclerviewlibrary;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class EmptyViewHolder extends RecyclerView.ViewHolder {

    public TextView emptyTextView;
    public EmptyViewHolder(@NonNull View itemView) {
        super(itemView);
        emptyTextView = (TextView) itemView.findViewById(R.id.empty);
    }
}

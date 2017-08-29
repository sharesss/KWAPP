package com.ts.fmxt.ui.discover;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by liangzhenxiong on 2017/4/6 0006.
 * 视图Item
 */

public interface BaseViewItem {
    int getViewType();

    View createView(ViewGroup parent);

    void onBindViewHolder(RecyclerView.ViewHolder holder, int position);

    RecyclerViewHolder createViewHolder(ViewGroup parent);
}

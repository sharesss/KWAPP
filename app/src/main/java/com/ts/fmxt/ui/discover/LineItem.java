package com.ts.fmxt.ui.discover;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ts.fmxt.R;

/**
 * Created by liangzhenxiong on 2017/11/30.
 */

public class LineItem implements BaseViewItem{
    @Override
    public int getViewType() {
        return getClass().hashCode();
    }

    @Override
    public View createView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.lineitem_layout, parent, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public RecyclerViewHolder createViewHolder(ViewGroup parent) {
        return null;
    }
}

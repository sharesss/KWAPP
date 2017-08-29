package com.ts.fmxt.ui.discover;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ts.fmxt.R;
import com.ts.fmxt.ui.discover.view.CircleBar;

import http.data.ConsumerEntity;

/**
 */

public class DiscoverCircleItem implements BaseViewItem {
    ConsumerEntity info;

    public DiscoverCircleItem(ConsumerEntity info) {
        this.info = info;
    }

    @Override
    public int getViewType() {
        return getClass().hashCode();
    }

    @Override
    public View createView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.discircle_item_layout, parent, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;


    }

    @Override
    public RecyclerViewHolder createViewHolder(ViewGroup parent) {
        View view = createView(parent);
        return new ViewHolder(view);
    }

    private class ViewHolder extends RecyclerViewHolder {
        CircleBar ivCirclebar;

        private ViewHolder(View view) {
            super(view);
            ivCirclebar = (CircleBar) view.findViewById(R.id.iv_circlebar);
        }
    }
}

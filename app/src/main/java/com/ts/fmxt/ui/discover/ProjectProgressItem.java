package com.ts.fmxt.ui.discover;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ts.fmxt.R;

import http.data.ProgressUpdateEntity;

/**
 * Created by A1 on 2017/12/4.
 */

public class ProjectProgressItem implements BaseViewItem {
    ProgressUpdateEntity info;
    DiscoverDetailsActivity context;

    public ProjectProgressItem(Activity context) {
        this.context = (DiscoverDetailsActivity) context;
    }

    @Override
    public int getViewType() {
        return getClass().hashCode();
    }

    @Override
    public View createView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project_progress, parent, false);
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

        protected ViewHolder(View convertView) {
            super(convertView);
        }
    }
}


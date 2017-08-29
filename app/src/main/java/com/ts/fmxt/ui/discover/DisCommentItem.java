package com.ts.fmxt.ui.discover;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ts.fmxt.R;

import http.data.ConsumerCommentEntity;
import widget.image.CircleImageView;

/**
 */

public class DisCommentItem implements BaseViewItem {
    ConsumerCommentEntity entity;

    public DisCommentItem(ConsumerCommentEntity entity) {
        this.entity = entity;
    }

    @Override
    public int getViewType() {
        return getClass().hashCode();
    }

    @Override
    public View createView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_comment_view, parent, false);
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
        private TextView tv_comment, tv_name, tv_time, tv_reply;
        private CircleImageView iv_portrait;
        private TextView tv_likeCount, tv_isInvestAuthen, tv_isfounder, tv_replys;

        private ViewHolder(View convertView) {
            super(convertView);
        }
    }
}

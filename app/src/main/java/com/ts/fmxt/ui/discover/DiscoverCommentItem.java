package com.ts.fmxt.ui.discover;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ts.fmxt.R;
import com.ts.fmxt.ui.discover.view.KeyMapDailog;

import utils.UISKipUtils;

/**
 */

public class DiscoverCommentItem implements BaseViewItem, View.OnClickListener {
    int totalNum, desre, bedesre;
    DiscoverDetailsActivity activity;
    public int istype =0;


    public DiscoverCommentItem(int totalNum, int desre, int bedesre, DiscoverDetailsActivity activity) {
        this.totalNum = totalNum;
        this.desre = desre;
        this.bedesre = bedesre;
        this.activity = activity;
    }

    @Override
    public int getViewType() {
        return getClass().hashCode();
    }

    @Override
    public View createView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.discommentitem_layout, parent, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.tvAllReviews.setText("全部评论(" + totalNum + ")");
//        viewHolder.tvWorthThrowing.setText("值得投(" + desre + ")");
//        viewHolder.tvNoWorthThrowing.setText("不值得投(" + bedesre + ")");
        if (activity != null) {
            viewHolder.tvAllReviews.setOnClickListener(this);
            viewHolder.tvWorthThrowing.setOnClickListener(this);
            viewHolder.tvNoWorthThrowing.setOnClickListener(this);
        }
        if (istype == 0) {
            viewHolder.tvAllReviews.setTextColor(activity.getResources().getColor(R.color.orange));
            viewHolder.tvWorthThrowing.setTextColor(activity.getResources().getColor(R.color.black));
            viewHolder.tvNoWorthThrowing.setTextColor(activity.getResources().getColor(R.color.black));
        }
        if (istype == 1) {
            viewHolder.tvAllReviews.setTextColor(activity.getResources().getColor(R.color.black));
            viewHolder.tvWorthThrowing.setTextColor(activity.getResources().getColor(R.color.orange));
            viewHolder.tvNoWorthThrowing.setTextColor(activity.getResources().getColor(R.color.black));
        }
        if (istype == 2) {
            viewHolder.tvAllReviews.setTextColor(activity.getResources().getColor(R.color.black));
            viewHolder.tvWorthThrowing.setTextColor(activity.getResources().getColor(R.color.black));
            viewHolder.tvNoWorthThrowing.setTextColor(activity.getResources().getColor(R.color.orange));
        }

    }

    @Override
    public RecyclerViewHolder createViewHolder(ViewGroup parent) {
        View view = createView(parent);
        return new ViewHolder(view);
    }

    @Override
    public void onClick(View v) {
        if (!activity.checkLogin()) {
            return;
        }
        switch (v.getId()) {
            case R.id.tv_all_reviews:

                istype = 0;
                activity.CommentRequest(0);
                activity.adapter.notifyDataSetChanged();
                break;
            case R.id.tv_worth_throwing:
                istype = 1;
                activity.CommentRequest(1);
                activity.adapter.notifyDataSetChanged();
                break;
            case R.id.tv_no_worth_throwing:
                istype = 2;
                activity.CommentRequest(2);
                activity.adapter.notifyDataSetChanged();
                break;
            case R.id.tv_write_comment:
                KeyMapDailog dialog = new KeyMapDailog("评论是疯蜜范的最大动力", activity);
                dialog.show(activity.getSupportFragmentManager(), "评论");
                break;
        }
    }

    private class ViewHolder extends RecyclerViewHolder {
        TextView tvAllReviews, tvNoWorthThrowing, tvWorthThrowing;

        private ViewHolder(View convert) {
            super(convert);
            tvAllReviews = (TextView) convert.findViewById(R.id.tv_all_reviews);
            tvWorthThrowing = (TextView) convert.findViewById(R.id.tv_worth_throwing);
            tvNoWorthThrowing = (TextView) convert.findViewById(R.id.tv_no_worth_throwing);
            if (activity != null) {
                convert.findViewById(R.id.tv_write_comment).setOnClickListener(DiscoverCommentItem.this);
            }
        }
    }
}

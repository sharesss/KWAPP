package com.ts.fmxt.ui.discover;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ts.fmxt.R;
import com.ts.fmxt.ui.discover.view.CircleBar;
import com.ts.fmxt.ui.discover.view.RedCircleBar;

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
        viewHolder.ivCirclebar.startCustomAnimation();
        float progress = progressPercentage(info.getVoteNum(), info.getDokels());
        float percentage = num(info.getVoteNum(), info.getDokels());
        int progres = Math.round(percentage);
        viewHolder.ivCirclebar.setText(String.valueOf(progres));//中间的数字
        viewHolder.ivCirclebar.setSweepAngle(progress);//进度
        viewHolder.ivRedCirclebar.startCustomAnimation();

        viewHolder.ivRedCirclebar.startCustomAnimation();
        float inxe = progressPercentage(info.getVoteNum(), info.getNotDokels());
        float percen = num(info.getVoteNum(), info.getNotDokels());
        int progre = Math.round(percen);
        viewHolder.ivRedCirclebar.setText(String.valueOf(progre));//中间的数字
        viewHolder.ivRedCirclebar.setSweepAngle(inxe);//进度
        if (info.getIsVote() == 0) {
//            view.findViewById(R.id.ll_dokels).setOnClickListener(this);
//            view.findViewById(R.id.ll_notdokels).setOnClickListener(this);
        } else if (info.getIsVote() == 1) {
//            viewHolder.tvWorth.setBackground(getResources().getDrawable(R.drawable.bg_gray_circle));
//            viewHolder.tvWorth.setTextColor(getResources().getColor(R.color.gray));
//            viewHolder.tvNoworth.setBackground(getResources().getDrawable(R.drawable.bg_gray_circle));
//            viewHolder.tvNoworth.setTextColor(getResources().getColor(R.color.gray));
//            type = 1;
        } else if (info.getIsVote() == 2) {
//            viewHolder.tvWorth.setBackground(getResources().getDrawable(R.drawable.bg_gray_circle));
//            viewHolder.tvWorth.setTextColor(getResources().getColor(R.color.gray));
//            viewHolder.tvNoworth.setBackground(getResources().getDrawable(R.drawable.bg_gray_circle));
//            viewHolder.tvNoworth.setTextColor(getResources().getColor(R.color.gray));
//            type = 2;
        }

    }

    @Override
    public RecyclerViewHolder createViewHolder(ViewGroup parent) {
        View view = createView(parent);
        return new ViewHolder(view);
    }

    private class ViewHolder extends RecyclerViewHolder {
        CircleBar ivCirclebar;
        RedCircleBar ivRedCirclebar;
        TextView tvWorth, tvNoworth;

        private ViewHolder(View view) {
            super(view);
            ivCirclebar = (CircleBar) view.findViewById(R.id.iv_circlebar);
            ivRedCirclebar = (RedCircleBar) view.findViewById(R.id.iv_redcirclebar);
            tvWorth = (TextView)view.findViewById(R.id.tv_worth);
            tvNoworth = (TextView) view.findViewById(R.id.tv_noworth);
        }
    }

    private float progressPercentage(float max, float min) {
        // TODO Auto-generated method stub
        float percentage = min / max;
        float progress = percentage * 360;
        return progress;

    }

    private float num(float max, float min) {
        // TODO Auto-generated method stub
        float percentage = min / max;
        float progress = percentage * 100;
        return progress;

    }
}

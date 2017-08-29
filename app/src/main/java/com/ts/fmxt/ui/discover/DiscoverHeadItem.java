package com.ts.fmxt.ui.discover;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ts.fmxt.R;

import http.data.ConsumerEntity;
import widget.image.FMNetImageView;

/**
 */

public class DiscoverHeadItem implements BaseViewItem {
    ConsumerEntity info;

    public DiscoverHeadItem(ConsumerEntity info) {
        this.info = info;
    }

    @Override
    public int getViewType() {
        return getClass().hashCode();
    }

    @Override
    public View createView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.dis_head_item_layout, parent, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.ivImage.loadImage(info.getInvestPhoto());
        viewHolder.tvBrandName.setText(info.getInvestName());
        viewHolder.tvBrandDetails.setText(info.getInvestDeion());
        Double index = info.getExponent() * 100;
        int exponent = (new Double(index)).intValue();
        if (exponent < 80) {
            viewHolder.pbIndex.setProgress(exponent);
            viewHolder.pbIndex.setVisibility(View.VISIBLE);
            viewHolder.pbGreenindex.setVisibility(View.GONE);
        } else {
            viewHolder.pbGreenindex.setProgress(exponent);
            viewHolder.pbGreenindex.setVisibility(View.VISIBLE);
            viewHolder.pbIndex.setVisibility(View.GONE);
        }
        viewHolder.tvIndex.setText(exponent + "%");

    }

    @Override
    public RecyclerViewHolder createViewHolder(ViewGroup parent) {
        View view = createView(parent);
        return new ViewHolder(view);
    }

    private class ViewHolder extends RecyclerViewHolder {
        FMNetImageView ivImage;
        TextView tvBrandName;
        TextView tvBrandDetails;
        TextView tvIndex;
        ProgressBar pbIndex;
        ProgressBar pbGreenindex;

        private ViewHolder(View view) {
            super(view);
            ivImage = (FMNetImageView) view.findViewById(R.id.iv_image);
            tvBrandName = (TextView) view.findViewById(R.id.tv_brand_name);
            tvBrandDetails = (TextView) view.findViewById(R.id.tv_brand_details);
            pbIndex = (ProgressBar) view.findViewById(R.id.pb_index);
            pbGreenindex = (ProgressBar) view.findViewById(R.id.pb_greenindex);
            tvIndex = (TextView) view.findViewById(R.id.tv_index);
        }
    }
}

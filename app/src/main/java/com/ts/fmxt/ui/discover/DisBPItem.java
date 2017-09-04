package com.ts.fmxt.ui.discover;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ts.fmxt.R;
import com.ts.fmxt.ui.discover.view.seekBar;

import http.data.InvestBPListEntity;
import widget.image.FMNetImageView;

/**
 *
 */

public class DisBPItem implements BaseViewItem {
    InvestBPListEntity info;
    int investId;

    public DisBPItem(InvestBPListEntity info, int investId) {
        this.info = info;
        this.investId = investId;
    }

    @Override
    public int getViewType() {
        return getClass().hashCode();
    }

    @Override
    public View createView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bp, parent, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.tv_brand_name.setText(info.getBpname());
        viewHolder.tv_brand_details.setText(info.getBpdeion());
        viewHolder.iv_image.loadImage(info.getBpphoto());
        viewHolder.iv_image.setVisibility(!info.getBpphoto().equals("") ? View.VISIBLE : View.GONE);
        if(investId!=0){
            viewHolder.seekBar.formatData(info,investId);
        }
    }

    @Override
    public RecyclerViewHolder createViewHolder(ViewGroup parent) {
        View view = createView(parent);
        return new ViewHolder(view);
    }

    private class ViewHolder extends RecyclerViewHolder {
        protected FMNetImageView iv_image;
        protected TextView tv_brand_name, tv_index;
        protected TextView tv_brand_details, tv_money;
        protected seekBar seekBar;

        protected ViewHolder(View convertView) {
            super(convertView);
            iv_image = (FMNetImageView) convertView.findViewById(R.id.iv_image);
            tv_brand_name = (TextView) convertView.findViewById(R.id.tv_brand_name);
            tv_brand_details = (TextView) convertView.findViewById(R.id.tv_brand_details);
            tv_index = (TextView) convertView.findViewById(R.id.tv_index);
            tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            seekBar = (seekBar) convertView.findViewById(R.id.seekBar);
        }
    }
}

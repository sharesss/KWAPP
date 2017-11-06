package com.ts.fmxt.ui.discover;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
    DiscoverDetailsActivity context;

    public DisBPItem(InvestBPListEntity info, int investId, Activity context) {
        this.info = info;
        this.investId = investId;
        this.context = (DiscoverDetailsActivity) context;
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
        if(info.getBpdeion()!=null){
            viewHolder.tv_brand_details.setText(info.getBpdeion());
        }else{
            for (int i = 0;i<info.getCeils().size();i++){
                if(!info.getCeils().get(i).getText().equals("")){
//                viewHolder.ll_details.
                    final TextView showText = new TextView(context);
                    showText.setTextColor(Color.BLACK);
                    showText.setTextSize(14);
                    showText.setText(info.getCeils().get(i).getText());
                    showText.setBackgroundColor(Color.WHITE);
                    // set 文本大小
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                            RecyclerView.LayoutParams.WRAP_CONTENT);
                    //set 四周距离
                    params.setMargins(30, 10, 10, 10);

                    showText.setLayoutParams(params);

                    //添加文本到主布局
                    viewHolder.ll_details.addView(showText);
                }else if(!info.getCeils().get(i).getImageUrl().equals("")){
                    final FMNetImageView imageView = new FMNetImageView(context);
                    imageView.loadImage(info.getCeils().get(i).getImageUrl());
                    // set 文本大小
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                            RecyclerView.LayoutParams.WRAP_CONTENT);
                    //set 四周距离
                    params.setMargins(10, 10, 10, 10);

                    imageView.setLayoutParams(params);

                    //添加文本到主布局
                    viewHolder.ll_details.addView(imageView);
                }
            }
        }
        info.getCeils().clear();
//        viewHolder.iv_image.loadImage(info.getBpphoto());
//        viewHolder.iv_image.setVisibility(!info.getBpphoto().equals("") ? View.VISIBLE : View.GONE);
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
        private LinearLayout ll_details;

        protected ViewHolder(View convertView) {
            super(convertView);
            iv_image = (FMNetImageView) convertView.findViewById(R.id.iv_image);
            tv_brand_name = (TextView) convertView.findViewById(R.id.tv_brand_name);
            tv_brand_details = (TextView) convertView.findViewById(R.id.tv_brand_details);
            tv_index = (TextView) convertView.findViewById(R.id.tv_index);
            tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            seekBar = (seekBar) convertView.findViewById(R.id.seekBar);
            ll_details = (LinearLayout) convertView.findViewById(R.id.ll_details);
        }
    }
}

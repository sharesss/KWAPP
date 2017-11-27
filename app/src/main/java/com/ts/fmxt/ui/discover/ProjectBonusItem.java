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

import http.data.ProgressUpdateEntity;
import widget.image.FMNetImageView;

/**
 * Created by kp on 2017/11/27.
 * 项目分红
 */

public class ProjectBonusItem implements BaseViewItem {
    ProgressUpdateEntity info;
    DiscoverDetailsActivity context;

    public ProjectBonusItem(ProgressUpdateEntity info, Activity context) {
        this.info = info;
        this.context = (DiscoverDetailsActivity) context;
    }

    @Override
    public int getViewType() {
        return getClass().hashCode();
    }

    @Override
    public View createView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project_bonus, parent, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        viewHolder.tv_brand_name.setText(info.getParticipationTitle());
        viewHolder.tv_time.setText(info.getCreateTime());
        viewHolder.tv_total_amount.setText(info.getTotalInvestAmount()+"万");
        viewHolder.tv_dividend_amount.setText(info.getBonusShareAmount()+"万");
        viewHolder.tv_return_rate.setText(info.getAnnualizedReturn()+"%");
        viewHolder.tv_cycle.setText(info.getParticipationStartTime()+"-"+info.getParticipationEndTime());
//        viewHolder.tv_brand_details.setText(info.getBpdeion());
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
                imageView.loadImage(info.getCeils().get(i).getImageUrl().trim());
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
        info.getCeils().clear();

//        viewHolder.iv_image.loadImage(info.getBpphoto());
//        viewHolder.iv_image.setVisibility(!info.getBpphoto().equals("") ? View.VISIBLE : View.GONE);
//        if(investId!=0){
//            viewHolder.seekBar.formatData(info,investId);
//        }
    }

    @Override
    public RecyclerViewHolder createViewHolder(ViewGroup parent) {
        View view = createView(parent);
        return new ViewHolder(view);
    }

    private class ViewHolder extends RecyclerViewHolder {
        protected FMNetImageView iv_image;
        protected TextView tv_brand_name, tv_index,tv_time,tv_total_amount;
        protected TextView tv_brand_details, tv_money,tv_dividend_amount,tv_return_rate,tv_cycle;
        private LinearLayout ll_details;

        protected ViewHolder(View convertView) {
            super(convertView);
            iv_image = (FMNetImageView) convertView.findViewById(R.id.iv_image);
            tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            tv_total_amount = (TextView) convertView.findViewById(R.id.tv_total_amount);
            tv_dividend_amount = (TextView) convertView.findViewById(R.id.tv_dividend_amount);
            tv_cycle = (TextView) convertView.findViewById(R.id.tv_cycle);
            tv_return_rate = (TextView) convertView.findViewById(R.id.tv_return_rate);
            tv_brand_name = (TextView) convertView.findViewById(R.id.tv_brand_name);
            tv_brand_details = (TextView) convertView.findViewById(R.id.tv_brand_details);
            tv_index = (TextView) convertView.findViewById(R.id.tv_index);
            tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            ll_details = (LinearLayout) convertView.findViewById(R.id.ll_details);
        }
    }
}


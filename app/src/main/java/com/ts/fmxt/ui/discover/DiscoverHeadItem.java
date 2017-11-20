package com.ts.fmxt.ui.discover;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ts.fmxt.R;

import java.text.DecimalFormat;

import http.data.ConsumerEntity;
import widget.image.CircleImageView;
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
        viewHolder.iv_portrait.loadImage(info.getHeadPic());
        viewHolder.tv_name.setText(info.getNickName());
        viewHolder.tv_time.setText(info.getCreateTime());
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
        if(info.getHoldRatios()!=null){
            Double indexs = info.getHoldRatios()*100;
            int exponents = (new Double(indexs)).intValue();
            viewHolder.pb_yellowindex.setProgress(exponents);
            if(info.getReservedAmount()<10000){
                viewHolder.tv_reservation.setText("¥"+info.getReservedAmount());
            }else{
                double n = (double)info.getReservedAmount()/10000;
                DecimalFormat  df   = new DecimalFormat("######0.00");
                viewHolder.tv_reservation.setText("¥"+df.format(n)+"万");
            }
//            viewHolder.tv_reservation.setText("¥"+info.getReservedAmount());
            Double holdRatios = info.getHoldRatios()*100;
            int holdRatio = (new Double(holdRatios)).intValue();
            viewHolder.tv_schedule.setText(holdRatio+"%");
            if(info.getReserveTargetAmount()<10000){
                viewHolder.tv_target_amount.setText("目标金额¥"+info.getReserveTargetAmount());
            }else{
                double n = (double)info.getReserveTargetAmount()/10000;
                DecimalFormat  df   = new DecimalFormat("######0.00");
                viewHolder.tv_target_amount.setText("目标金额¥"+df.format(n)+"万");
            }

            viewHolder.tv_subscription_number.setText("跟投人数"+info.getReservedPeopleNum());
            Long currenttime=System.currentTimeMillis()/1000;//获取系统时间的10位的时间戳
            Long finishtime =info.getReserveFinishTime()/1000;
            Long time =finishtime-currenttime;
            int hour = 0;
            int minute = 0;
            int second = 0;
            second = time.intValue() ;

            int day = (int) (time / (3600 * 24));
            if (second > 60) {
                minute = second / 60;         //取整
                second = second % 60;         //取余
            }

            if (minute > 60) {
                hour = minute / 3600;
                minute = minute % 60;
            }

            if(time<0){
                viewHolder.tv_remaining_days.setText("已结束");
            }else{
                if(day>0){
                    viewHolder.tv_remaining_days.setText("剩余天数"+day+"");
                }else{
                    viewHolder.tv_remaining_days.setText("剩余天数"+"1");
                }

            }
        }

    }

    @Override
    public RecyclerViewHolder createViewHolder(ViewGroup parent) {
        View view = createView(parent);
        return new ViewHolder(view);
    }

    private class ViewHolder extends RecyclerViewHolder {
        FMNetImageView ivImage;
        CircleImageView iv_portrait;
        TextView tvBrandName;
        TextView tvBrandDetails;
        TextView tv_name,tv_time;
        TextView tvIndex;
        ProgressBar pbIndex;
        ProgressBar pbGreenindex,pb_yellowindex;
        TextView tv_reservation,tv_schedule,tv_target_amount,tv_subscription_number,tv_remaining_days;

        private ViewHolder(View view) {
            super(view);
            ivImage = (FMNetImageView) view.findViewById(R.id.iv_image);
            iv_portrait  = (CircleImageView) view.findViewById(R.id.iv_portrait);
            tv_name = (TextView) view.findViewById(R.id.tv_name);
            tv_time= (TextView) view.findViewById(R.id.tv_time);
            tvBrandName = (TextView) view.findViewById(R.id.tv_brand_name);
            tvBrandDetails = (TextView) view.findViewById(R.id.tv_brand_details);
            pbIndex = (ProgressBar) view.findViewById(R.id.pb_index);
            pbGreenindex = (ProgressBar) view.findViewById(R.id.pb_greenindex);
            pb_yellowindex = (ProgressBar) view.findViewById(R.id.pb_yellowindex);
            tvIndex = (TextView) view.findViewById(R.id.tv_index);
            tv_reservation = (TextView) view.findViewById(R.id.tv_reservation);
            tv_schedule = (TextView) view.findViewById(R.id.tv_schedule);
            tv_target_amount = (TextView) view.findViewById(R.id.tv_target_amount);
            tv_subscription_number = (TextView) view.findViewById(R.id.tv_subscription_number);
            tv_remaining_days = (TextView) view.findViewById(R.id.tv_remaining_days);

        }
    }
}

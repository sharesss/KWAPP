package com.ts.fmxt.ui.ItemAdapter;/**
 * Created by A1 on 2017/7/28.
 */

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thindo.base.Adapter.FMBaseAdapter;
import com.ts.fmxt.R;

import java.text.DecimalFormat;
import java.util.List;

import http.data.ConsumerEntity;
import utils.UISKipUtils;
import widget.image.CircleImageView;
import widget.image.FMNetImageView;

import static com.ts.fmxt.R.id.pb_yellowindex;
import static com.ts.fmxt.R.id.tv_index;

/**
 * created by kp at 2017/7/28
 * 想跟投项目适配器
 */
public class FollowProjectAdapter extends FMBaseAdapter {

    public FollowProjectAdapter(Context context, List arrayList) {
        super(context, arrayList);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder ViewHolder = null;
        if (convertView == null) {
            ViewHolder = new ViewHolder();
            convertView = View.inflate(getContext(), R.layout.item_follow_project, null);
            ViewHolder.ll_consumer_info = (LinearLayout) convertView.findViewById(R.id.ll_consumer_info);
            ViewHolder.iv_portrait  = (CircleImageView) convertView.findViewById(R.id.iv_portrait);
            ViewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            ViewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            ViewHolder.iv_image  = (FMNetImageView) convertView.findViewById(R.id.iv_image);
            ViewHolder.tv_brand_name = (TextView) convertView.findViewById(R.id.tv_brand_name);
            ViewHolder.tv_brand_details = (TextView) convertView.findViewById(R.id.tv_brand_details);
            ViewHolder.tv_index = (TextView) convertView.findViewById(tv_index);
            ViewHolder.pb_yellowindex = (ProgressBar) convertView.findViewById(pb_yellowindex);
            ViewHolder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            ViewHolder.tv_reservation = (TextView) convertView.findViewById(R.id.tv_reservation);
            ViewHolder.tv_schedule = (TextView) convertView.findViewById(R.id.tv_schedule);
            ViewHolder.tv_remaining_days = (TextView) convertView.findViewById(R.id.tv_remaining_days);
            ViewHolder.tv_remaining_day = (TextView) convertView.findViewById(R.id.tv_remaining_day);
            ViewHolder.pb_index = (ProgressBar) convertView.findViewById(R.id.pb_index);
            ViewHolder.pb_greenindex = (ProgressBar) convertView.findViewById(R.id.pb_greenindex);
            ViewHolder.ll_money = (LinearLayout) convertView.findViewById(R.id.ll_money);
            ViewHolder.ll_reservation = (LinearLayout) convertView.findViewById(R.id.ll_reservation);
            convertView.setTag(ViewHolder);
        } else {
            ViewHolder = (ViewHolder) convertView.getTag();
        }
        ConsumerEntity info = (ConsumerEntity) getItem(position);
        ViewHolder.iv_image.loadImage(info.getInvestPhoto());
        ViewHolder.iv_portrait.loadImage(info.getHeadPic());
        ViewHolder.tv_name.setText(info.getNickName());
        ViewHolder.tv_time.setText(info.getCreateTime());
        ViewHolder.tv_brand_name.setText(info.getInvestName());
        ViewHolder.tv_brand_details.setText(info.getInvestDeion());
        Double index = info.getExponent()*100;
        int exponent = (new Double(index)).intValue();
        if(exponent<80){
            ViewHolder.pb_index.setProgress(exponent);
            ViewHolder.pb_index.setVisibility(View.VISIBLE);
            ViewHolder.pb_greenindex.setVisibility(View.GONE);
        }else{
            ViewHolder.pb_greenindex.setProgress(exponent);
            ViewHolder.pb_greenindex.setVisibility(View.VISIBLE);
            ViewHolder.pb_index.setVisibility(View.GONE);
        }
        ViewHolder.tv_index.setText(exponent+"%");

        if(info.getHoldRatios()!=null){
            ViewHolder.pb_yellowindex.setVisibility(View.VISIBLE);
            ViewHolder.ll_reservation.setVisibility(View.VISIBLE);
            Double indexs = info.getHoldRatios()*100;
            int exponents = (new Double(indexs)).intValue();
            ViewHolder.pb_yellowindex.setProgress(exponents);
            if(info.getReservedAmount()<10000){
                ViewHolder.tv_reservation.setText("¥"+info.getReservedAmount());
            }else{
                double n = (double)info.getReservedAmount()/10000;
                DecimalFormat  df   = new DecimalFormat("######0.00");
                ViewHolder.tv_reservation.setText("¥"+df.format(n)+"万");
            }
            Double holdRatios = info.getHoldRatios()*100;
            int holdRatio = (new Double(holdRatios)).intValue();
            ViewHolder.tv_schedule.setText(holdRatio+"%");
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
            ViewHolder.tv_remaining_days.setVisibility(time<0 ? View.GONE:View.VISIBLE);
            ViewHolder.tv_remaining_day.setText(time<0 ? "已结束" :"剩余天数");
            ViewHolder.tv_remaining_days.setText(day>0 ? day+"": "1");
        }else{
            ViewHolder.pb_yellowindex.setVisibility(View.GONE);
            ViewHolder.ll_reservation.setVisibility(View.GONE);
        }

            ViewHolder.ll_money.setVisibility(View.GONE);
        ViewHolder.ll_consumer_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConsumerEntity info = (ConsumerEntity) getItem(position);
                UISKipUtils.startDiscoverDetailsActivity((Activity) getContext(), info.getId());
            }
        });

        return convertView;
    }

    class ViewHolder {
        private FMNetImageView iv_image;
        private CircleImageView iv_portrait;
        private TextView tv_name,tv_time;
        private TextView tv_brand_name,tv_index;
        private TextView tv_brand_details,tv_money,tv_reservation,tv_schedule,tv_remaining_days,tv_remaining_day;
        private ProgressBar pb_index,pb_greenindex,pb_yellowindex;
        private LinearLayout ll_consumer_info,ll_money,ll_reservation;

    }
}

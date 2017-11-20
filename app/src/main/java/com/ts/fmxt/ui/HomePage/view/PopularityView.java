package com.ts.fmxt.ui.HomePage.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ts.fmxt.R;

import java.text.DecimalFormat;

import http.data.ConsumerEntity;
import utils.UISKipUtils;
import widget.image.CircleImageView;
import widget.image.FMNetImageView;

/**
 * Created by kp on 2017/9/12.
 * 人气top
 */

public class PopularityView  extends RelativeLayout implements View.OnClickListener{
    private Context context;
    private ConsumerEntity info;
    private FMNetImageView iv_image;
    private CircleImageView iv_portrait;
    private TextView tv_name,tv_time;
    private TextView tv_brand_name,tv_index;
    private TextView tv_brand_details,tv_money,tv_reservation,tv_schedule,tv_remaining_days,tv_remaining_day;
    private ProgressBar pb_index,pb_greenindex,pb_yellowindex;
    private LinearLayout ll_consumer_info,ll_money,ll_popularity_top_view,ll_reservation;


    public PopularityView(Context context) {
        super(context);
        initNavigation();
        this.context = context;
    }

    public PopularityView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initNavigation();
        this.context = context;
    }

    public PopularityView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initNavigation();
        this.context = context;
    }

    private void initNavigation() {
        LayoutInflater.from(getContext()).inflate(R.layout.popularity_top_view, this);
        ll_consumer_info = (LinearLayout) findViewById(R.id.ll_consumer_info);
        iv_image  = (FMNetImageView) findViewById(R.id.iv_image);
        tv_brand_name = (TextView) findViewById(R.id.tv_brand_name);
        tv_brand_details = (TextView) findViewById(R.id.tv_brand_details);
        tv_index = (TextView) findViewById(R.id.tv_index);
        tv_money = (TextView) findViewById(R.id.tv_money);
        pb_index = (ProgressBar) findViewById(R.id.pb_index);
        pb_greenindex = (ProgressBar) findViewById(R.id.pb_greenindex);
        ll_money = (LinearLayout) findViewById(R.id.ll_money);
        tv_reservation= (TextView) findViewById(R.id.tv_reservation);
        tv_schedule = (TextView) findViewById(R.id.tv_schedule);
        tv_remaining_days = (TextView) findViewById(R.id.tv_remaining_days);
        tv_remaining_day = (TextView) findViewById(R.id.tv_remaining_day);
        ll_reservation = (LinearLayout) findViewById(R.id.ll_reservation);
        pb_yellowindex = (ProgressBar) findViewById(R.id.pb_yellowindex);
        findViewById(R.id.ll_popularity_top_view).setOnClickListener(this);
    }

    public void formatData(final ConsumerEntity info){
        if(info==null){
            return;
        }
        this.info =info;
        iv_image.loadImage(info.getInvestPhoto());
        tv_brand_name.setText(info.getInvestName());
        tv_brand_details.setText(info.getInvestDeion());
        Double index = info.getExponent()*100;
        int exponent = (new Double(index)).intValue();
        if(exponent<80){
            pb_index.setProgress(exponent);
            pb_index.setVisibility(View.VISIBLE);
            pb_greenindex.setVisibility(View.GONE);
        }else{
            pb_greenindex.setProgress(exponent);
            pb_greenindex.setVisibility(View.VISIBLE);
            pb_index.setVisibility(View.GONE);
        }
        Double indexs = info.getHoldRatios()*100;
        int exponents = (new Double(indexs)).intValue();
        pb_yellowindex.setProgress(exponents);
        tv_index.setText(exponent+"%");
        double n = (double)info.getReservedAmount()/10000;
        DecimalFormat df   = new DecimalFormat("######0.00");
        tv_reservation.setText(info.getReservedAmount()<10000? "¥ "+info.getReservedAmount():"¥ "+df.format(n)+"万");
//        tv_reservation.setText("¥"+info.getReservedAmount());

        Double holdRatios = info.getHoldRatios()*100;
        int holdRatio = (new Double(holdRatios)).intValue();
        tv_schedule.setText(holdRatio+"%");
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
            tv_remaining_days.setVisibility(View.GONE);
            tv_remaining_day.setText("已结束");
        }else{
            if(day>0){
                tv_remaining_days.setText(day+"");
            }else{
                tv_remaining_days.setText("1");
            }

        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.ll_popularity_top_view:
                UISKipUtils.startDiscoverDetailsActivity((Activity) context,info.getId());
                break;
        }
    }
}


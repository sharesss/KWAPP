package com.ts.fmxt.ui.HomePage.view;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ts.fmxt.R;

import http.data.FindStockEquityHomeEntity;


/**
 * Created by kp on 2017/9/11.
 * 计时器
 */

public class TimerTask extends RelativeLayout {
    private Context context;
    private FindStockEquityHomeEntity info;
    private int investIds;
    private TextView tv_is_start,tv_day,tv_time,tv_branch,tv_second;
    private LinearLayout ll_day,ll_second;
    private Long time;
    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            time--;
            if(time<0){
                return;
            }
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
            if(day>0){
                tv_day.setText(day+"");
                ll_day.setVisibility(View.VISIBLE);
                ll_second.setVisibility(View.GONE);
            }else{
                ll_day.setVisibility(View.GONE);
            }
            tv_time.setText(hour+"");
            tv_branch.setText(minute+"");
            tv_second.setText(second+"");

            if(time>0){
                handler.postDelayed(this, 1000);
            }
        }
    };
    public TimerTask(Context context) {
        super(context);
        initNavigation();
        this.context = context;
    }

    public TimerTask(Context context, AttributeSet attrs) {
        super(context, attrs);
        initNavigation();
        this.context = context;
    }

    public TimerTask(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initNavigation();
        this.context = context;
    }

    private void initNavigation() {
        LayoutInflater.from(getContext()).inflate(R.layout.timertask_view, this);
        tv_is_start = (TextView) findViewById(R.id.tv_is_start);
        tv_day = (TextView) findViewById(R.id.tv_day);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_branch = (TextView) findViewById(R.id.tv_branch);
        tv_second = (TextView) findViewById(R.id.tv_second);

        ll_day = (LinearLayout) findViewById(R.id.ll_day);
        ll_second = (LinearLayout) findViewById(R.id.ll_second);

    }

    public void formatData(final FindStockEquityHomeEntity info){
        if(info==null){
            return;
        }
        this.info =info;
         time  = info.getAuctionEndTime() - info.getAuctionStartTime();
        handler.postDelayed(runnable, 1000);
        if(info.getAuctionState()==0){
            //未开始
            tv_is_start.setText("立即预约");
        }else if(info.getAuctionState()==1||info.getAuctionState()==2){
            //竞拍成功
            tv_is_start.setText("已结拍");

        }else if(info.getAuctionState()==3){
            tv_is_start.setText("立即抢拍");
        }
    }

}



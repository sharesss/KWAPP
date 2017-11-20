package com.ts.fmxt.ui.HomePage.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ts.fmxt.R;

import java.text.DecimalFormat;

import http.data.FindStockEquityHomeEntity;
import utils.UISKipUtils;
import widget.image.CircleImageView;
import widget.image.FMNetImageView;

/**
 * Created by A1 on 2017/9/11.
 */

public class StockAuctionView extends RelativeLayout implements View.OnClickListener{
    private Context context;
    private FindStockEquityHomeEntity info;
    private TimerTask tv_timetask;
    private FMNetImageView iv_image;
    private CircleImageView iv_portrait;
    private TextView tv_company_name,tv_follow_num,tv_type,tv_round,tv_bonus,tv_name,tv_isfounder,tv_founder,tv_isVfounder;
    private TextView tv_follow_up_project,tv_transfer_project,tv_auction_project,tv_selling_shares,tv_starting_price,tv_appointments,tv_highest_bid,tv_highest_bid_no,tv_isCandid;

    public StockAuctionView(Context context) {
        super(context);
        initNavigation();
        this.context = context;
    }

    public StockAuctionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initNavigation();
        this.context = context;
    }

    public StockAuctionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initNavigation();
        this.context = context;
    }

    private void initNavigation() {
        LayoutInflater.from(getContext()).inflate(R.layout.stock_auction_view, this);
        tv_timetask = (TimerTask) findViewById(R.id.tv_timetask);
        iv_image = (FMNetImageView) findViewById(R.id.iv_image);
        tv_company_name = (TextView) findViewById(R.id.tv_company_name);
        tv_follow_num = (TextView) findViewById(R.id.tv_follow_num);
        tv_type= (TextView) findViewById(R.id.tv_type);
        tv_round= (TextView) findViewById(R.id.tv_round);
        tv_bonus= (TextView) findViewById(R.id.tv_bonus);
        iv_portrait = (CircleImageView) findViewById(R.id.iv_portrait);
        tv_name= (TextView) findViewById(R.id.tv_name);
        tv_isfounder= (TextView) findViewById(R.id.tv_isfounder);
        tv_founder= (TextView) findViewById(R.id.tv_founder);
        tv_isVfounder = (TextView) findViewById(R.id.tv_isVfounder);
        tv_follow_up_project= (TextView) findViewById(R.id.tv_follow_up_project);
        tv_transfer_project= (TextView) findViewById(R.id.tv_transfer_project);
        tv_auction_project= (TextView) findViewById(R.id.tv_auction_project);
        tv_selling_shares= (TextView) findViewById(R.id.tv_selling_shares);
        tv_starting_price= (TextView) findViewById(R.id.tv_starting_price);
        tv_appointments= (TextView) findViewById(R.id.tv_appointments);
        tv_highest_bid= (TextView) findViewById(R.id.tv_highest_bid);
        tv_highest_bid_no = (TextView) findViewById(R.id.tv_highest_bid_no);
        tv_isCandid= (TextView) findViewById(R.id.tv_isCandid);
//        tv_isCandid.setOnClickListener(this);
        findViewById(R.id.ll_stock_auction_view).setOnClickListener(this);
    }

    public void formatData(final FindStockEquityHomeEntity info){
        if(info==null){
            return;
        }
        this.info =info;
        tv_timetask.formatData(info);
        if(info.getPhotoUrl()!=null){
            iv_image.loadImage(info.getPhotoUrl());
        }
        tv_company_name.setText(info.getStockName());
        tv_follow_num.setText(info.getAttentionNum()+"人关注");
        tv_type.setText(info.getIndustryName());
        tv_round.setText(info.getFinancingState());
        tv_bonus.setText(info.getReturnWay()+"");
        iv_portrait.loadImage(info.getHeadpic());
        tv_name.setText(info.getNickname());
        if( info.getIsinvestauthen()==1){
            tv_isfounder.setVisibility(View.VISIBLE);
        }else if(info.getIsinvestauthen()==2){
            tv_isVfounder.setVisibility(View.VISIBLE);
        }else{
            tv_isfounder.setVisibility(View.GONE);
            tv_isVfounder.setVisibility(View.GONE);
        }
        StringBuilder inf =  new StringBuilder().append(!info.getCompany().equals("")&&!info.getCompany().equals("null")  ? info.getCompany()+"/":"").append(!info.getPosition().equals("")&&!info.getPosition().equals("null")? info.getPosition()+"/":"");
        if(inf.length()>1){
            inf.delete(inf.length()-1, inf.length());
        }

        tv_founder.setText(inf);
        tv_follow_up_project.setText("跟投项目："+info.getFollowNum());
        tv_transfer_project.setText("转让项目："+info.getMakeOverNum());
        tv_auction_project.setText("竞拍项目："+info.getAuctionNum());
        tv_selling_shares.setText("出让股权："+info.getStockSellRate()+"%");
            double n = (double)info.getStartingPrice()/10000;
            DecimalFormat  df   = new DecimalFormat("######0.00");
        tv_starting_price.setText(info.getStartingPrice()<10000? "¥ "+info.getStartingPrice():"¥ "+df.format(n)+"万");
        tv_appointments.setText("预约人数: "+info.getReserveNum());
        if(info.getTransactionPrice()==0){
            tv_highest_bid.setVisibility(View.GONE);
            tv_highest_bid_no.setVisibility(View.VISIBLE);
        }else{
                double ns = (double)info.getTransactionPrice()/10000;
            tv_highest_bid.setText(info.getTransactionPrice()<10000 ? "¥ "+info.getTransactionPrice() :"¥ "+df.format(ns)+"万");
            tv_highest_bid_no.setVisibility(View.GONE);
        }

        Long time  =  info.getAuctionStartTime()/1000-info.getCurrentTime()/1000;
            //未开始
            if (time < 0) {
                //竞拍成功
                tv_isCandid.setText("已结拍");
            } else if (info.getAuctionState() == 0) {
                if (info.getCurrentTime() / 1000 < info.getAuctionStartTime() / 1000) {
                    tv_isCandid.setText("立即预约");
                } else if (time > 0) {
                    tv_isCandid.setText("立即抢拍");
                }
            }
//        if(info.getAuctionState()==0){
//            //未开始
//            tv_isCandid.setText("立即预约");
//        }else if(info.getAuctionState()==1||info.getAuctionState()==2){
//            //竞拍成功
//            tv_isCandid.setText("已结拍");
//        }else if(info.getAuctionState()==3){
//            tv_isCandid.setText("立即抢拍");
//        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
                case R.id.ll_stock_auction_view://进入详情
                    UISKipUtils.startAuctionDetailsActivity((Activity) context,info.getId());
                    break;

        }
    }
}


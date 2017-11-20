package com.ts.fmxt.ui.ItemAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thindo.base.Adapter.FMBaseAdapter;
import com.ts.fmxt.R;

import java.text.DecimalFormat;
import java.util.List;

import http.data.FindStockEquityHomeEntity;
import utils.UISKipUtils;
import widget.image.CircleImageView;
import widget.image.FMNetImageView;

import static com.ts.fmxt.R.id.tv_founder;


/**
 * Created by kp on 2017/9/13.
 * 股权拍卖
 */

public class StockAuctionAdapter extends FMBaseAdapter {
    private int type;
    public StockAuctionAdapter(Context context, List arrayList,int type) {
        super(context, arrayList);
        this.type = type;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder ViewHolder = null;
        if (convertView == null) {
            ViewHolder = new ViewHolder();
            convertView = View.inflate(getContext(), R.layout.item_stock_auction, null);
            ViewHolder.ll_stock_auction = (LinearLayout) convertView.findViewById(R.id.ll_stock_auction);
            ViewHolder.iv_portrait  = (CircleImageView) convertView.findViewById(R.id.iv_portrait);
            ViewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            ViewHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            ViewHolder.tv_company_name = (TextView) convertView.findViewById(R.id.tv_company_name);
            ViewHolder.iv_image  = (FMNetImageView) convertView.findViewById(R.id.iv_image);
            ViewHolder.tv_follow_num = (TextView) convertView.findViewById(R.id.tv_attention_number);
            ViewHolder.tv_isfounder= (TextView) convertView.findViewById(R.id.tv_isfounder);
            ViewHolder.tv_founder= (TextView) convertView.findViewById(tv_founder);
            ViewHolder.tv_isVfounder = (TextView) convertView.findViewById(R.id.tv_isVfounder);
            ViewHolder.tv_type= (TextView) convertView.findViewById(R.id.tv_type);
            ViewHolder.tv_round= (TextView) convertView.findViewById(R.id.tv_round);
            ViewHolder.tv_bonus= (TextView) convertView.findViewById(R.id.tv_bonus);
            ViewHolder.tv_follow_up_project= (TextView) convertView.findViewById(R.id.tv_follow_up_project);
            ViewHolder.tv_transfer_project= (TextView) convertView.findViewById(R.id.tv_transfer_project);
            ViewHolder.tv_auction_project= (TextView) convertView.findViewById(R.id.tv_auction_project);
            ViewHolder.tv_selling_shares= (TextView) convertView.findViewById(R.id.tv_selling_shares);
            ViewHolder.tv_starting_price= (TextView) convertView.findViewById(R.id.tv_starting_price);

            ViewHolder.rl_project_create = (RelativeLayout) convertView.findViewById(R.id.rl_project_create);
            ViewHolder.ll_project_num= (LinearLayout) convertView.findViewById(R.id.ll_project_num);
            ViewHolder.view = convertView.findViewById(R.id.view);
            ViewHolder.linview = convertView.findViewById(R.id.linview);
            ViewHolder.ll_participate_transaction = (LinearLayout) convertView.findViewById(R.id.ll_participate_transaction);
            ViewHolder.tv_participate_num= (TextView) convertView.findViewById(R.id.tv_participate_num);
            ViewHolder.tv_transaction_price= (TextView) convertView.findViewById(R.id.tv_transaction_price);
            convertView.setTag(ViewHolder);
        } else {
            ViewHolder = (ViewHolder) convertView.getTag();
        }
        FindStockEquityHomeEntity info = (FindStockEquityHomeEntity) getItem(position);
        if(info.getPhotoUrl()!=null){
            ViewHolder.iv_image.loadImage(info.getPhotoUrl());
        }
        ViewHolder.tv_company_name.setText(info.getStockName());
        ViewHolder.tv_follow_num.setText(info.getAttentionNum()+"人关注");
        ViewHolder.tv_type.setText(info.getIndustryName());
        ViewHolder.tv_round.setText(info.getFinancingState());
        if(info.getReturnWay()==1){
            ViewHolder.tv_bonus.setText("定期分红");
        }else if(info.getReturnWay()==2){
            ViewHolder.tv_bonus.setText("股权增值");
        }
        ViewHolder.iv_portrait.loadImage(info.getHeadpic());
        ViewHolder.tv_name.setText(info.getNickname());
        ViewHolder.tv_isfounder.setVisibility(info.getIsinvestauthen()==1? View.VISIBLE:View.GONE);
            ViewHolder.tv_isVfounder.setVisibility(info.getIsinvestauthen()==2 ? View.VISIBLE:View.GONE);
        StringBuilder inf =  new StringBuilder().append(!info.getCompany().equals("")&&!info.getCompany().equals("null")  ? info.getCompany()+"/":"").append(!info.getPosition().equals("")&&!info.getPosition().equals("null")? info.getPosition()+"/":"");
        if(inf.length()>1){
            inf.delete(inf.length()-1, inf.length());
        }

        ViewHolder.tv_founder.setText(inf);
        long time  =  info.getAuctionStartTime()/1000-info.getCurrentTime()/1000;
        if(time<0){
            //竞拍成功
            ViewHolder.tv_time.setText("已结拍");
        }else if(info.getAuctionState()==0){
            //未开始
            if(info.getCurrentTime()/1000<info.getAuctionStartTime()/1000){
                ViewHolder.tv_time.setText("未开始");
            }else if(time>0){
                ViewHolder.tv_time.setText("拍卖中");
            }
        }
//        if(info.getAuctionState()==0){
//            if(info.getAuctionStartTime()<info.getCurrentTime()){
//                ViewHolder.tv_time.setText("拍卖中");
//            }else{
//                ViewHolder.tv_time.setText("未开始");
//            }
//
//        }else if(info.getAuctionState()==1||info.getAuctionState()==2){
//            ViewHolder.tv_time.setText("已结束");
//        }else{
//            ViewHolder.tv_time.setText("拍卖中");
//        }
        ViewHolder.tv_follow_up_project.setText("跟投项目："+info.getFollowNum());
        ViewHolder.tv_transfer_project.setText("转让项目："+info.getMakeOverNum());
        ViewHolder.tv_auction_project.setText("竞拍项目："+info.getAuctionNum());
        ViewHolder.tv_selling_shares.setText(""+info.getStockSellRate()+"%");
        if(info.getStartingPrice()<10000){
            ViewHolder.tv_starting_price.setText("¥ "+info.getStartingPrice());
        }else{
            double n = (double)info.getStartingPrice()/10000;
            DecimalFormat  df   = new DecimalFormat("######0.00");
            ViewHolder.tv_starting_price.setText("¥ "+df.format(n)+"万");
        }
//        ViewHolder.tv_starting_price.setText("¥ "+info.getStartingPrice());
        ViewHolder.ll_stock_auction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FindStockEquityHomeEntity info = (FindStockEquityHomeEntity) getItem(position);
                UISKipUtils.startAuctionDetailsActivity((Activity) getContext(), info.getId());

            }
        });
        if(type ==1){
            ViewHolder.linview.setVisibility(View.VISIBLE);
            ViewHolder.view.setVisibility(View.VISIBLE);
            ViewHolder.ll_participate_transaction.setVisibility(View.VISIBLE);
            ViewHolder.tv_participate_num.setText("参与人数"+info.getReserveNum());
            if(info.getTransactionPrice()<10000){
                ViewHolder.tv_transaction_price.setText("成交价 ¥ "+info.getTransactionPrice());
            }else{
                double n = (double)info.getTransactionPrice()/10000;
                DecimalFormat  df   = new DecimalFormat("######0.00");
                ViewHolder.tv_transaction_price.setText("成交价 ¥ "+df.format(n)+"万");
            }

        }else if(type ==2){
            ViewHolder.rl_project_create.setVisibility(View.GONE);
            ViewHolder.ll_project_num.setVisibility(View.GONE);
        }
        return convertView;
    }

    class ViewHolder {
        private LinearLayout ll_stock_auction,ll_participate_transaction,ll_project_num;
        private RelativeLayout rl_project_create;
        private FMNetImageView iv_image;
        private CircleImageView iv_portrait;
        private View view,linview;
        private TextView tv_company_name,tv_follow_num,tv_type,tv_round,tv_bonus,tv_name,tv_isfounder,tv_founder,tv_isVfounder,tv_participate_num,tv_transaction_price;
        private TextView tv_follow_up_project,tv_transfer_project,tv_auction_project,tv_selling_shares,tv_starting_price,tv_appointments,tv_highest_bid,tv_isCandid,tv_time;

    }
}

package com.ts.fmxt.ui.ItemAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thindo.base.Adapter.FMBaseAdapter;
import com.ts.fmxt.R;

import java.math.BigDecimal;
import java.util.List;

import http.data.AuctionPayInfoEntity;

import static com.ts.fmxt.R.id.tv_money;

/**
 * Created by kp on 2017/9/25.
 * 历史明细适配器
 */

public class HistoryDetailsAdapter  extends FMBaseAdapter {

    public HistoryDetailsAdapter(Context context, List arrayList) {
        super(context, arrayList);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder ViewHolder = null;
        if (convertView == null) {
            ViewHolder = new ViewHolder();
            convertView = View.inflate(getContext(), R.layout.item_history_details, null);
            ViewHolder.tv_stockName = (TextView) convertView.findViewById(R.id.tv_stockName);
            ViewHolder.tv_earnestMoney = (TextView) convertView.findViewById(R.id.tv_earnestMoney);
            ViewHolder.tv_payTime = (TextView) convertView.findViewById(R.id.tv_payTime);
            ViewHolder.tv_payType = (TextView) convertView.findViewById(R.id.tv_payType);
            ViewHolder.tv_money = (TextView) convertView.findViewById(tv_money);
            ViewHolder.tv_fundFinallyFlowDesc = (TextView) convertView.findViewById(R.id.tv_fundFinallyFlowDesc);
            ViewHolder.tv_fundFinallyFlow = (TextView) convertView.findViewById(R.id.tv_fundFinallyFlow);
            convertView.setTag(ViewHolder);
        } else {
            ViewHolder = (ViewHolder) convertView.getTag();
        }
        AuctionPayInfoEntity info = (AuctionPayInfoEntity) getItem(position);
        ViewHolder.tv_stockName.setText(info.getStockName());
        String money = BigDecimal.valueOf(Integer.valueOf(info.getEarnestMoney())).divide(new BigDecimal(100)).toString();
        ViewHolder.tv_earnestMoney.setText("保证金¥"+money);
        ViewHolder.tv_payTime.setText(info.getPayTime());

        ViewHolder.tv_payType.setText(info.getPayType()==1?"发布拍卖":"参与拍卖");
        if(info.getFundFinallyFlow()==-1){
            ViewHolder.tv_fundFinallyFlow.setText("退款失败");
        }else if(info.getFundFinallyFlow()==0){
            ViewHolder.tv_fundFinallyFlow.setText("冻结中");
        }else if(info.getFundFinallyFlow()==1){
            ViewHolder.tv_fundFinallyFlow.setText("成功退还");
        }else if(info.getFundFinallyFlow()==2){
            ViewHolder.tv_fundFinallyFlow.setText("成功扣除");
        }
        ViewHolder.tv_money.setText("¥"+money);
        if(info.getFundFinallyFlowDesc()!="null"){
            ViewHolder.tv_fundFinallyFlowDesc.setText(info.getFundFinallyFlowDesc());
        }


        return convertView;
    }

    class ViewHolder {
        private TextView tv_stockName,tv_earnestMoney,tv_payTime,tv_payType,tv_fundFinallyFlow,tv_money,tv_fundFinallyFlowDesc;

    }
}


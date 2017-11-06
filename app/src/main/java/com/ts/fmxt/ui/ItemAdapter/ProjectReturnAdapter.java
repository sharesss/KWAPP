package com.ts.fmxt.ui.ItemAdapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thindo.base.Adapter.FMBaseAdapter;
import com.ts.fmxt.R;

import java.util.List;

import http.data.ProjectReturnEntity;
import utils.UISKipUtils;

import static com.ts.fmxt.R.id.tv_confirm;

/**
 * Created by kp on 2017/11/1.
 * 项目回报适配器
 */

public class ProjectReturnAdapter extends FMBaseAdapter {
    ProjectReturnEntity info;
    int isOver;
    public ProjectReturnAdapter(Context context, List arrayList,int isOver) {
        super(context, arrayList);
        this.isOver = isOver;
    }
    @Override
    public View getView(final int i, View convertView, ViewGroup viewGroup) {
        ViewHolder ViewHolder = null;
        if (convertView == null) {
            ViewHolder = new ViewHolder();
            convertView = View.inflate(getContext(), R.layout.item_projectreturn, null);
            ViewHolder.tv_reservation_money = (TextView) convertView.findViewById(R.id.tv_reservation_money);
            ViewHolder.tv_shares_num = (TextView) convertView.findViewById(R.id.tv_shares_num);
            ViewHolder.tv_company_name = (TextView) convertView.findViewById(R.id.tv_company_name);
            ViewHolder.tv_equityReserveExplain = (TextView) convertView.findViewById(R.id.tv_equityReserveExplain);
            ViewHolder.tv_confirm = (TextView) convertView.findViewById(tv_confirm);
            convertView.setTag(ViewHolder);
        } else {
            ViewHolder = (ViewHolder) convertView.getTag();
        }
        ProjectReturnEntity info = (ProjectReturnEntity) getItem(i);
        ViewHolder.tv_company_name.setText("份额预约|"+info.getShareTitle());
        ViewHolder.tv_equityReserveExplain.setText(info.getShareContent());
        ViewHolder.tv_confirm.setBackground(info.getIsYetBut()==0? getContext().getResources().getDrawable(R.drawable.bg_full_orange_5_shape):getContext().getResources().getDrawable(R.drawable.bg_full_gray_5_shape));
        ViewHolder.tv_confirm.setBackground(isOver==1? getContext().getResources().getDrawable(R.drawable.bg_full_orange_5_shape):getContext().getResources().getDrawable(R.drawable.bg_full_gray_5_shape));
//        info = (ProjectReturnEntity) getItem(position);
        ViewHolder.tv_reservation_money.setText("预约金：¥"+info.getReserveAmount());
        if(info.getReservePeopleNum().equals("null")){
            ViewHolder.tv_shares_num.setText(info.getYetReservePropleNum()+"人预约/无限制");
        }else{
           int num = Integer.valueOf(info.getReservePeopleNum())-Integer.valueOf(info.getYetReservePropleNum());
            if(num<0){
                num=0;
            }
            ViewHolder.tv_confirm.setBackground(num>0&&info.getIsYetBut()==0&&isOver==1? getContext().getResources().getDrawable(R.drawable.bg_full_orange_5_shape):getContext().getResources().getDrawable(R.drawable.bg_full_gray_5_shape));
            ViewHolder.tv_shares_num.setText(info.getYetReservePropleNum()+"人预约/剩余名额"+num+"人");
        }




        ViewHolder.tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ProjectReturnEntity info = (ProjectReturnEntity) getItem(i);
                if(info.getIsYetBut()==1){
                    return;
                }
                if(isOver==0){
                    return;
                }
                if(!info.getReservePeopleNum().equals("null")) {
                    int num = Integer.valueOf(info.getReservePeopleNum()) - Integer.valueOf(info.getYetReservePropleNum());
                    if (num < 0) {
                        num = 0;
                    }
                    if (num <= 0) {
                        return;
                    }
                }

                String CompanyName = info.getShareTitle();
                UISKipUtils.startConfirmPayment((Activity) getContext(),info.getReserveAmount(),info.getInvestId(),CompanyName,info.getId());
            }
        });

        return convertView;
    }

    class ViewHolder {
        private TextView tv_reservation_money,tv_shares_num;
        private TextView tv_company_name,tv_equityReserveExplain;
        private TextView tv_confirm;

    }
}

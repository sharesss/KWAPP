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
import widget.image.FMNetImageView;

import static com.ts.fmxt.R.id.tv_frozen_money;
import static com.ts.fmxt.R.id.tv_success_frozen_money;

/**
 * Created by KP on 2017/9/25.
 * 冻结保证金适配器
 */

public class FrozenDepositAdapter extends FMBaseAdapter {

    public FrozenDepositAdapter(Context context, List arrayList) {
        super(context, arrayList);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder ViewHolder = null;
        if (convertView == null) {
            ViewHolder = new ViewHolder();
            convertView = View.inflate(getContext(), R.layout.item_frozen_deposit, null);
            ViewHolder.iv_image  = (FMNetImageView) convertView.findViewById(R.id.iv_image);
            ViewHolder.tv_project_name = (TextView) convertView.findViewById(R.id.tv_project_name);
            ViewHolder.tv_frozen_money = (TextView) convertView.findViewById(tv_frozen_money);
            ViewHolder.tv_frozen_time = (TextView) convertView.findViewById(R.id.tv_frozen_time);
            ViewHolder.tv_follow_project = (TextView) convertView.findViewById(R.id.tv_follow_project);
            ViewHolder.tv_success_frozen_money = (TextView) convertView.findViewById(tv_success_frozen_money);
            convertView.setTag(ViewHolder);
        } else {
            ViewHolder = (ViewHolder) convertView.getTag();
        }
        AuctionPayInfoEntity info = (AuctionPayInfoEntity) getItem(position);
        if(info.getStockPic()!=null){
            ViewHolder.iv_image.loadImage(info.getStockPic());
            ViewHolder.tv_project_name.setText(info.getStockName());
            String money = BigDecimal.valueOf(Integer.valueOf(info.getEarnestMoney())).divide(new BigDecimal(100)).toString();
            ViewHolder.tv_frozen_money.setText("成功冻结¥"+money);
            ViewHolder.tv_frozen_time.setText(info.getPayTime());
            ViewHolder.tv_follow_project.setText(info.getPayType()==1?"发布拍卖":"参与拍卖");
            ViewHolder.tv_success_frozen_money.setText("成功冻结¥"+money);
        }

        return convertView;
    }

    class ViewHolder {
        private FMNetImageView iv_image;
        private TextView tv_project_name,tv_frozen_money,tv_frozen_time,tv_follow_project,tv_success_frozen_money;

    }
}

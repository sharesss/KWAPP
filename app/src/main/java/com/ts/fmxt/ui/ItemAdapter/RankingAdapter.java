package com.ts.fmxt.ui.ItemAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thindo.base.Adapter.FMBaseAdapter;
import com.ts.fmxt.R;

import java.util.ArrayList;

import http.data.FindStockEquityHomeEntity;
import widget.image.CircleImageView;

import static com.ts.fmxt.R.id.tv_num;

/**
 * Created by A1 on 2017/9/15.
 */

public class RankingAdapter extends FMBaseAdapter {

    public RankingAdapter(Context mContext, ArrayList<FindStockEquityHomeEntity.RankingEntity> data) {
        super(mContext, data);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = View.inflate(getContext(), R.layout.adapter_ranking_view, null);
            mHolder.tv_num = (TextView) convertView.findViewById(tv_num);
            mHolder.tv_highest_price = (TextView) convertView.findViewById(R.id.tv_highest_price);
            mHolder.tv_frequency = (TextView) convertView.findViewById(R.id.tv_frequency);
            mHolder.iv_portrait = (CircleImageView) convertView.findViewById(R.id.iv_portrait);
            mHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            convertView.setTag(mHolder);
        } else {
            mHolder = (RankingAdapter.ViewHolder) convertView.getTag();
        }
        FindStockEquityHomeEntity.RankingEntity item = (FindStockEquityHomeEntity.RankingEntity) getItem(position);
        mHolder.iv_portrait.loadImage(item.getHeadPic());
        mHolder.tv_name.setText(item.getNickName());
        mHolder.tv_highest_price.setText("最高价 ¥ "+item.getTopBidPrice());
        mHolder.tv_frequency.setText("出价"+item.getBidTime()+"次");
        mHolder.tv_num.setText(position+1+"");
        return convertView;
    }




    public class ViewHolder {
        private TextView tv_name,tv_highest_price,tv_frequency,tv_num;
        private CircleImageView iv_portrait;
    }
}


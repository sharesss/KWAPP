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

import java.util.List;

import http.data.ConsumerEntity;
import utils.UISKipUtils;
import widget.image.FMNetImageView;

import static com.ts.fmxt.R.id.ll_money;

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
            ViewHolder.iv_image  = (FMNetImageView) convertView.findViewById(R.id.iv_image);
            ViewHolder.tv_brand_name = (TextView) convertView.findViewById(R.id.tv_brand_name);
            ViewHolder.tv_brand_details = (TextView) convertView.findViewById(R.id.tv_brand_details);
            ViewHolder.tv_index = (TextView) convertView.findViewById(R.id.tv_index);
            ViewHolder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            ViewHolder.pb_index = (ProgressBar) convertView.findViewById(R.id.pb_index);
            ViewHolder.pb_greenindex = (ProgressBar) convertView.findViewById(R.id.pb_greenindex);
            ViewHolder.ll_money = (LinearLayout) convertView.findViewById(ll_money);
            convertView.setTag(ViewHolder);
        } else {
            ViewHolder = (ViewHolder) convertView.getTag();
        }
        ConsumerEntity info = (ConsumerEntity) getItem(position);
        ViewHolder.iv_image.loadImage(info.getInvestPhoto());
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

        if(info.getAmount()!=null){
            ViewHolder.ll_money.setVisibility(View.VISIBLE);
            ViewHolder.tv_money.setText("意向跟投金额:"+info.getAmount()+"万");
        }else{
            ViewHolder.ll_money.setVisibility(View.GONE);
        }
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
        private TextView tv_brand_name,tv_index;
        private TextView tv_brand_details,tv_money;
        private ProgressBar pb_index,pb_greenindex;
        private LinearLayout ll_consumer_info,ll_money;

    }
}

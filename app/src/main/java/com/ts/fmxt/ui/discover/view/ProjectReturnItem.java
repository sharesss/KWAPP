package com.ts.fmxt.ui.discover.view;

import android.app.Activity;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ts.fmxt.R;
import com.ts.fmxt.ui.discover.BaseViewItem;
import com.ts.fmxt.ui.discover.DiscoverDetailsActivity;
import com.ts.fmxt.ui.discover.RecyclerViewHolder;

import http.data.ConsumerEntity;
import widget.image.FMNetImageView;

/**
 * Created by kp on 2017/10/31.
 * 项目回报
 */

public class ProjectReturnItem  implements BaseViewItem {
    ConsumerEntity info;
    DiscoverDetailsActivity context;

    public ProjectReturnItem(ConsumerEntity info, Activity context) {
        this.info = info;
        this.context = (DiscoverDetailsActivity) context;
    }

    @Override
    public int getViewType() {
        return getClass().hashCode();
    }

    @Override
    public View createView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_project_return, parent, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
//        viewHolder.tv_brand_name.setText("项目回报");
//        viewHolder.tv_brand_details.setText(info.getBpdeion());
        for (int i = 0;i<info.getCeils().size();i++){
            if(!info.getCeils().get(i).getText().equals("")){
//                viewHolder.ll_details.
                final TextView showText = new TextView(context);
                showText.setTextColor( context.getResources().getColor(R.color.text_black_main));
                showText.setTextSize(12);
                showText.setText(info.getCeils().get(i).getText());
                showText.setBackgroundColor(Color.WHITE);
                showText.setLineSpacing(0,2);
                // set 文本大小
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT);
                //set 四周距离
                params.setMargins(50, 0, 50, 0);

                showText.setLayoutParams(params);

                //添加文本到主布局
                viewHolder.ll_details.addView(showText);
            }else if(!info.getCeils().get(i).getImageUrl().equals("")){
                final FMNetImageView imageView = new FMNetImageView(context);
                imageView.loadImage(info.getCeils().get(i).getImageUrl().trim());
                // set 文本大小
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                        RecyclerView.LayoutParams.WRAP_CONTENT);
                //set 四周距离
                params.setMargins(30, 10, 30, 10);
                params.height=700;
                imageView.setLayoutParams(params);

                //添加文本到主布局
                viewHolder.ll_details.addView(imageView);
            }
        }
        info.getCeils().clear();

//        viewHolder.iv_image.loadImage(info.getBpphoto());
//        viewHolder.iv_image.setVisibility(!info.getBpphoto().equals("") ? View.VISIBLE : View.GONE);
//        if(investId!=0){
//            viewHolder.seekBar.formatData(info,investId);
//        }
    }

    @Override
    public RecyclerViewHolder createViewHolder(ViewGroup parent) {
        View view = createView(parent);
        return new ViewHolder(view);
    }

    private class ViewHolder extends RecyclerViewHolder {
        protected FMNetImageView iv_image;
        protected TextView tv_brand_name, tv_index;
        protected TextView tv_brand_details, tv_money;
        protected seekBar seekBar;
        private LinearLayout ll_details;

        protected ViewHolder(View convertView) {
            super(convertView);
            iv_image = (FMNetImageView) convertView.findViewById(R.id.iv_image);
            tv_brand_name = (TextView) convertView.findViewById(R.id.tv_brand_name);
            tv_brand_details = (TextView) convertView.findViewById(R.id.tv_brand_details);
            tv_index = (TextView) convertView.findViewById(R.id.tv_index);
            tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            seekBar = (seekBar) convertView.findViewById(R.id.seekBar);
            ll_details = (LinearLayout) convertView.findViewById(R.id.ll_details);
        }
    }
}

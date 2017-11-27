package com.ts.fmxt.ui.discover;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ts.fmxt.R;
import com.ts.fmxt.ui.discover.view.FlowLayout;

import java.util.ArrayList;

import http.data.InvestBPListEntity;

/**
 * 点击定位功能
 */

public class DiscoverLabelItem implements BaseViewItem {
    ArrayList<InvestBPListEntity> arr;
    private String checktext;
    CallBack callBack;
    int o = 0;


    public interface CallBack {
        void onitem(int postion);
    }

    public DiscoverLabelItem(ArrayList<InvestBPListEntity> arr, CallBack callBack) {
        this.arr = arr;
        this.callBack = callBack;
    }

    @Override
    public int getViewType() {
        return getClass().hashCode();
    }

    @Override
    public View createView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.dislabelitem_layout, parent, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        //标签UI
        viewHolder.llTemp.setVisibility(View.VISIBLE);
    }

    @Override
    public RecyclerViewHolder createViewHolder(ViewGroup parent) {
        View view = createView(parent);
        return new ViewHolder(view);
    }

    private class ViewHolder extends RecyclerViewHolder {
        FlowLayout flow_layout;
        LinearLayout llTemp;


        private ViewHolder(final View covert) {
            super(covert);
            flow_layout = (FlowLayout) covert.findViewById(R.id.flow_layout);
            llTemp = (LinearLayout) covert.findViewById(R.id.ll_temp2);
            // 循环添加TextView到容器
            for (int i = 0; i < arr.size(); i++) {
                InvestBPListEntity info = arr.get(i);
                final TextView view = new TextView(covert.getContext());
                view.setText(info.getBpname());
                view.setTextColor(Color.GRAY);
                view.setPadding(5, 5, 5, 5);
                view.setGravity(Gravity.CENTER);
                view.setTextSize(18);
                // 设置背景选择器到TextView上
                Resources resources = covert.getResources();
                Drawable btnDrawable = resources.getDrawable(R.drawable.bg_gray_tag_shape);
                view.setBackground(btnDrawable);

                // 设置点击事件
                final int finalI = i;
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String name = ((TextView) v).getText().toString();
                        Resources resources = covert.getResources();
                        v.setBackground(resources.getDrawable(R.drawable.bg_orange_5_shape));
                        ((TextView) v).setTextColor(Color.WHITE);
                        if (callBack != null) {
                            callBack.onitem(finalI);
                        }
//                    refresh_lv.smoothScrollToPosition(10);
                        for (int i = 0; i < flow_layout.getChildCount(); i++) {
                            View indexview = flow_layout.getChildAt(i);
                            if (indexview instanceof TextView) {
                                TextView indexTextView = (TextView) indexview;
                                if (indexTextView.getText().equals(checktext)) {
                                    indexTextView.setBackground(resources.getDrawable(R.drawable.bg_gray_tag_shape));
                                    indexTextView.setTextColor(Color.GRAY);
                                    break;
                                }
                            }
                        }
                        checktext = name;
                    }
                });
                flow_layout.addView(view);
            }
        }
    }
}

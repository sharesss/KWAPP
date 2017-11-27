package com.ts.fmxt.ui.discover.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ts.fmxt.R;
import com.ts.fmxt.ui.discover.view.holder.MZHolderCreator;
import com.ts.fmxt.ui.discover.view.holder.MZViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by kp on 2017/11/23.
 */

public class RedPacketsWin  extends PopupWindow {
    private View mMenuView;
    private Activity context;
    private MZBannerView mMZBanner;
    private TextView tv_circleX;
    public String url;
    public static final int []RES = new int[]{R.mipmap.redback,R.mipmap.redback,R.mipmap.redback,R.mipmap.redback,R.mipmap.redback,R.mipmap.redback,R.mipmap.redback};
    public RedPacketsWin(Activity context){
        this.context = context;
        initParam();

    }

    private void initParam() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pop_window_red_packets, null);
        initView();

        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置按钮监听
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
        // 实例化一个ColorDrawable颜色为半透明 0xb0000000
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        this.setOutsideTouchable(true);
        // 显示窗口
        setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
    }

    private void initView() {
        tv_circleX = (TextView) mMenuView.findViewById(R.id.tv_circleX);
        mMZBanner = (MZBannerView) mMenuView.findViewById(R.id.banner);
        mMZBanner.start();//开始轮播
        // 设置页面点击事件
        mMZBanner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int position) {
                Toast.makeText(context,"click page:"+position,Toast.LENGTH_LONG).show();
            }
        });

        List<Integer> list = new ArrayList<>();
        for(int i=0;i<RES.length;i++){
            list.add(RES[i]);
        }
        // 设置数据
        mMZBanner.setPages(list, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });

        tv_circleX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                mMZBanner.pause();
            }
        });

//        tv_collection = (TextView) mMenuView.findViewById(R.id.tv_collection);
    }


    public static class BannerViewHolder implements MZViewHolder<Integer> {
        private ImageView mImageView;
        @Override
        public View createView(Context context) {
            // 返回页面布局文件
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item,null);
            mImageView = (ImageView) view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, Integer data) {
            // 数据绑定
            mImageView.setImageResource(data);
        }
    }

}

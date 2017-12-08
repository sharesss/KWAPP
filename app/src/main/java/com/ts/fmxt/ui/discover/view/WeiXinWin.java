package com.ts.fmxt.ui.discover.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.text.ClipboardManager;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ts.fmxt.R;

import utils.helper.ToastHelper;
import widget.image.FMNetImageView;

/**
 * Created by kp on 2017/11/28.
 * 微信群
 */

public class WeiXinWin extends PopupWindow {
    private View mMenuView;
    private Activity context;
    private WebView mWebView;
    private TextView tv_collection,tv_weixinnum,tv_investName,tv_circleX;
    private FMNetImageView iv_image;
    public String url;
    String weixinNum;
    String weixinCode,investName;
    public WeiXinWin(Activity context,String weixinNum,String weixinCode,String investName){
        this.context = context;
        this.weixinCode =weixinCode;
        this.weixinNum = weixinNum;
        this.investName = investName;
        initParam();

    }

    private void initParam() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pop_window_weixin, null);
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
        tv_collection = (TextView) mMenuView.findViewById(R.id.tv_collection);
        tv_weixinnum = (TextView) mMenuView.findViewById(R.id.tv_weixinnum);
        iv_image = (FMNetImageView) mMenuView.findViewById(R.id.iv_image);
        tv_investName = (TextView) mMenuView.findViewById(R.id.tv_investName);
        tv_circleX = (TextView) mMenuView.findViewById(R.id.tv_circleX);
        tv_investName.setText("【暗号："+investName+"】");
        tv_weixinnum.setText("客服微信："+weixinNum);
        iv_image.loadImage(weixinCode);
        tv_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                if(weixinNum==null){
                    ToastHelper.toastMessage(context,"微信号复制失败");
                }else{
                    ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                    // 将文本内容放到系统剪贴板里。
                    cm.setText(weixinNum);
                    ToastHelper.toastMessage(context,"微信号已复制");
                }

            }
        });
        tv_circleX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }


}

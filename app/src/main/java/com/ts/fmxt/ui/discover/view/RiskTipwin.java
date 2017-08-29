package com.ts.fmxt.ui.discover.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ts.fmxt.R;

import static javax.xml.transform.OutputKeys.METHOD;

/**
 * Created by kp on 2017/8/24.
 * 风险提示
 */

public class RiskTipwin extends PopupWindow {
    private View mMenuView;
    private WebSettings mWebSettings;
    private Activity context;
    private WebView mWebView;
    private TextView tv_collection;
    public String url;
    public RiskTipwin(Activity context,String url){
        this.context = context;
        this.url = url;
        initParam();

    }

    private void initParam() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pop_window_hse, null);
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
        setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void initView() {
        mWebView = (WebView) mMenuView.findViewById(R.id.refresh_web_view);
        tv_collection = (TextView) mMenuView.findViewById(R.id.tv_collection);
        mWebView.loadUrl(url);
        tv_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        initWebSetting();
    }



    private void initWebSetting() {
        mWebSettings = mWebView.getSettings();

        mWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        mWebView.setInitialScale(37);
        mWebView.getSettings().setBuiltInZoomControls(true); //显示放大缩小 controler
        mWebView.getSettings().setSupportZoom(true); //可以缩放
        mWebView.addJavascriptInterface(new JavaScriptInterface(), METHOD);

        mWebSettings.setBuiltInZoomControls(true); // 是否支持缩放（是否使用内置放大机制）
        mWebSettings.setUseWideViewPort(true); //WebView双击变大，再双击后变小，当手动放大后，双击可以恢复到原始大小
        mWebSettings.setJavaScriptEnabled(true); // 是否启用JavaScript
        mWebSettings.setBlockNetworkImage(false); // 把图片加载放在最后来加载渲染
        mWebSettings.setLoadsImagesAutomatically(true);// 是否自动加载图像资源
        mWebSettings.setSupportMultipleWindows(true);// 是否支持多窗口
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);// 是否允许JavaScript自动打开窗口
        mWebSettings.setBlockNetworkImage(false);
        mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);// 设置应用程序缓存模式: 不使用缓存全部从网络获取
        mWebSettings.setDomStorageEnabled(true);// 支持本地存储;
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setDisplayZoomControls(false);

        mWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);

            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {

                }
            }
        });
    }

    class JavaScriptInterface {
        @JavascriptInterface
        public String getToken() {
            String str = "";

            return str;
        }
    }
}

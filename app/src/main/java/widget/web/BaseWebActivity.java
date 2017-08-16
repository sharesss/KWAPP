package widget.web;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.thindo.base.Widget.refresh.RefreshWebView;
import com.thindo.base.Widget.refresh.base.RefreshBase;
import com.ts.fmxt.ui.base.activity.FMBaseActivity;

import java.util.HashMap;
import java.util.Map;

import utils.StringUtils;


public abstract class BaseWebActivity extends FMBaseActivity {

    private RefreshWebView mWebView;
    public WebView acutoWebView;
    private WebSettings mWebSettings;
    public String mTitile;
    public String url;
    private final String METHOD = "api";
    private final String PLAYTAG = "payftp://apApp";
    private final String LOGIN = "status=login";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public void bindWebView(int resourcesId, final String url) {
        if (resourcesId != 0) {
            this.url = url;
            mWebView = (RefreshWebView) findViewById(resourcesId);
            mWebView.setMode(RefreshBase.Mode.PULL_FROM_START);
            acutoWebView = mWebView.getRefreshableView();

            //刷新暂停请于WebView监听器中监听网页加载完毕调用暂停
            mWebView.setOnRefreshListener(new RefreshBase.OnRefreshListener<WebView>() {

                @Override
                public void onRefresh(RefreshBase<WebView> refreshView) {
                    //刷新执行
                    loadUrl();
                }
            });

            initWebSetting();


            acutoWebView.setWebViewClient(new WebViewClient() {

                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {
                    if (url.startsWith("http://") || url.startsWith("https://")) {
                        view.loadUrl(url);
                    } else if (url.indexOf(PLAYTAG) != -1) {
                        platformPay(handlePlayData(url));
                    } else if (url.indexOf(LOGIN) != -1) {
                        login();
                    } else if ("ceftp://apApp?status=exit".equals(url)) {
                        finish();
                    }
                    return true;
                }

                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon) {
                    super.onPageStarted(view, url, favicon);
                    mWebView.setMode(RefreshBase.Mode.DISABLED);
                }
            });

            mWebView.setRefreshing(true);
        }
    }

    private void initWebSetting() {
        mWebSettings = acutoWebView.getSettings();

        acutoWebView.setScrollBarStyle(WebView.SCROLLBARS_OUTSIDE_OVERLAY);
        acutoWebView.setInitialScale(37);
        acutoWebView.getSettings().setBuiltInZoomControls(true); //显示放大缩小 controler
        acutoWebView.getSettings().setSupportZoom(true); //可以缩放
        acutoWebView.addJavascriptInterface(new JavaScriptInterface(), METHOD);

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

        acutoWebView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (mTitile == null || "".equals(mTitile)) {
                    mTitile = title;
                    navigationView.setTitle(mTitile, new OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            getBackActive();
                        }
                    });
                }
            }

            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                if (newProgress == 100) {
                    mWebView.onRefreshComplete();
                }
            }
        });
    }

    /**
     * 返回
     */
    public void getBackActive() {
        if (acutoWebView != null) {
            if (acutoWebView.getUrl().equals(url)) {
                finish();
            } else {
                acutoWebView.goBack();
            }
        }
    }

    public abstract void platformPay(Map<String, String> map);

    public abstract void login();

    public abstract void loadUrl();

    public RefreshWebView getmWebView() {
        return mWebView;
    }

    public WebView getAcutoWebView() {
        return acutoWebView;
    }

    private Map<String, String> handlePlayData(String url) {
        Map<String, String> map = new HashMap<>();
        try {
            if (!StringUtils.isEmpty(url)) {
                String[] tempArray = url.split("&");
                for (int i = 0; i < tempArray.length; i++) {
                    String childStr = tempArray[i];
                    if (childStr.indexOf("App") != -1) {
                        String[] outArray = childStr.split("\\?");
                        if (outArray.length > 0)
                            childStr = outArray[1];
                    }
                    String[] mapChils = childStr.split("=");
                    map.put(mapChils[0], mapChils[1]);
                }
            }
        } catch (Exception e) {
            Log.e(this.getClass().getName(), "支付数据解析错误==" + e.toString());
        }
        return map;
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (goBack())
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private boolean goBack() {
        if (acutoWebView.canGoBack()) {
            acutoWebView.goBack();
            return true;
        }
        return false;
    }


    class JavaScriptInterface {
        @JavascriptInterface
        public String getToken() {
            String str = "";

            return str;
        }
    }
}

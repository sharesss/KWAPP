package widget.web;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.ts.fmxt.R;

import java.util.Map;

import utils.sharePreferences.FMWession;

/**
 * Created by kp on 2017/5/15.
 */
public class FMUserPrivilegeCouponsActivity extends BaseWebActivity {

    private String url;
    private int  num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.include_web_view);
        mTitile = getIntent().getStringExtra("title");
        url = getIntent().getStringExtra("url");
        num = getIntent().getIntExtra("num", 0);
        if (url == null || "".equals(url)) {
            finish();
        }
        initNavigation();
        bindWebView(R.id.refresh_web_view, url);

    }

    @Override
    public void platformPay(Map<String, String> map) {

    }

    @Override
    public void login() {
//        UISkipUtils.showMes(this,"login");
        finish();
    }

    public void initNavigation() {
        if (mTitile != null && !"".equals(mTitile)) {
            navigationView.setTitle(mTitile, new OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                    getBackActive();
                }
            });
        }
    }

    @Override
    public void loadUrl() {
        getAcutoWebView().loadUrl(url + "?userCashCouponId=" + num + "&Identification=App&userId="+ FMWession.getInstance().getUserId());
    }
}

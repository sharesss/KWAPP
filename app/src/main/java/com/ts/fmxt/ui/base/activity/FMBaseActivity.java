package com.ts.fmxt.ui.base.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;

import com.thindo.base.NetworkAPI.BaseResponse;
import com.thindo.base.NetworkAPI.OnResponseListener;
import com.thindo.base.UI.Activity.BaseActivity;
import com.thindo.base.Widget.refresh.RefreshScrollView;
import com.ts.fmxt.R;

import utils.StringUtils;
import utils.helper.ToastHelper;
import widget.NetworkView;
import widget.dialog.FMBProgressDialog;
import widget.titlebar.NavigationView;


public abstract class FMBaseActivity extends BaseActivity implements OnResponseListener,
        NetworkView.NetworkViewListener {

    protected FMBProgressDialog dialog;
    protected NavigationView navigationView;
    public NetworkView mNetworkView;
    private String noData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dialog = new FMBProgressDialog(this);
    }

    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);

        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        mNetworkView = (NetworkView) findViewById(R.id.network);
        if (mNetworkView != null)
            mNetworkView.setmCallBack(this);
    }

    @Override
    public void onStart(BaseResponse response) {
        if (response.isShowProgress()) {
            dialog.show();
        }
    }

    @Override
    public void onFailure(BaseResponse response) {
        if (mNetworkView != null && !cheakNetwork()) {
            mNetworkView.setVisibility(View.VISIBLE);
        }
        if (response.isShowProgress()) {
            dialog.dismiss();
        }
        String error;
        if (StringUtils.isEmpty(response.getError_msg())) {
            error = String.format("%s,异常编码[%s]", getResourcesStr(R.string.msg_error_msg), response.getStatus());
        } else {
            error = response.getError_msg();
        }
        if (!StringUtils.isEmpty(noData)) {
            error = noData;
        }
        ToastHelper.toastMessage(this, error);
    }

    @Override
    public void onSuccess(BaseResponse response) {
        if (mNetworkView != null)
            mNetworkView.setVisibility(View.GONE);

        if (response.isShowProgress()) {
            dialog.dismiss();
        }

//        if (response.getStatus() == 5) {//冻结账户
//            FMWession.clear(this);
//            FMWession.getInstance().setToken("");
//            FMWession.getInstance().setLoginInfo("");
//            FMWession.getInstance().setUserLike("");
//            FMWession.getInstance().setUserInfo("");
//            EMClient.getInstance().logout(true);
//            ReceiverUtils.sendReceiver(ReceiverUtils.OUT_LOGIN, null);
//            ShortcutBadger.applyCount(FMBApplication.getContext(), 0);
//            UISkipUtils.showMsgPopup(this, response.getError_msg());
//            return;
//        }
    }

    public void notRefreshFlag(int resourcesId) {
        notRefreshFlag(resourcesId, 0);
    }

    /**
     * 屏蔽刷新方法
     */
    public void notRefreshFlag(int resourcesId, int layoutId) {
        RefreshScrollView refreshSV = (RefreshScrollView) findViewById(resourcesId);
        refreshSV.setPullNoRefreshMode();
        if (layoutId != 0) {
            ScrollView sv = refreshSV.getRefreshableView();
            sv.removeAllViews();
            LayoutInflater.from(this).inflate(layoutId, sv);
        }
    }

    @Override
    public void networkErrorReload() {


    }

    //为了不用老是转换类型
    public <T> T findViewByIds(int id) {
        T view = (T) findViewById(id);
        return view;
    }

    public void setNoData(String noData) {
        this.noData = noData;
    }
}

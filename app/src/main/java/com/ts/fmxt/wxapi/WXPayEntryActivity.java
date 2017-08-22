package com.ts.fmxt.wxapi;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.tencent.mm.opensdk.constants.ConstantsAPI;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;

import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.ts.fmxt.R;

import utils.helper.ToastHelper;
import widget.weixinpay.Constants;

public class WXPayEntryActivity extends Activity implements IWXAPIEventHandler {

    private static final String TAG = "MicroMsg.SDKSample.WXPayEntryActivity";
    private IWXAPI api;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pay_result);

        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID);
        api.handleIntent(getIntent(), this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        api.handleIntent(intent, this);
    }

    @Override
    public void onReq(BaseReq req) {
    }

    @Override
    public void onResp(BaseResp resp) {
        if (resp.getType() == ConstantsAPI.COMMAND_PAY_BY_WX) {
            String msg = "";
            if (resp.errCode == 0) {
                msg = "支付成功";
//                ReceiverUtils.sendReceiver(ReceiverUtils.WX_PLAY, null);
//                if (!StringUtils.isEmpty(FMWession.getInstance().getHuaJiaoPlayKey())) {//通知花椒回调
//
//                } else {
//                    if (FMWession.getConfigBoolean(this, "isSendGift")) {//礼物打赏
//                        FMWession.setCofigBoolean(this, true, "GIFT_PLAY");
//                    }
//                    if (FMWession.getConfigBoolean(this, "SIGN_TAG")) {//活动报名
//
//                        ReceiverUtils.sendReceiver(ReceiverUtils.REFRESH_SIGN_CODE_TKE, null);
//                    }


//                }
            } else if (resp.errCode == -1) {
                msg = "已取消支付";
            } else if (resp.errCode == -2) {
                msg = "支付失败";
//                if (!StringUtils.isEmpty(FMWession.getInstance().getHuaJiaoPlayKey())) {

//                }
            }
            ToastHelper.toastMessage(this, msg);
            finish();
        }
    }
}
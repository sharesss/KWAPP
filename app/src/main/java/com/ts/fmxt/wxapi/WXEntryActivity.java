/*
 * 官网地站:http://www.mob.com
 * 技术支持QQ: 4006852216
 * 官方微信:ShareSDK   （如果发布新版本的话，我们将会第一时间通过微信将版本更新内容推送给您。如果使用过程中有任何问题，也可以通过微信与我们取得联系，我们将会在24小时内给予回复）
 *
 * Copyright (c) 2013年 mob.com. All rights reserved.
 */

package com.ts.fmxt.wxapi;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.squareup.okhttp.Request;
import com.tencent.mm.opensdk.modelbase.BaseReq;
import com.tencent.mm.opensdk.modelbase.BaseResp;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.thindo.base.NetworkAPI.BaseResponse;
import com.thindo.base.NetworkAPI.OnResponseListener;
import com.ts.fmxt.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.sharesdk.wechat.utils.WechatHandlerActivity;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.ReceiverUtils;
import utils.UISKipUtils;
import utils.helper.ToastHelper;
import widget.weixinpay.Constants;



/**
 * 微信客户端回调activity示例   微信授权登录与绑定
 */
public class WXEntryActivity extends WechatHandlerActivity implements IWXAPIEventHandler,
        OnResponseListener {
    private IWXAPI api;

    /**
     * 处理微信发出的向第三方应用请求app message
     * <p/>
     * 在微信客户端中的聊天页面有“添加工具”，可以将本应用的图标添加到其中
     * 此后点击图标，下面的代码会被执行。Demo仅仅只是打开自己而已，但你可
     * 做点其他的事情，包括根本不打开任何页面
     */
//    public void onGetMessageFromWXReq(WXMediaMessage msg) {
//        Intent iLaunchMyself = getPackageManager().getLaunchIntentForPackage(getPackageName());
//        startActivity(iLaunchMyself);
//    }

    /**
     * 处理微信向第三方应用发起的消息
     * <p/>
     * 此处用来接收从微信发送过来的消息，比方说本demo在wechatpage里面分享
     * 应用时可以不分享应用文件，而分享一段应用的自定义信息。接受方的微信
     * 客户端会通过这个方法，将这个信息发送回接收方手机上的本demo中，当作
     * 回调。
     * <p/>
     * 本Demo只是将信息展示出来，但你可做点其他的事情，而不仅仅只是Toast
     */
//    public void onShowMessageFromWXReq(WXMediaMessage msg) {
//        if (msg != null && msg.mediaObject != null
//                && (msg.mediaObject instanceof WXAppExtendObject)) {
//            WXAppExtendObject obj = (WXAppExtendObject) msg.mediaObject;
//            Toast.makeText(this, obj.extInfo, Toast.LENGTH_SHORT).show();
//        }
//    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wxlogin2);
        api = WXAPIFactory.createWXAPI(this, Constants.APP_ID, false);
        //开启微信登录页面
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
        Toast.makeText(this, "失败", Toast.LENGTH_LONG).show();
        finish();
    }

    //微信回调结果
    @Override
    public void onResp(BaseResp resp) {
        try {
            if (resp.errCode == BaseResp.ErrCode.ERR_OK) {
                    String code = ((SendAuth.Resp) resp).code;//需要转换一下才可以
                weiXinAuthorizationRequest(code);
            }
        } catch (Exception e) {
            e.toString();
        }
    }

    @Override
    public void onStart(BaseResponse response) {

    }

    @Override
    public void onFailure(BaseResponse response) {

    }

    @Override
    public void onSuccess(BaseResponse response) {

        if (response.getStatus() == BaseResponse.SUCCEED) {
            ReceiverUtils.sendReceiver(ReceiverUtils.REGISTER_FINISH, null);
//            FMWession.getInstance().setRefreshUser(true);
            ToastHelper.toastMessage(this, getResourcesStr(R.string.toast_login_succeed));
            //注册极光推送
//            JpushAPI.getInsert().setAlias();
            finish();
        } else {
            ToastHelper.toastMessage(this,"登录失败");
//            UISkipUtils.startLoginActivity(this);

            finish();


        }
    }


    /**
     * 获取文本资源
     */
    public String getResourcesStr(int resourcesId) {
        return getResources().getString(resourcesId);
    }

    private void weiXinAuthorizationRequest(String code){
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("code", String.valueOf(code));
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.WEIXINAUTHORIZATION,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String result) {
                        try {
                            JSONObject js = new JSONObject(result);
                            if (!js.isNull("statsMsg")) {
                                JSONObject json = js.optJSONObject("statsMsg");
                                String stats = json.getString("stats");
                                String msg = json.getString("msg");
                                if (stats.equals("1")) {

                                    if(!js.isNull("identification")){
                                        int  identification = js.getInt("identification");
                                        if(identification==0){
                                            ToastHelper.toastMessage(WXEntryActivity.this, msg);
                                            UISKipUtils.satrtWeChatCompleteInformation(WXEntryActivity.this,result);
                                        }else{
                                            UISKipUtils.startMainFrameActivity(WXEntryActivity.this);
                                            SharedPreferences share = getSharedPreferences("user",MODE_PRIVATE);
                                            SharedPreferences.Editor editor = share.edit(); //使处于可编辑状态
                                            String account = js.getString("account");
                                            String token = js.getString("token");
                                            editor.putString("token", token);
                                            editor.putString("phone", account);
                                            editor.commit();    //提交数据保存
                                        }
                                    }

                                } else {
                                    ToastHelper.toastMessage(WXEntryActivity.this, msg);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, staff
        );

    }

}

package com.ts.fmxt.ui.user.login;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.squareup.okhttp.Request;
import com.tencent.mm.opensdk.modelmsg.SendAuth;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.thindo.base.NetworkAPI.BaseResponse;
import com.ts.fmxt.FmxtApplication;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.base.activity.FMBaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.ReceiverUtils;
import utils.StringUtils;
import utils.UISKipUtils;
import utils.helper.ToastHelper;
import widget.ContainsEmojiEditText;

/**
 * Created by A1 on 2015/11/30.
 */
public class LoginActivity extends FMBaseActivity implements View.OnClickListener, ReceiverUtils
        .MessageReceiver {
    private IWXAPI api;
    private ContainsEmojiEditText edtAccount, edtPwd;
    private String weixin_code = "";
    private String weixinIMP = "Uak79nWqDh1VAYLV27S1mA==";//微信静态密码登录环信

    private boolean flg = false;
    private boolean accountFlg = false;
    private boolean passwordFlg = false;
    private TextView btLogin;

    @Override
    public void onMessage(int receiverType, Bundle bundle) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
//        ReceiverUtils.addReceiver(this);
        // 微信授权登录
        //通过微信工厂,获取实例
        api = WXAPIFactory.createWXAPI(this, FmxtApplication.APP_ID, true);
        //将应用appid注册到微信
        api.registerApp(FmxtApplication.APP_ID);
        initView();
    }


    private void initView() {
        btLogin = (TextView) findViewById(R.id.bt_login);
        btLogin.setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
        findViewById(R.id.click_wx).setOnClickListener(this);
        findViewById(R.id.bt_forget_password).setOnClickListener(this);
        findViewById(R.id.btn_finish).setOnClickListener(this);

        edtAccount = (ContainsEmojiEditText) findViewById(R.id.ed_account);
        edtPwd = (ContainsEmojiEditText) findViewById(R.id.ed_password);

        edtPwd.setContainsEmojiEditTextInterface(new ContainsEmojiEditText.ContainsEmojiEditTextInterface() {
            @Override
            public void onTextChangedInterface(String str) {
                if (!StringUtils.isEmpty(str) && str.length() >= 6) {
                    passwordFlg = true;
                } else {
                    passwordFlg = false;
                }
                onchageLoginButton();
            }
        });

        edtAccount.setContainsEmojiEditTextInterface(new ContainsEmojiEditText.ContainsEmojiEditTextInterface() {
            @Override
            public void onTextChangedInterface(String str) {
                if (!StringUtils.isEmpty(str) && str.length() == 11) {
                    accountFlg = true;
                } else {
                    accountFlg = false;
                }
                onchageLoginButton();
            }
        });
    }

    private void onchageLoginButton() {
        if (accountFlg && passwordFlg) {
            btLogin.setBackground(getResources().getDrawable(R.drawable.bg_orange_shape));
            btLogin.setTextColor(getResourcesColor(R.color.white));
        } else {
            btLogin.setBackground(getResources().getDrawable(R.drawable.bg_gray_shape));
            btLogin.setTextColor(getResourcesColor(R.color.font_main_secondary));
        }
    }


    @Override
    public void onSuccess(BaseResponse response) {
        super.onSuccess(response);

        if (response.getStatus() == BaseResponse.SUCCEED) {
            switch (response.getRequestType()) {
                case 1:
//                    FMWession.getInstance().setUserAccount(edtAccount.getText().toString());

//                    ReceiverUtils.sendReceiver(ReceiverUtils.REFRESH_INDEX, null);
                    finish();
                    break;
                case 2:
                    break;
            }

        } else {
//             UISkipUtils.showMsgPopup(this,getResourcesStr(R.string.tip_sys),response.getError_msg());
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_finish:
                finish();
                break;
            case R.id.bt_login://登录
                if (accountFlg && passwordFlg) {
                    String account = edtAccount.getText().toString();
                    String password = edtPwd.getText().toString();
                    loginRequest(account, password);
                }
                break;
            case R.id.btn_register:   //注册
                UISKipUtils.startRegister(this);
                break;
            case R.id.click_wx:
                boolean sIsWXAppInstalledAndSupported = api.isWXAppInstalled() && api.isWXAppSupportAPI();
                if (!sIsWXAppInstalledAndSupported) {
                    ToastHelper.toastMessage(this, getResourcesStr(R.string.toast_wx_login));
                    return;
                }
                SendAuth.Req req = new SendAuth.Req();
                req.scope = "snsapi_userinfo";
                api.sendReq(req);

                break;
            case R.id.bt_forget_password:   //重置密码
                UISKipUtils.startForgetActivity(this);
                break;
        }
    }


    //登录
    private void loginRequest(final String account, String password) {
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("account",account);
        staff.put("passWord",password);
        OkHttpClientManager.postAsyn(HttpPathManager.HOST+HttpPathManager.LOGIN,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String result) {
                        try {
                        JSONObject js  = new JSONObject(result);
                            if(!js.isNull("statsMsg")){
                                JSONObject json = js.optJSONObject("statsMsg");
                                String stats = json.getString("stats");
                                String msg = json.getString("msg");
                                if(stats.equals("1")){
                                    if(!js.isNull("token")){
                                        String token = js.optString("token");
                                        SharedPreferences share = getSharedPreferences("user",MODE_PRIVATE);
                                        SharedPreferences.Editor editor = share.edit(); //使处于可编辑状态
                                        editor.putString("token", token);
                                        editor.putString("phone", edtAccount.getText().toString());
                                        editor.commit();    //提交数据保存
                                        loginIM(account);
                                    }
                                    ToastHelper.toastMessage(LoginActivity.this,msg);
                                    UISKipUtils.startMainFrameActivity(LoginActivity.this,2);
                                    finish();
                                }else{
                                    ToastHelper.toastMessage(LoginActivity.this,msg);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },staff
        );

    }
    private String getAppInfo() {
        try {
            String pkName = this.getPackageName();
            String versionName = this.getPackageManager().getPackageInfo(
                    pkName, 0).versionName;
            int versionCode = this.getPackageManager()
                    .getPackageInfo(pkName, 0).versionCode;
            return pkName ;
        } catch (Exception e) {
        }
        return null;
    }
    //微信登录
    public void thirdpartyLoginRequest() {
        flg = true;
//        ThirdpartyLoginRequest request = new ThirdpartyLoginRequest();
//        request.setOnResponseListener(this);
//        request.setCode(weixin_code);
//        request.setRequestType(1);
//        request.setAccountType(1);
//        request.executePost(false);

    }


    private void loginIM(String account) {
        EMClient.getInstance().logout(true);
        EMClient.getInstance().login(account, FmxtApplication.IM_PSW, new EMCallBack() {//回调
            @Override
            public void onSuccess() {
                EMClient.getInstance().groupManager().loadAllGroups();
                EMClient.getInstance().chatManager().loadAllConversations();
            }

            @Override
            public void onProgress(int progress, String status) {

            }

            @Override
            public void onError(int code, String message) {
                final String msg = "第三方登录失败,请联系疯蜜客服!错误码[" + message + "]";
                LoginActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastHelper.toastMessage(getBaseContext(), msg);
                    }
                });
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ReceiverUtils.removeReceiver(this);
    }

}

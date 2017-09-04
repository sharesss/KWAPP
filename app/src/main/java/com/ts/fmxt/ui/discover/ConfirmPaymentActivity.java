package com.ts.fmxt.ui.discover;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.ts.fmxt.FmxtApplication;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.base.activity.FMBaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import http.data.WeiXinPayEntity;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.Tools;
import utils.helper.ToastHelper;
import widget.titlebar.NavigationView;
import widget.weixinpay.playUitls;



/**
 * Created by kp on 2017/8/18.
 * 确认支付
 */

public class ConfirmPaymentActivity extends FMBaseActivity {
    private TextView tvPercentage,tvTotalAmount,tvReservationMoney,tvConfirm;
    private EditText edtName,edtWechat,edtPhone;
    private int money;
    private String proportion;
    private int investId;
    private String arrmoney;
    private String ShareRatio;
    private String CompanyName;
    private WeiXinPayEntity entity;
    private IWXAPI api;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_payment);
        money = getIntent().getIntExtra("money", -1);
        proportion = getIntent().getStringExtra("proportion");
        investId = getIntent().getIntExtra("investId", -1);
        CompanyName= getIntent().getStringExtra("CompanyName");
        arrmoney = proportion.substring(0,proportion.indexOf("万"));
        ShareRatio = proportion.substring(proportion.indexOf("股")+1,proportion.indexOf("%"));

        api = WXAPIFactory.createWXAPI(this, FmxtApplication.APP_ID, true);
        //将应用appid注册到微信
        api.registerApp(FmxtApplication.APP_ID);
        initView();
    }

    private void initView(){
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setTitle(R.string.confirm_payment, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    finish();
            }
        });
        tvPercentage = (TextView) findViewById(R.id.tv_percentage);
        tvTotalAmount = (TextView) findViewById(R.id.tv_total_amount);
        edtName = (EditText) findViewById(R.id.edt_name);
        edtWechat = (EditText) findViewById(R.id.edt_wechat);
        edtPhone= (EditText) findViewById(R.id.edt_phone);
        tvReservationMoney = (TextView) findViewById(R.id.tv_reservation_money);
        tvPercentage.setText(CompanyName+"股权("+ShareRatio+"%)");
        tvTotalAmount.setText("¥"+arrmoney+"0000");
        tvReservationMoney.setText("¥"+money);
        tvConfirm = (TextView) findViewById(R.id.tv_wxconfirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ToastHelper.toastMessage(getBaseContext(), "此功能开发中，敬请期待");

                if (Tools.isFastDoubleClick()) {
                    ToastHelper.toastMessage(getBaseContext(), "请勿重复操作");
                    return;
                }
                boolean sIsWXAppInstalledAndSupported = api.isWXAppInstalled() && api.isWXAppSupportAPI();
                if (!sIsWXAppInstalledAndSupported) {
                    ToastHelper.toastMessage(ConfirmPaymentActivity.this,"您还没安装微信");
                    return;
                }
                String name = edtName.getText().toString();
                String wechat = edtWechat.getText().toString();
                String phone = edtPhone.getText().toString();
                if(name.equals("")){
                    ToastHelper.toastMessage(ConfirmPaymentActivity.this,"请填写您的名字");
                    return;
                }
                if(wechat.equals("")){
                    ToastHelper.toastMessage(ConfirmPaymentActivity.this,"请填写您的微信号");
                    return;
                }
                if(phone.equals("")){
                    ToastHelper.toastMessage(ConfirmPaymentActivity.this,"请填写您的手机号");
                    return;
                }
                WechatPay();
            }
        });
    }

    private void WechatPay(){
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                MODE_PRIVATE);
        String name = edtName.getText().toString();
        String wechat = edtWechat.getText().toString();
        String phone = edtPhone.getText().toString();
        String token=sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("investId", String.valueOf(investId));
        staff.put("tokenId", String.valueOf(token));
        staff.put("amount", arrmoney);
        staff.put("stockRate", ShareRatio);
        staff.put("investPeople", name);
        staff.put("wechatNumber", wechat);
        staff.put("contactWay", phone);
        staff.put("body", "支付预约金");
        staff.put("totalFee", "1");
        staff.put("clientType", "2");
        staff.put("orderType", "1");


        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.SAVEUSERINVESTPROJECTFOLLOW,
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
                                    if(!js.isNull("unifiedOrder")){
                                        entity =   new WeiXinPayEntity(js.optJSONObject("unifiedOrder"));
                                    }
                                  playUitls.getInstance().weixinPlay(entity, ConfirmPaymentActivity.this);

                                } else {
                                    ToastHelper.toastMessage(ConfirmPaymentActivity.this, msg);
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

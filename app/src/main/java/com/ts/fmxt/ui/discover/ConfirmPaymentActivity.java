package com.ts.fmxt.ui.discover;

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.ts.fmxt.FmxtApplication;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.base.activity.FMBaseActivity;
import com.ts.fmxt.ui.discover.view.PopupWheelMoneyView;
import com.ts.fmxt.ui.user.view.WheelListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import http.data.WeiXinPayEntity;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.ReceiverUtils;
import utils.Tools;
import utils.helper.ToastHelper;
import widget.titlebar.NavigationView;
import widget.weixinpay.playUitls;


/**
 * Created by kp on 2017/8/18.
 * 确认支付
 */

public class ConfirmPaymentActivity extends FMBaseActivity implements ReceiverUtils.MessageReceiver, WheelListener,View.OnClickListener {
    private TextView tvPercentage,tvTotalAmount,tvReservationMoney,tvConfirm;
    private TextView tv_reduce,tv_num,tv_add,tv_surplus_share,tv_surplus_num;
    private EditText edtName,edtWechat,edtPhone;
    private int money;
    private PopupWheelMoneyView mPopupWheelMoneyView;
//    private String proportion;
    private int investId,rewardId;
    private String arrmoney;
    private String ShareRatio;
    private String CompanyName,PeopleNum;
    private Double InitiateAmount;
    private int type,AlreadyBookedMoney,YetReservePropleNum,FinancingAmount;
    private WeiXinPayEntity entity;
    private IWXAPI api;
    int num = 1;
    @Override
    public void onMessage(int receiverType, Bundle bundle) {
        if(receiverType==ReceiverUtils.WX_PLAY){
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_payment);
        ReceiverUtils.addReceiver(this);
        money = getIntent().getIntExtra("money", -1);
        type = getIntent().getIntExtra("type", -1);//1按照名额，2按照股权金额
        InitiateAmount = getIntent().getDoubleExtra("InitiateAmount",0);//起投金额（万）
        AlreadyBookedMoney = getIntent().getIntExtra("AlreadyBookedMoney",0);//已预约金额
//        proportion = getIntent().getStringExtra("proportion");
        investId = getIntent().getIntExtra("investId", -1);
        rewardId = getIntent().getIntExtra("id", -1);
        CompanyName= getIntent().getStringExtra("CompanyName");
        PeopleNum= getIntent().getStringExtra("PeopleNum");//总人数
        YetReservePropleNum= getIntent().getIntExtra("YetReservePropleNum",0);//入股人数
        FinancingAmount = getIntent().getIntExtra("FinancingAmount",0);//目标金额
//        arrmoney = proportion.substring(0,proportion.indexOf("万"));
//        ShareRatio = proportion.substring(proportion.indexOf("股")+1,proportion.indexOf("%"));

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
        tvPercentage.setText(CompanyName);
        tv_reduce = (TextView) findViewById(R.id.tv_reduce);
        tv_num = (TextView) findViewById(R.id.tv_num);
        tv_add = (TextView) findViewById(R.id.tv_add);
        tv_surplus_share = (TextView) findViewById(R.id.tv_surplus_share);
        tv_surplus_num = (TextView) findViewById(R.id.tv_surplus_num);
//        tvTotalAmount.setText("¥"+arrmoney+"0000");
        if(type==1){
            tvReservationMoney.setText("¥"+money);
            if(PeopleNum.equals("null")){
                tv_surplus_share.setText("可预约份数无限份");
            }else{
                tv_surplus_share.setText("可预约份数"+PeopleNum+"份");
            }
            tv_num.setText("1");
//            tv_surplus_num.setText("（可预约份数"+PeopleNum+"份）");
        }else if(type==2){
            int mount= (int) (InitiateAmount*1000);
            tvReservationMoney.setText("¥"+mount);
            int money  =FinancingAmount-AlreadyBookedMoney;
            tv_surplus_share.setText("可投金额剩余"+money+"万");
            num = (new Double(InitiateAmount)).intValue();
            Drawable sexDrawble = getResources().getDrawable( R.mipmap.down_arrow );
            sexDrawble.setBounds(0, 0, sexDrawble.getMinimumWidth(), sexDrawble.getMinimumHeight());
            tv_num.setCompoundDrawables(null, null, sexDrawble, null);
            tv_num.setText(num+"万");
            tv_num.setOnClickListener(this);

        }

        tvConfirm = (TextView) findViewById(R.id.tv_wxconfirm);
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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
                if(!isMobileNO(phone)){
                    ToastHelper.toastMessage(ConfirmPaymentActivity.this,"手机号有误");
                    return;
                }
                WechatPay();
            }
        });



        tv_reduce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 1) {
                    if (num <= 1) {
                        return;
                    }
                    num = Integer.valueOf(tv_num.getText().toString()) - 1;
                    tvReservationMoney.setText("¥" + money * num);
                    tv_num.setText(num + "");
                }else if(type == 2){
                    int nums = (new Double(InitiateAmount)).intValue();
                    if (num  <= nums) {
                        return;
                    }
                    num =num-1;
                    int mount= (int) (  num*1000);
                    tvReservationMoney.setText("¥" + mount);
                    tv_num.setText(num + "万");
                }
            }
        });

        tv_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (type == 1) {
                    if(PeopleNum.equals("null")){
//                        int numl =  num+1;
//                        if (num >= numl) {
//                            return;
//                        }
                        num = Integer.valueOf(tv_num.getText().toString()) + 1;
                        tvReservationMoney.setText("¥" + money * num);
                        tv_num.setText(num + "");

                    }else{
                        int numl = Integer.valueOf(PeopleNum);
                        if (num >= numl) {
                            return;
                        }
                        num = Integer.valueOf(tv_num.getText().toString()) + 1;
                        tvReservationMoney.setText("¥" + money * num);
                        tv_num.setText(num + "");
                    }

                }else if(type == 2){
                    int money  =FinancingAmount-AlreadyBookedMoney;
                    if (num >= money) {
                        return;
                    }
                    num = num + 1;
                    int mount= (int) ( num*1000);//InitiateAmount *
                    tvReservationMoney.setText("¥" + mount);
                    tv_num.setText(num + "万");
                }
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
        staff.put("rewardId", String.valueOf(rewardId));
            staff.put("amount",num+"");
        staff.put("stockRate", "1");
        staff.put("investPeople", name);
        staff.put("shareType", type+"");
        staff.put("wechatNumber", wechat);
        staff.put("contactWay", phone);
        staff.put("body", "支付预约金");
        if(type == 1){
            staff.put("totalFee",  String.valueOf(money*num*100));//
        }else if(type == 2){
            int mount= (int) (num*1000*100);
            staff.put("totalFee",  String.valueOf(mount));//
        }

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

    private String age="未填写";
    @Override
    public void completeCall(String text, String text2, int type) {
        switch (type) {
            case 4://年龄
                age =text;
                tv_num.setText(text+"万");
                num = Integer.valueOf(text);
                int mount=  (Integer.valueOf(text)*1000);
                tvReservationMoney.setText("¥" + mount);
//                isChange = true;
                break;

        }
    }

    @Override
    public void clearCall(int type) {
        String str = getResourcesStr(R.string.user_info_nodata);
        switch (type) {
            case 4://年龄
                age =str;
//                isChange = true;
//                tvAge.setText(str);
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ReceiverUtils.removeReceiver(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_num:
                int money  = FinancingAmount-AlreadyBookedMoney;
                mPopupWheelMoneyView = new PopupWheelMoneyView(ConfirmPaymentActivity.this, tv_num.getText().toString(),InitiateAmount,money);
                mPopupWheelMoneyView.setWheelListener(this);
                mPopupWheelMoneyView.showPopupWindow();
                break;
        }
    }

    /**
     * 验证手机格式
     *
     移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
     联通：130、131、132、152、155、156、185、186
     电信：133、153、180、189、（1349卫通）
     总结起来就是第一位必定为1，第二位必定为3或5或8，其他位置的可以为0-9
     */

    public  boolean isMobileNO(String mobiles) {

        String telRegex = "[1][358]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else return mobiles.matches(telRegex);
    }
}

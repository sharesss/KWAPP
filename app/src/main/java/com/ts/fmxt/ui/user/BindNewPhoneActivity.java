package com.ts.fmxt.ui.user;/**
 * Created by A1 on 2017/7/27.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.base.activity.FMBaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.CountDownButtonHelper;
import utils.ReceiverUtils;
import utils.StringUtils;
import utils.Tools;
import utils.helper.ToastHelper;

/**
 * created by KP at 2017/7/27
 * 绑定新手机
 */
public class BindNewPhoneActivity extends FMBaseActivity implements View.OnClickListener{
    private String phone;
    private EditText registerPhone;
    private EditText registerSmsCode;
    private TextView tvSendCode;
    private TextView btnNexts;
    private CountDownButtonHelper helper;
    private boolean sendCodeFlg = false;
    private boolean registerPhoneFlg = false;
    private boolean registerSmsCodeFlg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bind_new_phone);
        initView();

    }

    private void initView() {
        registerPhone = (EditText) findViewById(R.id.register_phone);
        registerSmsCode = (EditText) findViewById(R.id.register_sms_code);
        tvSendCode = (TextView) findViewById(R.id.tv_send_code);
        btnNexts = (TextView) findViewById(R.id.btn_nexts);
        helper = new CountDownButtonHelper(tvSendCode, getResourcesStr(R.string.toast_register_send_code), 60, 1);
        registerPhone.addTextChangedListener(new PhoneTextWatcher());
        registerSmsCode.addTextChangedListener(new CodeTextWatcher());
        tvSendCode.setOnClickListener(this);
        btnNexts.setOnClickListener(this);
        findViewById(R.id.btn_finish).setOnClickListener(this);
        helper.setOnFinishListener(new CountDownButtonHelper.OnFinishListener() {
            @Override
            public void finish() {
                if (registerPhone.getText().toString().length() != 11)
                    registerPhoneFlg = false;
                tvSendCode.setText(getResourcesStr(R.string.toast_register_code));
                sendCodeFlg = false;
                nextButtonChange();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_send_code:
                if (Tools.isFastDoubleClick())
                    return;
                if (sendCodeFlg == false && registerPhoneFlg == true)
                    randomCodeRequest();
                break;
            case R.id.btn_nexts:
                if (Tools.isFastDoubleClick())
                    return;
                if (registerPhoneFlg && sendCodeFlg) {
                    VerifyRandomCodeRequest();
                }
//                finish();
                break;
            case R.id.btn_finish:
                finish();
                break;
        }
    }



    class PhoneTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!StringUtils.isEmpty(s.toString()) && s.toString().length() == 11) {
                registerPhoneFlg = true;
//                tvSendCode.setBackground(getResources().getDrawable(R.drawable.bg_orange_5_shape));
            } else {
                registerPhoneFlg = false;
//                tvSendCode.setBackground(getResources().getDrawable(R.drawable.bg_gray_5_shape));
            }
            nextButtonChange();
        }
    }

    //短信验证码改变监听
    class CodeTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (!StringUtils.isEmpty(s.toString()) && s.toString().length() == 6)
                registerSmsCodeFlg = true;
            else
                registerSmsCodeFlg = false;
            nextButtonChange();
        }
    }

    private void nextButtonChange() {
        if (registerPhoneFlg && registerSmsCodeFlg) {
            btnNexts.setTextColor(getResourcesColor(R.color.white));
            btnNexts.setBackground(getResources().getDrawable(R.drawable.bg_orange_shape));
        } else {
            btnNexts.setTextColor(getResourcesColor(R.color.white));
            btnNexts.setBackground(getResources().getDrawable(R.drawable.bg_gray_dcdc_hape));
        }
        if (sendCodeFlg == false && registerPhoneFlg == true) {
//            tvSendCode.setBackground(getResources().getDrawable(R.drawable.bg_orange_shape));
            tvSendCode.setTextColor(getResourcesColor(R.color.orange));
        } else {
//            tvSendCode.setBackground(getResources().getDrawable(R.drawable.bg_gray_shape));
            tvSendCode.setTextColor(getResourcesColor(R.color.orange));
        }
    }

    //检查发送短信
    public void randomCodeRequest() {
        String phone = registerPhone.getText().toString().trim();
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("telephoneint",phone);
        staff.put("type","3");
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.SENTOBTAIN,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String result) {
                        if (result.equals("1")) {
                            helper.start();
                            sendCodeFlg = true;
                            registerSmsCode.requestFocus();
                            registerSmsCode.setCursorVisible(true);
                            registerSmsCode.setFocusable(true);
                            tvSendCode.setBackground(getResources().getDrawable(R.drawable.bg_gray_5_shape));
                            tvSendCode.setTextColor(getResourcesColor(R.color.font_main_secondary));
                            return;
                        } else {
                            ToastHelper.toastMessage(BindNewPhoneActivity.this, result);
                        }

                    }
                }, staff
        );
    }

    private void VerifyRandomCodeRequest() {
        String phones = registerPhone.getText().toString().trim();
        String code  = registerSmsCode.getText().toString().trim();
        Map<String, String> staff = new HashMap<String, String>();
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        staff.put("tokenId",token);
        staff.put("telephoneint",phones);
        staff.put("code",code);
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.CHANGENUMBER,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String result) {
                        if (result.equals("1")) {
                            try {
                                JSONObject js = new JSONObject(result);
                                if (!js.isNull("statsMsg")) {
                                    JSONObject json = js.optJSONObject("statsMsg");
                                    String stats = json.getString("stats");
                                    String msg = json.getString("msg");
                                    if (stats.equals("1")) {
                                        Bundle bundle = new Bundle();
                                        String phones = registerPhone.getText().toString().trim();
                                        bundle.putString("str", phones);
                                        ReceiverUtils.sendReceiver(ReceiverUtils.MODIFY_PHONE,bundle);
                                        ToastHelper.toastMessage(BindNewPhoneActivity.this, msg);
                                        finish();

                                    } else {
                                        ToastHelper.toastMessage(BindNewPhoneActivity.this, result);
                                    }
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                }, staff
        );

    }
}

package com.ts.fmxt.ui.user;/**
 * Created by A1 on 2017/7/27.
 */

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.base.activity.FMBaseActivity;

import java.util.HashMap;
import java.util.Map;

import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.CountDownButtonHelper;
import utils.StringUtils;
import utils.Tools;
import utils.UISKipUtils;
import utils.helper.ToastHelper;

/**
 * created by kp at 2017/7/27
 * 修改手机
 */
public class EditPhoneActivity extends FMBaseActivity implements View.OnClickListener{
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
        setContentView(R.layout.activity_edit_phone);
        phone = getIntent().getStringExtra("phone");
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
        if(!phone.equals("未填写")){
            registerPhone.setText(phone);
        }
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
                if (sendCodeFlg == false && registerPhoneFlg == true){
                    helper.start();
                    randomCodeRequest();
                }

                break;
            case R.id.btn_nexts:
                if (Tools.isFastDoubleClick())
                    return;
                if (registerPhoneFlg && sendCodeFlg) {
                    VerifyRandomCodeRequest();
                }
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
        staff.put("type","2");
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.SENTOBTAIN,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String result) {
                        if (result.equals("1")) {
                            sendCodeFlg = true;
                            registerSmsCode.requestFocus();
                            registerSmsCode.setCursorVisible(true);
                            registerSmsCode.setFocusable(true);
                            return;
                        } else {
                            ToastHelper.toastMessage(EditPhoneActivity.this, result);
                        }

                    }
                }, staff
        );
    }

    private void VerifyRandomCodeRequest() {
        String phones = registerPhone.getText().toString().trim();
        String code  = registerSmsCode.getText().toString().trim();
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("telephoneint",phones);
        staff.put("code",code);
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.SMSCHECK,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String result) {
                        if (result.equals("1")) {
                            UISKipUtils.startBindNewPhone(EditPhoneActivity.this);
                            return;
                        } else {
                            ToastHelper.toastMessage(EditPhoneActivity.this, result);
                        }

                    }
                }, staff
        );

    }
}

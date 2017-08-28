package com.ts.fmxt.ui.user.login;/**
 * Created by A1 on 2017/7/24.
 */

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.thindo.base.NetworkAPI.BaseResponse;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.base.activity.FMBaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import http.data.RegisterEntity;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.CountDownButtonHelper;
import utils.Tools;
import utils.UISKipUtils;
import utils.helper.ToastHelper;
import widget.ContainsEmojiEditText;

/**
 * created by kp at 2017/7/24
 * 找回密码
 */
public class ForgetPswActivity extends FMBaseActivity implements View.OnClickListener
          {
    private EditText registerPhone;
    private EditText registerSmsCode;
    private TextView tvSendCode;
    private TextView tvProtocol;
    private TextView btnNexts;
    private CountDownButtonHelper helper;
    private RegisterEntity mRegisterEntity;
    private ContainsEmojiEditText registerPsw;

    private boolean registerPhoneFlg = false;
    private boolean registerSmsCodeFlg = false;
    private boolean sendCodeFlg = false;
    private boolean registerFlg = false;
    private ImageView iv_eye;
    private boolean flg = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_ps);
        registerPhone = (EditText) findViewById(R.id.register_phone);
        registerSmsCode = (EditText) findViewById(R.id.register_sms_code);
        tvSendCode = (TextView) findViewById(R.id.tv_send_code);
        btnNexts = (TextView) findViewById(R.id.btn_nexts);
        registerPsw = (ContainsEmojiEditText) findViewById(R.id.register_psw);
        findViewById(R.id.btn_finish).setOnClickListener(this);
        tvSendCode.setOnClickListener(this);
        btnNexts.setOnClickListener(this);
        tvProtocol = (TextView) findViewById(R.id.tv_protocol);
        tvProtocol.setOnClickListener(this);
        iv_eye = (ImageView) findViewByIds(R.id.iv_eye);
        iv_eye.setOnClickListener(this);
        helper = new CountDownButtonHelper(tvSendCode, getResourcesStr(R.string.toast_register_send_code), 60, 1);
        helper.setOnFinishListener(new CountDownButtonHelper.OnFinishListener() {
            @Override
            public void finish() {
                if (registerPhone.getText().toString().length() != 11)
                    registerPhoneFlg = false;
                tvSendCode.setText(getResourcesStr(R.string.toast_register_code));
                sendCodeFlg = false;
            }
        });

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.iv_eye:
                iv_eye.setSelected(!flg);
                flg = !flg;
                registerPsw.setInputType(!flg ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD : InputType.TYPE_CLASS_TEXT);
                break;
            case R.id.tv_send_code:
                if (Tools.isFastDoubleClick())
                    return;
                if (registerPhone.getText().toString().equals("")){
                ToastHelper.toastMessage(this,"请输入手机");
                return;
            }
                if (registerPhone.getText().length()!=11){
                    ToastHelper.toastMessage(this,"手机号码长度应为11位数字");
                    return;
                }

                randomCodeRequest();
                break;
            case R.id.btn_nexts:
                if (Tools.isFastDoubleClick())
                    return;
                if (registerPhone.getText().toString().equals("")){
                    ToastHelper.toastMessage(this,"请输入手机");
                    return;
                }
                if (registerPsw.getText().toString().equals("")){
                    ToastHelper.toastMessage(this,"请输入密码");
                    return;
                }
                if (registerSmsCode.getText().toString().equals("")){
                    ToastHelper.toastMessage(this,"请输入验证码");
                    return;
                }
                VerifyRandomCodeRequest();
                break;
            case R.id.btn_finish:
                finish();
                break;
            //            //协议
            case R.id.tv_protocol:
//                UISKipUtils.startFMBrowserActivity(this, getResourcesStr(R.string.html_fm_agreement), getResourcesStr(R.string.text_fm_agreement));
                break; //男
        }
    }


    /**
     * 获取文本资源
     */
    public String getResourcesStr(int resourcesId) {
        return getResources().getString(resourcesId);
    }

    @Override
    public void onStart(BaseResponse response) {

    }

    @Override
    public void onFailure(BaseResponse response) {

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
                        if(result.equals("1")){
                            helper.start();
                            sendCodeFlg = true;
                            registerSmsCode.requestFocus();
                            registerSmsCode.setCursorVisible(true);
                            registerSmsCode.setFocusable(true);
//                            tvSendCode.setBackground(getResources().getDrawable(R.drawable.bg_gray_5_shape));
//                            tvSendCode.setTextColor(getResourcesColor(R.color.font_main_secondary));
                            return;
                        }else{
                            ToastHelper.toastMessage(ForgetPswActivity.this, "获取验证码失败");
                        }

                    }
                }, staff
        );


    }

    //校验验证码
    private void VerifyRandomCodeRequest() {
        String phone = registerPhone.getText().toString().trim();
        String  password =registerPsw.getText().toString().trim();
        String  code = registerSmsCode.getText().toString().trim();
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("telephone",phone);
        staff.put("code",code);
        staff.put("password",password);
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.FINDPASSWORD,
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
                                    UISKipUtils.startLoginActivity(ForgetPswActivity.this);
                                    ToastHelper.toastMessage(ForgetPswActivity.this, msg);
//                                    finish();
                                } else {
                                    ToastHelper.toastMessage(ForgetPswActivity.this, msg);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, staff
        );

    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
//        ReceiverUtils.removeReceiver(this);
    }

}

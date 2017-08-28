package com.ts.fmxt.ui.user.login;

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
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
import utils.ReceiverUtils;
import utils.StringUtils;
import utils.Tools;
import utils.UISKipUtils;
import utils.helper.ToastHelper;
import widget.ContainsEmojiEditText;


/**
 * 手机获取验证码
 */
public class RandomCodeActivity extends FMBaseActivity implements View.OnClickListener,
        ReceiverUtils.MessageReceiver {
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
    public void onMessage(int receiverType, Bundle bundle) {
        if (receiverType == ReceiverUtils.REGISTER_FINISH) {
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_random_code);
        Bundle bundle = getIntent().getBundleExtra("bundle");
        mRegisterEntity = (RegisterEntity) bundle.getSerializable("entity");
        ReceiverUtils.addReceiver(this);
        registerPhone = (EditText) findViewById(R.id.register_phone);
        registerSmsCode = (EditText) findViewById(R.id.register_sms_code);
        tvSendCode = (TextView) findViewById(R.id.tv_send_code);
        btnNexts = (TextView) findViewById(R.id.btn_nexts);
        registerPsw = (ContainsEmojiEditText) findViewById(R.id.register_psw);
        findViewById(R.id.btn_finish).setOnClickListener(this);
        registerPhone.addTextChangedListener(new PhoneTextWatcher());
        registerSmsCode.addTextChangedListener(new CodeTextWatcher());
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
                nextButtonChange();
            }
        });
        registerPsw.addTextChangedListener(new PaswordTextWatcher());

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
                if (registerPhone.getText().length()!=11){
                    ToastHelper.toastMessage(this,"手机号码长度应为11位数字");
                    return;
                }
                if (registerPhone.getText().toString().equals("")){
                    ToastHelper.toastMessage(this,"请输入手机");
                    return;
                }
                if (sendCodeFlg == false && registerPhoneFlg == true){
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
            case R.id.tv_protocol://协议
                UISKipUtils.satrtUserAgreement(this, getResources().getString(R.string.html_fm_user_agreement), getResources().getString(R.string.user_agreement));
                break;
        }
    }


    private void nextButtonChange() {
        if (registerPhoneFlg && registerSmsCodeFlg&&registerFlg) {
            btnNexts.setTextColor(getResourcesColor(R.color.white));
            btnNexts.setBackground(getResources().getDrawable(R.drawable.bg_orange_shape));
        } else {
            btnNexts.setTextColor(getResourcesColor(R.color.font_main_secondary));
            btnNexts.setBackground(getResources().getDrawable(R.drawable.bg_gray_shape));
        }
        if (sendCodeFlg == false && registerPhoneFlg == true) {
//            tvSendCode.setBackground(getResources().getDrawable(R.drawable.bg_orange_shape));
            tvSendCode.setTextColor(getResourcesColor(R.color.orange));
        } else {
//            tvSendCode.setBackground(getResources().getDrawable(R.drawable.bg_gray_shape));
            tvSendCode.setTextColor(getResourcesColor(R.color.orange));
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
        staff.put("type","1");
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
                            ToastHelper.toastMessage(RandomCodeActivity.this, result);
                        }

                    }
                }, staff
        );
    }

    //注册
    private void VerifyRandomCodeRequest() {
        String phone = registerPhone.getText().toString().trim();
        String code  =registerSmsCode.getText().toString().trim();
        String passWord  =registerPsw.getText().toString().trim();
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("mobile",phone);
        staff.put("passWord",passWord);
        staff.put("code",code);
        staff.put("sex",mRegisterEntity.getSex());
        staff.put("nickName",mRegisterEntity.getNickName());
        if(mRegisterEntity.getPortraitUri()!=null){
            staff.put("headPic",mRegisterEntity.getPortraitUri());
        }else{
            staff.put("headPic","");
        }
        staff.put("phoneModel",getDeviceBrand());

        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.REGISTER,
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
                                    Bundle bundle = new Bundle();
                                    ReceiverUtils.sendReceiver(ReceiverUtils.REGISTER_FINISH,bundle);
                                    ToastHelper.toastMessage(RandomCodeActivity.this, "注册成功");
                                } else {
                                    ToastHelper.toastMessage(RandomCodeActivity.this, msg);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, staff
        );

    }

    /**
     * 获取手机型号
     *
     * @return  手机型号
     */
    public static String getSystemModel() {
        return android.os.Build.MODEL;
    }
    /**
     * 获取手机厂商
     *
     * @return  手机厂商
     */
    public static String getDeviceBrand() {
        return android.os.Build.BRAND;
    }
    //手机号码改变监听
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ReceiverUtils.removeReceiver(this);
    }
    //检测密码变化
    class PaswordTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            registerFlg = s.toString().length() < 6 ? false : true;
            nextButtonChange();
//            register.setBackground(getResources().getDrawable(registerFlg ? R.drawable.bg_orange_5_shape : R.drawable.bg_gray_5_shape));
//            register.setTextColor(getResourcesColor(registerFlg ? R.color.white : R.color.font_main_secondary));
        }
    }
}

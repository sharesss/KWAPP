package com.ts.fmxt.ui.user.login;/**
 * Created by A1 on 2017/7/24.
 */

import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.thindo.base.NetworkAPI.BaseResponse;
import com.thindo.base.NetworkAPI.OnResponseListener;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.base.activity.FMBaseActivity;

import http.data.RegisterEntity;
import utils.CountDownButtonHelper;
import utils.ReceiverUtils;
import utils.StringUtils;
import utils.Tools;
import widget.ContainsEmojiEditText;

/**
 * created by kp at 2017/7/24
 * 找回密码
 */
public class ForgetPswActivity extends FMBaseActivity implements View.OnClickListener,
        OnResponseListener, ReceiverUtils.MessageReceiver {
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
//        if (receiverType == ReceiverUtils.REGISTER_FINISH) {
//            finish();
//        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_ps);
        Bundle bundle = getIntent().getBundleExtra("bundle");
//        mRegisterEntity = (RegisterEntity) bundle.getSerializable("entity");
//        ReceiverUtils.addReceiver(this);
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
                if (sendCodeFlg == false && registerPhoneFlg == true)
                    randomCodeRequest();
                break;
            case R.id.btn_nexts:
                if (Tools.isFastDoubleClick())
                    return;
                if (registerPhoneFlg && registerPhoneFlg) {
                    VerifyRandomCodeRequest();
                }
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


    @Override
    public void onSuccess(BaseResponse response) {

        if (response.getStatus() == BaseResponse.SUCCEED) {
            switch (response.getRequestType()) {
//                case 1:
//                    boolean flg = (boolean) response.getData();
//                    if (flg) {
//                        helper.start();
//                        sendCodeFlg = true;
//                        registerSmsCode.requestFocus();
//                        registerSmsCode.setCursorVisible(true);
//                        registerSmsCode.setFocusable(true);
//                        tvSendCode.setBackground(getResources().getDrawable(R.drawable.bg_gray_5_shape));
//                        tvSendCode.setTextColor(getResourcesColor(R.color.font_main_secondary));
//                        ToastHelper.toastMessage(this, response.getExData().toString());
//                        return;
//                    } else {
//                        UISkipUtils.showMes(this, response.getExData().toString());
//                    }
//                    break;
//                case 2:
//                    mRegisterEntity.setPhone(registerPhone.getText().toString().trim());
//                    mRegisterEntity.setCode(registerSmsCode.getText().toString().trim());
//                    UISkipUtils.startPswSetActivity(this, mRegisterEntity);
//                    break;
            }
            return;
        } else {
//            UISkipUtils.showMes(this, response.getError_msg());
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
            tvSendCode.setBackground(getResources().getDrawable(R.drawable.bg_orange_5_shape));
            tvSendCode.setTextColor(getResourcesColor(R.color.white));
        } else {
            tvSendCode.setBackground(getResources().getDrawable(R.drawable.bg_gray_5_shape));
            tvSendCode.setTextColor(getResourcesColor(R.color.font_main_secondary));
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
//        RandomCodeRequest request = new RandomCodeRequest();
//        request.setOnResponseListener(this);
//        request.setRequestType(1);
//        request.setType(1);
//        request.setMobile(phone);
//        request.executePost(false);
    }

    //校验验证码
    private void VerifyRandomCodeRequest() {
//        VerifyRandomCodeRequest request = new VerifyRandomCodeRequest();
//        request.setOnResponseListener(this);
//        request.setRequestType(2);
//        request.setType(1);
//        request.setCode(registerSmsCode.getText().toString().trim());
//        request.setMobile(registerPhone.getText().toString().trim());
//        request.executePost(false);
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
                tvSendCode.setBackground(getResources().getDrawable(R.drawable.bg_orange_5_shape));
            } else {
                registerPhoneFlg = false;
                tvSendCode.setBackground(getResources().getDrawable(R.drawable.bg_gray_5_shape));
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
//        ReceiverUtils.removeReceiver(this);
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
//            register.setBackground(getResources().getDrawable(registerFlg ? R.drawable.bg_orange_5_shape : R.drawable.bg_gray_5_shape));
//            register.setTextColor(getResourcesColor(registerFlg ? R.color.white : R.color.font_main_secondary));
        }
    }
}

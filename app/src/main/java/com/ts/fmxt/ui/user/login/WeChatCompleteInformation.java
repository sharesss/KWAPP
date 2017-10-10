package com.ts.fmxt.ui.user.login;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.squareup.okhttp.Request;
import com.ts.fmxt.FmxtApplication;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.base.activity.FMBaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.CountDownButtonHelper;
import utils.Tools;
import utils.UISKipUtils;
import utils.helper.ToastHelper;
import widget.ContainsEmojiEditText;
import widget.image.FMRadiusNetImageView;

import static com.ts.fmxt.R.id.iv_picture;

/**
 * created by kp at 2017/7/25
 * 补全账户信息
 */
public class WeChatCompleteInformation extends FMBaseActivity implements View.OnClickListener {
    private FMRadiusNetImageView ivPicture;
    private EditText etdRegisterPhone,edtRegisteSmsCode;
    private ContainsEmojiEditText edtRegisterPsw;
    private CountDownButtonHelper helper;
    private TextView tvSendCode;
    private ImageView iv_eye;
    private TextView tvName;
    private boolean flg = false;
    private boolean sendCodeFlg = false;
    private boolean registerFlg = false;
    private boolean registerPhoneFlg = false;
    private boolean registerSmsCodeFlg = false;
    private String info;
    private String headpic,nickname,unionId,openId;
    private int sex;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_complete_information);
        info =getIntent().getStringExtra("info");
        initView();
    }

    private void initView() {
        ivPicture = (FMRadiusNetImageView) findViewById(iv_picture);
        etdRegisterPhone = (EditText) findViewById(R.id.register_phone);
        edtRegisteSmsCode = (EditText) findViewById(R.id.register_sms_code);
        edtRegisterPsw = (ContainsEmojiEditText) findViewById(R.id.register_psw);
        tvSendCode = (TextView) findViewById(R.id.tv_send_code);
        tvName = (TextView) findViewById(R.id.tv_name);
        iv_eye = findViewByIds(R.id.iv_eye);
        iv_eye.setOnClickListener(this);
        findViewById(R.id.tv_protocol).setOnClickListener(this);
        findViewById(R.id.btn_nexts).setOnClickListener(this);
        findViewById(R.id.btn_finish).setOnClickListener(this);
        tvSendCode.setOnClickListener(this);
        try {
            JSONObject js = new JSONObject(info);
            headpic =  js.getString("headpic");
            nickname =  js.getString("nickname");
            unionId =  js.getString("unionId");
            openId =  js.getString("openId");
            sex =  js.getInt("sex");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ivPicture.loadImage(headpic);
        helper = new CountDownButtonHelper(tvSendCode, getResourcesStr(R.string.toast_register_send_code), 60, 1);
        helper.setOnFinishListener(new CountDownButtonHelper.OnFinishListener() {
            @Override
            public void finish() {
                if (etdRegisterPhone.getText().toString().length() != 11)
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
            case R.id.iv_eye:
                iv_eye.setSelected(!flg);
                flg = !flg;
                edtRegisterPsw.setInputType(!flg ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD : InputType.TYPE_CLASS_TEXT);
                break;
            case R.id.tv_send_code:
                if (Tools.isFastDoubleClick())
                    return;
                if (etdRegisterPhone.getText().toString().equals("")){
                    ToastHelper.toastMessage(this,"请输入手机");
                    return;
                }
                if (etdRegisterPhone.getText().length()!=11){
                    ToastHelper.toastMessage(this,"手机号码长度应为11位数字");
                    return;
                }
                randomCodeRequest();

                break;
            case R.id.btn_nexts:
                if (Tools.isFastDoubleClick())
                    return;
                if(etdRegisterPhone.getText().toString().equals("")){
                    ToastHelper.toastMessage(this,"请填写您的手机号");
                    return;
                }
            if(edtRegisteSmsCode.getText().toString().equals("")){
                ToastHelper.toastMessage(this,"请填写您的验证码");
                return;
            }
                if(edtRegisterPsw.getText().toString().equals("")){
                    ToastHelper.toastMessage(this,"请填写您的密码");
                    return;
                }
                weiXinBindMobileRequest();
                break;
            case R.id.btn_finish:
                finish();
                break;
            case R.id.tv_protocol://协议
                UISKipUtils.satrtUserAgreement(this, getResources().getString(R.string.html_fm_user_agreement), getResources().getString(R.string.user_agreement));
                break;
        }
    }

    //检查发送短信
    public void randomCodeRequest() {
        String phone = etdRegisterPhone.getText().toString().trim();
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
                            edtRegisteSmsCode.requestFocus();
                            edtRegisteSmsCode.setCursorVisible(true);
                            edtRegisteSmsCode.setFocusable(true);
//                            tvSendCode.setBackground(getResources().getDrawable(R.drawable.bg_gray_5_shape));
//                            tvSendCode.setTextColor(getResourcesColor(R.color.font_main_secondary));
                            return;
                        }else{
                            ToastHelper.toastMessage(WeChatCompleteInformation.this, result);
                        }

                    }
                }, staff
        );


    }

    private void weiXinBindMobileRequest(){
        final String phone = etdRegisterPhone.getText().toString().trim();
        String  code = edtRegisteSmsCode.getText().toString().trim();
        String  passWord = edtRegisterPsw.getText().toString().trim();
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("telepHoneint",phone);
        staff.put("code",code);
        staff.put("passWord", passWord);
        staff.put("sex",String.valueOf(sex));
        staff.put("headPhoto", headpic);
        staff.put("unionId", unionId);
        staff.put("openId", openId);
        staff.put("nickName", nickname);
        staff.put("accountType", "1");

        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.WEIXINBINDMOBILE,
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
                                    UISKipUtils.startMainFrameActivity(WeChatCompleteInformation.this);
                                    SharedPreferences share = getSharedPreferences("user",MODE_PRIVATE);
                                    SharedPreferences.Editor editor = share.edit(); //使处于可编辑状态
                                    String account = js.getString("account");
                                    String token = js.getString("token");
                                    editor.putString("token", token);
                                    editor.putString("phone", account);
                                    editor.commit();    //提交数据保存
                                    loginIM(phone);
                                    ToastHelper.toastMessage(WeChatCompleteInformation.this, msg);
                                    finish();
                                } else {
                                    ToastHelper.toastMessage(WeChatCompleteInformation.this, msg);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, staff
        );
    }

    private void nextButtonChange() {

        if (sendCodeFlg == false && registerPhoneFlg == true) {
            tvSendCode.setTextColor(getResourcesColor(R.color.orange));
        } else {
            tvSendCode.setTextColor(getResourcesColor(R.color.orange));
        }
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
                WeChatCompleteInformation.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ToastHelper.toastMessage(getBaseContext(), msg);
                    }
                });
            }
        });
    }

}

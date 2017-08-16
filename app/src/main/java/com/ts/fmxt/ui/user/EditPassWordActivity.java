package com.ts.fmxt.ui.user;/**
 * Created by A1 on 2017/7/27.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

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
import utils.UISKipUtils;
import utils.helper.ToastHelper;
import widget.ContainsEmojiEditText;
import widget.shortcutbadger.ShortcutBadger;
import widget.titlebar.NavigationView;

/**
 * created by kp at 2017/7/27
 * 修改密码
 */
public class EditPassWordActivity extends FMBaseActivity implements View.OnClickListener{
    private ContainsEmojiEditText edtOldPassWord,edtNewPassWord;
    private ImageView ivOldEye,ivNewEye;
    private TextView btnNexts;
    private boolean oldflg = false;
    private boolean newflg = false;
    private boolean oldregisterFlg = false;
    private boolean newregisterFlg = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_password);
        initView();
    }

    private void initView() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setTitle(R.string.title_edit_password, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edtOldPassWord = (ContainsEmojiEditText) findViewById(R.id.edt_old_psw);
        edtNewPassWord = (ContainsEmojiEditText) findViewById(R.id.edt_new_psw);
        btnNexts = (TextView) findViewById(R.id.btn_nexts);
        ivOldEye = (ImageView) findViewById(R.id.iv_old_eye);
        ivNewEye = (ImageView) findViewById(R.id.iv_new_eye);
        ivOldEye.setOnClickListener(this);
        ivNewEye.setOnClickListener(this);
        btnNexts.setOnClickListener(this);
        edtOldPassWord.addTextChangedListener(new PaswordTextWatcher());
        edtNewPassWord.addTextChangedListener(new NewPaswordTextWatcher());


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_old_eye:
                ivOldEye.setSelected(!oldflg);
                oldflg = !oldflg;
                edtOldPassWord.setInputType(!oldflg ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD : InputType.TYPE_CLASS_TEXT);
                break;
            case R.id.iv_new_eye:
                ivNewEye.setSelected(!newflg);
                newflg = !newflg;
                edtNewPassWord.setInputType(!newflg ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD : InputType.TYPE_CLASS_TEXT);
                break;
            case R.id.btn_nexts:
                String  old = edtOldPassWord.getText().toString();
                String news = edtNewPassWord.getText().toString();
                if (newregisterFlg && oldregisterFlg){
                if(old.equals(news)){
                    ToastHelper.toastMessage(EditPassWordActivity.this, "旧密码不能和新密码一样");
                    return;
                }
                    modifyPasswordRequest();
                }
                break;
        }
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
            oldregisterFlg = s.toString().length() < 6 ? false : true;
            nextButtonChange();
//            register.setBackground(getResources().getDrawable(registerFlg ? R.drawable.bg_orange_5_shape : R.drawable.bg_gray_5_shape));
//            register.setTextColor(getResourcesColor(registerFlg ? R.color.white : R.color.font_main_secondary));
        }
    }

    //检测密码变化
    class NewPaswordTextWatcher implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            newregisterFlg = s.toString().length() < 6 ? false : true;
            nextButtonChange();
//            register.setBackground(getResources().getDrawable(registerFlg ? R.drawable.bg_orange_5_shape : R.drawable.bg_gray_5_shape));
//            register.setTextColor(getResourcesColor(registerFlg ? R.color.white : R.color.font_main_secondary));
        }
    }

    private void nextButtonChange() {
        if (newregisterFlg && oldregisterFlg) {
            btnNexts.setTextColor(getResourcesColor(R.color.white));
            btnNexts.setBackground(getResources().getDrawable(R.drawable.bg_orange_shape));
        } else {
            btnNexts.setTextColor(getResourcesColor(R.color.white));
            btnNexts.setBackground(getResources().getDrawable(R.drawable.bg_gray_dcdc_hape));
        }
    }

    private void modifyPasswordRequest(){
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        String oldPassword = edtOldPassWord.getText().toString();
        String newPassword = edtNewPassWord.getText().toString();
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("tokenId", token);
        staff.put("oldPassword", oldPassword);
        staff.put("newPassword", newPassword);
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.MODIFYPASSWORD,
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
                                    SharedPreferences sharedPreferences= getSharedPreferences("user",
                                            MODE_PRIVATE);
                                    sharedPreferences.edit().clear().commit();
                                    ShortcutBadger.applyCount(FmxtApplication.getContext(), 0);
                                    UISKipUtils.startMainFrameActivity(EditPassWordActivity.this);
                                    ToastHelper.toastMessage(EditPassWordActivity.this, msg);
                                    finish();
                                } else {
                                    ToastHelper.toastMessage(EditPassWordActivity.this, msg);
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

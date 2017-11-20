package com.ts.fmxt.ui.user.authentication;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.moblie.zmxy.antgroup.creditsdk.app.CreditApp;
import com.android.moblie.zmxy.antgroup.creditsdk.app.ICreditListener;
import com.squareup.okhttp.Request;
import com.thindo.base.NetworkAPI.BaseResponse;
import com.ts.fmxt.FmxtApplication;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.base.activity.FMBaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.SpannableUtils;
import utils.StringUtils;
import utils.Tools;
import utils.UISKipUtils;
import utils.helper.ToastHelper;
import widget.titlebar.NavigationView;


/**
 * 实名认证
 */
public class RealNameActivity extends FMBaseActivity implements View.OnClickListener {
    private NavigationView navigationView;
    private TextView tv_desc;
    private EditText ed_name, ed_no;
    private boolean nameFlg = false, noFlg = false;
    private Button bt_authorization;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_name_view);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        ed_name = (EditText) findViewById(R.id.ed_name);
        ed_no = (EditText) findViewById(R.id.ed_no);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setTitle(getResourcesStr(R.string.user_realname), new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bt_authorization = (Button) findViewById(R.id.bt_authorization);
        bt_authorization.setOnClickListener(this);
        bt_authorization.setVisibility(View.VISIBLE);
        tv_desc.setText(SpannableUtils.getSpannableStr(getResourcesStr(R.string.text_authorization_1), getResourcesStr(R.string.text_authorization_2), getResourcesColor(R.color.font_main_third), 0f));
        ed_name.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (!StringUtils.isEmpty(text))
                    nameFlg = true;
                else
                    nameFlg = false;
                ontifyButton();
            }
        });
        ed_no.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (!StringUtils.isEmpty(text) && text.length() > 10)
                    noFlg = true;
                else
                    noFlg = false;
                ontifyButton();
            }
        });
    }

    private void ontifyButton() {
        if (nameFlg && noFlg)
            bt_authorization.setBackground(getResources().getDrawable(R.drawable.bg_full_orange_5_shape));
        else
            bt_authorization.setBackground(getResources().getDrawable(R.drawable.bg_full_gray_5_shape));
    }


    @Override
    public void onSuccess(BaseResponse response) {
        super.onSuccess(response);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_authorization:
                if (Tools.isFastDoubleClick())
                    return;
                if (nameFlg && noFlg)
                    creditParameterRequest();
                break;
        }
    }

    private void creditParameterRequest() {
        Map<String, String> staff = new HashMap<String, String>();
        String name = ed_name.getText().toString();
        String certNo =ed_no.getText().toString();
        staff.put("name",name);
        staff.put("certNo",certNo);

        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.getGenerateParams,
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
                                 String param =  js.optString("param");
                                    String sgin =   js.optString("sgin");
                                    doCreditRequest(param, sgin);
                                } else {
                                    ToastHelper.toastMessage(RealNameActivity.this, msg);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, staff
        );
    }

    private void saveCreditParameterRequest(String params) {
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                Activity.MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("tokenId",token);
        String name = ed_name.getText().toString();
        String certNo =ed_no.getText().toString();
        staff.put("name",name);
        staff.put("certNo",certNo);
        staff.put("params",params);

        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.ZHIMASCOREGET,
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
                                    UISKipUtils.startRealNameResultActivity(RealNameActivity.this, 1);
                                    finish();
                                } else {
                                    ToastHelper.toastMessage(RealNameActivity.this, msg);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, staff
        );
    }

    private void doCreditRequest(String params, String sign) {
        try {
            CreditAuthHelper.creditAuth(this, FmxtApplication.CreditAPPID, params, sign, new HashMap<String, String>(), new ICreditListener() {
                @Override
                public void onComplete(Bundle result) {
                    //toast message
                    //从result中获取params参数,然后解析params数据,可以获取open_id。
                    if (result != null) {
                        Set<String> keys = result.keySet();
                        for (String key : keys) {
                            if ("params".equals(key)) {
                                saveCreditParameterRequest(result.getString(key));
                            }
                        }
                    }
                }

                @Override
                public void onError(Bundle result) {
                    //toast message
                    UISKipUtils.startRealNameResultActivity(RealNameActivity.this, 0);
                    finish();
                    ToastHelper.toastMessage(getBaseContext(), "授权失败");
                    Log.e("RealNameActivity", "DemoPresenterImpl.doCreditAuthRequest.onError.");
                }

                @Override
                public void onCancel() {
                    //toast message
                    ToastHelper.toastMessage(getBaseContext(), "授权取消");
                    Log.d("RealNameActivity", "DemoPresenterImpl.doCreditAuthRequest.onCancel.");
                }
            });
        } catch (Exception e) {
            ToastHelper.toastMessage(getBaseContext(), "授权失败[" + e.toString() + "]");
            Log.e("RealNameActivity", "DemoPresenterImpl.doCreditAuthRequest.exception=" + e.toString());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        CreditApp.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CreditApp.destroy();
    }
}

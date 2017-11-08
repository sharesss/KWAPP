package com.ts.fmxt.ui.user.authentication;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import utils.SpannableUtils;
import utils.helper.ToastHelper;
import widget.titlebar.NavigationView;


/**
 * 实名认证-已经认证成功
 */
public class RealNameSuccessActivity extends FMBaseActivity {
    private NavigationView navigationView;
    private TextView tv_desc;
    private EditText ed_name, ed_no;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_success_view);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        ed_name = (EditText) findViewById(R.id.ed_name);
        ed_name.setCursorVisible(false);
        ed_name.setFocusable(false);
        ed_name.setFocusableInTouchMode(false);
        ed_no = (EditText) findViewById(R.id.ed_no);
        ed_no.setCursorVisible(false);
        ed_no.setFocusable(false);
        ed_no.setFocusableInTouchMode(false);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

        navigationView.setTitle(getResourcesStr(R.string.user_realname_success), new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });

        tv_desc.setText(SpannableUtils.getSpannableStr(getResourcesStr(R.string.text_authorization_1), getResourcesStr(R.string.text_authorization_2), getResourcesColor(R.color.font_main_third), 0f));
        userRealRequest();
    }

    private void userRealRequest(){
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                Activity.MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("tokenId",token);

        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.FINDUSERCREDITPROPERTYRECORD,
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
                                    JSONObject jsonobj = js.optJSONObject("record");
                                    String name = jsonobj.optString("realName");
                                    String no = jsonobj.optString("identityCard");
                                    name = "*"+name.substring(1, name.length());
                                    ed_name.setText(name);
                                    no = no.substring(0, 2) + "**************" + no.substring(16, 18);
                                    ed_no.setText(no);

                                } else {
                                    ToastHelper.toastMessage(RealNameSuccessActivity.this, msg);
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

package com.ts.fmxt.ui.discover;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.View;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.base.activity.FMBaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import http.data.UserInfoEntity;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.UISKipUtils;
import utils.helper.ToastHelper;
import widget.popup.BaseDoubleEventPopup;
import widget.popup.PopupObject;
import widget.popup.dialog.MessageContentDialog;
import widget.titlebar.NavigationView;

/**
 * Created by kp on 2017/11/21.
 * 发布流程
 */

public class ReleaseProjectActivity extends FMBaseActivity {
    private NavigationView navigationView;
    private TextView tv_copy_website,tv_authentication;
    private int isTruenameAuthen,isinvestauthen,auditstate;
    private UserInfoEntity userInfo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_project);
        tv_authentication = (TextView) findViewById(R.id.tv_authentication);
        tv_copy_website = (TextView) findViewById(R.id.tv_copy_website);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setTitle("发布流程", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                MODE_PRIVATE);
        isTruenameAuthen=sharedPreferences.getInt("isTruenameAuthen", -1);
        isinvestauthen=sharedPreferences.getInt("isinvestauthen", -1);
         auditstate=sharedPreferences.getInt("auditstate", -1);
        if(isTruenameAuthen!=0&&isinvestauthen!=0){
            tv_authentication.setVisibility(View.INVISIBLE);
        }
        tv_authentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //认证按钮
                if(isTruenameAuthen==0){
                    MessageContentDialog mPopupDialogWidget = new MessageContentDialog(ReleaseProjectActivity.this);
                    mPopupDialogWidget.setMessage("您还未实名认证，是否去认证？");
                    mPopupDialogWidget.setOnEventClickListener(new BaseDoubleEventPopup.onEventClickListener() {

                        @Override
                        public void onEventClick(PopupObject obj) {
                            if (obj.getWhat() == 1)
                                UISKipUtils.startRealNameActivity(ReleaseProjectActivity.this);
                        }
                    });
                    mPopupDialogWidget.showPopupWindow();
                    return;
                }
                if(isinvestauthen==0){
                    MessageContentDialog mPopupDialogWidget = new MessageContentDialog(ReleaseProjectActivity.this);
                    mPopupDialogWidget.setMessage("您还未认证投资人，是否去认证？");
                    mPopupDialogWidget.setOnEventClickListener(new BaseDoubleEventPopup.onEventClickListener() {

                        @Override
                        public void onEventClick(PopupObject obj) {
                            if (obj.getWhat() == 1)
                                if(auditstate==0){
                                    UISKipUtils.startInvestmentRecordActivity(ReleaseProjectActivity.this);
                                }else{
                                    UISKipUtils.startCertifiedInvestorActivity(ReleaseProjectActivity.this,auditstate,1);//1是外面进去的，展示查看我的投资偏好，0是设计我的投资偏好
                                }

                        }
                    });
                    mPopupDialogWidget.showPopupWindow();
                    return;
                }
            }
        });

        tv_copy_website.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //复制按钮
                ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                // 将文本内容放到系统剪贴板里。
                cm.setText("https://fmb.fmsecret.cn/fm-html5/fmxiaotou/html/login.html");
                ToastHelper.toastMessage(ReleaseProjectActivity.this,"网址已复制");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        userInfoRequest();
    }

    private void  userInfoRequest(){
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("tokenId",token);
        staff.put("userId","");

        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.INFORMATION,
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
                                    if(!js.isNull("information")){
                                        JSONObject jsonobj = js.optJSONObject("information");
                                        userInfo = new UserInfoEntity(jsonobj);
                                        isTruenameAuthen = userInfo.getIsTruenameAuthen();
                                        isinvestauthen = userInfo.getIsinvestauthen();
                                        auditstate = userInfo.getAuditstate();
                                        if(userInfo.getIsTruenameAuthen()!=0&&userInfo.getIsinvestauthen()!=0){
                                            tv_authentication.setVisibility(View.INVISIBLE);
                                        }
                                    }
                                } else {
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

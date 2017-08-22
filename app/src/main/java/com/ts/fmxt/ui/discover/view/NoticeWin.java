package com.ts.fmxt.ui.discover.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.ts.fmxt.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.helper.ToastHelper;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created kp A1 on 2017/8/18.
 * 通知弹窗
 */

public class NoticeWin extends PopupWindow {
    private View mMenuView;
    private Activity context;
    private TextView tv_x,tv_notive,tv_confirm;
    private int investId;

    public NoticeWin(Activity context,int investId){
        this.context = context;
        this.investId = investId;
        initParam();

    }

    private void initParam() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pop_window_notice, null);
        initView();

        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置按钮监听
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
        // 实例化一个ColorDrawable颜色为半透明 0xb0000000
        ColorDrawable dw = new ColorDrawable(0x00000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        this.setOutsideTouchable(true);
        // 显示窗口
        setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    private void initView() {
        tv_x = (TextView) mMenuView.findViewById(R.id.tv_x);
        tv_notive = (TextView) mMenuView.findViewById(R.id.tv_notive);
        tv_confirm = (TextView) mMenuView.findViewById(R.id.tv_confirm);
        SharedPreferences sharedPreferences= context.getSharedPreferences("user",
                MODE_PRIVATE);
        String phone=sharedPreferences.getString("phone", "");

        String  notive= tv_notive.getText().toString();
        if(!TextUtils.isEmpty(phone) && phone.length() > 6 ){
            StringBuilder sb  =new StringBuilder();
            for (int i = 0; i < phone.length(); i++) {
                char c = phone.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }
            tv_notive.setText(notive+sb.toString());
        }

        tv_x.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NoticeRequest();
            }
        });
    }

    private void NoticeRequest(){
        SharedPreferences sharedPreferences= context.getSharedPreferences("user",
                MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        String phone=sharedPreferences.getString("phone", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("investId", String.valueOf(investId));
        staff.put("phone", String.valueOf(phone) );
        staff.put("tokenId", String.valueOf(token));

        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.SAVEINVESTPROJECTNOTICE,
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
                                    ToastHelper.toastMessage(context, "设定成功");
                                    dismiss();
                                } else {
                                    ToastHelper.toastMessage(context, msg);

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

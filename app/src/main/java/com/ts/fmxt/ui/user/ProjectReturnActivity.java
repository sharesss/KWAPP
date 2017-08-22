package com.ts.fmxt.ui.user;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.base.activity.FMBaseActivity;
import com.ts.fmxt.ui.discover.view.FlowLayout;
import com.ts.fmxt.ui.discover.view.NoticeWin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import http.data.InvestMoneyEntity;
import http.data.ProjectReserveEntity;
import http.data.TableList;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.UISKipUtils;
import utils.helper.ToastHelper;

import static com.ts.fmxt.R.id.tv_confirm;

/**
 * Created by A1 on 2017/8/16.
 */

public class ProjectReturnActivity extends FMBaseActivity implements View.OnClickListener {
    private int investId;
    private TextView tvReservationMoney,tvSharesNum,tvCompanyName,tvEquityReserveExplain,tvShareholderEquity,tvConfirm;
    private FlowLayout flow_layout;
    private ArrayList arr;
    private String checktext;
    private NoticeWin noticeWin;
    private ProjectReserveEntity info;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_return);
        investId = getIntent().getIntExtra("investId", -1);
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_finish).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
        tvReservationMoney = (TextView) findViewById(R.id.tv_reservation_money);
        tvSharesNum = (TextView) findViewById(R.id.tv_shares_num);
        tvCompanyName = (TextView) findViewById(R.id.tv_company_name);
        tvEquityReserveExplain = (TextView) findViewById(R.id.tv_equityReserveExplain);
        tvShareholderEquity = (TextView) findViewById(R.id.tv_shareholderequity);
        flow_layout = (FlowLayout) findViewById(R.id.flow_layout);
        tvConfirm = (TextView) findViewById(tv_confirm);
        tvConfirm.setOnClickListener(this);
        GetEquityInfoRequest();
        InvestMoneyRequest();
    }

    private void formatData(ProjectReserveEntity info){
        if (info == null) {
            return;
        }
        tvReservationMoney.setText("预约金："+info.getReserveAmount());
        int num = info.getReservePeopleNum()-info.getReservePeopleNum();
//        if(num==0){
//            tvConfirm.setText("有份额通知我");
//        }else{
//            tvConfirm.setText("确定");
//        }
        tvSharesNum.setText(info.getReservePeopleNum()+"人入股/剩余名额"+num+"人");
        tvCompanyName.setText(info.getInvestCompany());
        tvEquityReserveExplain.setText(info.getEquityReserveExplain());
        tvShareholderEquity.setText(info.getShareholderEquity());

    }

    private void formatDPData(){
        if(arr.size()==0){
            flow_layout.setVisibility(View.GONE);
            return;
        }
        // 循环添加TextView到容器
        for (int i = 0; i < arr.size(); i++) {
            InvestMoneyEntity info = (InvestMoneyEntity)  arr.get(i);
            final TextView view = new TextView(this);
            view.setText(info.getAmount()+"万(占股"+info.getStockRate()+"%)");
            view.setTextColor(Color.GRAY);
            view.setPadding(5, 5, 5, 5);
            view.setGravity(Gravity.CENTER);
            view.setTextSize(18);
            // 设置背景选择器到TextView上
            Resources resources = getResources();
            Drawable btnDrawable = resources.getDrawable(R.drawable.bg_gray_tag_shape);
            view.setBackground(btnDrawable);

            // 设置点击事件
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = ((TextView) v).getText().toString();
                    Resources resources = getResources();
                    v.setBackground(resources.getDrawable(R.drawable.bg_orange_5_shape));
                    ((TextView) v).setTextColor(Color.WHITE);

                    for (int i = 0; i < flow_layout.getChildCount(); i++) {
                        View indexview = flow_layout.getChildAt(i);
                        if (indexview instanceof TextView) {
                            TextView indexTextView = (TextView) indexview;
                            if (indexTextView.getText().equals(checktext)) {
                                indexTextView.setBackground(resources.getDrawable(R.drawable.bg_gray_tag_shape));
                                indexTextView.setTextColor(Color.GRAY);
                                break;
                            }
                        }
                    }
                    checktext = name;
                }
            });
            flow_layout.addView(view);


        }

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_finish:
                finish();
                break;
            case R.id.tv_confirm:
                if(tvConfirm.getText().equals("确定")){
                    if(checktext==null){
                        ToastHelper.toastMessage(this,"请选择要跟投的金额");
                        return;
                    }
                    UISKipUtils.startConfirmPayment(this,info.getReserveAmount(),checktext,investId);
                }else if(tvConfirm.getText().equals("有份额通知我")){
                    noticeWin = new NoticeWin(this,investId);
                    noticeWin.showAtLocation(
                            findViewById(R.id.AppWidget),
                            Gravity.BOTTOM | Gravity.BOTTOM, 0, 0); // 设置layout在PopupWindow中显示的位置
            }
                break;
        }

    }

    private void GetEquityInfoRequest(){
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("investId", String.valueOf(investId));
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.FINDINVESTRESERVEINFO,
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
                                    if(!js.isNull("projectReserve")){
                                        JSONObject jsonobj = js.optJSONObject("projectReserve");
                                        info = new ProjectReserveEntity(jsonobj);
                                        formatData(info);
                                    }

                                } else {
                                    ToastHelper.toastMessage(ProjectReturnActivity.this, msg);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, staff
        );
    }

    private void InvestMoneyRequest(){
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("investId", String.valueOf(investId));
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.INVESTAMOUNTLIST,
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
                                    if (!js.isNull("amounts")) {
                                        TableList tableList = new TableList();
                                        JSONArray array = js.optJSONArray("amounts");
                                        for (int i = 0; i < array.length(); i++) {
                                            tableList.getArrayList().add(new InvestMoneyEntity(array.getJSONObject(i)));
                                        }
                                        arr = tableList.getArrayList();
                                        formatDPData();

                                    }
                                } else {
                                    ToastHelper.toastMessage(ProjectReturnActivity.this, msg);
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

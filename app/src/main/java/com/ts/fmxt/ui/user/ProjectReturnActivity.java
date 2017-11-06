package com.ts.fmxt.ui.user;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.thindo.base.Widget.refresh.RefreshListView;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.ItemAdapter.FollowProjectAdapter;
import com.ts.fmxt.ui.ItemAdapter.ProjectReturnAdapter;
import com.ts.fmxt.ui.base.activity.FMBaseTableActivity;
import com.ts.fmxt.ui.discover.view.FlowLayout;
import com.ts.fmxt.ui.discover.view.NoticeWin;
import com.ts.fmxt.ui.discover.view.RiskTipwin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import http.data.ProjectReserveEntity;
import http.data.ProjectReturnEntity;
import http.data.TableList;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.ReceiverUtils;
import utils.helper.ToastHelper;
import widget.EmptyView;

import static com.ts.fmxt.R.id.btn_register;

/**
 * Created by kp on 2017/8/16.
 */

public class ProjectReturnActivity extends FMBaseTableActivity implements View.OnClickListener, ReceiverUtils.MessageReceiver {
    private int investId,isOver;
    private TextView tvReservationMoney,tvSharesNum,tvCompanyName,tvEquityReserveExplain,tvShareholderEquity,tvConfirm;
    private FlowLayout flow_layout;
    private EmptyView mEmptyView;
    private RefreshListView refresh_lv;
    private ArrayList arr;
    private String checktext;
    private NoticeWin noticeWin;
    private RiskTipwin hsewin;
    private ProjectReserveEntity info;
    private ProjectReturnAdapter adapter;

    @Override
    public void onMessage(int receiverType, Bundle bundle) {
        if(receiverType==ReceiverUtils.WX_PLAY){
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_return);
        ReceiverUtils.addReceiver(this);
        investId = getIntent().getIntExtra("investId", -1);
        isOver = getIntent().getIntExtra("isOver", -1);
        initView();
        bindRefreshAdapter((RefreshListView) findViewById(R.id.refresh_lv), new FollowProjectAdapter(this, arrayList));
        mEmptyView = (EmptyView) findViewById(R.id.empty_view);
        mEmptyView.setEmptyText("什么都没有");
        refresh_lv = (RefreshListView) findViewById(R.id.refresh_lv);
        EmptyView mEmptyView = new EmptyView(this);
        mEmptyView.setEmptyText("什么都没有");
        setEmptyView(mEmptyView);
        startRefreshState();
    }

    private void initView() {
        findViewById(R.id.btn_finish).setOnClickListener(this);
        findViewById(btn_register).setOnClickListener(this);
//        tvReservationMoney = (TextView) findViewById(R.id.tv_reservation_money);
//        tvSharesNum = (TextView) findViewById(R.id.tv_shares_num);
//        tvCompanyName = (TextView) findViewById(R.id.tv_company_name);
//        tvEquityReserveExplain = (TextView) findViewById(R.id.tv_equityReserveExplain);
//        tvShareholderEquity = (TextView) findViewById(R.id.tv_shareholderequity);
//        flow_layout = (FlowLayout) findViewById(R.id.flow_layout);
//        tvConfirm = (TextView) findViewById(tv_confirm);
//        tvConfirm.setOnClickListener(this);
        GetEquityInfoRequest();
    }


    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

    }

    @Override
    public void onReload() {
        GetEquityInfoRequest();
    }

    @Override
    public void loadMore() {
        GetEquityInfoRequest();
    }

    private void formatData(ProjectReserveEntity info){
        if (info == null) {
            return;
        }
        tvReservationMoney.setText("预约金："+info.getReserveAmount());
        int num = info.getReservePeopleNum()-info.getYetReservePropleNum();
        if(num==0){
            tvConfirm.setText("有份额通知我");
        }else{
            tvConfirm.setText("确定");
        }
        tvSharesNum.setText(info.getYetReservePropleNum()+"人入股/剩余名额"+num+"人");
        tvCompanyName.setText(info.getInvestCompany());
        tvEquityReserveExplain.setText(info.getEquityReserveExplain());
        tvShareholderEquity.setText(info.getShareholderEquity());

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_finish:
                finish();
                break;
            case R.id.btn_register:
                hsewin =new RiskTipwin(this,getString(R.string.html_fm_user_hse));
                hsewin.showAtLocation(
                        findViewById(R.id.AppWidget),
                        Gravity.CENTER | Gravity.CENTER, 0, 0); // 设置layout在PopupWindow中显示的位置
                break;
//            case R.id.tv_confirm:
//                if(tvConfirm.getText().equals("确定")){
//                    if(checktext==null){
//                        ToastHelper.toastMessage(this,"请选择要跟投的金额");
//                        return;
//                    }

//                }else if(tvConfirm.getText().equals("有份额通知我")){
//                    noticeWin = new NoticeWin(this,investId);
//                    noticeWin.showAtLocation(
//                            findViewById(R.id.AppWidget),
//                            Gravity.BOTTOM | Gravity.BOTTOM, 0, 0); // 设置layout在PopupWindow中显示的位置
//            }
//                break;
        }

    }

    private void GetEquityInfoRequest(){
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("investId", String.valueOf(investId));
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.FINDINVESTPROJECTREARDINFO,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        stopRefreshState();
                        refresh_lv.setAdapter(null);
                        mEmptyView.setVisibility(View.VISIBLE);
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
                                    if(!js.isNull("projectRewards")){
                                        TableList tableList = new TableList();
                                        JSONArray array = js.optJSONArray("projectRewards");
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject jsn =array.getJSONObject(i);
                                            tableList.getArrayList().add(new ProjectReturnEntity(jsn));
                                        }
                                        adapter = new ProjectReturnAdapter(ProjectReturnActivity.this, tableList.getArrayList(),isOver);
                                        refresh_lv.setAdapter(adapter);
                                    }
                                    stopRefreshState();
                                    mEmptyView.setVisibility(View.GONE);
                                } else {
                                    ToastHelper.toastMessage(ProjectReturnActivity.this, msg);
                                    stopRefreshState();
                                    mEmptyView.setVisibility(View.VISIBLE);
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
        ReceiverUtils.removeReceiver(this);
    }
}

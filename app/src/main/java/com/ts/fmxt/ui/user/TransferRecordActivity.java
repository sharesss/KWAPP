package com.ts.fmxt.ui.user;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.thindo.base.Widget.refresh.RefreshListView;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.ItemAdapter.StockAuctionAdapter;
import com.ts.fmxt.ui.base.activity.FMBaseTableActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import http.data.FindStockEquityHomeEntity;
import http.data.TableList;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.helper.ToastHelper;
import widget.EmptyView;
import widget.titlebar.NavigationView;

/**
 * Created by kp on 2017/9/25.
 * 转让记录
 */

public class TransferRecordActivity extends FMBaseTableActivity implements View.OnClickListener{
    private EmptyView mEmptyView;
    private RefreshListView refresh_lv;
    private StockAuctionAdapter adapter;
    private TextView tv_in_audit,tv_audited,tv_audit_failure;//审核中，审核通过，审核失败
    private int type=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);
        initView();
    }

    private void initView(){
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setTitle("转让记录", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bindRefreshAdapter((RefreshListView) findViewById(R.id.refresh_lv), new StockAuctionAdapter(this, arrayList,1));
        mEmptyView = (EmptyView) findViewById(R.id.empty_view);
        mEmptyView.setEmptyText("拍得股权，及时跟踪项目动态");
        refresh_lv = (RefreshListView) findViewById(R.id.refresh_lv);
        EmptyView mEmptyView = new EmptyView(this);
        mEmptyView.setEmptyText("拍得股权，及时跟踪项目动态");
        setEmptyView(mEmptyView);
        startRefreshState();

        tv_in_audit = (TextView) findViewById(R.id.tv_in_audit);
        tv_audited = (TextView) findViewById(R.id.tv_audited);
        tv_audit_failure = (TextView) findViewById(R.id.tv_audit_failure);
        tv_in_audit.setOnClickListener(this);
        tv_audited.setOnClickListener(this);
        tv_audit_failure.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_in_audit:
                //审核中
                tv_in_audit.setTextColor(getResources().getColor(R.color.white));
                tv_audited.setTextColor(getResources().getColor(R.color.black));
                tv_audit_failure.setTextColor(getResources().getColor(R.color.black));
                tv_in_audit.setBackgroundResource(R.drawable.bg_orange_5_shape);
                tv_audited.setBackground(null);
                tv_audit_failure.setBackground(null);
                type =0;
                FrozenDepositRequest();
                break;
            case R.id.tv_audited:
                //审核通过
                type =1;
                FrozenDepositRequest();
                tv_in_audit.setTextColor(getResources().getColor(R.color.black));
                tv_audited.setTextColor(getResources().getColor(R.color.white));
                tv_audit_failure.setTextColor(getResources().getColor(R.color.black));
                tv_audited.setBackgroundResource(R.drawable.bg_orange_5_shape);
                tv_in_audit.setBackground(null);
                tv_audit_failure.setBackground(null);
                break;
            case R.id.tv_audit_failure:
                //审核失败
                type =2;
                FrozenDepositRequest();
                tv_in_audit.setTextColor(getResources().getColor(R.color.black));
                tv_audited.setTextColor(getResources().getColor(R.color.black));
                tv_audit_failure.setTextColor(getResources().getColor(R.color.white));
                tv_audit_failure.setBackgroundResource(R.drawable.bg_orange_5_shape);
                tv_in_audit.setBackground(null);
                tv_audited.setBackground(null);
                break;
        }
    }
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

    }

    @Override
    public void onReload() {
        FrozenDepositRequest();
    }

    @Override
    public void loadMore() {
        FrozenDepositRequest();
    }

    private int Requesttype;
    private void FrozenDepositRequest(){
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("dataType", String.valueOf(1));
        staff.put("tokenId", String.valueOf(token));

        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.FINDSTOCKEQUITYREALINFO,
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
                                if(stats.equals("1")){
                                    TableList tableList = new TableList();
                                    tableList.getArrayList().clear();
                                    if (!js.isNull("equities")) {
                                        FindStockEquityHomeEntity info;

                                        JSONArray array = js.optJSONArray("equities");
                                        for (int i = 0; i < array.length(); i++) {
                                            info = new FindStockEquityHomeEntity(array.getJSONObject(i));
                                            if(info.getAuditState()==0){
                                                Requesttype =0;
                                                tableList.getArrayList().add(info);
                                            }else if(info.getAuditState()==1){
                                                Requesttype =1;
                                                tableList.getArrayList().add(info);
                                            }else if(info.getAuditState()==2){
                                                Requesttype = 2;
                                                tableList.getArrayList().add(info);
                                            }

                                        }
                                        refresh_lv.setAdapter(null);
                                        if(type==0&&Requesttype==0){
                                            adapter = new StockAuctionAdapter(TransferRecordActivity.this, tableList.getArrayList(),2);
                                            refresh_lv.setAdapter(adapter);
                                        }else if(type==1&&Requesttype==1){
                                            adapter = new StockAuctionAdapter(TransferRecordActivity.this, tableList.getArrayList(),0);
                                            refresh_lv.setAdapter(adapter);
                                        }else if(type==2&&Requesttype==2){
                                            adapter = new StockAuctionAdapter(TransferRecordActivity.this, tableList.getArrayList(),2);
                                            refresh_lv.setAdapter(adapter);
                                        }
                                    }
                                    stopRefreshState();
                                    mEmptyView.setVisibility(View.GONE);
                                }else{
                                    ToastHelper.toastMessage(TransferRecordActivity.this,msg);
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


}


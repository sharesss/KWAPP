package com.ts.fmxt.ui.discover;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.thindo.base.Widget.refresh.RefreshListView;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.ItemAdapter.ReportInfoAdapter;
import com.ts.fmxt.ui.base.activity.FMBaseTableActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import http.data.ReportInfoEntity;
import http.data.TableList;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.ReceiverUtils;
import utils.helper.ToastHelper;
import widget.EmptyView;

/**
 * Created by A1 on 2017/9/5.
 * 举报
 */

public class ReportActivity extends FMBaseTableActivity implements View.OnClickListener, ReceiverUtils.MessageReceiver{
    private int investId;
    private EmptyView mEmptyView;
    private RefreshListView refresh_lv;
    private ReportInfoAdapter adapter;
    private EditText edtInfo;
    private TextView bt_report;


    @Override
    public void onMessage(int receiverType, Bundle bundle) {
        if (receiverType == ReceiverUtils.REPORT) {
            edtInfo.setVisibility(View.VISIBLE);
        }else if (receiverType == ReceiverUtils.GONE){
            edtInfo.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        ReceiverUtils.addReceiver(this);
        investId = getIntent().getIntExtra("investId", -1);
        bindRefreshAdapter((RefreshListView) findViewById(R.id.refresh_lv), new ReportInfoAdapter(this, arrayList));
        findViewById(R.id.iv_back).setOnClickListener(this);
        mEmptyView = (EmptyView) findViewById(R.id.empty_view);
        edtInfo = (EditText) findViewById(R.id.edt_info);
        refresh_lv = (RefreshListView) findViewById(R.id.refresh_lv);
        bt_report = (TextView) findViewById(R.id.bt_report);
        bt_report.setOnClickListener(this);
        EmptyView mEmptyView = new EmptyView(this);
        mEmptyView.setEmptyText("获取举报详情失败");
        setEmptyView(mEmptyView);
        startRefreshState();
    }
    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

    }

    @Override
    public void onReload() {
        ReportInfoRequest();
    }

    @Override
    public void loadMore() {

    }


    private void ReportInfoRequest(){
        OkHttpClientManager.getAsyn(HttpPathManager.HOST+HttpPathManager.FINDINFORMINFO,new OkHttpClientManager.ResultCallback<String>(){

            @Override
            public void onError(Request request, Exception e) {
                stopRefreshState();
                refresh_lv.setAdapter(null);
                mEmptyView.setVisibility(View.VISIBLE);
                edtInfo.setVisibility(View.INVISIBLE);
                bt_report.setVisibility(View.INVISIBLE);
            }

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject js  = new JSONObject(response);
                    if(!js.isNull("statsMsg")){
                        JSONObject json = js.optJSONObject("statsMsg");
                        String stats = json.getString("stats");
                        String msg = json.getString("msg");
                        if(stats.equals("1")){
                            TableList tableList = new TableList();
                            if (!js.isNull("informs")) {
                                JSONArray array = js.optJSONArray("informs");
                                for (int i = 0; i < array.length(); i++) {
                                    tableList.getArrayList().add(new ReportInfoEntity(array.getJSONObject(i)));
                                }
                                adapter = new ReportInfoAdapter(ReportActivity.this, tableList.getArrayList());
                                refresh_lv.setAdapter(adapter);
                            }
                            stopRefreshState();
                            mEmptyView.setVisibility(View.GONE);
//                            ToastHelper.toastMessage(getContext(), msg);
                        }else{
                            ToastHelper.toastMessage(ReportActivity.this,msg);
                            stopRefreshState();
                            mEmptyView.setVisibility(View.VISIBLE);
                            edtInfo.setVisibility(View.INVISIBLE);
                            bt_report.setVisibility(View.INVISIBLE);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_report://
                if(adapter.getName().equals("其他")){

                }
                ReportRequest();

                break;
        }

    }

    public void ReportRequest() {
        SharedPreferences sharedPreferences = getSharedPreferences("user",
                MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        String info = edtInfo.getText().toString();
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("articleType","1");
        if(adapter.getName().equals("其他")){
            if(edtInfo.getText().length()>0){
                staff.put("informReason",info);
            }else{
                ToastHelper.toastMessage(ReportActivity.this,"请输入举报详情");
                return;
            }

        }
        staff.put("informId",String.valueOf(adapter.getIsSelePosition()));
        staff.put("investId",String.valueOf(investId));
        staff.put("tokenId",token);

        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.SAVEINVESTINFORMINFO,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String result) {
                        try {
                            JSONObject js  = new JSONObject(result);
                            if(!js.isNull("statsMsg")){
                                JSONObject json = js.optJSONObject("statsMsg");
                                String stats = json.getString("stats");
                                String msg = json.getString("msg");
                                if(stats.equals("1")){
                                    ToastHelper.toastMessage(ReportActivity.this,"举报成功");
                                    finish();
                                }else{
                                    ToastHelper.toastMessage(ReportActivity.this,msg);
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

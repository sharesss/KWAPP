package com.ts.fmxt.ui.user;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import com.squareup.okhttp.Request;
import com.thindo.base.Widget.refresh.RefreshListView;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.ItemAdapter.HistoryDetailsAdapter;
import com.ts.fmxt.ui.base.activity.FMBaseTableActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import http.data.AuctionPayInfoEntity;
import http.data.TableList;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.helper.ToastHelper;
import widget.EmptyView;
import widget.titlebar.NavigationView;

/**
 * Created by kp on 2017/9/25.
 * 历史明细
 */

public class HistoryDetailsActivity extends FMBaseTableActivity {
    private EmptyView mEmptyView;
    private RefreshListView refresh_lv;
    private HistoryDetailsAdapter adapter;
    private LinearLayout ll_audited;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);
        initView();
    }

    private void initView(){
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setTitle("历史明细", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bindRefreshAdapter((RefreshListView) findViewById(R.id.refresh_lv), new HistoryDetailsAdapter(this, arrayList));
        mEmptyView = (EmptyView) findViewById(R.id.empty_view);
        refresh_lv = (RefreshListView) findViewById(R.id.refresh_lv);
        EmptyView mEmptyView = new EmptyView(this);
        setEmptyView(mEmptyView);
        startRefreshState();
        ll_audited = (LinearLayout) findViewById(R.id.ll_audited);
        ll_audited.setVisibility(View.GONE);
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

    private void FrozenDepositRequest(){
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("flowType", String.valueOf(1));
        staff.put("tokenId", String.valueOf(token));

        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.FINDSTOCKEQUITYAUCTIONPAYINFO,
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
                                    if (!js.isNull("auctions")) {
                                        JSONArray array = js.optJSONArray("auctions");
                                        for (int i = 0; i < array.length(); i++) {
                                            JSONObject jsn =array.getJSONObject(i);
                                            tableList.getArrayList().add(new AuctionPayInfoEntity(jsn));
                                        }
                                        adapter = new HistoryDetailsAdapter(HistoryDetailsActivity.this, tableList.getArrayList());
                                        refresh_lv.setAdapter(adapter);
                                    }
                                    stopRefreshState();
                                    mEmptyView.setVisibility(View.GONE);
//                            ToastHelper.toastMessage(getContext(), msg);
                                }else{
                                    ToastHelper.toastMessage(HistoryDetailsActivity.this,msg);
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

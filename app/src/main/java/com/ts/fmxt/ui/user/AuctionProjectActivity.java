package com.ts.fmxt.ui.user;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

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
 * 想竞拍的项目
 */

public class AuctionProjectActivity extends FMBaseTableActivity {
    private EmptyView mEmptyView;
    private RefreshListView refresh_lv;
    private StockAuctionAdapter adapter;
    private LinearLayout ll_audited;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);
        initView();
    }

    private void initView(){
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setTitle("想竞拍的项目", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bindRefreshAdapter((RefreshListView) findViewById(R.id.refresh_lv), new StockAuctionAdapter(this, arrayList,1));
        mEmptyView = (EmptyView) findViewById(R.id.empty_view);
        mEmptyView.setEmptyText("什么也没有");
        refresh_lv = (RefreshListView) findViewById(R.id.refresh_lv);
        ll_audited = (LinearLayout) findViewById(R.id.ll_audited);
        ll_audited.setVisibility(View.GONE);
        EmptyView mEmptyView = new EmptyView(this);
        mEmptyView.setEmptyText("什么也没有");
        setEmptyView(mEmptyView);
        startRefreshState();
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
        staff.put("dataType", String.valueOf(2));
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
                                    if (!js.isNull("equities")) {
                                        JSONArray array = js.optJSONArray("equities");
                                        for (int i = 0; i < array.length(); i++) {
                                            tableList.getArrayList().add(new FindStockEquityHomeEntity(array.getJSONObject(i)));
                                        }
                                        adapter = new StockAuctionAdapter(AuctionProjectActivity.this, tableList.getArrayList(),1);
                                        refresh_lv.setAdapter(adapter);
                                    }
                                    stopRefreshState();
                                    mEmptyView.setVisibility(View.GONE);
                                }else{
                                    ToastHelper.toastMessage(AuctionProjectActivity.this,msg);
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


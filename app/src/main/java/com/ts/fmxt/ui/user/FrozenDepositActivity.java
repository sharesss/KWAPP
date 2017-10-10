package com.ts.fmxt.ui.user;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.thindo.base.Widget.refresh.RefreshListView;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.ItemAdapter.FrozenDepositAdapter;
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
import utils.UISKipUtils;
import utils.helper.ToastHelper;
import widget.EmptyView;

import static com.ts.fmxt.R.id.btn_register;

/**
 * Created by kp on 2017/9/23.
 * 冻结保证金
 */

public class FrozenDepositActivity extends FMBaseTableActivity implements View.OnClickListener{
    private EmptyView mEmptyView;
    private RefreshListView refresh_lv;
    private FrozenDepositAdapter adapter;
    private String cashdeposit;
    private TextView tv_money;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_frozen_deposit);
        cashdeposit = getIntent().getStringExtra("cashdeposit");
        initView();
    }

    private void initView(){
        findViewById(R.id.btn_finish).setOnClickListener(this);
        findViewById(btn_register).setOnClickListener(this);
        bindRefreshAdapter((RefreshListView) findViewById(R.id.refresh_lv), new FrozenDepositAdapter(this, arrayList));
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_money.setText(cashdeposit+"");

        mEmptyView = (EmptyView) findViewById(R.id.empty_view);
        refresh_lv = (RefreshListView) findViewById(R.id.refresh_lv);
        EmptyView mEmptyView = new EmptyView(this);
        setEmptyView(mEmptyView);
        startRefreshState();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_finish:
                finish();
                break;
            case R.id.btn_register:
                UISKipUtils.startHistoryDetailsActivity(FrozenDepositActivity.this);
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

    private void FrozenDepositRequest(){
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("flowType", String.valueOf(0));
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
                                        adapter = new FrozenDepositAdapter(FrozenDepositActivity.this, tableList.getArrayList());
                                        refresh_lv.setAdapter(adapter);
                                    }
                                    stopRefreshState();
                                    mEmptyView.setVisibility(View.GONE);
                                }else{
                                    ToastHelper.toastMessage(FrozenDepositActivity.this,msg);
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

package com.ts.fmxt.ui.StockAuction;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.thindo.base.Widget.refresh.RefreshListView;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.ItemAdapter.StockAuctionAdapter;
import com.ts.fmxt.ui.base.frameng.FMBaseTableFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import http.data.ConsumerEntity;
import http.data.FindStockEquityHomeEntity;
import http.data.TableList;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.helper.ToastHelper;
import widget.EmptyView;

/**
 * Created by kp on 2017/9/11.
 * 股权拍卖
 */

public class StockAuctionFrament extends FMBaseTableFragment implements View.OnClickListener {
    private int pageNo = 1;
    private String brandId = "";
    private EmptyView mEmptyView;
    private RefreshListView refresh_lv;
    private StockAuctionAdapter adapter;
    private TextView iv_share;
    private ConsumerEntity info;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.include_stock_auction_view, container, false);
        initTitle(inflate);
        bindRefreshAdapter((RefreshListView) inflate.findViewById(R.id.refresh_lv), new StockAuctionAdapter(getActivity(), arrayList,0));
//        ReceiverUtils.addReceiver(this);
        startRefreshState();
        return inflate;
    }

    //初始化头部
    private void initTitle(View inflate) {
        mEmptyView = (EmptyView) inflate.findViewById(R.id.empty_view);
        refresh_lv = (RefreshListView) inflate.findViewById(R.id.refresh_lv);
        iv_share = (TextView) inflate.findViewById(R.id.iv_share);
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //添加项目
                ToastHelper.toastMessage(getActivity(),"功能开发中，敬请期待");
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

    }

    @Override
    public void onReload() {
        pageNo = 1;
        consumerListRequest();
    }

    @Override
    public void loadMore() {
        pageNo = pageNo + 1;
        consumerListRequest();
    }

    public void stopRefreshState(){
        stop();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    private void consumerListRequest(){
        OkHttpClientManager.getAsyn(HttpPathManager.HOST+HttpPathManager.FINDSTOCKEQUITYHOME,new OkHttpClientManager.ResultCallback<String>(){

            @Override
            public void onError(Request request, Exception e) {
                stopRefreshState();
                refresh_lv.setAdapter(null);
                mEmptyView.setVisibility(View.VISIBLE);

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
                            if (!js.isNull("equities")) {
                                JSONArray array = js.optJSONArray("equities");
                                for (int i = 0; i < array.length(); i++) {
                                    tableList.getArrayList().add(new FindStockEquityHomeEntity(array.getJSONObject(i)));
                                }
                                adapter = new StockAuctionAdapter(getActivity(), tableList.getArrayList(),0);
                                refresh_lv.setAdapter(adapter);
                            }
                            stopRefreshState();
                            mEmptyView.setVisibility(View.GONE);
//                            ToastHelper.toastMessage(getContext(), msg);
                        }else{
                            ToastHelper.toastMessage(getContext(),msg);
                            stopRefreshState();
                            mEmptyView.setVisibility(View.VISIBLE);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }



}

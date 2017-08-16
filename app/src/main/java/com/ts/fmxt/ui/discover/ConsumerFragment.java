package com.ts.fmxt.ui.discover;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.thindo.base.Widget.refresh.RefreshListView;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.ItemAdapter.FollowProjectAdapter;
import com.ts.fmxt.ui.base.frameng.FMBaseTableFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import http.data.ConsumerEntity;
import http.data.TableList;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.helper.ToastHelper;
import widget.EmptyView;
import widget.Share.PopupShareView;

/**
 * 消费返利
 */

public class ConsumerFragment extends FMBaseTableFragment implements View.OnClickListener {
    private int pageNo = 1;
    private String brandId = "";
    private EmptyView mEmptyView;
    private RefreshListView refresh_lv;
    private FollowProjectAdapter adapter;
    private TextView iv_share;
    private ConsumerEntity info;





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.include_navigation_refresh_list_view, container, false);
        initTitle(inflate);
        bindRefreshAdapter((RefreshListView) inflate.findViewById(R.id.refresh_lv), new FollowProjectAdapter(getActivity(), arrayList));
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
                showShareDialog();
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
        OkHttpClientManager.getAsyn(HttpPathManager.HOST+HttpPathManager.INVESTLIST,new OkHttpClientManager.ResultCallback<String>(){

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
                            if (!js.isNull("projects")) {
                                JSONArray array = js.optJSONArray("projects");
                                for (int i = 0; i < array.length(); i++) {
                                    tableList.getArrayList().add(new ConsumerEntity(array.getJSONObject(i)));
                                }
                                adapter = new FollowProjectAdapter(getActivity(), tableList.getArrayList());
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

    //分享对话框
    private void showShareDialog() {
        String title = String.format(getString(R.string.share_consumenr_title));
        PopupShareView popupShareView = new PopupShareView(getActivity());
        popupShareView.setContent(getString(R.string.share_consumenr_context));

        popupShareView.setWechatMomentsTitle(title);
        popupShareView.setTitle(title);
        String uri = String.format(getString( R.string.html_fm_fmoneyShare_user));//
        popupShareView.setUrl(uri);
        popupShareView.setLogo("https://oixnydddk.qnssl.com/bill/1502790203975.jpg");
        popupShareView.showPopupWindow();
    }

}

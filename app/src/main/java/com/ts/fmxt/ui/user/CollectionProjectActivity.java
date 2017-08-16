package com.ts.fmxt.ui.user;/**
 * Created by A1 on 2017/8/15.
 */

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.squareup.okhttp.Request;
import com.thindo.base.Widget.refresh.RefreshListView;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.ItemAdapter.FollowProjectAdapter;
import com.ts.fmxt.ui.base.activity.FMBaseTableActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import http.data.ConsumerEntity;
import http.data.TableList;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.helper.ToastHelper;
import widget.EmptyView;
import widget.titlebar.NavigationView;

/**
 * created by kp at 2017/8/15
 * 收藏的项目
 */
public class CollectionProjectActivity extends FMBaseTableActivity {
    private EmptyView mEmptyView;
    private RefreshListView refresh_lv;
    private FollowProjectAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_project);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setTitle(getResourcesStr(R.string.collection_project), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        bindRefreshAdapter((RefreshListView) findViewById(R.id.refresh_lv), new FollowProjectAdapter(this, arrayList));
        mEmptyView = (EmptyView) findViewById(R.id.empty_view);
        refresh_lv = (RefreshListView) findViewById(R.id.refresh_lv);
        EmptyView mEmptyView = new EmptyView(this);
        setEmptyView(mEmptyView);
        startRefreshState();
    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

    }

    @Override
    public void onReload() {
        FollowProjectRequest();
    }

    @Override
    public void loadMore() {
        FollowProjectRequest();
    }

    private void FollowProjectRequest(){
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("tokenId", String.valueOf(token));

        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.GETINVESTPROJECTCOLLECT,
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
                                    TableList tableList = new TableList();
                                    if (!js.isNull("investProject")) {
                                        JSONArray array = js.optJSONArray("investProject");
                                        for (int i = 0; i < array.length(); i++) {
                                            tableList.getArrayList().add(new ConsumerEntity(array.getJSONObject(i)));
                                        }
                                        adapter = new FollowProjectAdapter(CollectionProjectActivity.this, tableList.getArrayList());
                                        refresh_lv.setAdapter(adapter);
                                    }
                                    stopRefreshState();
                                    mEmptyView.setVisibility(View.GONE);
//                            ToastHelper.toastMessage(getContext(), msg);
                                } else {
                                    ToastHelper.toastMessage(CollectionProjectActivity.this, msg);
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

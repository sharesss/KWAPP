package com.ts.fmxt.ui.user;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;

import com.squareup.okhttp.Request;
import com.thindo.base.Widget.refresh.RefreshListView;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.ItemAdapter.FollowProjectAdapter;
import com.ts.fmxt.ui.ItemAdapter.MessageAdapter;
import com.ts.fmxt.ui.base.activity.FMBaseTableActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import http.data.MessageEntity;
import http.data.TableList;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.ReceiverUtils;
import utils.UISKipUtils;
import utils.helper.ToastHelper;
import widget.EmptyView;
import widget.testlistview.view.CstSwipeDelMenuViewGroup;
import widget.titlebar.NavigationView;

import static com.ts.fmxt.R.id.empty_view;

/**
 * created by kp at 2017/8/3
 * 通知
 */
public class MessageActivity  extends FMBaseTableActivity implements  CstSwipeDelMenuViewGroup.MenuOnItemClick {
    private EmptyView mEmptyView;
    private RefreshListView refresh_lv;
    private MessageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setTitle(getResourcesStr(R.string.notice), new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        navigationView.showRightButtomAndTextColor(R.string.empty, R.color.black, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //现金明细
                cleanNotifyRequest();
            }
        });
        bindRefreshAdapter((RefreshListView) findViewById(R.id.refresh_lv), new FollowProjectAdapter(this, arrayList));
        mEmptyView = (EmptyView) findViewById(empty_view);
        refresh_lv = (RefreshListView) findViewById(R.id.refresh_lv);
        mEmptyView.setEmptyText("再怎么看也没有啦");
        EmptyView mEmptyView = new EmptyView(this);
        mEmptyView.setEmptyText("暂无消息，不妨主动点");
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

    private ArrayList<Object> arrays;
    private void FollowProjectRequest(){
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("tokenId", String.valueOf(token));

        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.MESSAGELIST,
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
                                    if (!js.isNull("notifies")) {
                                        JSONArray array = js.optJSONArray("notifies");
                                        for (int i = 0; i < array.length(); i++) {
                                             tableList.getArrayList().add(new MessageEntity(array.getJSONObject(i)));
                                        }
                                        arrays = tableList.getArrayList();
                                        adapter = new MessageAdapter(MessageActivity.this, tableList.getArrayList());
                                        refresh_lv.setAdapter(adapter);
                                    }
                                    stopRefreshState();
                                    mEmptyView.setVisibility(View.GONE);
//                                    ToastHelper.toastMessage(MessageActivity.this, msg);
                                } else {
                                    ToastHelper.toastMessage(MessageActivity.this, msg);
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
    public void onMenuOnItemClick(int position) {
        MessageEntity info = (MessageEntity) adapter.getItem(position);
        ((MessageEntity) arrays.get(position)).setReadFlag(1);
        DelReadFlagRequest(info);
        adapter.notifyDataSetChanged();
        UISKipUtils.startDiscoverDetailsActivity(MessageActivity.this,info.getRealId(),1);

    }

    public void cleanNotifyRequest() {
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("tokenId", String.valueOf(token));
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.UPDATEMESSAGEINFORMFLAG,
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
                                    FollowProjectRequest();
                                    adapter.notifyDataSetChanged();
                                    Bundle bundle = new Bundle();
                                    ReceiverUtils.sendReceiver(ReceiverUtils.REFRESH,bundle);
                                } else {
                                    ToastHelper.toastMessage(MessageActivity.this,msg);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, staff
        );
    }

    public void cleanNotifyRequest(String type, String id) {
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("tokenId", String.valueOf(token));
        staff.put("type", type);
        staff.put("msgId", id);
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.UPDATEMESSAGEINFORMFLAG,
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
                                    FollowProjectRequest();
                                    adapter.notifyDataSetChanged();
                                    Bundle bundle = new Bundle();
                                    ReceiverUtils.sendReceiver(ReceiverUtils.REFRESH,bundle);
                                } else {
                                ToastHelper.toastMessage(MessageActivity.this,msg);

                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, staff
        );
    }

    private void DelReadFlagRequest(MessageEntity info){
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("msgId", String.valueOf(info.getMessageId()) );
        staff.put("type", String.valueOf(info.getType()) );

        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.UPDATEMESSAGEREADFLAG,
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
                                    Bundle bundle = new Bundle();
                                    ReceiverUtils.sendReceiver(ReceiverUtils.REFRESH,bundle);
                                } else {

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

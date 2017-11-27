package com.ts.fmxt.ui.user;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.thindo.base.Widget.refresh.RefreshListView;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.ItemAdapter.FollowProjectAdapter;
import com.ts.fmxt.ui.base.activity.FMBaseTableActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import http.data.ConsumerEntity;
import http.data.TableList;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.helper.ToastHelper;
import widget.EmptyView;

/**
 * Created by kp on 2017/9/25.
 * 我的跟投
 */

public class MyHeelShotActivity extends FMBaseTableActivity implements View.OnClickListener {
    private EmptyView mEmptyView;
    private RefreshListView refresh_lv;
    private FollowProjectAdapter adapter;
    private ArrayAdapter<String> arr_adapter;
    private int userid;
    private TextView tv_spinner;
    private List<String> data_list;
    boolean isclick = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_follow_project);
        userid  =getIntent().getIntExtra("userid",0);
        bindRefreshAdapter((RefreshListView) findViewById(R.id.refresh_lv), new FollowProjectAdapter(this, arrayList));
        findViewById(R.id.btn_finish).setOnClickListener(this);
        tv_spinner = (TextView) findViewById(R.id.tv_spinner);
//        tv_spinner.setOnClickListener(this);
        mEmptyView = (EmptyView) findViewById(R.id.empty_view);
        mEmptyView.setEmptyText("什么也没有");
        refresh_lv = (RefreshListView) findViewById(R.id.refresh_lv);
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
        FollowProjectRequest(0);
    }

    @Override
    public void loadMore() {
    }

    private void FollowProjectRequest(int investProjectState){
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        if(userid!=0){
            staff.put("userId", String.valueOf(userid));
        }else
            staff.put("tokenId", String.valueOf(token));
        staff.put("queryType", String.valueOf(2));
        staff.put("investProjectState", String.valueOf(investProjectState));
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.GETINVESTPROJECTFOLLOW,
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
                                    if (!js.isNull("investProject")) {
                                        JSONArray array = js.optJSONArray("investProject");
                                        for (int i = 0; i < array.length(); i++) {
                                            String amount = array.getJSONObject(i).getString("amount");
                                            JSONObject jsn =array.getJSONObject(i).getJSONObject(("investProject"));
                                            tableList.getArrayList().add(new ConsumerEntity(jsn,amount));
                                        }
                                        adapter = new FollowProjectAdapter(MyHeelShotActivity.this, tableList.getArrayList());
                                        refresh_lv.setAdapter(adapter);
                                    }
                                    stopRefreshState();
                                    mEmptyView.setVisibility(View.GONE);
//                            ToastHelper.toastMessage(getContext(), msg);
                                }else{
                                    ToastHelper.toastMessage(MyHeelShotActivity.this,msg);
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
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_finish:
                finish();
                break;
            case R.id.tv_spinner:
                if(isclick){
                    isclick = false;
                }else {
                    isclick =true;
                }
                Drawable sexDrawble = getResources().getDrawable(isclick ? R.mipmap.down : R.mipmap.upward);
                sexDrawble.setBounds(0, 0, sexDrawble.getMinimumWidth(), sexDrawble.getMinimumHeight());
                tv_spinner.setCompoundDrawables(null, null, null, sexDrawble);
                spinnerWin win =new spinnerWin(MyHeelShotActivity.this);
                win.showAtLocation(
                        findViewById(R.id.AppWidget),
                        Gravity.TOP | Gravity.TOP, 0, 0); // 设置layout在PopupWindow中显示的位置
                break;
        }
    }

    class spinnerWin extends PopupWindow {
        private View mMenuView;
        private Activity context;
        private TextView tv_all,tv_in_hand,tv_ended;
        public String url;

        public spinnerWin(Activity context) {
            this.context = context;
            initParam();

        }

        private void initParam() {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            mMenuView = inflater.inflate(R.layout.pop_window_spinner, null);
            initView();

            // 设置SelectPicPopupWindow的View
            this.setContentView(mMenuView);
            // 设置按钮监听
            this.setFocusable(true);
            // 设置SelectPicPopupWindow弹出窗体可点击
            this.setFocusable(true);
            // 设置SelectPicPopupWindow弹出窗体动画效果
            this.setAnimationStyle(R.style.PopupAnimation);
            // 实例化一个ColorDrawable颜色为半透明 0xb0000000
            ColorDrawable dw = new ColorDrawable(0x00000000);
            // 设置SelectPicPopupWindow弹出窗体的背景
            this.setBackgroundDrawable(dw);
            this.setOutsideTouchable(true);
            // 显示窗口
            setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
            setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
        }

        private void initView() {
            tv_all = (TextView) mMenuView.findViewById(R.id.tv_all);
            tv_in_hand = (TextView) mMenuView.findViewById(R.id.tv_in_hand);
            tv_ended = (TextView) mMenuView.findViewById(R.id.tv_ended);
            tv_all.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_spinner.setText("全部");
                    FollowProjectRequest(0);
                    dismiss();
                }
            });
            tv_in_hand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_spinner.setText("进行中");
                    FollowProjectRequest(1);
                    dismiss();
                }
            });
            tv_ended.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_spinner.setText("已结束");
                    FollowProjectRequest(2);
                    dismiss();
                }
            });
        }
    }
}

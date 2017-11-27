package com.ts.fmxt.ui.discover;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.squareup.okhttp.Request;
import com.thindo.base.Widget.refresh.RefreshListView;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.ItemAdapter.CommentsAdapter;
import com.ts.fmxt.ui.base.activity.FMBaseScrollActivityV2;
import com.ts.fmxt.ui.discover.view.KeyMapDailog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import http.data.ConsumerCommentEntity;
import http.data.TableList;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.helper.ToastHelper;
import widget.EmptyView;

/**
 * Created by kp on 2017/11/21.
 * 评论界面
 *
 */

public class CommentActivity extends FMBaseScrollActivityV2 implements View.OnClickListener, KeyMapDailog.SendBackListener {
    private int investId;//股票ID
    private EmptyView mEmptyView;
    private RefreshListView refresh_lv;
    private KeyMapDailog dialog;
    private CommentsAdapter mCommentAdapter;
    private ConsumerCommentEntity mConsumerCommentEntity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_view);
        investId = getIntent().getIntExtra("investId", -1);
//        bindRefreshAdapter((RefreshListView) findViewById(R.id.refresh_lv), new CommentAdapter(this, arrayList,0,1));
        mEmptyView = (EmptyView) findViewById(R.id.empty_view);
        mEmptyView.setEmptyText("什么也没有");
        refresh_lv = (RefreshListView) findViewById(R.id.refresh_lv);
        EmptyView mEmptyView = new EmptyView(this);
        mEmptyView.setEmptyText("什么也没有");
//        setEmptyView(mEmptyView);
        CommentRequest(0);
        startRefreshState();
        initView();
    }

    private void initView(){
        findViewById(R.id.btn_finish).setOnClickListener(this);
        findViewById(R.id.bt_comment).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_finish:
                finish();
                break;
            case R.id.bt_comment:
                KeyMapDailog dialog = new KeyMapDailog("评论是我们的最大动力", this);
                dialog.show(getSupportFragmentManager(), "评论");
                break;
        }
    }


    @Override
    public void onReload() {
        CommentRequest(0);
    }

    @Override
    public void loadMore() {
        CommentRequest(0);

    }

    private List<Object> arrayList = new ArrayList();
    private String token;
    //评论
    public void CommentRequest(final int type) {
        arrayList.clear();
        SharedPreferences sharedPreferences = getSharedPreferences("user",
                MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("investId", String.valueOf(investId));
        staff.put("commentType", String.valueOf(type));
        if(token!=null){
            staff.put("tokenId", String.valueOf(token));
        }
        staff.put("moduleType", String.valueOf(1));
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.INVESTCOMMENTLIST,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String result) {
                        try {
                            JSONObject js = new JSONObject(result);
                            int totalNum = 0;//总评
                            int desre = 0;//值投
                            int bedesre = 0;//不值投
                            totalNum = js.optInt("totalNum");
                            desre = js.optInt("desre");
                            bedesre = js.optInt("bedesre");
//                            tv_auction_evaluation.setText("拍卖评价("+totalNum+")");

                            if (!js.isNull("statsMsg")) {
                                JSONObject json = js.optJSONObject("statsMsg");
                                String stats = json.getString("stats");
                                String msg = json.getString("msg");
                                if (stats.equals("1")) {
                                    TableList tableList = new TableList();
                                    if (!js.isNull("comments")) {
                                        JSONArray array = js.optJSONArray("comments");
                                        for (int i = 0; i < array.length(); i++) {
                                            tableList.getArrayList().add(new ConsumerCommentEntity(array.getJSONObject(i)));
                                        }
                                        arrayList.addAll(tableList.getArrayList());
                                        mCommentAdapter = new CommentsAdapter(CommentActivity.this,arrayList,type,0);
                                        refresh_lv.setAdapter(mCommentAdapter);
                                    }
                                } else {
                                    ToastHelper.toastMessage(CommentActivity.this, msg);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, staff
        );
    }

    //发表评论，回复评论
    private void consumerContentRequest(String inputText, final ConsumerCommentEntity mConsumerCommentEntity, final int type, int investId) {

        SharedPreferences sharedPreferences = getSharedPreferences("user",
                MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("investId", String.valueOf(investId));
        staff.put("commentType", String.valueOf(type));
        staff.put("content", String.valueOf(inputText));
        staff.put("tokenId", String.valueOf(token));
        if (mConsumerCommentEntity != null) {
            staff.put("parentId", String.valueOf(mConsumerCommentEntity.getId()));
            staff.put("parentUserId", String.valueOf(mConsumerCommentEntity.getUserId()));
            staff.put("parentUserName", String.valueOf(mConsumerCommentEntity.getNickName()));
        }
        staff.put("moduleType", String.valueOf(1));
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.SAVEINVESTCOMMENT,
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
                                    CommentRequest(type);
                                } else {
                                    ToastHelper.toastMessage(CommentActivity.this, msg);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, staff
        );
    }

    public void replys(String name, ConsumerCommentEntity info) {
        mConsumerCommentEntity = info;
        dialog = null;
        dialog = new KeyMapDailog(String.format("回复:%s", name), CommentActivity.this);
        dialog.show(getSupportFragmentManager(), "回复评论");
    }


    @Override
    public void sendBack(String inputText) {
        consumerContentRequest(inputText, mConsumerCommentEntity, 0, investId);
    }
}

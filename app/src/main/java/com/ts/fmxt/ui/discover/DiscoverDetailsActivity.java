package com.ts.fmxt.ui.discover;/**
 * Created by A1 on 2017/8/1.
 */

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.ItemAdapter.BpresultAdapter;
import com.ts.fmxt.ui.ItemAdapter.CommentAdapter;
import com.ts.fmxt.ui.ItemAdapter.ItemBpAdapter;
import com.ts.fmxt.ui.base.activity.FMBaseScrollActivityV2;
import com.ts.fmxt.ui.discover.view.CircleBar;
import com.ts.fmxt.ui.discover.view.FlowLayout;
import com.ts.fmxt.ui.discover.view.KeyMapDailog;
import com.ts.fmxt.ui.discover.view.RedCircleBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import http.data.ConsumerCommentEntity;
import http.data.ConsumerEntity;
import http.data.InvestBPListEntity;
import http.data.TableList;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.UISKipUtils;
import utils.helper.ToastHelper;
import widget.FMNoScrollListView;
import widget.Share.PopupShareView;
import widget.image.FMNetImageView;

/**
 * created by kp at 2017/8/1
 * 发现详情
 */
public class DiscoverDetailsActivity extends FMBaseScrollActivityV2 implements View.OnClickListener, KeyMapDailog.SendBackListener {
    private int investId;
    private FMNetImageView ivImage;
    private TextView tvBrandName, tvBrandDetails, tvIndex;
    private LinearLayout llTemp,llCollection;
    private ProgressBar pbIndex, pbGreenindex;
    private ConsumerEntity info;
    private CircleBar ivCirclebar;
    private RedCircleBar ivRedCirclebar;
    private FlowLayout flow_layout;
    private boolean isClick;
    private String checktext;
    private FMNoScrollListView refresh_lv,reviews_lv,lv_result;
    private ItemBpAdapter adapter;
    private BpresultAdapter mBpresultAdapter;
    private CommentAdapter mCommentAdapter;
    private TextView tvAllReviews,tvWorthThrowing,tvNoWorthThrowing;
    private ConsumerCommentEntity mConsumerCommentEntity = null;
    private KeyMapDailog dialog;
    private ArrayList arr;
    private int type=0;//请求的评论类型0是全部，1是值得投，2是不值得投
    private int totalNum = 0;//总评
    private int desre = 0;//值投
    private int bedesre = 0;//不值投
    private TextView tvCollection,tvWithTheVote,tvBpresult,tvResult,tvPrompt;
    private boolean isCollect;
    private ScrollView svArr;
    private int cont=0;
    private int recLen = 3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_details);
        investId = getIntent().getIntExtra("id", -1);
        initView();
    }

    private void initView() {
        //顶部UI
        ivImage = (FMNetImageView) findViewById(R.id.iv_image);
        tvBrandName = (TextView) findViewById(R.id.tv_brand_name);
        tvBrandDetails = (TextView) findViewById(R.id.tv_brand_details);
        pbIndex = (ProgressBar) findViewById(R.id.pb_index);
        pbGreenindex = (ProgressBar) findViewById(R.id.pb_greenindex);
        tvIndex = (TextView) findViewById(R.id.tv_index);
        findViewById(R.id.iv_share).setOnClickListener(this);
        findViewById(R.id.btn_finish).setOnClickListener(this);
        tvPrompt = (TextView) findViewById(R.id.tv_prompt);
        Message message = handler.obtainMessage(1);     // Message
        handler.sendMessageDelayed(message, 1000);
        //饼图UI
        ivCirclebar = (CircleBar) findViewById(R.id.iv_circlebar);
        ivRedCirclebar = (RedCircleBar) findViewById(R.id.iv_redcirclebar);

        DiscoverDetailsRequest();//顶部的数据获取
        InvestBPListRequest();
        CommentRequest(type);


        //12项BP
        refresh_lv = (FMNoScrollListView) findViewById(R.id.lv_bp_item);
        llTemp = (LinearLayout) findViewById(R.id.ll_temp2);
        flow_layout = (FlowLayout) findViewById(R.id.flow_layout);//标签布局
        tvBpresult = (TextView) findViewById(R.id.tv_bpresult);
        tvResult = (TextView) findViewById(R.id.tv_result);
        tvBpresult.setOnClickListener(this);
        lv_result = (FMNoScrollListView) findViewById(R.id.lv_result);
        //评论
        tvAllReviews = (TextView) findViewById(R.id.tv_all_reviews);
        tvWorthThrowing = (TextView) findViewById(R.id.tv_worth_throwing);
        tvNoWorthThrowing = (TextView) findViewById(R.id.tv_no_worth_throwing);
        reviews_lv = (FMNoScrollListView) findViewById(R.id.lv_comment);
        findViewById(R.id.tv_write_comment).setOnClickListener(this);
        tvAllReviews.setOnClickListener(this);
        tvWorthThrowing.setOnClickListener(this);
        tvNoWorthThrowing.setOnClickListener(this);

        //底部两个按钮
        llCollection = (LinearLayout) findViewById(R.id.ll_collection);
        tvCollection = (TextView) findViewById(R.id.tv_collection);
        tvWithTheVote = (TextView) findViewById(R.id.tv_with_the_vote);
        tvWithTheVote.setOnClickListener(this);
        llCollection.setOnClickListener(this);
        findViewById(R.id.tv_top).setOnClickListener(this);
        svArr = (ScrollView) findViewById(R.id.sv_arr);

    }

    private void formatData(ConsumerEntity info) {
        if (info == null) {
            return;
        }

        ivImage.loadImage(info.getInvestPhoto());
        tvBrandName.setText(info.getInvestName());
        tvBrandDetails.setText(info.getInvestDeion());
        Double index = info.getExponent() * 100;
        int exponent = (new Double(index)).intValue();
        if (exponent < 80) {
            pbIndex.setProgress(exponent);
            pbIndex.setVisibility(View.VISIBLE);
            pbGreenindex.setVisibility(View.GONE);
        } else {
            pbGreenindex.setProgress(exponent);
            pbGreenindex.setVisibility(View.VISIBLE);
            pbIndex.setVisibility(View.GONE);
        }
        tvIndex.setText(exponent + "%");

        ivCirclebar.setText(String.valueOf(info.getDokels()));//中间的数字
        ivCirclebar.startCustomAnimation();
        float progress = progressPercentage(info.getVoteNum(), info.getDokels());
        ivCirclebar.setSweepAngle(progress);//进度

        ivRedCirclebar.setText(String.valueOf(info.getNotDokels()));//中间的数字
        ivRedCirclebar.startCustomAnimation();
        float inxe = progressPercentage(info.getVoteNum(), info.getNotDokels());
        ivRedCirclebar.setSweepAngle(inxe);//进度

        Drawable sexDrawble = getResources().getDrawable(info.getIsCollect() == 1 ? R.mipmap.card_detail_s : R.mipmap.card_detail_n);
        sexDrawble.setBounds(0, 0, sexDrawble.getMinimumWidth(), sexDrawble.getMinimumHeight());
        tvCollection.setCompoundDrawables(sexDrawble, null, null, null);
        if(info.getIsCollect() == 1){
            isCollect = true;
        }else{
            isCollect = false;
        }
    }

    private void formatDPData(){
        if(arr.size()==0){
            flow_layout.setVisibility(View.GONE);
            return;
        }
        //标签UI
        llTemp.setVisibility(View.VISIBLE);
        // 循环添加TextView到容器
        for (int i = 0; i < arr.size(); i++) {
            InvestBPListEntity info = (InvestBPListEntity)  arr.get(i);
            final TextView view = new TextView(this);
            view.setText(info.getBpname());
            view.setTextColor(Color.GRAY);
            view.setPadding(5, 5, 5, 5);
            view.setGravity(Gravity.CENTER);
            view.setTextSize(18);
            // 设置背景选择器到TextView上
            Resources resources = getResources();
            Drawable btnDrawable = resources.getDrawable(R.drawable.bg_gray_tag_shape);
            view.setBackground(btnDrawable);

            // 设置点击事件
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = ((TextView) v).getText().toString();
                    Resources resources = getResources();
                    v.setBackground(resources.getDrawable(R.drawable.bg_orange_5_shape));
                    ((TextView) v).setTextColor(Color.WHITE);

                    refresh_lv.smoothScrollToPosition(10);
                    for (int i = 0; i < flow_layout.getChildCount(); i++) {
                        View indexview = flow_layout.getChildAt(i);
                        if (indexview instanceof TextView) {
                            TextView indexTextView = (TextView) indexview;
                            if (indexTextView.getText().equals(checktext)) {
                                indexTextView.setBackground(resources.getDrawable(R.drawable.bg_gray_tag_shape));
                                indexTextView.setTextColor(Color.GRAY);
                                break;
                            }
                        }
                    }
                    checktext = name;
                }
            });
            flow_layout.addView(view);

            if(info.isScore()==1){
                cont++;
            }
        }
        if(cont>=6){
            tvBpresult.setVisibility(View.VISIBLE);
        }else {
            tvBpresult.setVisibility(View.GONE);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_finish:
                finish();
                break;
            case R.id.iv_share:
                showShareDialog();
                break;
            case R.id.tv_all_reviews:
                type=0;
                tvAllReviews.setTextColor(this.getResources().getColor(R.color.orange));
                tvWorthThrowing.setTextColor(this.getResources().getColor(R.color.black));
                tvNoWorthThrowing.setTextColor(this.getResources().getColor(R.color.black));
                CommentRequest(type);
                break;
            case R.id.tv_worth_throwing:
                type=1;
                tvAllReviews.setTextColor(this.getResources().getColor(R.color.black));
                tvWorthThrowing.setTextColor(this.getResources().getColor(R.color.orange));
                tvNoWorthThrowing.setTextColor(this.getResources().getColor(R.color.black));
                CommentRequest(type);
                break;
            case R.id.tv_no_worth_throwing:
                type=2;
                tvAllReviews.setTextColor(this.getResources().getColor(R.color.black));
                tvWorthThrowing.setTextColor(this.getResources().getColor(R.color.black));
                tvNoWorthThrowing.setTextColor(this.getResources().getColor(R.color.orange));
                CommentRequest(type);
                break;
            case R.id.tv_write_comment:
                SharedPreferences sharedPreferences= getSharedPreferences("user",
                        MODE_PRIVATE);
                String token=sharedPreferences.getString("token", "");
                if (token.equals("")) {
                    UISKipUtils.startLoginActivity(DiscoverDetailsActivity.this);
                    return;
                }
//                mConsumerCommentEntity = null;
                dialog = new KeyMapDailog("评论是疯蜜范的最大动力", DiscoverDetailsActivity.this);
                dialog.show(getSupportFragmentManager(), "评论");
                break;
            case R.id.ll_collection:
                if(isCollect){
                    collectionRequest(0);
                    isCollect =false;
                }else{
                    collectionRequest(1);
                    isCollect =true;
                }
                break;
            case R.id.tv_bpresult:
                tvBpresult.setVisibility(View.GONE);
                tvResult.setVisibility(View.VISIBLE);
                InvestBPListRequest();
                break;
            case R.id.tv_top:
                RequestTop();
                break;
        }
    }

    private void DiscoverDetailsRequest() {
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("investId", String.valueOf(investId));
        staff.put("tokenId", String.valueOf(token));
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.INVESTDETAIL,
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
                                    if (!js.isNull("project")) {
                                        JSONObject jsonobj = js.optJSONObject("project");
                                        info = new ConsumerEntity(jsonobj);
                                        formatData(info);

                                    }
                                } else {
                                    ToastHelper.toastMessage(DiscoverDetailsActivity.this, msg);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, staff
        );
    }

    //12项BP
    private void  InvestBPListRequest() {
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("investId", String.valueOf(investId));
        staff.put("tokenId", String.valueOf(token));
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.INVESTBPLIST,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        llTemp.setVisibility(View.GONE);
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
                                    if (!js.isNull("projectBPs")) {
                                        TableList tableList = new TableList();
                                        JSONArray array = js.optJSONArray("projectBPs");
                                        for (int i = 0; i < array.length(); i++) {
                                            tableList.getArrayList().add(new InvestBPListEntity(array.getJSONObject(i)));
                                        }
                                        arr = tableList.getArrayList();
                                        if(cont>1){
                                            mBpresultAdapter = new BpresultAdapter(DiscoverDetailsActivity.this, tableList.getArrayList());
                                            lv_result.setAdapter(mBpresultAdapter);
                                        }else
                                        formatDPData();
                                        adapter = new ItemBpAdapter(DiscoverDetailsActivity.this, tableList.getArrayList(),investId);
                                        refresh_lv.setAdapter(adapter);

                                    }
                                } else {
                                    llTemp.setVisibility(View.GONE);
                                    ToastHelper.toastMessage(DiscoverDetailsActivity.this, msg);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            llTemp.setVisibility(View.GONE);
                        }
                    }
                }, staff
        );
    }

    //评论
    public void   CommentRequest(final int type){
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("investId", String.valueOf(investId));
        staff.put("commentType", String.valueOf(type));
        staff.put("tokenId", String.valueOf(token));
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
                            totalNum = js.optInt("totalNum");
                            desre = js.optInt("desre");
                            bedesre = js.optInt("bedesre");
                            tvAllReviews.setText("全部评论(" + totalNum + ")");
                            tvWorthThrowing.setText("值得投(" + desre + ")");
                            tvNoWorthThrowing.setText("不值得投(" + bedesre + ")");
                            if (!js.isNull("statsMsg")) {
                                JSONObject json = js.optJSONObject("statsMsg");
                                String stats = json.getString("stats");
                                String msg = json.getString("msg");
                                if (stats.equals("1")) {
                                    if (!js.isNull("comments")) {
                                        TableList tableList = new TableList();
                                        JSONArray array = js.optJSONArray("comments");
                                        for (int i = 0; i < array.length(); i++) {
                                            tableList.getArrayList().add(new ConsumerCommentEntity(array.getJSONObject(i)));
                                        }
                                        mCommentAdapter = new CommentAdapter(DiscoverDetailsActivity.this, tableList.getArrayList(),type);
                                        reviews_lv.setAdapter(mCommentAdapter);
                                        mCommentAdapter.notifyDataSetChanged();
                                        RequestTop();
                                    }
                                } else {
                                    ToastHelper.toastMessage(DiscoverDetailsActivity.this, msg);
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
    private void consumerContentRequest(String inputText){
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
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

                                } else {
                                    ToastHelper.toastMessage(DiscoverDetailsActivity.this, msg);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, staff
        );
    }

    //收藏接口
    private void collectionRequest(final int enabled){
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("investId", String.valueOf(investId));
        staff.put("enabled", String.valueOf(enabled) );
        staff.put("tokenId", String.valueOf(token));

        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.EDITINVESTPROJECTCOLLECT,
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
                                    if(enabled==1){
                                        Drawable sexDrawble = getResources().getDrawable( R.mipmap.card_detail_s);
                                        sexDrawble.setBounds(0, 0, sexDrawble.getMinimumWidth(), sexDrawble.getMinimumHeight());
                                        tvCollection.setCompoundDrawables(sexDrawble, null, null, null);
                                    }else{
                                        Drawable sexDrawble = getResources().getDrawable( R.mipmap.card_detail_n);
                                        sexDrawble.setBounds(0, 0, sexDrawble.getMinimumWidth(), sexDrawble.getMinimumHeight());
                                        tvCollection.setCompoundDrawables(sexDrawble, null, null, null);

                                    }

                                } else {
                                    if(enabled==1){
                                        ToastHelper.toastMessage(DiscoverDetailsActivity.this, "收藏失败");
                                    }else{
                                        ToastHelper.toastMessage(DiscoverDetailsActivity.this, "取消收藏失败");
                                    }

                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, staff
        );
    }

    //分享对话框
    private void showShareDialog() {
        if (info == null) return;
        String title = String.format(info.getInvestName());
        PopupShareView popupShareView = new PopupShareView(DiscoverDetailsActivity.this);
        popupShareView.setContent(info.getInvestDeion());

        popupShareView.setWechatMomentsTitle(title);
        popupShareView.setTitle(title);
        String uri = String.format("%s?id=%s", getString( R.string.html_fm_fmoneyShare_detail), String.valueOf(investId));//
        popupShareView.setUrl(uri);
        popupShareView.setLogo(info.getInvestPhoto());
        popupShareView.showPopupWindow();
    }

    public void replys(String name,ConsumerCommentEntity info){
        mConsumerCommentEntity=info;
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        if (token.equals("")) {
            UISKipUtils.startLoginActivity(DiscoverDetailsActivity.this);
            return;
        }
        dialog = null;
        dialog = new KeyMapDailog(String.format("回复:%s",name), DiscoverDetailsActivity.this);
        dialog.show(getSupportFragmentManager(), "回复评论");
    }
    private float progressPercentage(float max, float min) {
        // TODO Auto-generated method stub
        float percentage = min / max;
        float progress = percentage * 360;
        return progress;

    }

    @Override
    public void sendBack(String inputText) {
        dialog.dismiss();
        consumerContentRequest(inputText);
    }

    public static abstract interface OnClickListener {
        public abstract void onClick(); //单击事件处理接口
    }

    OnClickListener listener = null;   //监听器类对象

    //实现这个View的监听器
    public void setOnClickListener(OnClickListener listener) {

        this.listener = listener;   //引用监听器类对象,在这里可以使用监听器类的对象

    }

    @Override
    public void onReload() {

    }

    @Override
    public void loadMore() {

    }

    final Handler handler = new Handler(){

        public void handleMessage(Message msg){         // handle message
            switch (msg.what) {
                case 1:
                    recLen--;

                    if(recLen > 0){
                        Message message = handler.obtainMessage(1);
                        handler.sendMessageDelayed(message, 1000);      // send message
                    }else{
                        tvPrompt.setVisibility(View.GONE);
                    }
            }

            super.handleMessage(msg);
        }
    };

    private void RequestTop(){
        svArr.post(new Runnable() {

            @Override
            public void run() {
                svArr.post(new Runnable() {
                    public void run() {
                        // 滚动至顶部
                        svArr.fullScroll(ScrollView.FOCUS_UP);
                    }
                });
            }
        });
    }

}

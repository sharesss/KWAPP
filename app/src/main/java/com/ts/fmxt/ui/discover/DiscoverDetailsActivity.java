package com.ts.fmxt.ui.discover;/**
 * Created by A1 on 2017/8/1.
 */

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.base.activity.FMBaseScrollActivityV2;
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
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.UISKipUtils;
import utils.helper.ToastHelper;
import widget.FMNoScrollListView;
import widget.Share.PopupShareView;

/**
 * created by kp at 2017/8/1
 * 发现详情
 */
public class DiscoverDetailsActivity extends FMBaseScrollActivityV2 implements View.OnClickListener, KeyMapDailog.SendBackListener {
    private int investId;
    //    private FMNetImageView ivImage;
    private TextView tvWorth, tvNoworth;
    private LinearLayout llTemp, llCollection;
    //    private ProgressBar pbIndex, pbGreenindex;
    private ConsumerEntity info;

    private RedCircleBar ivRedCirclebar;
//    private FlowLayout flow_layout;

    private FMNoScrollListView refresh_lv, reviews_lv, lv_result;
    //    private ItemBpAdapter adapter;
//    private BpresultAdapter mBpresultAdapter;
//    private CommentAdapter mCommentAdapter;
    private TextView tvAllReviews, tvWorthThrowing, tvNoWorthThrowing;
    private ConsumerCommentEntity mConsumerCommentEntity = null;
    private KeyMapDailog dialog;
    //    private ArrayList arr;
    private int type = 0;//请求的评论类型0是全部，1是值得投，2是不值得投

    private TextView tvCollection, tvWithTheVote, tvBpresult, tvResult, tvPrompt;
    private boolean isCollect;
    private ScrollView svArr;

    private int recLen = 3;
    private int types;
    private int istype;
    public RecyclerViewAdapter adapter;
    ArrayList<BaseViewItem> list;
    RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_details);
        investId = getIntent().getIntExtra("id", -1);
        types = getIntent().getIntExtra("type", -1);

        initView();
    }

    private void initView() {
        list = new ArrayList<BaseViewItem>();
        adapter = new RecyclerViewAdapter(this, list);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        LinearLayoutManager wrapContentLinearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(wrapContentLinearLayoutManager);
        recyclerView.setAdapter(adapter);


        //顶部UI
//        ivImage = (FMNetImageView) findViewById(R.id.iv_image);
//        tvBrandName = (TextView) findViewById(R.id.tv_brand_name);
//        tvBrandDetails = (TextView) findViewById(R.id.tv_brand_details);
//        pbIndex = (ProgressBar) findViewById(R.id.pb_index);
//        pbGreenindex = (ProgressBar) findViewById(R.id.pb_greenindex);
//        tvIndex = (TextView) findViewById(R.id.tv_index);
        findViewById(R.id.iv_share).setOnClickListener(this);
        findViewById(R.id.btn_finish).setOnClickListener(this);
        tvPrompt = (TextView) findViewById(R.id.tv_prompt);
        Message message = handler.obtainMessage(1);     // Message
        handler.sendMessageDelayed(message, 1000);
        //饼图UI
//        ivCirclebar = (CircleBar) findViewById(R.id.iv_circlebar);
//        ivRedCirclebar = (RedCircleBar) findViewById(R.id.iv_redcirclebar);
//        tvWorth = (TextView) findViewById(R.id.tv_worth);
//        tvNoworth = (TextView) findViewById(R.id.tv_noworth);
        DiscoverDetailsRequest();//顶部的数据获取
        InvestBPListRequest();
        CommentRequest(type);

//        //12项BP
//        refresh_lv = (FMNoScrollListView) findViewById(R.id.lv_bp_item);
//        llTemp = (LinearLayout) findViewById(R.id.ll_temp2);
//        flow_layout = (FlowLayout) findViewById(R.id.flow_layout);//标签布局
//        tvBpresult = (TextView) findViewById(R.id.tv_bpresult);
//        tvResult = (TextView) findViewById(R.id.tv_result);
//        tvBpresult.setOnClickListener(this);
//        lv_result = (FMNoScrollListView) findViewById(R.id.lv_result);
//        //评论
//        tvAllReviews = (TextView) findViewById(R.id.tv_all_reviews);
//        tvWorthThrowing = (TextView) findViewById(R.id.tv_worth_throwing);
//        tvNoWorthThrowing = (TextView) findViewById(R.id.tv_no_worth_throwing);
//        reviews_lv = (FMNoScrollListView) findViewById(R.id.lv_comment);
//        findViewById(R.id.tv_write_comment).setOnClickListener(this);
//        tvAllReviews.setOnClickListener(this);
//        tvWorthThrowing.setOnClickListener(this);
//        tvNoWorthThrowing.setOnClickListener(this);
//
//        //底部两个按钮
        llCollection = (LinearLayout) findViewById(R.id.ll_collection);
        tvCollection = (TextView) findViewById(R.id.tv_collection);
        tvWithTheVote = (TextView) findViewById(R.id.tv_with_the_vote);
        tvWithTheVote.setOnClickListener(this);
        llCollection.setOnClickListener(this);
        findViewById(R.id.tv_top).setOnClickListener(this);
    }

    private void formatData(ConsumerEntity info) {
        if (info == null) {
            return;
        }
        DiscoverHeadItem discoverHeadItem = new DiscoverHeadItem(info);
        list.add(0, discoverHeadItem);
        DiscoverCircleItem discoverCircleItem = new DiscoverCircleItem(info);
        list.add(1, discoverCircleItem);


        /**
         * 这里可以添加各种Item,参照以上代码
         */
        adapter.notifyDataSetChanged();

//        ivCirclebar.startCustomAnimation();
//        float progress = progressPercentage(info.getVoteNum(), info.getDokels());
//        float percentage = num(info.getVoteNum(), info.getDokels());
//        int progres = Math.round(percentage);
//        ivCirclebar.setText(String.valueOf(progres));//中间的数字
//        ivCirclebar.setSweepAngle(progress);//进度
//        ivRedCirclebar.startCustomAnimation();
//
//        ivRedCirclebar.startCustomAnimation();
//        float inxe = progressPercentage(info.getVoteNum(), info.getNotDokels());
//        float percen = num(info.getVoteNum(), info.getNotDokels());
//        int progre = Math.round(percen);
//        ivRedCirclebar.setText(String.valueOf(progre));//中间的数字
//        ivRedCirclebar.setSweepAngle(inxe);//进度
//        if (info.getIsVote() == 0) {
//            findViewById(R.id.ll_dokels).setOnClickListener(this);
//            findViewById(R.id.ll_notdokels).setOnClickListener(this);
//        } else if (info.getIsVote() == 1) {
//            tvWorth.setBackground(getResources().getDrawable(R.drawable.bg_gray_circle));
//            tvWorth.setTextColor(getResources().getColor(R.color.gray));
//            tvNoworth.setBackground(getResources().getDrawable(R.drawable.bg_gray_circle));
//            tvNoworth.setTextColor(getResources().getColor(R.color.gray));
//            type = 1;
//        } else if (info.getIsVote() == 2) {
//            tvWorth.setBackground(getResources().getDrawable(R.drawable.bg_gray_circle));
//            tvWorth.setTextColor(getResources().getColor(R.color.gray));
//            tvNoworth.setBackground(getResources().getDrawable(R.drawable.bg_gray_circle));
//            tvNoworth.setTextColor(getResources().getColor(R.color.gray));
//            type = 2;
//        }
//
//        Drawable sexDrawble = getResources().getDrawable(info.getIsCollect() == 1 ? R.mipmap.card_detail_s : R.mipmap.card_detail_n);
//        sexDrawble.setBounds(0, 0, sexDrawble.getMinimumWidth(), sexDrawble.getMinimumHeight());
//        tvCollection.setCompoundDrawables(sexDrawble, null, null, null);
//        if (info.getIsCollect() == 1) {
//            isCollect = true;
//        } else {
//            isCollect = false;
//        }
    }

    DiscoverLabelItem discoverLabelItem;

    private void formatDPData(ArrayList<InvestBPListEntity> arr, int investId) {
        if (arr.size() == 0) {
            if (discoverLabelItem != null) {
                list.remove(discoverLabelItem);
            }
//            flow_layout.setVisibility(View.GONE);
            return;
        }
        DiscoverLabelItem.CallBack callBack = new DiscoverLabelItem.CallBack() {
            @Override
            public void onitem(int postion) {
                int index = list.indexOf(discoverLabelItem);
                recyclerView.smoothScrollToPosition(index + postion + 2);
            }
        };
        int index = -1;
        if (discoverLabelItem != null) {
            index = list.indexOf(discoverLabelItem);
        }
        if (index > -1) {
            DiscoverLabelItem labelItem = new DiscoverLabelItem(arr, callBack, DiscoverDetailsActivity.this);
            list.set(index, labelItem);
            discoverLabelItem = labelItem;
        } else {
            discoverLabelItem = new DiscoverLabelItem(arr, callBack, DiscoverDetailsActivity.this);
            list.add(discoverLabelItem);
        }

        for (InvestBPListEntity entity : arr) {
            DisBPItem disBPItem = new DisBPItem(entity, investId);
            list.add(disBPItem);
        }
    }

    @Override
    public void onClick(View v) {
        SharedPreferences sharedPreferences = getSharedPreferences("user",
                MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        switch (v.getId()) {
            case R.id.btn_finish:
                finish();
                break;
            case R.id.iv_share:
                if (token.equals("")) {
                    UISKipUtils.startLoginActivity(DiscoverDetailsActivity.this);
                    return;
                }
                showShareDialog();
                break;
//            case R.id.tv_all_reviews:
//                if (token.equals("")) {
//                    UISKipUtils.startLoginActivity(DiscoverDetailsActivity.this);
//                    return;
//                }
//                tvAllReviews.setTextColor(this.getResources().getColor(R.color.orange));
//                tvWorthThrowing.setTextColor(this.getResources().getColor(R.color.black));
//                tvNoWorthThrowing.setTextColor(this.getResources().getColor(R.color.black));
//                istype = 0;
//                CommentRequest(istype);
//                break;
//            case R.id.tv_worth_throwing:
//                if (token.equals("")) {
//                    UISKipUtils.startLoginActivity(DiscoverDetailsActivity.this);
//                    return;
//                }
//                tvAllReviews.setTextColor(this.getResources().getColor(R.color.black));
//                tvWorthThrowing.setTextColor(this.getResources().getColor(R.color.orange));
//                tvNoWorthThrowing.setTextColor(this.getResources().getColor(R.color.black));
//                istype = 1;
//                CommentRequest(istype);
//                break;
//            case R.id.tv_no_worth_throwing:
//                if (token.equals("")) {
//                    UISKipUtils.startLoginActivity(DiscoverDetailsActivity.this);
//                    return;
//                }
//                tvAllReviews.setTextColor(this.getResources().getColor(R.color.black));
//                tvWorthThrowing.setTextColor(this.getResources().getColor(R.color.black));
//                tvNoWorthThrowing.setTextColor(this.getResources().getColor(R.color.orange));
//                istype = 2;
//                CommentRequest(istype);
//                break;
//            case R.id.tv_write_comment:
//                if (token.equals("")) {
//                    UISKipUtils.startLoginActivity(DiscoverDetailsActivity.this);
//                    return;
//                }
//
//                dialog = new KeyMapDailog("评论是疯蜜范的最大动力", DiscoverDetailsActivity.this);
//                dialog.show(getSupportFragmentManager(), "评论");
//                break;
            case R.id.ll_collection:
                if (token.equals("")) {
                    UISKipUtils.startLoginActivity(DiscoverDetailsActivity.this);
                    return;
                }
                if (isCollect) {
                    collectionRequest(0);
                    isCollect = false;
                } else {
                    collectionRequest(1);
                    isCollect = true;
                }
                break;
//            case R.id.tv_bpresult:
//                tvBpresult.setVisibility(View.GONE);
//                tvResult.setVisibility(View.VISIBLE);
//                InvestBPListRequest();
//                break;
            case R.id.tv_top:
                RequestTop();
                break;
            case R.id.tv_with_the_vote:
                if (token.equals("")) {
                    UISKipUtils.startLoginActivity(DiscoverDetailsActivity.this);
                    return;
                }
                UISKipUtils.startProjectReturnActivity(DiscoverDetailsActivity.this, investId);
                break;
            case R.id.ll_dokels://值得投
                if (token.equals("")) {
                    UISKipUtils.startLoginActivity(DiscoverDetailsActivity.this);
                    return;
                }
                type = 1;
                IsWorthRequest(type);
                break;
            case R.id.ll_notdokels://不值得投
                if (token.equals("")) {
                    UISKipUtils.startLoginActivity(DiscoverDetailsActivity.this);
                    return;
                }
                type = 2;
                IsWorthRequest(type);
                break;
        }
    }

    private void DiscoverDetailsRequest() {
        SharedPreferences sharedPreferences = getSharedPreferences("user",
                MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
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
    public void InvestBPListRequest() {
        SharedPreferences sharedPreferences = getSharedPreferences("user",
                MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
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
//                                        TableList tableList = new TableList();
                                        JSONArray array = js.optJSONArray("projectBPs");
                                        ArrayList<InvestBPListEntity> arr = new ArrayList<InvestBPListEntity>();
                                        for (int i = 0; i < array.length(); i++) {
                                            arr.add(new InvestBPListEntity(array.getJSONObject(i)));
                                        }
                                        if (discoverLabelItem != null && discoverLabelItem.getCont() > 1) {
                                            for (InvestBPListEntity entity : arr) {
                                                DisBPresultItem disBPItem = new DisBPresultItem(entity);
                                                list.add(disBPItem);
                                            }
//                                            mBpresultAdapter = new BpresultAdapter(DiscoverDetailsActivity.this, arr);
//                                            lv_result.setAdapter(mBpresultAdapter);
                                        } else
                                            formatDPData(arr, investId);
//                                        adapter = new ItemBpAdapter(DiscoverDetailsActivity.this, tableList.getArrayList(), investId);
//                                        refresh_lv.setAdapter(adapter);

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

    DiscoverCommentItem discoverCommentItem;

    //评论
    public void CommentRequest(final int type) {
        SharedPreferences sharedPreferences = getSharedPreferences("user",
                MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
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
                            int totalNum = 0;//总评
                            int desre = 0;//值投
                            int bedesre = 0;//不值投
                            totalNum = js.optInt("totalNum");
                            desre = js.optInt("desre");
                            bedesre = js.optInt("bedesre");
                            int index = -1;
                            if (discoverCommentItem != null) {
                                index = list.indexOf(discoverLabelItem);
                            }
                            if (index > -1) {
                                DiscoverCommentItem item = new DiscoverCommentItem(totalNum, desre, bedesre, DiscoverDetailsActivity.this);
                                list.set(index, item);
                                discoverCommentItem = item;
                            } else {
                                discoverCommentItem = new DiscoverCommentItem(totalNum, desre, bedesre, DiscoverDetailsActivity.this);
                                list.add(discoverCommentItem);
                            }


                            if (!js.isNull("statsMsg")) {
                                JSONObject json = js.optJSONObject("statsMsg");
                                String stats = json.getString("stats");
                                String msg = json.getString("msg");
                                if (stats.equals("1")) {
                                    if (!js.isNull("comments")) {
                                        ArrayList<ConsumerCommentEntity> entities = new ArrayList<ConsumerCommentEntity>();
                                        JSONArray array = js.optJSONArray("comments");
                                        for (int i = 0; i < array.length(); i++) {
                                            entities.add(new ConsumerCommentEntity(array.getJSONObject(i)));
                                        }
                                        addComment(entities);
//                                        mCommentAdapter = new CommentAdapter(DiscoverDetailsActivity.this, tableList.getArrayList(), type);
//                                        reviews_lv.setAdapter(mCommentAdapter);
//                                        mCommentAdapter.notifyDataSetChanged();
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

    ArrayList<BaseViewItem> listcomment = new ArrayList<BaseViewItem>();

    private void addComment(ArrayList<ConsumerCommentEntity> entities) {
        listcomment.clear();
        for (ConsumerCommentEntity entity : entities) {
            DisCommentItem disCommentItem = new DisCommentItem(entity);
            listcomment.add(disCommentItem);
        }
        if (list.indexOf(listcomment) == -1) {
            list.addAll(listcomment);
        }
        adapter.notifyDataSetChanged();
    }

    //发表评论，回复评论
    private void consumerContentRequest(String inputText) {
        SharedPreferences sharedPreferences = getSharedPreferences("user",
                MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
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
                                    CommentRequest(type);
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
    private void collectionRequest(final int enabled) {
        SharedPreferences sharedPreferences = getSharedPreferences("user",
                MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("investId", String.valueOf(investId));
        staff.put("enabled", String.valueOf(enabled));
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
                                    if (enabled == 1) {
                                        Drawable sexDrawble = getResources().getDrawable(R.mipmap.card_detail_s);
                                        sexDrawble.setBounds(0, 0, sexDrawble.getMinimumWidth(), sexDrawble.getMinimumHeight());
                                        tvCollection.setCompoundDrawables(sexDrawble, null, null, null);
                                        ToastHelper.toastMessage(DiscoverDetailsActivity.this, "收藏成功");
                                    } else {
                                        Drawable sexDrawble = getResources().getDrawable(R.mipmap.card_detail_n);
                                        sexDrawble.setBounds(0, 0, sexDrawble.getMinimumWidth(), sexDrawble.getMinimumHeight());
                                        tvCollection.setCompoundDrawables(sexDrawble, null, null, null);
                                        ToastHelper.toastMessage(DiscoverDetailsActivity.this, "取消收藏成功");
                                    }

                                } else {
                                    if (enabled == 1) {
                                        ToastHelper.toastMessage(DiscoverDetailsActivity.this, "收藏失败");
                                    } else {
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

    //是否值得投
    private void IsWorthRequest(int voteType) {
        SharedPreferences sharedPreferences = getSharedPreferences("user",
                MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("investId", String.valueOf(investId));
        staff.put("voteType", String.valueOf(voteType));
        staff.put("tokenId", String.valueOf(token));

        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.SAVEINVESTPROJECTVOTE,
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
                                    tvWorth.setBackground(getResources().getDrawable(R.drawable.bg_gray_circle));
                                    tvWorth.setTextColor(getResources().getColor(R.color.gray));
                                    tvNoworth.setBackground(getResources().getDrawable(R.drawable.bg_gray_circle));
                                    tvNoworth.setTextColor(getResources().getColor(R.color.gray));
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

    //分享对话框
    private void showShareDialog() {
        if (info == null) return;
        String title = String.format(info.getInvestName());
        PopupShareView popupShareView = new PopupShareView(DiscoverDetailsActivity.this);
        popupShareView.setContent(info.getInvestDeion());

        popupShareView.setWechatMomentsTitle(title);
        popupShareView.setTitle(title);
        String uri = String.format("%s?id=%s", getString(R.string.html_fm_fmoneyShare_detail), String.valueOf(investId));//
        popupShareView.setUrl(uri);
        popupShareView.setLogo(info.getInvestPhoto());
        popupShareView.showPopupWindow();
    }

    public void replys(String name, ConsumerCommentEntity info) {
        mConsumerCommentEntity = info;
        SharedPreferences sharedPreferences = getSharedPreferences("user",
                MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        if (token.equals("")) {
            UISKipUtils.startLoginActivity(DiscoverDetailsActivity.this);
            return;
        }
        dialog = null;
        dialog = new KeyMapDailog(String.format("回复:%s", name), DiscoverDetailsActivity.this);
        dialog.show(getSupportFragmentManager(), "回复评论");
    }

    private float progressPercentage(float max, float min) {
        // TODO Auto-generated method stub
        float percentage = min / max;
        float progress = percentage * 360;
        return progress;

    }

    private float num(float max, float min) {
        // TODO Auto-generated method stub
        float percentage = min / max;
        float progress = percentage * 100;
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

    final Handler handler = new Handler() {

        public void handleMessage(Message msg) {         // handle message
            switch (msg.what) {
                case 1:
                    recLen--;

                    if (recLen > 0) {
                        Message message = handler.obtainMessage(1);
                        handler.sendMessageDelayed(message, 1000);
//                        if (types != -1) {
//                            RequestBoot();
//                        } else {
//                            RequestTop();
//                        }
                    } else {
                        tvPrompt.setVisibility(View.GONE);
                    }
            }

            super.handleMessage(msg);
        }
    };

    private void RequestTop() {
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

    private void RequestBoot() {
        svArr.post(new Runnable() {

            @Override
            public void run() {
                svArr.post(new Runnable() {
                    public void run() {
                        // 滚动至顶部
                        svArr.fullScroll(ScrollView.FOCUS_DOWN);
                    }
                });
            }
        });
    }
}

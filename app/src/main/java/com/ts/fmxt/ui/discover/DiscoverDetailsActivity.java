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
import android.text.TextUtils;
import android.view.View;
import android.widget.LinearLayout;
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
import widget.popup.BaseDoubleEventPopup;
import widget.popup.PopupObject;
import widget.popup.dialog.MessageContentDialog;

/**
 * created by kp at 2017/8/1
 * 发现详情
 */
public class DiscoverDetailsActivity extends FMBaseScrollActivityV2 implements View.OnClickListener, KeyMapDailog.SendBackListener {
    private int investId;
    private TextView tvWorth, tvNoworth;
    private LinearLayout llTemp, llCollection;
    private ConsumerEntity info;

    private RedCircleBar ivRedCirclebar;

    private FMNoScrollListView refresh_lv, reviews_lv, lv_result;
    private ConsumerCommentEntity mConsumerCommentEntity = null;
    private KeyMapDailog dialog;
    private int type = 0;//请求的评论类型0是全部，1是值得投，2是不值得投

    private TextView tvCollection, tvWithTheVote, tvBpresult, tvResult;
    private boolean isCollect;

    private int recLen = 3;
    private int types;
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
        findViewById(R.id.iv_share).setOnClickListener(this);
        findViewById(R.id.btn_finish).setOnClickListener(this);
        findViewById(R.id.tv_report).setOnClickListener(this);

        Message message = handler.obtainMessage(1);     // Message
        handler.sendMessageDelayed(message, 3000);
        //饼图UI
        DiscoverDetailsRequest();//顶部的数据获取
        InvestBPListRequest(false);
        CommentRequest(type);

//        //底部两个按钮
        llCollection = (LinearLayout) findViewById(R.id.ll_collection);
        tvCollection = (TextView) findViewById(R.id.tv_collection);
        tvWithTheVote = (TextView) findViewById(R.id.tv_with_the_vote);
        tvWithTheVote.setOnClickListener(this);
        llCollection.setOnClickListener(this);
        findViewById(R.id.tv_top).setOnClickListener(this);
    }

    ArrayList<BaseViewItem> headlist = new ArrayList<BaseViewItem>();

    private void formatData(ConsumerEntity info) {
        if (info == null) {
            return;
        }
        DiscoverHeadItem discoverHeadItem = new DiscoverHeadItem(info);
        headlist.add(0, discoverHeadItem);
        DiscoverCircleItem discoverCircleItem = new DiscoverCircleItem(info, DiscoverDetailsActivity.this, investId);
        list.addAll(0, headlist);
        if (listcomment.isEmpty()) {
            list.add(discoverCircleItem);
        } else {
            list.add(list.size() - listcomment.size(), discoverCircleItem);
        }


        /**
         * 这里可以添加各种Item,参照以上代码
         */
        adapter.notifyDataSetChanged();


        Drawable sexDrawble = getResources().getDrawable(info.getIsCollect() == 1 ? R.mipmap.card_detail_s : R.mipmap.card_detail_n);
        sexDrawble.setBounds(0, 0, sexDrawble.getMinimumWidth(), sexDrawble.getMinimumHeight());
        tvCollection.setCompoundDrawables(sexDrawble, null, null, null);
        if(info.getIsCollect() == 1){
            isCollect=true;
        }else{
            isCollect=false;
        }

    }

    ArrayList<BaseViewItem> labellist = new ArrayList<>();
    ArrayList<BaseViewItem> labelBPlist = new ArrayList<>();

    private int formatDPData(ArrayList<InvestBPListEntity> arr, int investId) {
        if (arr.size() == 0) {
            list.removeAll(labellist);
            return 0;
        }
        DiscoverLabelItem.CallBack callBack = new DiscoverLabelItem.CallBack() {
            @Override
            public void onitem(int postion) {
                int index = headlist.size() + 1;
                recyclerView.smoothScrollToPosition(index + postion);
            }
        };
        list.removeAll(labellist);
        labellist.clear();
        DiscoverLabelItem labelItem = new DiscoverLabelItem(arr, callBack);
        labellist.add(labelItem);
        int cont = 0;
        for (InvestBPListEntity entity : arr) {
            DisBPItem disBPItem = new DisBPItem(entity, investId);
            if (entity.isScore() == 1) {
                cont++;
                TextView tvPrompt = (TextView) findViewById(R.id.tv_prompt);
                tvPrompt.setVisibility(View.GONE);
            }
            labellist.add(disBPItem);
        }
        DisBPLabelItem disBPLabelItem = new DisBPLabelItem(cont, DiscoverDetailsActivity.this);
        labellist.add(disBPLabelItem);
        if (headlist.isEmpty()) {
            list.addAll(0, labellist);
        } else {
            list.addAll(headlist.size(), labellist);
        }
        adapter.notifyDataSetChanged();
        return cont;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_finish:
                finish();
                break;
            case R.id.iv_share:
//                if (!checkLogin()) {
//                    return;
//                }
                showShareDialog(info);
                break;
            case R.id.tv_report://举报
                if (!checkLogin()) {
                    MessageContentDialog mPopupDialogWidget = new MessageContentDialog(DiscoverDetailsActivity.this);
                    mPopupDialogWidget.setMessage("您还未登录，是否去登录？");
                    mPopupDialogWidget.setOnEventClickListener(new BaseDoubleEventPopup.onEventClickListener() {

                        @Override
                        public void onEventClick(PopupObject obj) {
                            if (obj.getWhat() == 1)
                                UISKipUtils.startLoginActivity(DiscoverDetailsActivity.this);
                        }
                    });
                    mPopupDialogWidget.showPopupWindow();

                    return;
                }
                UISKipUtils.startReportActivity(DiscoverDetailsActivity.this,investId);
                break;
            case R.id.ll_collection:
                if (!checkLogin()) {
                    MessageContentDialog mPopupDialogWidget = new MessageContentDialog(DiscoverDetailsActivity.this);
                    mPopupDialogWidget.setMessage("您还未登录，是否去登录？");
                    mPopupDialogWidget.setOnEventClickListener(new BaseDoubleEventPopup.onEventClickListener() {

                        @Override
                        public void onEventClick(PopupObject obj) {
                            if (obj.getWhat() == 1)
                                UISKipUtils.startLoginActivity(DiscoverDetailsActivity.this);
                        }
                    });
                    mPopupDialogWidget.showPopupWindow();

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
            case R.id.tv_top:
                RequestTop();
                break;
            case R.id.tv_with_the_vote:
                SharedPreferences sharedPreferences= getSharedPreferences("user",
                        MODE_PRIVATE);
                String token=sharedPreferences.getString("token", "");
                int isTruenameAuthen=sharedPreferences.getInt("isTruenameAuthen", -1);
                if (!checkLogin()) {
                    MessageContentDialog mPopupDialogWidget = new MessageContentDialog(DiscoverDetailsActivity.this);
                    mPopupDialogWidget.setMessage("您还未登录，是否去登录？");
                    mPopupDialogWidget.setOnEventClickListener(new BaseDoubleEventPopup.onEventClickListener() {

                        @Override
                        public void onEventClick(PopupObject obj) {
                            if (obj.getWhat() == 1)
                                UISKipUtils.startLoginActivity(DiscoverDetailsActivity.this);
                        }
                    });
                    mPopupDialogWidget.showPopupWindow();

                    return;
                }
                if(isTruenameAuthen==0){
                    MessageContentDialog mPopupDialogWidget = new MessageContentDialog(DiscoverDetailsActivity.this);
                    mPopupDialogWidget.setMessage("您还实名认证，是否去认证？");
                    mPopupDialogWidget.setOnEventClickListener(new BaseDoubleEventPopup.onEventClickListener() {

                        @Override
                        public void onEventClick(PopupObject obj) {
                            if (obj.getWhat() == 1)
                                UISKipUtils.startRealNameActivity(DiscoverDetailsActivity.this);
                        }
                    });
                    mPopupDialogWidget.showPopupWindow();
                    return;
                }
                UISKipUtils.startProjectReturnActivity(DiscoverDetailsActivity.this, investId);
                break;
            case R.id.ll_dokels://值得投
                if (!checkLogin()) {
                    return;
                }
                type = 1;
                IsWorthRequest(type);
                break;
            case R.id.ll_notdokels://不值得投
                if (!checkLogin()) {
                    return;
                }
                type = 2;
                IsWorthRequest(type);
                break;
        }
    }

    private void DiscoverDetailsRequest() {
//        if (!checkLogin()) {
//            return;
//        }
        SharedPreferences sharedPreferences = getSharedPreferences("user",
                MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("investId", String.valueOf(investId));
        if(token!=null){
            staff.put("tokenId", String.valueOf(token));
        }
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

    boolean oncheckBP = false;

    //12项BP
    public void InvestBPListRequest(final boolean oncheckBP) {//
        this.oncheckBP = oncheckBP;
//        if (!checkLogin()) {
//            return;
//        }
        SharedPreferences sharedPreferences = getSharedPreferences("user",
                MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        final Map<String, String> staff = new HashMap<String, String>();
        staff.put("investId", String.valueOf(investId));
        if(token!=null){
            staff.put("tokenId", String.valueOf(token));
        }
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.INVESTBPLIST,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
//                        llTemp.setVisibility(View.GONE);
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
                                        int cont = formatDPData(arr, investId);
                                        labelBPlist.clear();
                                        if (oncheckBP && cont > 1) {
                                            for (InvestBPListEntity entity : arr) {
                                                DisBPresultItem disBPItem = new DisBPresultItem(entity);
                                                labelBPlist.add(disBPItem);
                                            }
                                            int index = labellist.size() + headlist.size();
                                            list.addAll(index, labelBPlist);
                                            adapter.notifyDataSetChanged();
                                            if (oncheckBP) {
                                                recyclerView.smoothScrollToPosition(index + 1);
                                            }
                                        }

                                    }
                                } else {
//                                    llTemp.setVisibility(View.GONE);
                                    ToastHelper.toastMessage(DiscoverDetailsActivity.this, msg);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
//                            llTemp.setVisibility(View.GONE);
                        }
                    }
                }, staff
        );
    }


    //评论
    public void CommentRequest(final int type) {
//        if (!checkLogin()) {
//            return;
//        }
        SharedPreferences sharedPreferences = getSharedPreferences("user",
                MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("investId", String.valueOf(investId));
        staff.put("commentType", String.valueOf(type));
        if(token!=null){
            staff.put("tokenId", String.valueOf(token));
        }
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
                            list.removeAll(listcomment);
                            listcomment.clear();
                            DiscoverCommentItem discoverCommentItem = new DiscoverCommentItem(totalNum, desre, bedesre, DiscoverDetailsActivity.this);
                            listcomment.add(discoverCommentItem);
                            if (!js.isNull("statsMsg")) {
                                JSONObject json = js.optJSONObject("statsMsg");
                                String stats = json.getString("stats");
                                String msg = json.getString("msg");
                                if (stats.equals("1")) {
                                    if (!js.isNull("comments")) {
                                        JSONArray array = js.optJSONArray("comments");
                                        for (int i = 0; i < array.length(); i++) {
                                            DisCommentItem disCommentItem = new DisCommentItem(new ConsumerCommentEntity(array.getJSONObject(i)), DiscoverDetailsActivity.this, type);
                                            listcomment.add(disCommentItem);
                                        }
                                    }
                                } else {
                                    ToastHelper.toastMessage(DiscoverDetailsActivity.this, msg);
                                }
                            }
                            list.addAll(listcomment);
                            adapter.notifyDataSetChanged();
                            if (types == 1) {
                                RequestBoot();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, staff
        );
    }

    ArrayList<BaseViewItem> listcomment = new ArrayList<BaseViewItem>();


    //发表评论，回复评论
    private void consumerContentRequest(String inputText, final ConsumerCommentEntity mConsumerCommentEntity, final int type, int investId) {
        if (!checkLogin()) {
            return;
        }
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
        if (!checkLogin()) {
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("user",
                MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
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
        if (!checkLogin()) {
            return;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("user",
                MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
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
    private void showShareDialog(ConsumerEntity info) {
        if (info == null) return;
        String title = info.getInvestName();
        PopupShareView popupShareView = new PopupShareView(DiscoverDetailsActivity.this);
        popupShareView.setContent(getString(R.string.share_consumenr_context));
        popupShareView.setWechatMomentsTitle(title);
        popupShareView.setTitle(title);
        String uri = String.format("%s?id=%d", getString(R.string.html_fm_fmoneyShare_detail), investId);//
        popupShareView.setUrl(uri);
        popupShareView.setLogo(info.getInvestPhoto());
        popupShareView.showPopupWindow();
    }

    private String token;

    public boolean checkLogin() {
        if (!TextUtils.isEmpty(token)) {
            return true;
        }
        SharedPreferences sharedPreferences = getSharedPreferences("user",
                MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        if (TextUtils.isEmpty(token)) {
            UISKipUtils.startLoginActivity(DiscoverDetailsActivity.this);
            return false;
        }
        return true;
    }

    public void replys(String name, ConsumerCommentEntity info) {
        if (!checkLogin()) {
            return;
        }
        mConsumerCommentEntity = info;
        dialog = null;
        dialog = new KeyMapDailog(String.format("回复:%s", name), DiscoverDetailsActivity.this);
        dialog.show(getSupportFragmentManager(), "回复评论");
    }


    @Override
    public void sendBack(String inputText) {
        consumerContentRequest(inputText, mConsumerCommentEntity, type, investId);
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
                    TextView tvPrompt = (TextView) findViewById(R.id.tv_prompt);
                    tvPrompt.setVisibility(View.GONE);
            }
            super.handleMessage(msg);
        }
    };

    private void RequestTop() {
        recyclerView.smoothScrollToPosition(0);
    }

    private void RequestBoot() {
        recyclerView.smoothScrollToPosition(adapter.getItemCount() - 1);
    }
}

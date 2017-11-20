package com.ts.fmxt.ui.StockAuction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.ts.fmxt.FmxtApplication;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.ItemAdapter.CommentAdapter;
import com.ts.fmxt.ui.ItemAdapter.RankingAdapter;
import com.ts.fmxt.ui.base.activity.FMBaseScrollActivityV2;
import com.ts.fmxt.ui.discover.view.KeyMapDailog;
import com.ts.fmxt.ui.im.ChatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import http.data.ConsumerCommentEntity;
import http.data.FindStockEquityHomeEntity;
import http.data.TableList;
import http.data.WeiXinPayEntity;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.ReceiverUtils;
import utils.Tools;
import utils.UISKipUtils;
import utils.helper.ToastHelper;
import widget.FMNoScrollListView;
import widget.guidepage.GuideViewPagerAdapter;
import widget.image.CircleImageView;
import widget.image.FMNetImageView;
import widget.weixinpay.playUitls;

import static com.ts.fmxt.R.id.bt_comment;


/**
 * Created by kp on 2017/9/13.
 * 拍卖详情
 */

public class AuctionDetailsActivity extends FMBaseScrollActivityV2 implements ViewPager.OnPageChangeListener,View.OnClickListener , KeyMapDailog.SendBackListener,  ReceiverUtils.MessageReceiver{
    private int investId;//股票ID
    private LinearLayout ll_guida_button;
    private ViewPager viewpager;
    // 定义ViewPager适配器
    private GuideViewPagerAdapter vpAdapter;
    // 定义一个ArrayList来存放View
    private ArrayList<View> views;
    // 引导图片资源
    private FindStockEquityHomeEntity info;
    // 底部小点的图片
    private ImageView[] points;
    // 记录当前选中位置
    private int currentIndex;
    //当前索引位置以及上一个索引位置
    private int index = 0,preIndex = 0;
    private CircleImageView iv_portrait,iv_auction_result_portrait;
    private TextView tv_auction_type,tv_attention_number,tv_name,tv_isfounder,tv_founder,tv_isVfounder,tv_praise;
    private TextView tv_follow_up_project,tv_transfer_project,tv_auction_project,tv_starting_price,tv_fare_increase,tv_company_equity,tv_equity;
    private TextView tv_starting_prices,tv_round,tv_type,tv_names,tv_bonus,tv_stockDesc,tv_disclaimer,tv_transaction_price;
    private TextView tv_auction_failure,tv_auction_result_name;
    private TextView tv_auction_evaluation,tv_V_evaluation,tv_set_remind;
    private FMNoScrollListView lv_ranking,lv_comment;
    private LinearLayout ll_ranking_list,ll_auction_successful,ll_auction_result;
    private RankingAdapter mRankingAdapter;
    private CommentAdapter mCommentAdapter;
    private ConsumerCommentEntity mConsumerCommentEntity;
    private KeyMapDailog dialog;
    private WeiXinPayEntity entity;
    private IWXAPI api;
    private int type=0;
    //定时器，用于实现轮播
    private Timer timer;

    Handler mHandler  = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    index++;
                    if(index==info.getArr().size()){
                        index=0;
                    }
                    viewpager.setCurrentItem(index);
                    break;

            }
        }
    };
    @Override
    public void onMessage(int receiverType, Bundle bundle) {
        if (receiverType == ReceiverUtils.WX_PLAY) {
            findStockEquityHomeRequest();
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_details);
        ReceiverUtils.addReceiver(this);
        investId = getIntent().getIntExtra("id", -1);
        SharedPreferences share = getSharedPreferences("ImInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit(); //使处于可编辑状态
        editor.putInt("investId", investId);
        editor.commit();    //提交数据保存
        bindRefreshScrollAdapter(R.id.refresh_scroll, R.layout.auction_details_view, true);
        startRefreshState();
        api = WXAPIFactory.createWXAPI(this, FmxtApplication.APP_ID, true);
        //将应用appid注册到微信
        api.registerApp(FmxtApplication.APP_ID);
        initView();
    }

    @Override
    public void onReload() {
        CommentRequest(type);
        findStockEquityHomeRequest();
    }

    @Override
    public void loadMore() {
        findStockEquityHomeRequest();
    }

    private void initView(){
        findViewById(R.id.btn_finish).setOnClickListener(this);
        findViewById(bt_comment).setOnClickListener(this);
        findViewById(R.id.tv_auction_house).setOnClickListener(this);
        tv_auction_evaluation = (TextView) findViewById(R.id.tv_auction_evaluation);
        tv_auction_evaluation.setOnClickListener(this);
        tv_V_evaluation = (TextView) findViewById(R.id.tv_V_evaluation);
        tv_V_evaluation.setOnClickListener(this);
        viewpager = (ViewPager) findViewById(R.id.viewpager);
        ll_guida_button = (LinearLayout) findViewById(R.id.ll_guida_button);
        tv_auction_type = (TextView) findViewById(R.id.tv_auction_type);
        iv_portrait = (CircleImageView) findViewById(R.id.iv_portrait);
        iv_auction_result_portrait = (CircleImageView) findViewById(R.id.iv_auction_result_portrait);
        tv_attention_number = (TextView) findViewById(R.id.tv_attention_number);
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_isfounder= (TextView) findViewById(R.id.tv_isfounder);
        tv_founder= (TextView) findViewById(R.id.tv_founder);
        tv_isVfounder = (TextView) findViewById(R.id.tv_isVfounder);
        tv_follow_up_project= (TextView) findViewById(R.id.tv_follow_up_project);
        tv_transfer_project= (TextView) findViewById(R.id.tv_transfer_project);
        tv_auction_project= (TextView) findViewById(R.id.tv_auction_project);
        tv_starting_price = (TextView) findViewById(R.id.tv_starting_price);
        tv_fare_increase = (TextView) findViewById(R.id.tv_fare_increase);
        lv_ranking = (FMNoScrollListView) findViewById(R.id.lv_ranking);
        tv_company_equity = (TextView) findViewById(R.id.tv_company_equity);
        tv_equity = (TextView) findViewById(R.id.tv_equity);
        tv_starting_prices = (TextView) findViewById(R.id.tv_starting_prices);
        tv_round = (TextView) findViewById(R.id.tv_round);
        tv_type = (TextView) findViewById(R.id.tv_type);
        tv_names = (TextView) findViewById(R.id.tv_names);
        tv_bonus = (TextView) findViewById(R.id.tv_bonus);
        tv_stockDesc = (TextView) findViewById(R.id.tv_stockDesc);
        lv_comment = (FMNoScrollListView) findViewById(R.id.lv_comment);
        tv_disclaimer = (TextView) findViewById(R.id.tv_disclaimer);
        ll_ranking_list = (LinearLayout) findViewById(R.id.ll_ranking_list);
        ll_auction_result = (LinearLayout) findViewById(R.id.ll_auction_result);
        ll_auction_successful = (LinearLayout) findViewById(R.id.ll_auction_successful);
        tv_transaction_price = (TextView) findViewById(R.id.tv_transaction_price);
        tv_praise = (TextView) findViewById(R.id.tv_praise);
        tv_set_remind = (TextView) findViewById(R.id.tv_set_remind);
        tv_auction_failure= (TextView) findViewById(R.id.tv_auction_failure);
        tv_auction_result_name= (TextView) findViewById(R.id.tv_auction_result_name);
        tv_praise.setOnClickListener(this);
        tv_set_remind.setOnClickListener(this);
    }
    /**
     * 初始化数据
     */
    private void initData() {
        views = new ArrayList<View>();
        vpAdapter = new GuideViewPagerAdapter(views);
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        for (int i = 0; i < info.getArr().size(); i++) {
            FMNetImageView iv = new FMNetImageView(this);
            iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
            iv.setLayoutParams(mParams);
            iv.loadImage(info.getArr().get(i));
            views.add(iv);
        }
        info.getArr().clear();
        viewpager.setAdapter(vpAdapter);
        viewpager.setOnPageChangeListener(this);
        Long time  =  info.getAuctionStartTime()/1000-info.getCurrentTime()/1000;
        //未开始

        if(time < 0){
            tv_auction_type.setText("拍卖结束");
            tv_set_remind.setVisibility(View.GONE);
            if(!info.getForthoseName().equals("null")||!info.getForthoseHeadPic().equals("null")){
                tv_auction_failure.setVisibility(View.GONE);
                iv_auction_result_portrait.loadImage(info.getForthoseHeadPic());
                tv_auction_result_name.setText(info.getForthoseName());
            }else{
                tv_auction_failure.setVisibility(View.VISIBLE);
                ll_auction_successful.setVisibility(View.GONE);
                ll_ranking_list.setVisibility(View.GONE);
            }

        }else if(info.getAuctionState()==0){
            if(info.getCurrentTime() / 1000 < info.getAuctionStartTime() / 1000){
                tv_auction_type.setText("等待开拍");
                ll_ranking_list.setVisibility(View.GONE);
                ll_auction_result.setVisibility(View.GONE);
            }else if (time > 0){
                tv_auction_type.setText("出价竞拍");
                tv_set_remind.setVisibility(View.GONE);
                ll_auction_result.setVisibility(View.GONE);
                ll_ranking_list.setVisibility(View.GONE);
            }

        }

        tv_attention_number.setText(info.getAttentionNum()+"人关注");
        iv_portrait.loadImage(info.getHeadpic());
        tv_name.setText(info.getNickname());
        if( info.getIsinvestauthen()==1){
            tv_isfounder.setVisibility(View.VISIBLE);
        }else if(info.getIsinvestauthen()==2){
            tv_isVfounder.setVisibility(View.VISIBLE);
        }else{
            tv_isfounder.setVisibility(View.GONE);
            tv_isVfounder.setVisibility(View.GONE);
        }
        StringBuilder inf =  new StringBuilder().append(!info.getCompany().equals("")&&!info.getCompany().equals("null")  ? info.getCompany()+"/":"").append(!info.getPosition().equals("")&&!info.getPosition().equals("null")? info.getPosition()+"/":"");
        if(inf.length()>1){
            inf.delete(inf.length()-1, inf.length());
        }
        tv_founder.setText(inf);
        tv_follow_up_project.setText("跟投项目："+info.getFollowNum());
        tv_transfer_project.setText("转让项目："+info.getMakeOverNum());
        tv_auction_project.setText("竞拍项目："+info.getAuctionNum());
        if(info.getStartingPrice()<10000){
            tv_starting_price.setText("¥ "+info.getStartingPrice());
            tv_starting_prices.setText("¥ "+info.getStartingPrice());
        }else{
            double n = (double)info.getStartingPrice()/10000;
            DecimalFormat  df   = new DecimalFormat("######0.00");
            tv_starting_price.setText("¥ "+df.format(n)+"万");
            tv_starting_prices.setText("¥ "+df.format(n)+"万");
        }
        if(info.getPriceRisingRate()<10000){
            tv_fare_increase.setText("¥ "+info.getPriceRisingRate());
        }else{
            double n = (double)info.getPriceRisingRate()/10000;
            DecimalFormat  df   = new DecimalFormat("######0.00");
            tv_fare_increase.setText("¥ "+df.format(n)+"万");
        }
        mRankingAdapter = new RankingAdapter(this, info.getRankingarr());
        lv_ranking.setAdapter(mRankingAdapter);
        tv_company_equity.setText(info.getStockName());
        SharedPreferences share = getSharedPreferences("ImInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit(); //使处于可编辑状态
        editor.putString("title", info.getStockName());
        editor.putInt("stockId", info.getId());
        editor.putInt("attentionNumber",info.getAttentionNum());
        editor.putInt("startingPrice",info.getStartingPrice());
        editor.putInt("priceRisingRate",info.getPriceRisingRate());
        editor.putInt("isApply",info.getIsApply());
        editor.commit();    //提交数据保存
        tv_equity.setText(info.getStockSellRate()+"% ");

        tv_round.setText(info.getFinancingState());
        tv_type.setText(info.getIndustryName());
        tv_names.setText(info.getLeadAgency());
        if(info.getTransactionPrice()<10000){
            tv_transaction_price.setText("¥ "+info.getTransactionPrice());
        }else{
            double n = (double)info.getTransactionPrice()/10000;
            DecimalFormat  df   = new DecimalFormat("######0.00");
            tv_transaction_price.setText("¥ "+df.format(n)+"万");
        }
        if(info.getReturnWay()==1){
            tv_bonus.setText("定期分红");
        }else if(info.getReturnWay()==2){
            tv_bonus.setText("股权增值");
        }
        tv_stockDesc.setText(info.getStockDesc()+"");
        tv_disclaimer.setText(info.getDisclaimer());

        //未开始
        if (time < 0) {
            //竞拍成功
            tv_praise.setText("已结拍");
        } else if (info.getAuctionState() == 0) {
            if (info.getCurrentTime() / 1000 < info.getAuctionStartTime() / 1000) {
                if(info.getIsApply()==0){
                    tv_praise.setText("预交保证金");
                }else {
                    tv_praise.setText("等待开拍");
                }
            } else if (time > 0) {
                if(info.getIsApply()==0){
                    tv_praise.setText("预交保证金");
                }else{
                    tv_praise.setText("出价竞拍");
                }
            }
        }

        tv_set_remind.setText(info.getIsRemind()==0 ? "开启提醒":"提醒已开启");
        Drawable sexDrawble = getResources().getDrawable(tv_set_remind.getText().toString().equals("开启提醒") ? R.mipmap.stock_detail_icon_alarm_n : R.mipmap.stock_detail_icon_alarm_s);
        sexDrawble.setBounds(0, 0, sexDrawble.getMinimumWidth(), sexDrawble.getMinimumHeight());
        tv_set_remind.setCompoundDrawables(null, sexDrawble, null, null);
        timer = new Timer();//创建Timer对象
        //执行定时任务
        timer.schedule(new TimerTask() {
            public void run() {
                //首先判断是否需要轮播，是的话我们才发消息
                mHandler.sendEmptyMessage(1);
            }
        },5000,2000);//延迟5秒，每隔2秒发一次消息
        initPoint();
    }
    /**
     * 初始化底部小点
     */
    private void initPoint() {
        if(info.getArr().size()<0){
            return;
        }

        for(int i = 0;i<info.getArr().size();i++){
            SharedPreferences share = getSharedPreferences("ImInfo",MODE_PRIVATE);
            SharedPreferences.Editor editor = share.edit(); //使处于可编辑状态
            editor.putString("picture", info.getArr().get(0));
            editor.commit();    //提交数据保存
            ImageView imageview = new ImageView(this);
            imageview.setImageResource(R.drawable.point);//设置背景选择器
            imageview.setPadding(20,0,0,0);//设置每个按钮之间的间距
            //将按钮依次添加到RadioGroup中
            ll_guida_button.addView(imageview, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //默认选中第一个按钮，因为默认显示第一张图片
            ll_guida_button.getChildAt(0).setEnabled(false);
        }
    }


    /**
     * 设置当前的小点的位置
     */
    private void setCurDot(int positon) {
        if(ll_guida_button.getChildAt(positon)!=null){
            ll_guida_button.getChildAt(positon).setEnabled(false);//当前按钮选中
        }
        if(ll_guida_button.getChildAt(preIndex)!=null){
            ll_guida_button.getChildAt(preIndex).setEnabled(true);//上一个取消选中
            preIndex = positon;//当前位置变为上一个，继续下次轮播
        }
    }

    @Override
    public void onClick(View view) {
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        int isTruenameAuthen=sharedPreferences.getInt("isTruenameAuthen", -1);
        switch (view.getId()){
            case R.id.btn_finish:
                finish();
                break;
            case R.id.tv_auction_evaluation:
                tv_auction_evaluation.setTextColor(getResources().getColor(R.color.orange));
                tv_V_evaluation.setTextColor(getResources().getColor(R.color.black));
                CommentRequest(type);
                break;
            case R.id.tv_V_evaluation:
                tv_auction_evaluation.setTextColor(getResources().getColor(R.color.black));
                tv_V_evaluation.setTextColor(getResources().getColor(R.color.orange));
                CommentRequest(3);
                break;
            case R.id.bt_comment:
                if (token.equals("")) {
                                UISKipUtils.startLoginActivity(AuctionDetailsActivity.this);

                    return;
                }
                KeyMapDailog dialog = new KeyMapDailog("评论是我们的最大动力", this);
                dialog.show(getSupportFragmentManager(), "评论");
                break;
            case R.id.tv_auction_house://拍卖场
                if (token.equals("")) {
                    UISKipUtils.startLoginActivity(AuctionDetailsActivity.this);
                    return;
                }
//                if(!tv_auction_type.getText().toString().equals("拍卖结束")){
                    startActivity(new Intent(AuctionDetailsActivity.this, ChatActivity.class).putExtra("chatType", 3).
                            putExtra("userId", info.getChatRoomId()));
//                }else {
//                    ToastHelper.toastMessage(this,"拍卖已结束");
//                }

                break;
            case R.id.tv_set_remind://开启提现
                if (token.equals("")) {
                    UISKipUtils.startLoginActivity(AuctionDetailsActivity.this);
                    return;
                }
                if(tv_set_remind.getText().toString().equals("开启提醒")){
                    RemindRequest();
                }
                break;
            case R.id.tv_praise://预交保证金
                if (token.equals("")) {
                                UISKipUtils.startLoginActivity(AuctionDetailsActivity.this);
                    return;
                }
                if(tv_praise.getText().equals("预交保证金")){
                    if (Tools.isFastDoubleClick()) {
                        ToastHelper.toastMessage(getBaseContext(), "请勿重复操作");
                        return;
                    }
                    boolean sIsWXAppInstalledAndSupported = api.isWXAppInstalled() && api.isWXAppSupportAPI();
                    if (!sIsWXAppInstalledAndSupported) {
                        ToastHelper.toastMessage(AuctionDetailsActivity.this,"您还没安装微信");
                        return;
                    }
                    WechatPay();
                }else if(!tv_praise.getText().toString().equals("拍卖结束")){
                    startActivity(new Intent(AuctionDetailsActivity.this, ChatActivity.class).putExtra("chatType", 3).
                            putExtra("userId", info.getChatRoomId()));
                }

                break;
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        index = position;//当前位置赋值给索引
        setCurDot(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    private void findStockEquityHomeRequest(){
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("tokenId", token);
        staff.put("stockId", String.valueOf(investId));

        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.FINDSTOCKEQUITYDETAILS,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        stopRefreshState();
//                        mEmptyView.setVisibility(View.VISIBLE);
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
                                    if (!js.isNull("equity")) {
                                        JSONObject jsonobj = js.optJSONObject("equity");
                                        info =  new FindStockEquityHomeEntity(jsonobj);
                                        stopRefreshState();
                                        initData();
                                    }
//                                    mEmptyView.setVisibility(View.GONE);
                                } else {
                                    ToastHelper.toastMessage(AuctionDetailsActivity.this, msg);
                                    stopRefreshState();
//                                    mEmptyView.setVisibility(View.VISIBLE);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            stopRefreshState();
                        }
                    }
                }, staff
        );
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
        staff.put("moduleType", String.valueOf(2));
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
                            tv_auction_evaluation.setText("拍卖评价("+totalNum+")");

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
                                        mCommentAdapter = new CommentAdapter (AuctionDetailsActivity.this,arrayList,type,1);
                                        lv_comment.setAdapter(mCommentAdapter);
                                    }
                                } else {
                                    ToastHelper.toastMessage(AuctionDetailsActivity.this, msg);
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
        staff.put("moduleType", String.valueOf(2));
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
                                    ToastHelper.toastMessage(AuctionDetailsActivity.this, msg);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, staff
        );
    }

    private void RemindRequest(){
        SharedPreferences sharedPreferences = getSharedPreferences("user",
                MODE_PRIVATE);
        token = sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("stockId", String.valueOf(investId));
        staff.put("tokenId", String.valueOf(token));
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.SAVEUSERSTOCKEQUITYREMIND,
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
                                    tv_set_remind.setText("提醒已开启");
                                    Drawable sexDrawble = getResources().getDrawable(tv_set_remind.getText().toString().equals("开启提醒") ? R.mipmap.stock_detail_icon_alarm_n : R.mipmap.stock_detail_icon_alarm_s);
                                    sexDrawble.setBounds(0, 0, sexDrawble.getMinimumWidth(), sexDrawble.getMinimumHeight());
                                    tv_set_remind.setCompoundDrawables(null, sexDrawble, null, null);
                                } else {
                                    ToastHelper.toastMessage(AuctionDetailsActivity.this, "拍卖即将开始，请选择其他未开拍的项目");
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, staff
        );

    }

    private void WechatPay(){
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("investId", String.valueOf(investId));
        staff.put("tokenId", String.valueOf(token));
        staff.put("body", "支付保证金");
        staff.put("totalFee",String.valueOf(2000*100));//2000*100
        staff.put("roleType", "2");
        staff.put("clientType", "2");
        staff.put("orderType", "2");

        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.SAVEUSERINVESTPROJECTFOLLOW,
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
                                    if(!js.isNull("unifiedOrder")){
                                        entity =   new WeiXinPayEntity(js.optJSONObject("unifiedOrder"));
                                    }
                                    playUitls.getInstance().weixinPlay(entity, AuctionDetailsActivity.this);

                                } else {
                                    ToastHelper.toastMessage(AuctionDetailsActivity.this, msg);
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
        dialog = new KeyMapDailog(String.format("回复:%s", name), AuctionDetailsActivity.this);
        dialog.show(getSupportFragmentManager(), "回复评论");
    }

    @Override
    public void sendBack(String inputText) {
        consumerContentRequest(inputText, mConsumerCommentEntity, type, investId);
    }
}

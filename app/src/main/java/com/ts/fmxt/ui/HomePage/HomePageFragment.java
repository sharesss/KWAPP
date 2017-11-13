package com.ts.fmxt.ui.HomePage;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.okhttp.Request;
import com.thindo.base.Widget.refresh.RefreshListView;
import com.thindo.base.Widget.refresh.RefreshScrollView;
import com.thindo.base.Widget.refresh.base.RefreshBase;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.HomePage.view.PopularityView;
import com.ts.fmxt.ui.HomePage.view.StockAuctionView;
import com.ts.fmxt.ui.ItemAdapter.FollowProjectAdapter;
import com.ts.fmxt.ui.base.frameng.FMBaseScrollFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import http.data.ConsumerEntity;
import http.data.FindStockEquityHomeEntity;
import http.data.TableList;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.UISKipUtils;
import utils.helper.ToastHelper;
import widget.EmptyView;
import widget.guidepage.GuideViewPagerAdapter;

import static com.ts.fmxt.R.id.btn_finish;

/**
 * Created by kp on 2017/9/11.
 * 小投首页
 */

public class HomePageFragment extends FMBaseScrollFragment implements View.OnClickListener {
    private EmptyView mEmptyView;
    private RefreshListView refresh_lv;
    private FollowProjectAdapter adapter;
    private ConsumerEntity info;
    private ViewPager stock_auction_viewpager,popularity_viewpager;

    private LinearLayout linearLayout,linearLayouts;
    // 定义ViewPager适配器
    private GuideViewPagerAdapter vpAdapter,gvAdapter;
    // 定义一个ArrayList来存放View
    private ArrayList<View> views = new ArrayList<View>();
    // 引导图片资源
    private static final int[] pics = {R.mipmap.one, R.mipmap.two, R.mipmap.three};
    private ArrayList arrays = new ArrayList();
    // 记录当前选中位置
    private int currentIndex;
    //当前索引位置以及上一个索引位置
    private int index = 0,preIndex = 0;

    // 定义一个ArrayList来存放View
    private ArrayList<View> viewdow = new ArrayList<View>();;
    // 引导图片资源
    private ArrayList arraysdow = new ArrayList();
    // 底部小点的图片
    private ImageView[] pointsdow;
    // 记录当前选中位置
    private int currentIndexdow;
    //当前索引位置以及上一个索引位置
    private int indextop = 0,preIndextop = 0;
    //定时器，用于实现轮播
    private Timer timer;
    private Timer Toptimer;
    private Activity mActivity;

    Handler mHandler  = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    index++;
                    if(index==arrays.size()){
                        index=0;
                    }
                    stock_auction_viewpager.setCurrentItem(index);
                    break;
                case 2:
                    indextop++;
                    if(indextop==arraysdow.size()){
                        indextop=0;
                    }
                    popularity_viewpager.setCurrentItem(indextop);

                    break;
            }
        }
    };


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.include_homepage_fragment, container, false);
        initTitle(inflate);

        return inflate;
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        this.mActivity =context;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        bindRefreshScrollAdapter((RefreshScrollView) getView().findViewById(R.id.refresh_scroll),
                R.layout.include_home_page);
        getRefreshScrollView().setMode(RefreshBase.Mode.PULL_FROM_START);
        stock_auction_viewpager = (ViewPager) getView().findViewById(R.id.stock_auction_viewpager);
        popularity_viewpager = (ViewPager) getView().findViewById(R.id.popularity_viewpager);
        linearLayout = (LinearLayout) getView().findViewById(R.id.point_container);
        linearLayouts = (LinearLayout) getView().findViewById(R.id.point_position);
        startRefreshState();

    }

    //初始化头部
    private void initTitle(View inflate) {
        refresh_lv = (RefreshListView) inflate.findViewById(R.id.refresh_lv);
        inflate.findViewById(R.id.iv_message).setOnClickListener(this);
        inflate.findViewById(btn_finish).setOnClickListener(this);


    }

    @Override
    public void onReload() {
        findStockEquityHomeRequest();
        investListRequest();
    }

    @Override
    public void loadMore() {

    }

    public void stopRefreshState(){
        stop();
    }
    /**
     * 初始化数据
     */
    private void initData() {
        vpAdapter = new GuideViewPagerAdapter(views);
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);

        for (int i = 0; i < arrays.size(); i++) {
            StockAuctionView iv = new StockAuctionView(getContext());
            iv.setLayoutParams(mParams);
            iv.formatData((FindStockEquityHomeEntity) arrays.get(i));
            views.add(iv);
        }
        stock_auction_viewpager.setAdapter(vpAdapter);
        stock_auction_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
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
        });
        if(timer==null){
            timer = new Timer();//创建Timer对象
        }else{
            timer.cancel();
            timer = new Timer();//创建Timer对象
        }

        //执行定时任务
        timer.schedule(new TimerTask() {
            public void run() {
                //首先判断是否需要轮播，是的话我们才发消息
                    mHandler.sendEmptyMessage(1);
            }
        },5000,5000);//延迟2秒，每隔2秒发一次消息
        initPoint();
    }

    /**
     * 初始化数据
     */
    private void initDataTop() {
        gvAdapter = new GuideViewPagerAdapter(viewdow);
        LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.FILL_PARENT,
                LinearLayout.LayoutParams.FILL_PARENT);

        for (int i = 0; i < arraysdow.size(); i++) {
            PopularityView iv = new PopularityView(getContext());
            iv.setLayoutParams(mParams);
            iv.formatData((ConsumerEntity) arraysdow.get(i));
            viewdow.add(iv);
        }
        popularity_viewpager.setAdapter(gvAdapter);
        popularity_viewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                indextop =position;//当前位置赋值给索引
                setCurDotTop(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        if(Toptimer==null){
            Toptimer = new Timer();//创建Timer对象
        }else{
            Toptimer.cancel();
            Toptimer = new Timer();//创建Timer对象
        }
        //执行定时任务
        Toptimer.schedule(new TimerTask() {
            public void run() {
                //首先判断是否需要轮播，是的话我们才发消息
                mHandler.sendEmptyMessage(2);
            }
        },8000,8000);//延迟2秒，每隔2秒发一次消息
        initPointTop();
    }

    /**
     * 初始化底部小点
     */
    private void initPoint() {
        if(arrays.size()<0){
            return;
        }
        for(int i = 0;i<arrays.size();i++){
            ImageView imageview = new ImageView(getContext());
            imageview.setImageResource(R.drawable.point);//设置背景选择器
            imageview.setPadding(20,0,0,0);//设置每个按钮之间的间距
            //将按钮依次添加到RadioGroup中
            linearLayout.addView(imageview, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //默认选中第一个按钮，因为默认显示第一张图片
            linearLayout.getChildAt(0).setEnabled(false);
        }

    }

    /**
     * 初始化底部小点
     */
    private void initPointTop() {
        if(arraysdow.size()<0){
            return;
        }
        for(int i = 0;i<arraysdow.size();i++){
            ImageView imageview = new ImageView(getContext());
            imageview.setImageResource(R.drawable.point);//设置背景选择器
            imageview.setPadding(20,0,0,0);//设置每个按钮之间的间距
            //将按钮依次添加到RadioGroup中
            linearLayouts.addView(imageview, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            //默认选中第一个按钮，因为默认显示第一张图片
            linearLayouts.getChildAt(0).setEnabled(false);
        }
    }

    /**
     * 设置当前的小点的位置
     */
    private void setCurDot(int positon) {
        if(linearLayout.getChildAt(positon)!=null){
            linearLayout.getChildAt(positon).setEnabled(false);//当前按钮选中
        }
        if(linearLayout.getChildAt(preIndex)!=null){
            linearLayout.getChildAt(preIndex).setEnabled(true);//上一个取消选中
            preIndex = positon;//当前位置变为上一个，继续下次轮播
        }
    }

    /**
     * 设置当前的小点的位置
     */
    private void setCurDotTop(int positon) {
        if(linearLayouts.getChildAt(positon)!=null){
            linearLayouts.getChildAt(positon).setEnabled(false);//当前按钮选中
        }
        if(linearLayouts.getChildAt(preIndextop)!=null){
            linearLayouts.getChildAt(preIndextop).setEnabled(true);//上一个取消选中
            preIndextop = positon;//当前位置变为上一个，继续下次轮播
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_message:
                UISKipUtils.startMessageActivity(getActivity());

                break;
            case R.id.btn_finish:
                UISKipUtils.statrSmallThrowRuleActivity(getActivity());
                break;
        }
    }

    private void findStockEquityHomeRequest(){
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("isFirst", String.valueOf(1));

        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.FINDSTOCKEQUITYHOME,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        stopRefreshState();
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
                                    if (!js.isNull("equities")) {
                                        JSONArray array = js.optJSONArray("equities");
                                        if(arrays.size()>=0){
                                            arrays.clear();
                                            views.clear();
                                            linearLayout.removeAllViews();
                                            tableList.getArrayList().clear();
                                        }
                                        for (int i = 0; i < array.length(); i++) {
                                            tableList.getArrayList().add(new FindStockEquityHomeEntity(array.getJSONObject(i)));
                                        }
                                        arrays = tableList.getArrayList();
                                        initData();

                                    }
                                    stopRefreshState();
                                } else {
                                    ToastHelper.toastMessage(getActivity(), msg);
                                    stopRefreshState();
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

    private void investListRequest(){
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("isFirst", String.valueOf(1));

        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.INVESTLIST,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                        stopRefreshState();
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
                                    if (!js.isNull("projects")) {
                                        JSONArray array = js.optJSONArray("projects");
                                        if(arraysdow.size()>=0){
                                            arraysdow.clear();
                                            viewdow.clear();
                                            linearLayouts.removeAllViews();
                                            tableList.getArrayList().clear();
                                        }
                                        for (int i = 0; i < array.length(); i++) {
                                            tableList.getArrayList().add(new ConsumerEntity(array.getJSONObject(i)));
                                        }
                                        arraysdow = tableList.getArrayList();
                                        initDataTop();
                                    }
                                    stopRefreshState();
                                } else {
                                    ToastHelper.toastMessage(getActivity(), msg);
                                    stopRefreshState();
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

}

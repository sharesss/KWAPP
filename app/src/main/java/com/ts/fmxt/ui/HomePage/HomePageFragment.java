package com.ts.fmxt.ui.HomePage;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.okhttp.Request;
import com.thindo.base.Widget.refresh.RefreshListView;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.HomePage.view.PopularityView;
import com.ts.fmxt.ui.HomePage.view.StockAuctionView;
import com.ts.fmxt.ui.ItemAdapter.FollowProjectAdapter;
import com.ts.fmxt.ui.base.frameng.FMBaseTableFragment;

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

public class HomePageFragment extends FMBaseTableFragment implements View.OnClickListener {
    private EmptyView mEmptyView;
    private RefreshListView refresh_lv;
    private FollowProjectAdapter adapter;
    private ConsumerEntity info;
    private ViewPager stock_auction_viewpager,popularity_viewpager;

    private LinearLayout linearLayout,linearLayouts;
    // 定义ViewPager适配器
    private GuideViewPagerAdapter vpAdapter,gvAdapter;
    // 定义一个ArrayList来存放View
    private ArrayList<View> views;
    // 引导图片资源
    private static final int[] pics = {R.mipmap.one, R.mipmap.two, R.mipmap.three};
    private ArrayList arrays;
    // 记录当前选中位置
    private int currentIndex;
    //当前索引位置以及上一个索引位置
    private int index = 0,preIndex = 0;

    // 定义一个ArrayList来存放View
    private ArrayList<View> viewdow;
    // 引导图片资源
    private ArrayList arraysdow;
    // 底部小点的图片
    private ImageView[] pointsdow;
    // 记录当前选中位置
    private int currentIndexdow;
    //当前索引位置以及上一个索引位置
    private int indextop = 0,preIndextop = 0;
    //定时器，用于实现轮播
    private Timer timer;

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
        findStockEquityHomeRequest();
        investListRequest();
        return inflate;
    }

    //初始化头部
    private void initTitle(View inflate) {
        mEmptyView = (EmptyView) inflate.findViewById(R.id.empty_view);
        refresh_lv = (RefreshListView) inflate.findViewById(R.id.refresh_lv);
        inflate.findViewById(R.id.iv_message).setOnClickListener(this);
        inflate.findViewById(btn_finish).setOnClickListener(this);
        stock_auction_viewpager = (ViewPager) inflate.findViewById(R.id.stock_auction_viewpager);
        popularity_viewpager = (ViewPager) inflate.findViewById(R.id.popularity_viewpager);
        linearLayout = (LinearLayout) inflate.findViewById(R.id.point_container);
        linearLayouts = (LinearLayout) inflate.findViewById(R.id.point_position);

    }
    /**
     * 初始化数据
     */
    private void initData() {
        views = new ArrayList<View>();
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
        timer = new Timer();//创建Timer对象
        //执行定时任务
        timer.schedule(new TimerTask() {
            public void run() {
                //首先判断是否需要轮播，是的话我们才发消息
                    mHandler.sendEmptyMessage(1);
            }
        },2000,2000);//延迟2秒，每隔2秒发一次消息
        initPoint();
    }

    /**
     * 初始化数据
     */
    private void initDataTop() {
        viewdow = new ArrayList<View>();
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
        timer = new Timer();//创建Timer对象
        //执行定时任务
        timer.schedule(new TimerTask() {
            public void run() {
                //首先判断是否需要轮播，是的话我们才发消息
                mHandler.sendEmptyMessage(2);
            }
        },2000,2000);//延迟2秒，每隔2秒发一次消息
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
                                    if (!js.isNull("equities")) {
                                        JSONArray array = js.optJSONArray("equities");
                                        for (int i = 0; i < array.length(); i++) {
                                            tableList.getArrayList().add(new FindStockEquityHomeEntity(array.getJSONObject(i)));
                                        }
                                        arrays = tableList.getArrayList();
                                        initData();
                                    }
                                    mEmptyView.setVisibility(View.GONE);
                                } else {
                                    ToastHelper.toastMessage(getActivity(), msg);
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

    private void investListRequest(){
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("isFirst", String.valueOf(1));

        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.INVESTLIST,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
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
                                    if (!js.isNull("projects")) {
                                        JSONArray array = js.optJSONArray("projects");
                                        for (int i = 0; i < array.length(); i++) {
                                            tableList.getArrayList().add(new ConsumerEntity(array.getJSONObject(i)));
                                        }
                                        arraysdow = tableList.getArrayList();
                                        initDataTop();
                                    }
                                    mEmptyView.setVisibility(View.GONE);
                                } else {
                                    ToastHelper.toastMessage(getActivity(), msg);
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
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

    }

    @Override
    public void onReload() {

    }

    @Override
    public void loadMore() {

    }
}

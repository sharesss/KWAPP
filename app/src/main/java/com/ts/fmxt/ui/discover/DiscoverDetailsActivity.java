package com.ts.fmxt.ui.discover;/**
 * Created by A1 on 2017/8/1.
 */

import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OlLinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.base.activity.FMBaseScrollActivityV2;
import com.ts.fmxt.ui.discover.view.KeyMapDailog;
import com.ts.fmxt.ui.discover.view.ProjectReturnItem;
import com.ts.fmxt.ui.discover.view.RedCircleBar;
import com.ts.fmxt.ui.discover.view.WeiXinWin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import http.data.ConsumerCommentEntity;
import http.data.ConsumerEntity;
import http.data.InvestBPListEntity;
import http.data.ProgressUpdateEntity;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.ReceiverUtils;
import utils.UISKipUtils;
import utils.helper.ToastHelper;
import widget.FMNoScrollListView;
import widget.Share.PopupShareView;
import widget.popup.BaseDoubleEventPopup;
import widget.popup.PopupObject;
import widget.popup.dialog.MessageContentDialog;

import static com.ts.fmxt.R.id.ll_group;

/**
 * created by kp at 2017/8/1
 * 发现详情
 */
public class DiscoverDetailsActivity extends FMBaseScrollActivityV2 implements View.OnClickListener, KeyMapDailog.SendBackListener ,  ReceiverUtils.MessageReceiver{
    private int investId;
    private TextView tvWorth, tvNoworth;
    private LinearLayout llTemp, llCollection;
    private ConsumerEntity info;
    private WeiXinWin win;
    private RedCircleBar ivRedCirclebar;

    private FMNoScrollListView refresh_lv, reviews_lv, lv_result;
    private ConsumerCommentEntity mConsumerCommentEntity = null;
    private KeyMapDailog dialog;
    private int type = 0;//请求的评论类型0是全部，1是值得投，2是不值得投

    private TextView tvCollection, tvWithTheVote, tvBpresult, tvResult,tv_message;
    private boolean isCollect;

    private int recLen = 3;
    private int types,isOver=0,isFollow=0;
    public RecyclerViewAdapter adapter;
    ArrayList<BaseViewItem> list;
    RecyclerView recyclerView;
    String weixinNum;
    String weixinCode;
    @Override
    public void onMessage(int receiverType, Bundle bundle) {
        if (receiverType == ReceiverUtils.SEEKBAR) {
            InvestBPListRequest(false);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_discover_details);
        investId = getIntent().getIntExtra("id", -1);
        types = getIntent().getIntExtra("type", -1);
        ReceiverUtils.addReceiver(this);
        initView();
    }



    private void initView() {
        list = new ArrayList<BaseViewItem>();
        adapter = new RecyclerViewAdapter(this, list);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        OlLinearLayoutManager wrapContentLinearLayoutManager = new OlLinearLayoutManager(this);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(wrapContentLinearLayoutManager);
        recyclerView.setAdapter(adapter);

        //顶部UI
        findViewById(R.id.iv_share).setOnClickListener(this);
        findViewById(R.id.btn_finish).setOnClickListener(this);
        findViewById(R.id.tv_report).setOnClickListener(this);



//        //底部两个按钮
        llCollection = (LinearLayout) findViewById(R.id.ll_collection);
        tvCollection = (TextView) findViewById(R.id.tv_collection);
        tvWithTheVote = (TextView) findViewById(R.id.tv_with_the_vote);
        tv_message = (TextView) findViewById(R.id.tv_message);
//        tvWithTheVote.setOnClickListener(this);
        llCollection.setOnClickListener(this);
        findViewById(R.id.tv_top).setOnClickListener(this);
        findViewById(R.id.ll_message).setOnClickListener(this);
        findViewById(ll_group).setOnClickListener(this);

        tab_bar = findViewById(R.id.tab_bar);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            recyclerView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
                @Override
                public void onScrollChange(View view, int i, int i1, int i2, int i3) {
                    tabView(tab_bar,view);
                }
            });
        } else {
            recyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                }

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    tabView(tab_bar,recyclerView);
                }
            });
        }
        Message message = handler.obtainMessage(1);     // Message
        handler.sendMessageDelayed(message, 3000);
        //饼图UI
        DiscoverDetailsRequest();//顶部的数据获取
        InvestBPListRequest(false);
        ProjectRenewalRequest();
        CommentRequest(type);
    }
    private View tab_bar;
    private int firstItemPosition = -1;
    private int oldindex = -1;
//    int state;//状态
    /**
     * 滑动头部哪个模块的 就标记哪个模块
     * @param index
     */
    private void scrollUpdateTab(int index,int lastindex) {
        if (index != lastindex && lastindex == list.size() - 1) {
            index = lastindex;
        }
        BaseViewItem baseViewItem = list.get(index);
        boolean find = findItem(baseViewItem);
        if (!find) {
            int i = lastindex - index;
            i = i >= 1 ? 1 : 0;
            findItem(list.get(i + index));
        }
    }

    boolean findItem(BaseViewItem baseViewItem) {

        if (baseViewItem instanceof MyStoryItem) {
            if (tabItem != null) {
                TabItem.ViewHolder viewHolder = tabItem.getViewHolder();
                tabItem.select(viewHolder.tv_my_story);
                selectTab(tabItem);
                return true;
            }
        } else if (baseViewItem instanceof ProjectReturnItem) {
            if (tabItem != null) {
                TabItem.ViewHolder viewHolder = tabItem.getViewHolder();
                tabItem.select(viewHolder.tv_project_return);
                selectTab(tabItem);
                return true;
            }
        } else if (baseViewItem instanceof DisBPItem || baseViewItem instanceof DisBPLabelItem || baseViewItem instanceof ProjectHighlightsItem) {
            if (tabItem != null) {
                TabItem.ViewHolder viewHolder = tabItem.getViewHolder();
                tabItem.select(viewHolder.tv_project_highlights);
                selectTab(tabItem);
                return true;
            }
        } else if (baseViewItem instanceof ProgressUpdateItem || baseViewItem instanceof ProjectBonusItem || baseViewItem instanceof ProjectProgressItem) {
            if (tabItem != null) {
                TabItem.ViewHolder viewHolder = tabItem.getViewHolder();
                tabItem.select(viewHolder.tv_project_schedule);
                selectTab(tabItem);
                return true;
            }
        }
        return false;
    }
    private void tabView(View tab_bar,View view) {
        if (tab_bar != null) {
            RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
            if (layoutManager instanceof LinearLayoutManager) {
                LinearLayoutManager linearManager = (LinearLayoutManager) layoutManager;
                int index = linearManager.findFirstVisibleItemPosition();
                int laseindex = linearManager.findLastVisibleItemPosition();
                scrollUpdateTab(index,laseindex);
                if (firstItemPosition <= -1) {
                    if (oldindex == -1) {
                        oldindex = index;
                    }
                    for (; oldindex <= index; oldindex++) {
                        if (list.get(oldindex) instanceof TabItem) {
                            firstItemPosition = index;
                        }
                    }
                }
                {
                    if (firstItemPosition > index) {
                        view.setPadding(0, 0, 0, 0);
                        tab_bar.setVisibility(View.GONE);
                    } else if (firstItemPosition > -1) {
                        view.setPadding(0, (int) getResources().getDimension(R.dimen.tap_h), 0, 0);
                        tab_bar.setVisibility(View.VISIBLE);
                    }
                }
                oldindex = index;
            }
        }
    }

    private void setBar(final TabItem item) {
        if (tab_bar == null) {
            return;
        }
        TextView tv_my_story = (TextView) tab_bar.findViewById(R.id.tv_my_story);
        TextView tv_project_return = (TextView) tab_bar.findViewById(R.id.tv_project_return);
        TextView tv_project_highlights = (TextView) tab_bar.findViewById(R.id.tv_project_highlights);
        TextView tv_project_schedule = (TextView) tab_bar.findViewById(R.id.tv_project_schedule);
        tv_my_story.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.onClick(view);
            }
        });
        tv_project_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.onClick(view);
            }
        });
        tv_project_highlights.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                item.onClick(view);
            }
        });
        tv_project_schedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (updatelist.isEmpty()) {
                    ToastHelper.toastMessage(DiscoverDetailsActivity.this, "暂未发布项目更新");
                    return;
                }
                item.onClick(view);
            }
        });
    }

    private void selectTab(TabItem item) {
        if (tab_bar == null || item == null) {
            return;
        }
        TabItem.ViewHolder viewHolder = item.getViewHolder();
        TextView tv_my_story = (TextView) tab_bar.findViewById(R.id.tv_my_story);
        TextView tv_project_return = (TextView) tab_bar.findViewById(R.id.tv_project_return);
        TextView tv_project_highlights = (TextView) tab_bar.findViewById(R.id.tv_project_highlights);
        TextView tv_project_schedule = (TextView) tab_bar.findViewById(R.id.tv_project_schedule);
        tv_my_story.setTextColor(viewHolder.tv_my_story.getCurrentTextColor());
        tv_project_return.setTextColor(viewHolder.tv_project_return.getCurrentTextColor());
        tv_project_highlights.setTextColor(viewHolder.tv_project_highlights.getCurrentTextColor());
        tv_project_schedule.setTextColor(viewHolder.tv_project_schedule.getCurrentTextColor());
        TextView tv_my_story_lin = (TextView) tab_bar.findViewById(R.id.tv_my_story_lin);
        TextView tv_project_return_lin = (TextView) tab_bar.findViewById(R.id.tv_project_return_lin);
        TextView tv_project_highlights_lin = (TextView) tab_bar.findViewById(R.id.tv_project_highlights_lin);
        TextView tv_project_schedule_lin = (TextView) tab_bar.findViewById(R.id.tv_project_schedule_lin);
        tv_my_story_lin.setBackground(viewHolder.tv_my_story_lin.getBackground());
        tv_project_return_lin.setBackground(viewHolder.tv_project_return_lin.getBackground());
        tv_project_highlights_lin.setBackground(viewHolder.tv_project_highlights_lin.getBackground());
        tv_project_schedule_lin.setBackground(viewHolder.tv_project_schedule_lin.getBackground());

    }
    TabItem tabItem;
    ArrayList<BaseViewItem> headlist = new ArrayList<BaseViewItem>();
    private void formatData(ConsumerEntity info) {
        if (info == null) {
            return;
        }
        weixinNum = info.getWeixinNum();
        weixinCode = info.getWeixinCode();
        TabItem.CallBack callBack = new TabItem.CallBack() {
            @Override
            public void onitem(int postion,TabItem item) {
//                int index = list.indexOf(item);
//                    index = postion + index + 1;


                int index = 0;
                for (int i = 0; i < list.size(); i++) {
                    BaseViewItem baseViewItem = list.get(i);
                    if (postion == 0) {
                        if (baseViewItem instanceof MyStoryItem) {
                            index = i;
                            break;
                        }
                    } else if (postion == 1) {
                        if (baseViewItem instanceof ProjectReturnItem) {
                            index = i;
                            break;
                        }
                    } else if (postion == 2) {
                        if (baseViewItem instanceof ProjectHighlightsItem) {
                            index = i;
                            break;
                        }
                    } else if (postion == 3) {
                        if (baseViewItem instanceof ProjectProgressItem) {
                            index = i;
                            break;
                        }
                    }
                }
                recyclerView.smoothScrollToPosition(index);
            }
        };
        DiscoverHeadItem discoverHeadItem = new DiscoverHeadItem(info);

        headlist.add(0, discoverHeadItem);
        DiscoverCircleItem discoverCircleItem = new DiscoverCircleItem(info, DiscoverDetailsActivity.this, investId);
        ProjectReturnItem projectReturnItem = new ProjectReturnItem(info, DiscoverDetailsActivity.this);
        MyStoryItem myStoryItem = new MyStoryItem(info, DiscoverDetailsActivity.this);

//        for(int i=0;i< arr.size();i++){
//            if(arr.get(i).getParticipationState()==1){
//                state = arr.get(i).getParticipationState();
//            }
//
//        }
        tabItem = new TabItem(callBack,DiscoverDetailsActivity.this);
        setBar(tabItem);
        headlist.add(1, discoverCircleItem);
        headlist.add(2, new LineItem());
        headlist.add(3, tabItem);
        if(info.getCeis().size()!=0){
            headlist.add(myStoryItem);
        }
        if(info.getCeils().size()!=0){
            headlist.add(projectReturnItem);
        }

        list.addAll(0, headlist);

        Long currenttime=System.currentTimeMillis()/1000;//获取系统时间的10位的时间戳
        Long finishtime =info.getReserveFinishTime()/1000;
        Long time =finishtime-currenttime;

        if(time>0) {
            if(info.getIsFollow()==0){
                isOver=1;
                tvWithTheVote.setOnClickListener(this);
                tvWithTheVote.setText("我要跟投");
                tvWithTheVote.setTextColor(getResources().getColor(R.color.text_black_main));
                tvWithTheVote.setBackground(getResources().getDrawable(R.drawable.bg_orange_5_shape));
            }else{
                isOver=1;
                isFollow=1;
                tvWithTheVote.setOnClickListener(this);
                tvWithTheVote.setText("继续跟投");
                tvWithTheVote.setTextColor(getResources().getColor(R.color.text_black_main));
                tvWithTheVote.setBackground(getResources().getDrawable(R.drawable.bg_orange_5_shape));
            }

        }else{
            tvWithTheVote.setOnClickListener(this);
            tvWithTheVote.setText("跟投已经结束");
            tvWithTheVote.setTextColor(getResources().getColor(R.color.white));
            tvWithTheVote.setBackground(getResources().getDrawable(R.drawable.bg_gray));
        }
        /**
         * 这里可以添加各种Item,参照以上代码
         */
        adapter.notifyDataSetChanged();


        Drawable sexDrawble = getResources().getDrawable(info.getIsCollect() == 1 ? R.mipmap.card_detail_s : R.mipmap.card_detail_n);
        sexDrawble.setBounds(0, 0, sexDrawble.getMinimumWidth(), sexDrawble.getMinimumHeight());
        tvCollection.setCompoundDrawables(sexDrawble, null, null, null);
        tvCollection.setText(info.getCollectNum()+"");
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

//        DiscoverLabelItem labelItem = new DiscoverLabelItem(arr, callBack);
//        labellist.add(labelItem);

        ProjectHighlightsItem  projectHighlights = new ProjectHighlightsItem( DiscoverDetailsActivity.this);
        labellist.add(projectHighlights);
        int cont = 0;
        for (InvestBPListEntity entity : arr) {
            DisBPItem disBPItem = new DisBPItem(entity, investId,this);
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
    ArrayList<BaseViewItem> updatelist = new ArrayList<>();
    private void ProgressUpdateData(ArrayList<ProgressUpdateEntity> arr){
//        if (arr.size() == 0) {
//            list.removeAll(updatelist);
//        }
        list.removeAll(updatelist);
        updatelist.clear();
        if (arr != null && arr.size() > 0) {
            ProjectProgressItem projectProgressItem = new ProjectProgressItem(DiscoverDetailsActivity.this);
            updatelist.add(projectProgressItem);
            for (ProgressUpdateEntity entity : arr) {
                if (entity.getParticipationType() == 1) {
                    ProgressUpdateItem disBPItem = new ProgressUpdateItem(entity, this);
                    updatelist.add(disBPItem);
                } else if (entity.getParticipationType() == 2) {
                    ProjectBonusItem disBPItem = new ProjectBonusItem(entity, this);
                    updatelist.add(disBPItem);
                }

            }
        }

//        DisBPLabelItem disBPLabelItem = new DisBPLabelItem(cont, DiscoverDetailsActivity.this);
//        labellist.add(disBPLabelItem);
        if (headlist.isEmpty()) {
            list.addAll(labellist.size(), updatelist);
        } else {
            list.addAll(headlist.size()+labellist.size(), updatelist);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
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
            case R.id.ll_group:
                if(info.getIsFollow()==0){
                        ToastHelper.toastMessage(DiscoverDetailsActivity.this,"预约跟投后可加入路演群");
                }else if(info.getIsFollow()==1){
                    win =new WeiXinWin(this,weixinNum,weixinCode,info.getInvestName());
                    win.showAtLocation(
                            findViewById(R.id.AppWidget),
                            Gravity.CENTER | Gravity.CENTER, 0, 0); // 设置layout在PopupWindow中显示的位置
                }

//                ToastHelper.toastMessage(this,"微信群");
                break;
            case R.id.tv_report://举报
                if (token.equals("")) {
                    MessageContentDialog mPopupDialogWidget = new MessageContentDialog(DiscoverDetailsActivity.this);
                    mPopupDialogWidget.setMessage("您还未登录，是否去登录？");
                    mPopupDialogWidget.setOnEventClickListener(new BaseDoubleEventPopup.onEventClickListener() {

                        @Override
                        public void onEventClick(PopupObject obj) {
                            if (obj.getWhat() == 1){
                                UISKipUtils.startLoginActivity(DiscoverDetailsActivity.this);
                            }

                        }
                    });
                    mPopupDialogWidget.showPopupWindow();

                    return;
                }
                UISKipUtils.startReportActivity(DiscoverDetailsActivity.this,investId);
                break;
            case R.id.ll_collection:
                if (token.equals("")) {
                    MessageContentDialog mPopupDialogWidget = new MessageContentDialog(DiscoverDetailsActivity.this);
                    mPopupDialogWidget.setMessage("您还未登录，是否去登录？");
                    mPopupDialogWidget.setOnEventClickListener(new BaseDoubleEventPopup.onEventClickListener() {

                        @Override
                        public void onEventClick(PopupObject obj) {
                            if (obj.getWhat() == 1){
                                UISKipUtils.startLoginActivity(DiscoverDetailsActivity.this);
                            }

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
                int isTruenameAuthen=sharedPreferences.getInt("isTruenameAuthen", -1);
                int isinvestauthen=sharedPreferences.getInt("isinvestauthen", -1);
                final int auditstate=sharedPreferences.getInt("auditstate", -1);
                if (token.equals("")) {
                    MessageContentDialog mPopupDialogWidget = new MessageContentDialog(DiscoverDetailsActivity.this);
                    mPopupDialogWidget.setMessage("您还未登录，是否去登录？");
                    mPopupDialogWidget.setOnEventClickListener(new BaseDoubleEventPopup.onEventClickListener() {

                        @Override
                        public void onEventClick(PopupObject obj) {
                            if (obj.getWhat() == 1){
                                UISKipUtils.startLoginActivity(DiscoverDetailsActivity.this);
                            }

                        }
                    });
                    mPopupDialogWidget.showPopupWindow();

                    return;
                }
//                if(isTruenameAuthen==0){
//                    MessageContentDialog mPopupDialogWidget = new MessageContentDialog(DiscoverDetailsActivity.this);
//                    mPopupDialogWidget.setMessage("您还未实名认证，是否去认证？");
//                    mPopupDialogWidget.setOnEventClickListener(new BaseDoubleEventPopup.onEventClickListener() {
//
//                        @Override
//                        public void onEventClick(PopupObject obj) {
//                            if (obj.getWhat() == 1)
//                                UISKipUtils.startRealNameActivity(DiscoverDetailsActivity.this);
//                        }
//                    });
//                    mPopupDialogWidget.showPopupWindow();
//                    return;
//                }
//                if(isinvestauthen==0){
//                    MessageContentDialog mPopupDialogWidget = new MessageContentDialog(DiscoverDetailsActivity.this);
//                    mPopupDialogWidget.setMessage("您还未认证投资人，是否去认证？");
//                    mPopupDialogWidget.setOnEventClickListener(new BaseDoubleEventPopup.onEventClickListener() {
//
//                        @Override
//                        public void onEventClick(PopupObject obj) {
//                            if (obj.getWhat() == 1)
//                                if(auditstate==0){
//                                    UISKipUtils.startInvestmentRecordActivity(DiscoverDetailsActivity.this);
//                                }else{
//                                    UISKipUtils.startCertifiedInvestorActivity(DiscoverDetailsActivity.this,auditstate,1);//1是外面进去的，展示查看我的投资偏好，0是设计我的投资偏好
//                                }
//
//                        }
//                    });
//                    mPopupDialogWidget.showPopupWindow();
//                    return;
//                }
                UISKipUtils.startProjectReturnActivity(DiscoverDetailsActivity.this, investId,isOver);
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
             case R.id.ll_message:
                 //跳转到评论列表
                 if (token.equals("")) {
                     MessageContentDialog mPopupDialogWidget = new MessageContentDialog(DiscoverDetailsActivity.this);
                     mPopupDialogWidget.setMessage("您还未登录，是否去登录？");
                     mPopupDialogWidget.setOnEventClickListener(new BaseDoubleEventPopup.onEventClickListener() {

                         @Override
                         public void onEventClick(PopupObject obj) {
                             if (obj.getWhat() == 1){
                                 UISKipUtils.startLoginActivity(DiscoverDetailsActivity.this);
                             }

                         }
                     });
                     mPopupDialogWidget.showPopupWindow();

                     return;
                 }
                 UISKipUtils.startCommentActivity(DiscoverDetailsActivity.this, investId,isOver,isFollow);
                    break;
        }
    }

    public void DiscoverDetailsRequest() {
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
                            list.removeAll(listcomment);
                            listcomment.clear();
                            tv_message.setText(totalNum+"");
//                            DiscoverCommentItem discoverCommentItem = new DiscoverCommentItem(totalNum, desre, bedesre, DiscoverDetailsActivity.this);
//                            listcomment.add(discoverCommentItem);
                            if (!js.isNull("statsMsg")) {
                                JSONObject json = js.optJSONObject("statsMsg");
                                String stats = json.getString("stats");
                                String msg = json.getString("msg");
                                if (stats.equals("1")) {
                                    if (!js.isNull("comments")) {
                                        JSONArray array = js.optJSONArray("comments");
                                        for (int i = 0; i < array.length(); i++) {
//                                            DisCommentItem disCommentItem = new DisCommentItem(new ConsumerCommentEntity(array.getJSONObject(i)), DiscoverDetailsActivity.this, type);
//                                            listcomment.add(disCommentItem);
                                        }
                                    }
                                } else {
                                    ToastHelper.toastMessage(DiscoverDetailsActivity.this, msg);
                                }
                            }
//                            list.addAll(listcomment);
//                            adapter.notifyDataSetChanged();
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
                                        if(info.getIsCollect() == 1){
                                            tvCollection.setText(info.getCollectNum()+"");
                                        }else{
                                            tvCollection.setText(info.getCollectNum()+1+"");
                                        }

                                        ToastHelper.toastMessage(DiscoverDetailsActivity.this, "收藏成功");
                                    } else {
                                        Drawable sexDrawble = getResources().getDrawable(R.mipmap.card_detail_n);
                                        sexDrawble.setBounds(0, 0, sexDrawble.getMinimumWidth(), sexDrawble.getMinimumHeight());
                                        tvCollection.setCompoundDrawables(sexDrawble, null, null, null);
                                        if(info.getIsCollect() == 1){
                                            tvCollection.setText(info.getCollectNum()-1+"");
                                        }else{
                                            tvCollection.setText(info.getCollectNum()+"");
                                        }

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
    ArrayList<ProgressUpdateEntity> arr= new ArrayList<ProgressUpdateEntity>();
    //项目更新信息
    public void ProjectRenewalRequest() {//
        final Map<String, String> staff = new HashMap<String, String>();
        staff.put("investId", String.valueOf(investId));
         staff.put("participationState", String.valueOf("1"));
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.FINDPROJECTPARTICIPATION,
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
                                arr.clear();
                                if (stats.equals("1")) {
                                    if (!js.isNull("projectParticipations")) {
//                                        TableList tableList = new TableList();
                                        JSONArray array = js.optJSONArray("projectParticipations");
                                        for (int i = 0; i < array.length(); i++) {
                                            arr.add(new ProgressUpdateEntity(array.getJSONObject(i)));
                                        }
                                        ProgressUpdateData(arr);
                                    }
                                } else {
                                    ProgressUpdateData(arr);
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
        recyclerView.smoothScrollToPosition(adapter.getItemCount() );
    }


}

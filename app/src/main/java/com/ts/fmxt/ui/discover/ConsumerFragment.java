package com.ts.fmxt.ui.discover;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.thindo.base.Widget.refresh.RefreshListView;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.ItemAdapter.FollowProjectAdapter;
import com.ts.fmxt.ui.base.frameng.FMBaseTableFragment;
import com.ts.fmxt.ui.discover.view.ReleaseProjectWin;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import http.data.ConsumerEntity;
import http.data.TableList;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.helper.ToastHelper;
import widget.EmptyView;
import widget.Share.PopupShareView;

/**
 *
 */

public class ConsumerFragment extends FMBaseTableFragment implements View.OnClickListener {
    private int pageNo = 1;
    private String brandId = "";
    private EmptyView mEmptyView;
    private RefreshListView refresh_lv;
    private FollowProjectAdapter adapter;
    private ReleaseProjectWin win;
    private TextView iv_share,tv_spinner;
    private ConsumerEntity info;
    private View inflate;
    boolean isclick = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         inflate = inflater.inflate(R.layout.include_navigation_refresh_list_view, container, false);
        initTitle(inflate);
        bindRefreshAdapter((RefreshListView) inflate.findViewById(R.id.refresh_lv), new FollowProjectAdapter(getActivity(), arrayList));
//        ReceiverUtils.addReceiver(this);
        tv_spinner = (TextView) inflate.findViewById(R.id.tv_spinner);
        tv_spinner.setOnClickListener(this);
        startRefreshState();
        return inflate;
    }

    //初始化头部
    private void initTitle(final View inflate) {
        mEmptyView = (EmptyView) inflate.findViewById(R.id.empty_view);
        refresh_lv = (RefreshListView) inflate.findViewById(R.id.refresh_lv);
        iv_share = (TextView) inflate.findViewById(R.id.iv_share);
        iv_share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                showShareDialog();  //分享功能
                win =new ReleaseProjectWin(getActivity());
                win.showAtLocation(
                        inflate.findViewById(R.id.AppWidget),
                        Gravity.CENTER | Gravity.CENTER, 0, 0); // 设置layout在PopupWindow中显示的位置
            }
        });

    }

    @Override
    public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

    }

    @Override
    public void onReload() {
        consumerListRequest(0);
    }

    @Override
    public void loadMore() {
    }

    public void stopRefreshState(){
        stop();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_spinner:
                if(isclick){
                    isclick = false;
                }else {
                    isclick =true;
                }
                Drawable sexDrawble = getResources().getDrawable(isclick ? R.mipmap.down : R.mipmap.upward);
                sexDrawble.setBounds(0, 0, sexDrawble.getMinimumWidth(), sexDrawble.getMinimumHeight());
                tv_spinner.setCompoundDrawables(null, null, null, sexDrawble);
                spinnerWin win =new spinnerWin(getActivity());
                win.showAtLocation(
                        inflate.findViewById(R.id.AppWidget),
                        Gravity.TOP | Gravity.TOP, 0, 0); // 设置layout在PopupWindow中显示的位置
                break;

        }
    }

    private void consumerListRequest(int investProjectState){
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("investProjectState", String.valueOf(investProjectState));
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.INVESTLIST,
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
                            if(!js.isNull("statsMsg")){
                                JSONObject json = js.optJSONObject("statsMsg");
                                String stats = json.getString("stats");
                                String msg = json.getString("msg");
                                if(stats.equals("1")){
                                    TableList tableList = new TableList();
                                    if (!js.isNull("projects")) {
                                        JSONArray array = js.optJSONArray("projects");
                                        for (int i = 0; i < array.length(); i++) {
                                            tableList.getArrayList().add(new ConsumerEntity(array.getJSONObject(i)));
                                        }
                                        adapter = new FollowProjectAdapter(getActivity(), tableList.getArrayList());
                                        refresh_lv.setAdapter(adapter);
                                    }
                                    stopRefreshState();
                                    mEmptyView.setVisibility(View.GONE);
//                            ToastHelper.toastMessage(getContext(), msg);
                                }else{
                                    ToastHelper.toastMessage(getContext(),msg);
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

    //分享对话框
    private void showShareDialog() {
        String title = String.format(getString(R.string.share_consumenr_title));
        PopupShareView popupShareView = new PopupShareView(getActivity());
        popupShareView.setContent(getString(R.string.share_consumenr_context));

        popupShareView.setWechatMomentsTitle(title);
        popupShareView.setTitle(title);
        String uri = String.format(getString( R.string.html_fm_fmoneyShare_user));//
        popupShareView.setUrl(uri);
        popupShareView.setLogo("https://ss0.bdstatic.com/7Ls0a8Sm1A5BphGlnYG//sys/portrait/item/1f8d4e6f64655f616e64726f69641648.jpg?cdnversion=0022");//
        popupShareView.showPopupWindow();
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
                    consumerListRequest(0);
                    dismiss();
                }
            });
            tv_in_hand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_spinner.setText("进行中");
                    consumerListRequest(1);
                    dismiss();
                }
            });
            tv_ended.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    tv_spinner.setText("已结束");
                    consumerListRequest(2);
                    dismiss();
                }
            });
        }
    }

}

package com.ts.fmxt.ui.user.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.thindo.base.Widget.refresh.RefreshScrollView;
import com.thindo.base.Widget.refresh.base.RefreshBase;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.base.frameng.FMBaseScrollFragment;
import com.ts.fmxt.ui.user.view.UserHeadView;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import http.data.UserInfoEntity;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.ReceiverUtils;
import utils.UISKipUtils;
import utils.helper.ToastHelper;

/**
 * 用户
 * Mode.BOTH：同时支持上拉下拉
 * Mode.PULL_FROM_START：只支持下拉Pulling Down
 * Mode.PULL_FROM_END：只支持上拉Pulling Up
 */
public class UserFragment extends FMBaseScrollFragment implements View.OnClickListener, ReceiverUtils.MessageReceiver
         {

    private UserHeadView mHeader;
    private RelativeLayout bingSetting;
    private TextView ivBack;
    private ImageView tv_num;
//    private BadgeView mBadgeView;
    private TextView tv_realname, tv_realcar;
    private RelativeLayout rlEndorsementManagement;
    private Activity mActivity;
    private UserInfoEntity userInfo;

    @Override
    public void onMessage(int receiverType, Bundle bundle) {

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() == null) return;
    }
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        this.mActivity =context;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable
    Bundle savedInstanceState) {
        return inflater.inflate(R.layout.frame_user_scroll_view, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        ReceiverUtils.addReceiver(this);
        bindRefreshScrollAdapter((RefreshScrollView) getView().findViewById(R.id.refresh_scroll),
                R.layout.include_user_centre_v3);
        getRefreshScrollView().setMode(RefreshBase.Mode.PULL_FROM_START);
        mHeader = (UserHeadView) getView().findViewById(R.id.header);
        mHeader.setOnClickListener(this);
        ivBack = (TextView) getView().findViewById(R.id.iv_back);
        tv_num = (ImageView) getView().findViewById(R.id.tv_num);
        tv_realname = (TextView) getView().findViewById(R.id.tv_realname);
        tv_realcar = (TextView) getView().findViewById(R.id.tv_realcar);
        getView().findViewById(R.id.tv_title_user).setOnClickListener(this);

        bingSetting = (RelativeLayout) getView().findViewById(R.id.bing_setting);
        bingSetting.setOnClickListener(this);
        getView().findViewById(R.id.rl_guide).setOnClickListener(this);
        getView().findViewById(R.id.iv_msg).setOnClickListener(this);
        getView().findViewById(R.id.iv_setting).setOnClickListener(this);
        getView().findViewById(R.id.rl_user_realname).setOnClickListener(this);
        getView().findViewById(R.id.rl_user_realcar).setOnClickListener(this);
        rlEndorsementManagement = (RelativeLayout) getView().findViewById(R.id.rl_endorsement_management);
        rlEndorsementManagement.setOnClickListener(this);
        startRefreshState();
//        if (!FMWession.getInstance().isLogin()) {
//            startRefreshState();
//        } else {
//            noRefresh();
//        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        ReceiverUtils.removeReceiver(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_msg:
                UISKipUtils.startMessageActivity(getActivity());
                break;
        }
    }

     @Override
      public void onReload() {
         userInfoRequest();
      }

      @Override
      public void loadMore() {

      }

     public void stopRefreshState(){
         stop();
     }

     private void  userInfoRequest(){
         SharedPreferences sharedPreferences= mActivity.getSharedPreferences("user",
                 Activity.MODE_PRIVATE);
         String token=sharedPreferences.getString("token", "");
         Map<String, String> staff = new HashMap<String, String>();
         staff.put("tokenId",token);
         OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.INFORMATION,
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
                                     if(!js.isNull("information")){
                                         JSONObject jsonobj = js.optJSONObject("information");
                                         userInfo = new UserInfoEntity(jsonobj);
                                         mHeader.formatData(userInfo);
                                         stopRefreshState();
                                     }

                                 } else {
                                     ToastHelper.toastMessage(getActivity(), msg);
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

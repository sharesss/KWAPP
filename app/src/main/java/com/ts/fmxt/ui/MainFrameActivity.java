package com.ts.fmxt.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;

import com.thindo.base.UI.Activity.BaseFragmentActivity;
import com.thindo.base.Utils.AppManager;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.discover.ConsumerFragment;
import com.ts.fmxt.ui.user.fragment.UserFragment;

import java.util.HashMap;
import java.util.Map;

import http.data.CallbackInfo;
import utils.Tools;
import utils.helper.ToastHelper;
import widget.MainFragmentBottomLayout;


/**
 * @author kp
 * @Description:描述:主页FrameActivity
 */
public class MainFrameActivity extends BaseFragmentActivity implements MainFragmentBottomLayout.BottomItemOnClick{


    private MainFragmentBottomLayout mBottomLayout;

    private FragmentManager mFragmentManager;
    private HashMap<Integer, Fragment> mFragmentMap;
    private View inflate;
//    private SweetAlertDialog dialog;
    private int activity_id;
    private String circle_name;
    private boolean mlink1;
    private UserFragment user;
    private int id;

    //private LocationService locationService;
    //初始化高德地图


    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        inflate = View.inflate(this, R.layout.activity_main_fragment, null);
        setContentView(inflate);
        initView();
        initFragmentManager();
        id  =getIntent().getIntExtra("id",0);
        if(id!=0){
            setCurrentFragment(id); //这里是指定跳转到指定的fragment
        }
        if(id==2){
            mBottomLayout.currentIndex();
        }
        //initBaidu();
//        ReceiverUtils.addReceiver(this);
    }

    /**/
    private void initFragmentManager() {
        mFragmentMap = new HashMap<Integer, Fragment>();
        mFragmentManager = getSupportFragmentManager();
        setCurrentFragment(1);
    }

    /**/
    private void setCurrentFragment(int index) {
        FragmentTransaction mTransaction = mFragmentManager.beginTransaction();
        initFragement(index, mTransaction);
        for (Map.Entry<Integer, Fragment> entry : mFragmentMap.entrySet()) {
            mTransaction.hide(entry.getValue());
        }


        mTransaction.show(mFragmentMap.get(index));
        mTransaction.commitAllowingStateLoss();
//        if (!FMWession.getInstance().isLogin()) {
//            messgeRequest();
//        } else {//隐藏消息数量
//            mBottomLayout.showMsg(0);
//        }
    }

    private void initFragement(int index, FragmentTransaction mTransaction) {

        if (mFragmentMap.containsKey(index)) {
            return;
        }

        switch (index) {
            case 0:
//                mFragmentMap.put(index, new NearByFragment());
                break;
            case 1:
                mFragmentMap.put(index, new ConsumerFragment());
                break;
            case 2:
                //
                user = new UserFragment();
                mFragmentMap.put(index, user);
                break;
            default:
                break;
        }
        mTransaction.add(R.id.fragment_manager_layout, mFragmentMap.get(index));
    }

    public void initView() {
        mBottomLayout = (MainFragmentBottomLayout) findViewById(R.id.bottom_navigation_layout);
        mBottomLayout.setOnItemClick(this);



    }

    @Override
    public void onItemClick(CallbackInfo mCallbackInfo) {
//        if (mCallbackInfo.getPosition() == 0) {
//            NearByFragment mNearByFragment = (NearByFragment) mFragmentMap.get(0);
//            if (mNearByFragment != null)
//                mNearByFragment.showLeftImage();
//        }
        setCurrentFragment(mCallbackInfo.getPosition());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        if (user != null) {
//            user.initMsgInfo();
//            messgeRequest();
//        }
//        if (FMWession.getInstance().getUserInfo() != null)
//            userInfoRequest();
//
//
//        ConsumerFragment mConsumerFragment = (ConsumerFragment) mFragmentMap.get(1);
//        if (mConsumerFragment != null)
//            mConsumerFragment.notifyChage();
//
//        if (FMWession.getInstance().isLuckyUser())
//            sysDictionariesRequest();
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent
                .ACTION_UP) {

            if (Tools.isFastDoubleClick()) {
                AppManager.getInstance().exitApp();
                finish();
            } else {
                ToastHelper.toastMessage(getBaseContext(), "再按一次退出疯蜜");
            }
        }
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }



}

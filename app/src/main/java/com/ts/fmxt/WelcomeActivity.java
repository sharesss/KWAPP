package com.ts.fmxt;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.telephony.TelephonyManager;
import android.widget.HorizontalScrollView;

import com.thindo.base.NetworkAPI.BaseResponse;
import com.thindo.base.NetworkAPI.OnResponseListener;
import com.thindo.base.UI.Activity.BaseActivity;

import cn.jpush.android.api.JPushInterface;
import utils.UISKipUtils;


/**
 * Created by A1 on 2016/1/15.
 */
public class WelcomeActivity extends BaseActivity implements OnResponseListener {
    private HorizontalScrollView hsv_welcome;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        //去除title
        setContentView(R.layout.activity_welcome);
        hsv_welcome = (HorizontalScrollView) findViewById(R.id.hsv_welcome);
//        Timer timer=new Timer();
//        timer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                hsv_welcome.fullScroll(HorizontalScrollView.FOCUS_RIGHT);
//            }
//        },1000);
//        .scrollTo(0,100);
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences setting = getSharedPreferences("YOUR_PREF_FILE_NAME", 0);
                Boolean user_first = setting.getBoolean("FIRST",true);
                if(user_first){//第一次
                    setting.edit().putBoolean("FIRST", false).commit();
                    Intent intent = new Intent(WelcomeActivity.this, GuideActivity.class);
                    startActivity(intent);
                }else{
                    UISKipUtils.startMainFrameActivity(WelcomeActivity.this);
                }

                finish();
            }
        }, 1200);

        TelephonyManager mTm = (TelephonyManager)this.getSystemService(TELEPHONY_SERVICE);
//        FMBApplication.IMEI=mTm.getSubscriberId();
//        if(!FMWession.getInstance().isLogin()){
//            userInfoRequest();
//        }
    }

    public void userInfoRequest() {
//        UserInfoRequest request = new UserInfoRequest();
//        request.setRequestType(1);
//        request.setOnResponseListener(this);
//        request.executePost(false);
    }





    @Override
    public void onStart(BaseResponse response) {

    }

    @Override
    public void onFailure(BaseResponse response) {

    }

    @Override
    public void onSuccess(BaseResponse response) {

    }
    @Override
    protected void onResume()
    {
        super.onResume();
        JPushInterface.onResume(this);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        JPushInterface.onPause(this);
    }

}

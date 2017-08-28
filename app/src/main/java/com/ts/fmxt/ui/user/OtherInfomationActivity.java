package com.ts.fmxt.ui.user;/**
 * Created by A1 on 2017/7/28.
 */

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.base.activity.FMBaseActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import http.data.UserInfoEntity;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.StringUtils;
import utils.UISKipUtils;
import utils.helper.ToastHelper;
import widget.image.CircleImageView;

import static com.ts.fmxt.R.id.iv_portrait;
import static com.ts.fmxt.R.id.tv_company_name;
import static com.ts.fmxt.R.id.tv_user_name;

/**
 * created by kp at 2017/7/28
 * 他人信息
 */
public class OtherInfomationActivity extends FMBaseActivity implements View.OnClickListener{
    private CircleImageView ivPortrait;
    private TextView tvUserName,tvFmIdentity,tvCompanyName,tvAutograph,tv_fm_identity;
    private TextView tv_place,tv_salary,tv_age,tv_items_to_follow,tv_collection_items;
    private int userid;
    private UserInfoEntity userInfo;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_infomation);
        userid = getIntent().getIntExtra("userid",0);
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_finish).setOnClickListener(this);
        findViewById(R.id.rl_items_to_follow).setOnClickListener(this);
        findViewById(R.id.rl_collection_items).setOnClickListener(this);
        tv_items_to_follow = (TextView) findViewById(R.id.tv_items_to_follow);
         tv_collection_items= (TextView) findViewById(R.id.tv_collection_items);
        ivPortrait = (CircleImageView) findViewById(iv_portrait);
        tvUserName = (TextView) findViewById(tv_user_name);
        tvFmIdentity = (TextView) findViewById(R.id.tv_fm_identity);
        tvCompanyName = (TextView) findViewById(tv_company_name);
        tvAutograph = (TextView) findViewById(R.id.tv_autograph);
        tv_place = (TextView) findViewById(R.id.tv_place);//地点
        tv_salary= (TextView) findViewById(R.id.tv_salary);//工资
        tv_age = (TextView) findViewById(R.id.tv_age);//年龄
        tv_fm_identity = (TextView) findViewById(R.id.tv_fm_identity);//职位
        userInfoRequest();
    }

    public void formatData(UserInfoEntity info) {

        if (!StringUtils.isEmpty(info.getHeadpic()))
            ivPortrait.loadImage(info.getHeadpic());
        if(!info.getNickname().equals("null")) {
            tvUserName.setText(info.getNickname());
        }
        Drawable sexDrawble = getResources().getDrawable(info.getSex() == 1 ? R.mipmap.icon_nearby_sex_man : R.mipmap.icon_nearby_sex_woman);
        sexDrawble.setBounds(0, 0, sexDrawble.getMinimumWidth(), sexDrawble.getMinimumHeight());
        tvUserName.setCompoundDrawables(sexDrawble, null, null, null);

        if(!info.getResidence().equals("")){
            tv_place.setText(info.getResidence() + "/");
        }
        if(!info.getPosition().equals("")){
            tv_fm_identity.setText( info.getPosition()+"/" );
        }
        if(!info.getAnnualincome().equals("")){
            tv_salary.setText( info.getAnnualincome()+"/" );
        }
        if(!String.valueOf(info.getAge()).equals("")&&info.getAge()!=0) {
            tv_age.setText(info.getAge()+"");
        }
        if(!info.getCompany().equals("null")){
            tvCompanyName.setText(info.getCompany());
        }
        if(!info.getSignature().equals("null")){
            tvAutograph.setText(info.getSignature());
        }
        tv_items_to_follow.setText(info.getInvestprojectcount()+"");
        tv_collection_items.setText(info.getInvestprojectcollectcount()+"");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_finish:
                finish();
                break;
            case R.id.rl_items_to_follow:
                UISKipUtils.startFollowProject(this,userid);
                break;
            case R.id.rl_collection_items:
                UISKipUtils.startCollectionProject(this,userid);
                break;
        }

    }

    private void  userInfoRequest(){
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("tokenId","");
        staff.put("userId",String.valueOf(userid));

        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.INFORMATION,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
//                        stopRefreshState();
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
                                        formatData(userInfo);

                                    }
//                                    stopRefreshState();
                                } else {
                                    ToastHelper.toastMessage(OtherInfomationActivity.this, msg);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
//                            stopRefreshState();
                        }
                    }
                }, staff
        );
    }
}
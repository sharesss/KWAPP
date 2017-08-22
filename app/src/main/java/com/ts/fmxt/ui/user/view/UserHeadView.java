package com.ts.fmxt.ui.user.view;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thindo.base.NetworkAPI.BaseResponse;
import com.thindo.base.NetworkAPI.OnResponseListener;
import com.ts.fmxt.R;

import http.data.UserInfoEntity;
import utils.StringUtils;
import utils.UISKipUtils;
import widget.image.CircleImageView;

/**
 * 用户头部
 */
public class UserHeadView extends RelativeLayout implements View.OnClickListener, OnResponseListener {
    private Context context;
    private CircleImageView iv_portrait;
    private TextView tv_user_name, tv_fm_identity, tv_user_signature, tv_consumere,tv_place,tv_salary,tv_age;
    private TextView tv_asset, tv_fb, tv_gift_num, tv_profile,tv_privilege_securities_num,tv_company_name,tv_investor;
    private UserInfoEntity info;

    public UserHeadView(Context context) {
        super(context);
        initNavigation();
        this.context = context;
    }

    public UserHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initNavigation();
        this.context = context;
    }

    public UserHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initNavigation();
        this.context = context;
    }

    private void initNavigation() {
        LayoutInflater.from(getContext()).inflate(R.layout.header_user_view, this);
        iv_portrait = (CircleImageView) findViewById(R.id.iv_portrait);//头像
        tv_user_name = (TextView) findViewById(R.id.tv_user_name);//名称
        tv_fm_identity = (TextView) findViewById(R.id.tv_fm_identity);//职位
        tv_user_signature = (TextView) findViewById(R.id.tv_autograph);//个性签名
        tv_asset = (TextView) findViewById(R.id.tv_asset);//想跟投项目个数
        tv_consumere = (TextView) findViewById(R.id.tv_consumere);//审核状态
        tv_fb = (TextView) findViewById(R.id.tv_fb);//收藏数量
        tv_place = (TextView) findViewById(R.id.tv_place);//地点
        tv_salary= (TextView) findViewById(R.id.tv_salary);//工资
        tv_age = (TextView) findViewById(R.id.tv_age);//年龄
        tv_investor = (TextView) findViewById(R.id.tv_investor);
        tv_profile = (TextView) findViewById(R.id.tv_profile);
        tv_gift_num = (TextView) findViewById(R.id.tv_gift_num);
        tv_company_name = (TextView) findViewById(R.id.tv_company_name);//公司名称
//        tv_privilege_securities_num = (TextView) findViewById(R.id.tv_privilege_securities_num);
//        findViewById(R.id.bt_edit_user).setOnClickListener(this);//编辑用户资料
        findViewById(R.id.rl_consumere).setOnClickListener(this);//认证投资人
        findViewById(R.id.rl_asset).setOnClickListener(this);//想跟投的项目
        findViewById(R.id.rl_fmb).setOnClickListener(this);//我的FB
        findViewById(R.id.rl_profile).setOnClickListener(this);//我的F豆
        findViewById(R.id.rl_gift).setOnClickListener(this);//关于我们
//        findViewById(R.id.rl_privilege_securities).setOnClickListener(this);//我的特权劵
        findViewById(R.id.rly_paren).setOnClickListener(this);//整个头部
    }


    public void formatData(UserInfoEntity info) {

        this.info = info;
        if (!StringUtils.isEmpty(info.getHeadpic()))
            iv_portrait.loadImage(info.getHeadpic());
        if(!info.getNickname().equals("null")) {
            tv_user_name.setText(info.getNickname());
        }
        Drawable sexDrawble = getResources().getDrawable(info.getSex() == 1 ? R.mipmap.icon_nearby_sex_man : R.mipmap.icon_nearby_sex_woman);
        sexDrawble.setBounds(0, 0, sexDrawble.getMinimumWidth(), sexDrawble.getMinimumHeight());
        tv_user_name.setCompoundDrawables(sexDrawble, null, null, null);
        if(!info.getSignature().equals("null")){
            tv_user_signature.setText(info.getSignature());
        }

        if(!info.getResidence().equals("null")){
            tv_place.setText(info.getResidence() + "/");
        }
        if(!info.getPosition().equals("null")){
            tv_fm_identity.setText( info.getPosition()+"/" );
        }
        if(!info.getAnnualincome().equals("null")){
            tv_salary.setText( info.getAnnualincome()+"/" );
        }
        if(!String.valueOf(info.getAge()).equals("null")&&info.getAge()!=0) {
            tv_age.setText(info.getAge()+"");
        }

        //审核状态：'状态：0删除，1待审，2审核通过，3审核不通过',
        if(info.getAuditstate()==0){
            tv_consumere.setText("未审核");
        }else if (info.getAuditstate()==1){
            tv_consumere.setText("待审核");
        }else if (info.getAuditstate()==2){
            tv_consumere.setText("审核通过");
        }else if (info.getAuditstate()==3){
            tv_consumere.setText("审核不通过");
        }else{
            tv_consumere.setText("未审核");
        }
       if(info.getIsinvestauthen()==1){
           tv_investor.setVisibility(View.VISIBLE);
       }

        tv_asset.setText(info.getInvestprojectcount()+"");
        tv_fb.setText(info.getInvestprojectcollectcount()+"");
        if(!info.getCompany().equals("null")){
            tv_company_name.setText(info.getCompany());
        }

    }

    public void recoveryView() {
        iv_portrait.setImageResource(R.mipmap.def_02);
        tv_user_name.setText(getContext().getResources().getString(R.string.text_no_login));
        tv_user_name.setCompoundDrawables(null, null, null, null);
        tv_user_signature.setText("");
//        tv_fm_identity.setText(String.format("%s%s", getResources().getString(R.string.text_user_fm_identity), ""));
        tv_asset.setText(String.format("%s元", "0"));
        tv_fb.setText(String.format("%sFB", "0"));
        tv_gift_num.setText("0");

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_asset://想跟投的项目
                UISKipUtils.startFollowProject((Activity) getContext());
                break;
            case R.id.rl_fmb://收藏的项目
                UISKipUtils.startCollectionProject((Activity) getContext());
                break;
            case R.id.rl_gift://关于我们
//                UISKipUtils.startOtherInfomation((Activity) getContext());
                UISKipUtils.satrtUserAgreement((Activity) getContext(), getResources().getString(R.string.html_fm_user_about_us), getResources().getString(R.string.about_us));
                break;
//            case R.id.rl_privilege_securities://我的特权劵
//                if (info == null) return;
//                UISkipUtils.startPrivilegeSecuritiesActivity((Activity) getContext());
//                break;
//            case R.id.tv_click:
            case R.id.rly_paren:
                UISKipUtils.startEditUserInfoActivity((Activity) getContext());
                break;
//            case R.id.iv_portrait:
//                if (FMWession.getInstance().isLogin()) {
//                    UISkipUtils.startLoginActivity((Activity) getContext());
//                    return;
//                }
//                if (info == null) return;
//                UISkipUtils.startOtherUserActivity((Activity) getContext(), info.getId(), 1, "0");
//                break;
//
//            case R.id.rl_msg:
////                UISkipUtils.startMessageListActivity((Activity) getContext());
//                UISkipUtils.showMes((Activity) getContext(), "消息列表");
//                break;
//
//            case R.id.rly_parent:
////                if (FMWession.getInstance().isLogin()) {
////                    FMWession.getInstance().setRefreshUser(true);
////                    UISkipUtils.startLoginActivity((Activity) getContext());
////                    return;
////                }
//                //  UISkipUtils.startOUserFBActivity((Activity) context);
//                break;
            case R.id.rl_profile://用户协议
                UISKipUtils.satrtUserAgreement((Activity) getContext(), getResources().getString(R.string.html_fm_user_agreement), getResources().getString(R.string.user_agreement));
                break;
            case R.id.rl_consumere://认证
                UISKipUtils.startCertifiedInvestorActivity((Activity) getContext(),info.getAuditstate());
                break;

        }
    }


    @Override
    public void onStart(BaseResponse response) {

    }

    @Override
    public void onFailure(BaseResponse response) {

    }

    @Override
    public void onSuccess(BaseResponse response) {
        if (response.getStatus() == BaseResponse.SUCCEED) {

        }
    }
}

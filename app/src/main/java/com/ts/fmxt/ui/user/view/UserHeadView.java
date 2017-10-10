package com.ts.fmxt.ui.user.view;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ts.fmxt.R;

import java.math.BigDecimal;

import http.data.UserInfoEntity;
import utils.StringUtils;
import utils.UISKipUtils;
import utils.helper.ToastHelper;
import widget.image.CircleImageView;
import widget.popup.BaseDoubleEventPopup;
import widget.popup.PopupObject;
import widget.popup.dialog.MessageContentDialog;

import static android.content.Context.MODE_PRIVATE;

/**
 * 用户头部
 */
public class UserHeadView extends RelativeLayout implements View.OnClickListener {
    private Context context;
    private CircleImageView iv_portrait;
    private TextView tv_user_name, tv_fm_identity, tv_user_signature, tv_consumere,tv_place,tv_salary,tv_age,tv_realname;
    private TextView tv_asset, tv_fb, tv_gift_num, tv_profile,tv_privilege_securities_num,tv_company_name,tv_investor,tv_phone,tv_money,tv_auction_num;
    private UserInfoEntity info;
    private int auctionNum;
    private String  cashdeposit;
    /**金额为分的格式 */

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
        tv_realname = (TextView)findViewById(R.id.tv_realname);
        tv_gift_num = (TextView) findViewById(R.id.tv_gift_num);
        findViewById(R.id.ll_callphone).setOnClickListener(this);
        tv_phone  = (TextView) findViewById(R.id.tv_phone);
        tv_money = (TextView) findViewById(R.id.tv_money);
        tv_auction_num = (TextView) findViewById(R.id.tv_auction_num);
        tv_company_name = (TextView) findViewById(R.id.tv_company_name);//公司名称
        findViewById(R.id.rl_consumere).setOnClickListener(this);//认证投资人
        findViewById(R.id.rl_asset).setOnClickListener(this);//想跟投的项目
        findViewById(R.id.rl_fmb).setOnClickListener(this);//我的FB
        findViewById(R.id.rl_profile).setOnClickListener(this);//我的F豆
        findViewById(R.id.rl_gift).setOnClickListener(this);//关于我们
        findViewById(R.id.rl_user_real_name).setOnClickListener(this);//实名认证
        findViewById(R.id.rly_paren).setOnClickListener(this);//整个头部

        //头部以下的四个按钮
        findViewById(R.id.bt_share_option).setOnClickListener(this);//我的跟投
        findViewById(R.id.bt_stake_in).setOnClickListener(this);//拍得股权
        findViewById(R.id.bt_transfer_record).setOnClickListener(this);//转账记录
        findViewById(R.id.bt_issue_equity).setOnClickListener(this);//发布股权
        findViewById(R.id.rl_frozen_deposit).setOnClickListener(this);//冻结保证金
        findViewById(R.id.rl_auction_project).setOnClickListener(this);//想竞拍的项目

    }


    public void formatData(UserInfoEntity info) {

        this.info = info;
        if (!StringUtils.isEmpty(info.getHeadpic()))
            iv_portrait.loadImage(info.getHeadpic());
        if(!info.getNickname().equals("")) {
            tv_user_name.setText(info.getNickname());
        }
        cashdeposit = BigDecimal.valueOf(Integer.valueOf(info.getCashdeposit())).divide(new BigDecimal(100)).toString();
        auctionNum = info.getThinkactioncount();//竞拍数
        tv_money.setText(cashdeposit+"");
        tv_auction_num.setText(auctionNum+"");
        SharedPreferences share = context.getSharedPreferences("ImInfo",MODE_PRIVATE);
        SharedPreferences.Editor editor = share.edit(); //使处于可编辑状态
        editor.putString("headpic", info.getHeadpic());
        editor.putString("name", info.getNickname());
        editor.commit();    //提交数据保存
        Drawable sexDrawble = getResources().getDrawable(info.getSex() == 1 ? R.mipmap.icon_nearby_sex_man : R.mipmap.icon_nearby_sex_woman);
        sexDrawble.setBounds(0, 0, sexDrawble.getMinimumWidth(), sexDrawble.getMinimumHeight());
        tv_user_name.setCompoundDrawables(sexDrawble, null, null, null);
        if(!info.getSignature().equals("null")){
            tv_user_signature.setText(info.getSignature());
        }

        StringBuilder inf =  new StringBuilder().append(!info.getResidence().equals("")&&!info.getResidence().equals("null")  ? info.getResidence()+"/":"").append(!info.getPosition().equals("")&&!info.getPosition().equals("null")? info.getPosition()+"/":"")
                .append(!info.getAnnualincome().equals("")&&!info.getAnnualincome().equals("null")?info.getAnnualincome()+"/":"").append(!String.valueOf(info.getAge()).equals("")&&!!String.valueOf(info.getAge()).equals("null")&&info.getAge()!=0?String.valueOf(info.getAge())+"/":"");
        if(inf.length()>1){
            inf.delete(inf.length()-1, inf.length());
        }

        tv_place.setText(inf);

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
        tv_investor.setVisibility(info.getIsinvestauthen()==1 ? View.VISIBLE :View.GONE);
        tv_asset.setText(info.getInvestprojectcount()+"");
        tv_fb.setText(info.getInvestprojectcollectcount()+"");
        if(!info.getCompany().equals("null")){
            tv_company_name.setText(info.getCompany());
        }
        tv_realname.setText((info.getIsTruenameAuthen() == 1 ? R.string.text_authorization_status_on : R.string.text_authorization_status_off));
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
                UISKipUtils.satrtUserAgreement((Activity) getContext(), getResources().getString(R.string.html_fm_user_about_us), getResources().getString(R.string.about_us));
                break;
            case R.id.rly_paren://整个头部
                UISKipUtils.startEditUserInfoActivity((Activity) getContext());
                break;
            case R.id.rl_user_real_name://实名认证
                if (info.getIsTruenameAuthen() == 1)
                    UISKipUtils.startRealNameSuccessActivity((Activity) getContext());
                else
                    UISKipUtils.startRealNameActivity((Activity) getContext());
                break;
            case R.id.rl_profile://用户协议
                UISKipUtils.satrtUserAgreement((Activity) getContext(), getResources().getString(R.string.html_fm_user_agreement), getResources().getString(R.string.user_agreement));
                break;
            case R.id.rl_consumere://认证
                UISKipUtils.startCertifiedInvestorActivity((Activity) getContext(),info.getAuditstate());
                break;
            case R.id.ll_callphone://呼叫客服
                MessageContentDialog mPopupDialogWidget = new MessageContentDialog((Activity) getContext());
                String phone =tv_phone.getText().toString();
                mPopupDialogWidget.setMessage(phone);
                mPopupDialogWidget.setRithButtonText(R.string.phone);
                mPopupDialogWidget.setOnEventClickListener(new BaseDoubleEventPopup.onEventClickListener() {

                    @Override
                    public void onEventClick(PopupObject obj) {
                        if (obj.getWhat() == 1) {
                            //打电话
                            String phone =tv_phone.getText().toString();
                            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                            context.startActivity(intent);
                        }

                    }
                });
                mPopupDialogWidget.showPopupWindow();
                break;

            case R.id.bt_share_option://我的跟投
                UISKipUtils.startMyHeelShotActivity((Activity) getContext());
                break;
            case R.id.bt_stake_in://拍得股权
                UISKipUtils.startStakeInActivity((Activity) getContext());
                break;
            case R.id.bt_transfer_record://转账记录
                UISKipUtils.startTransferRecordActivity((Activity) getContext());
                break;
            case R.id.bt_issue_equity://发布股权
                ToastHelper.toastMessage(getContext(),"功能开发中，敬请期待");
                break;
            case R.id.rl_frozen_deposit://冻结保证金
                UISKipUtils.startFrozenDepositActivity((Activity) getContext(),cashdeposit);
                break;
            case R.id.rl_auction_project://想竞拍的项目
                UISKipUtils.startAuctionProjectActivity((Activity) getContext());
                break;
        }
    }

}

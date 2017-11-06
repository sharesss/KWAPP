package com.ts.fmxt.ui.user.Certifiedinvestor;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ts.fmxt.R;
import com.ts.fmxt.ui.base.activity.FMBaseActivity;
import com.ts.fmxt.ui.user.view.PopupWheelCurrencyTypeView;
import com.ts.fmxt.ui.user.view.PopupWheelIndustryView;
import com.ts.fmxt.ui.user.view.PopupWheelIvestmentRundView;
import com.ts.fmxt.ui.user.view.PopupWheelTimeView;
import com.ts.fmxt.ui.user.view.WheelListener;

import utils.ReceiverUtils;
import utils.UISKipUtils;
import utils.helper.ToastHelper;

import static com.ts.fmxt.R.id.ll_currency_type;

/**
 * Created by kp on 2017/10/19.
 * 投资记录
 */

public class InvestmentRecordActivity  extends FMBaseActivity implements View.OnClickListener,WheelListener, ReceiverUtils.MessageReceiver {
    private EditText edt_input_project_num,edt_historical_investment,edt_out_num,edt_project_name,edt_input_money;
    private TextView tv_industry,tv_time,tv_currency_type,tv_investment_round,edt_return_multiples;
    private PopupWheelTimeView mPopupWheelView;
    private PopupWheelIndustryView mPopupWheelIndustryView;
    PopupWheelCurrencyTypeView mpopupWheelCurrencyTypeView;
    private PopupWheelIvestmentRundView mPopupWheelIvestmentRundView;

    @Override
    public void onMessage(int receiverType, Bundle bundle) {
         if(receiverType == ReceiverUtils.CERTIFIEDINVESTOR_FINISH){
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_investment_record);
        ReceiverUtils.addReceiver(this);
        initView();
    }

    private void initView(){
        findViewById(R.id.btn_finish).setOnClickListener(this);
        edt_input_project_num = (EditText) findViewById(R.id.edt_input_project_num);//项目总数
        edt_historical_investment = (EditText) findViewById(R.id.edt_historical_investment);//历史总额
        edt_out_num = (EditText) findViewById(R.id.edt_out_num);//退出总数
        edt_project_name = (EditText) findViewById(R.id.edt_project_name);//项目名称
        tv_industry = (TextView) findViewById(R.id.tv_industry);//行业
        tv_time = (TextView) findViewById(R.id.tv_time);//时间
        edt_input_money = (EditText) findViewById(R.id.edt_input_money);//金额
        tv_currency_type = (TextView) findViewById(R.id.tv_currency_type);//币种
        tv_investment_round = (TextView) findViewById(R.id.tv_investment_round);//投资轮次
        edt_return_multiples = (TextView) findViewById(R.id.edt_return_multiples);//回报倍数
        findViewById(R.id.ll_industry).setOnClickListener(this);
        findViewById(R.id.ll_time).setOnClickListener(this);
        findViewById(R.id.but_next).setOnClickListener(this );
        findViewById(R.id.ll_investment_round).setOnClickListener(this);
        findViewById(ll_currency_type).setOnClickListener(this);


    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_finish:
                finish();
                break;
            case ll_currency_type:
                mpopupWheelCurrencyTypeView = new PopupWheelCurrencyTypeView(this, tv_currency_type.getText().toString());
                mpopupWheelCurrencyTypeView.setWheelListener(this);
                mpopupWheelCurrencyTypeView.showPopupWindow();
                break;
            case R.id.ll_time://时间选择器
                mPopupWheelView = new PopupWheelTimeView(this);
                mPopupWheelView.setWheelListener(this);
                mPopupWheelView.showPopupWindow();
                break;
            case R.id.ll_industry://行业选择器
                mPopupWheelIndustryView = new PopupWheelIndustryView(this, tv_industry.getText().toString());
                mPopupWheelIndustryView.setWheelListener(this);
                mPopupWheelIndustryView.showPopupWindow();
                break;
            case R.id.ll_investment_round://融资轮次选择器
                mPopupWheelIvestmentRundView = new PopupWheelIvestmentRundView(this, tv_investment_round.getText().toString());
                mPopupWheelIvestmentRundView.setWheelListener(this);
                mPopupWheelIvestmentRundView.showPopupWindow();
                break;
            case R.id.but_next://下一步
                if(edt_input_project_num.getText().toString().equals("")){
                    ToastHelper.toastMessage(this,"请输入投资项目数量");
                    return;
                }
                if(edt_historical_investment.getText().toString().equals("")){
                    ToastHelper.toastMessage(this,"请输入历史投资总金额");
                    return;
                }
                if(edt_out_num.getText().toString().equals("")){
                    ToastHelper.toastMessage(this,"请输入退出数量");
                    return;
                }
                if(edt_project_name.getText().toString().equals("")){
                    ToastHelper.toastMessage(this,"请输入投资项目名称");
                    return;
                }
                if(tv_industry.getText().toString().equals("")||tv_industry.getText().toString().equals("未填写")){
                    ToastHelper.toastMessage(this,"请选择行业");
                    return;
                }
                if(tv_time.getText().toString().equals("")||tv_time.getText().toString().equals("未填写")){
                    ToastHelper.toastMessage(this,"请选择时间");
                    return;
                }
                if(edt_input_money.getText().toString().equals("")){
                    ToastHelper.toastMessage(this,"请输入金额");
                    return;
                }
                if(tv_investment_round.getText().toString().equals("")||tv_investment_round.getText().toString().equals("未填写")){
                    ToastHelper.toastMessage(this,"请选择轮次");
                    return;
                }
                if(edt_return_multiples.getText().toString().equals("")){
                    ToastHelper.toastMessage(this,"请输入投资回报倍数");
                    return;
                }
                SharedPreferences share = getSharedPreferences("projectInfo",MODE_PRIVATE);
                SharedPreferences.Editor editor = share.edit(); //使处于可编辑状态
                editor.putString("projectNum", edt_input_project_num.getText().toString());
                editor.putString("historicalInvestment", edt_historical_investment.getText().toString());
                editor.putString("outNum", edt_out_num.getText().toString());
                editor.putString("projectName", edt_project_name.getText().toString());
                editor.putString("industry", tv_industry.getText().toString());
                editor.putString("industryId", industryId);
                editor.putString("time", tv_time.getText().toString());
                editor.putString("inputMoney", edt_input_money.getText().toString());
                editor.putString("investmentRound", tv_investment_round.getText().toString());
                editor.putString("roundId", roundId);
                editor.putString("returnMultiples", edt_return_multiples.getText().toString());
                editor.putString("currency_type", tv_currency_type.getText().toString());
                editor.commit();    //提交数据保存
                UISKipUtils.startUploadPhotoActivity(InvestmentRecordActivity.this);
                break;
        }
    }
    private  String industryId,roundId;
    @Override
    public void completeCall(String text, String text2, int type) {
        switch (type) {
            case 1://时间
                tv_time.setText(text);
                break;
            case 2://行业
                tv_industry.setText(text);
                industryId = text2;
                break;
            case 3://投资轮次
                tv_investment_round.setText(text);
                roundId = text2;
                break;
            case 4://投资轮次
                tv_currency_type.setText(text);
                break;

        }
    }

    @Override
    public void clearCall(int type) {
        String str = getResourcesStr(R.string.user_info_nodata);
        switch (type) {
            case 1://年龄
                tv_time.setText(str);
                break;
            case 2://年薪
                tv_industry.setText(str);
                break;
            case 3://投资轮次
                tv_investment_round.setText(str);
                break;
            case 4://投资轮次
                tv_currency_type.setText(str);
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ReceiverUtils.removeReceiver(this);
    }
}

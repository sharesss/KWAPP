package com.ts.fmxt.ui.user.Certifiedinvestor;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.base.activity.FMBaseActivity;
import com.ts.fmxt.ui.discover.view.FlowLayout;
import com.ts.fmxt.ui.user.view.PopupWheelCurrencyTypeView;
import com.ts.fmxt.ui.user.view.WheelListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import http.data.InvestmentPreferenceEntity;
import http.data.UserIndustryEntity;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.ReceiverUtils;
import utils.helper.ToastHelper;

import static com.ts.fmxt.R.id.ll_currency_type;


/**
 * Created by kp on 2017/10/25.
 * 设置投资偏好
 */

public class SettingInvestmentPreferenceActivity  extends FMBaseActivity implements View.OnClickListener,WheelListener {
    ArrayList<UserIndustryEntity> arr=new ArrayList<>();
    ArrayList<UserIndustryEntity> arr2=new ArrayList<>();
    ArrayList<Integer>  industryIds=new ArrayList<>();
    ArrayList<Integer>  raundIds=new ArrayList<>();
    ArrayList<String>  industryNames=new ArrayList<>();
    ArrayList<String>  raundNames=new ArrayList<>();
    ArrayList<String> arr3=new ArrayList<String>();
    ArrayList<String> arr4=new ArrayList<String>();
    InvestmentPreferenceEntity info;
    PopupWheelCurrencyTypeView mpopupWheelCurrencyTypeView;
    private EditText edt_mini_num,edt_maxi_num;
    private TextView tv_currency_type;
    private String checktext ="",checktext2 = "",checktext3 = "",checktext4 = "";
    FlowLayout flow_layout,flow_round_layout,flow_return_layout,flow_iswell_known_layout;
    private int fieldSecond=0,roundSecond=0;
    private int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_investment_preference);
        type = getIntent().getIntExtra("type",-1);
//        ReceiverUtils.addReceiver(this);
        initView();

    }

    private void initView(){
        findViewById(R.id.btn_finish).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
        edt_mini_num = (EditText) findViewById(R.id.edt_mini_num);
        edt_maxi_num = (EditText) findViewById(R.id.edt_maxi_num);
        flow_layout = (FlowLayout) findViewById(R.id.flow_layout);
        flow_round_layout = (FlowLayout) findViewById(R.id.flow_round_layout);
        flow_return_layout = (FlowLayout) findViewById(R.id.flow_return_layout);
        tv_currency_type = (TextView) findViewById(R.id.tv_currency_type);
        flow_iswell_known_layout = (FlowLayout) findViewById(R.id.flow_iswell_known_layout);
        findViewById(ll_currency_type).setOnClickListener(this);
        if(type==1){
            //个人中心直接进来的
            findUserInvestPreferenceRequest();
        }else{
            initInvestmentPreferenceData();
        }

    }
    private void initstateDeta(){
        arr3.add("定期分红");
        arr3.add("股权增值");
        for (int i = 0; i < arr3.size(); i++) {
            final TextView view = new TextView(this);
            view.setText(arr3.get(i));
            view.setTextColor(Color.GRAY);
            view.setPadding(5, 5, 5, 5);
            view.setGravity(Gravity.CENTER);
            view.setTextSize(14);
            // 设置背景选择器到TextView上
            Resources resources = getResources();
            Drawable btnDrawable = resources.getDrawable(R.drawable.bg_gray_tag_shape);
            view.setBackground(btnDrawable);
            if(view.getText().toString().equals("定期分红")){
                view.setBackground(resources.getDrawable(R.drawable.bg_orange_5_shape));
                view.setTextColor(Color.WHITE);
                checktext3 = view.getText().toString();
            }
            if(info!=null){
                if(info.getReturnWay()==1){
                    if(view.getText().toString().equals("定期分红")){
                        view.setBackground(resources.getDrawable(R.drawable.bg_orange_5_shape));
                        view.setTextColor(Color.WHITE);
                        checktext3 = view.getText().toString();
                    }
                }else if(info.getReturnWay()==2){
                    if(view.getText().toString().equals("股权增值")){
                        view.setBackground(resources.getDrawable(R.drawable.bg_orange_5_shape));
                        view.setTextColor(Color.WHITE);
                        checktext3 = view.getText().toString();
                    }
            }


            }
            // 设置点击事件
//            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = ((TextView) v).getText().toString();
                    Resources resources = getResources();
                    v.setBackground(resources.getDrawable(R.drawable.bg_orange_5_shape));
                    ((TextView) v).setTextColor(Color.WHITE);
                    for (int i = 0; i < flow_return_layout.getChildCount(); i++) {
                        View indexview = flow_return_layout.getChildAt(i);
                        if (indexview instanceof TextView) {
                            TextView indexTextView = (TextView) indexview;
                            if (indexTextView.getText().equals(checktext3)) {
                                indexTextView.setBackground(resources.getDrawable(R.drawable.bg_gray_tag_shape));
                                indexTextView.setTextColor(Color.GRAY);
                                break;
                            }
                        }
                    }
                    checktext3 = name;
                }
            });
            flow_return_layout.addView(view);
        }
        arr4.add("是");
        arr4.add("否");
        for (int i = 0; i < arr4.size(); i++) {

            final TextView view = new TextView(this);
            view.setText(arr4.get(i));
            view.setTextColor(Color.GRAY);
            view.setPadding(5, 5, 5, 5);
            view.setGravity(Gravity.CENTER);
            view.setTextSize(14);
            // 设置背景选择器到TextView上
            Resources resources = getResources();
            Drawable btnDrawable = resources.getDrawable(R.drawable.bg_gray_tag_shape);
            view.setBackground(btnDrawable);
            if (view.getText().toString().equals("是")) {
                view.setBackground(resources.getDrawable(R.drawable.bg_orange_5_shape));
                view.setTextColor(Color.WHITE);
                checktext4 = view.getText().toString();
            }
            if(info!=null){
                if(info.getIsFamousInvestor()==0){
                    if(view.getText().toString().equals("否")){
                        view.setBackground(resources.getDrawable(R.drawable.bg_orange_5_shape));
                        view.setTextColor(Color.WHITE);
                        checktext4 = view.getText().toString();
                    }
                }else if(info.getIsFamousInvestor()==1) {
                    if (view.getText().toString().equals("是")) {
                        view.setBackground(resources.getDrawable(R.drawable.bg_orange_5_shape));
                        view.setTextColor(Color.WHITE);
                        checktext4 = view.getText().toString();
                    }
                }
            }

            // 设置点击事件
//            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = ((TextView) v).getText().toString();
                    Resources resources = getResources();
                    v.setBackground(resources.getDrawable(R.drawable.bg_orange_5_shape));
                    ((TextView) v).setTextColor(Color.WHITE);
                    for (int i = 0; i < flow_iswell_known_layout.getChildCount(); i++) {
                        View indexview = flow_iswell_known_layout.getChildAt(i);
                        if (indexview instanceof TextView) {
                            TextView indexTextView = (TextView) indexview;
                            if (indexTextView.getText().equals(checktext4)) {
                                indexTextView.setBackground(resources.getDrawable(R.drawable.bg_gray_tag_shape));
                                indexTextView.setTextColor(Color.GRAY);
                                break;
                            }
                        }
                    }
                    checktext4 = name;
                }
            });
            flow_iswell_known_layout.addView(view);
        }
    }

    private void initDeta(){

        for (int i = 0; i < arr.size(); i++) {
            final UserIndustryEntity infos = arr.get(i);
            final TextView view = new TextView(this);
            view.setText(infos.getName());
            view.setTextColor(Color.GRAY);
            view.setPadding(5, 5, 5, 5);
            view.setGravity(Gravity.CENTER);
            view.setTextSize(14);
            // 设置背景选择器到TextView上
            Resources resources = getResources();
            Drawable btnDrawable = resources.getDrawable(R.drawable.bg_gray_tag_shape);
            view.setBackground(btnDrawable);
            if(info!=null){
                final String industryId = info.getIndustryIds();
                String[] industryArray=industryId.split("\\,") ;
                for (int j = 0; j <industryArray.length; j++) {
                    String num = industryArray[j];
                    if(num.equals(infos.getId()+"")){
                        fieldSecond++;
                        industryIds.add(infos.getId());
                        industryNames.add(infos.getName());
                        view.setBackground(resources.getDrawable(R.drawable.bg_orange_5_shape));
                        view.setTextColor(Color.WHITE);
                    }
                }
            }


            // 设置点击事件
//            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = ((TextView) v).getText().toString();
                    Resources resources = getResources();
                    if(industryNames.size()>0){
                        for(int i =0;i<industryNames.size();i++){
                            String names =industryNames.get(i);
                            if(name.equals(names)){
                                v.setBackground(resources.getDrawable(R.drawable.bg_gray_tag_shape));
                                ((TextView) v).setTextColor(Color.GRAY);
                                fieldSecond--;
                                industryIds.remove(i);
                                industryNames.remove(name);
                                return;
                            }
                        }

                    }
                        if(fieldSecond>=5){
                            ToastHelper.toastMessage(SettingInvestmentPreferenceActivity.this,"最多只能选择5个");
                            return;
                        }

                    v.setBackground(resources.getDrawable(R.drawable.bg_orange_5_shape));
                    ((TextView) v).setTextColor(Color.WHITE);
                    fieldSecond++;
                    industryIds.add(infos.getId());
                    industryNames.add(infos.getName());

                    checktext = name;
                }
            });
            flow_layout.addView(view);
        }

    }

    private void initDate2(){
        for (int i = 0; i < arr2.size(); i++) {
            final UserIndustryEntity infos = arr2.get(i);
            final TextView view = new TextView(this);
            view.setText(infos.getName());
            view.setTextColor(Color.GRAY);
            view.setPadding(5, 5, 5, 5);
            view.setGravity(Gravity.CENTER);
            view.setTextSize(14);
            // 设置背景选择器到TextView上
            Resources resources = getResources();
            Drawable btnDrawable = resources.getDrawable(R.drawable.bg_gray_tag_shape);
            view.setBackground(btnDrawable);
            if(info!=null){
                String raundId = info.getRaundIds();
                String[] raundIdsArray=raundId.split("\\,") ;
                for (int j = 0; j <raundIdsArray.length; j++) {
                    String num = raundIdsArray[j];
                    if(num.equals(infos.getId()+"")){
                        view.setBackground(resources.getDrawable(R.drawable.bg_orange_5_shape));
                        view.setTextColor(Color.WHITE);
                        roundSecond++;
                        raundIds.add(infos.getId());
                        raundNames.add(infos.getName());
                    }
                }
            }

            // 设置点击事件
//            final int finalI = i;
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String name = ((TextView) v).getText().toString();
                    Resources resources = getResources();
                    if(raundNames.size()>0){
                        for(int i =0;i<raundNames.size();i++){
                            String names =raundNames.get(i);
                            if(name.equals(names)){
                                v.setBackground(resources.getDrawable(R.drawable.bg_gray_tag_shape));
                                ((TextView) v).setTextColor(Color.GRAY);
                                raundIds.remove(i);
                                raundNames.remove(name);
                                return;
                            }
                        }

                    }
                        v.setBackground(resources.getDrawable(R.drawable.bg_orange_5_shape));
                        ((TextView) v).setTextColor(Color.WHITE);
                        raundIds.add(infos.getId());
                        raundNames.add(infos.getName());


                    checktext2 = name;
                }
            });
            flow_round_layout.addView(view);
        }

    }

    private void initInvestmentPreferenceData(){
        getIndustryRequest();
        getRaundRequest();
        initstateDeta();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_finish:
                finish();
                break;
            case R.id.ll_currency_type:
                mpopupWheelCurrencyTypeView = new PopupWheelCurrencyTypeView(this, tv_currency_type.getText().toString());
                mpopupWheelCurrencyTypeView.setWheelListener(this);
                mpopupWheelCurrencyTypeView.showPopupWindow();
                break;
            case R.id.btn_register:
                if(edt_mini_num.getText().toString().equals("")){
                    ToastHelper.toastMessage(SettingInvestmentPreferenceActivity.this,"请输入最小值");
                    return;
                }
                if(edt_maxi_num.getText().toString().equals("")){
                    ToastHelper.toastMessage(SettingInvestmentPreferenceActivity.this,"请输入最大值");
                    return;
                }
                if(Integer.valueOf(edt_mini_num.getText().toString())>Integer.valueOf(edt_maxi_num.getText().toString())){
                    ToastHelper.toastMessage(SettingInvestmentPreferenceActivity.this,"最小值不能大于最大值");
                    return;
                }
                if(checktext3.equals("")){
                    ToastHelper.toastMessage(SettingInvestmentPreferenceActivity.this,"请选择回报方式");
                    return;
                }
                if(checktext4.equals("")){
                    ToastHelper.toastMessage(SettingInvestmentPreferenceActivity.this,"请选择是否知名投资机构");
                    return;
                }
                if(industryIds.size()<=0){
                    ToastHelper.toastMessage(SettingInvestmentPreferenceActivity.this,"请选择关注行业的领域");
                    return;
                }
                if(raundIds.size()<=0){
                    ToastHelper.toastMessage(SettingInvestmentPreferenceActivity.this,"关注投资轮次");
                    return;
                }

                addOrUpdateUserInvestPreferenceInfoRequest();
                break;
        }
    }

    private  String industryId,roundId;
    @Override
    public void completeCall(String text, String text2, int type) {
        switch (type) {
            case 4://投资轮次
                tv_currency_type.setText(text);
                break;

        }
    }

    @Override
    public void clearCall(int type) {
        String str = getResourcesStr(R.string.user_info_nodata);
        switch (type) {
            case 4://投资轮次
                tv_currency_type.setText(str);
                break;
        }
    }

    private void addOrUpdateUserInvestPreferenceInfoRequest(){
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("tokenId", token);
        if(info!=null){
            staff.put("id", info.getId()+"");
        }
        staff.put("investSumMin", edt_mini_num.getText().toString());//最小值
        staff.put("investSumMax", edt_maxi_num.getText().toString());//最大值
        if(checktext3.equals("定期分红")){
            staff.put("returnWay", "1");//回报方式
        }else  if(checktext3.equals("股权增值")){
            staff.put("returnWay", "2");//回报方式
        }
        if(checktext4.equals("否")){
            staff.put("isFamousInvestor", "0");//是否知名机构
        }else if(checktext4.equals("是")){
            staff.put("isFamousInvestor", "1");//是否知名机构
        }
        if(tv_currency_type.equals("RMB¥")){
            staff.put("investMoneyUnit", "1");//是否知名机构
        }else{
            staff.put("investMoneyUnit", "2");//是否知名机构
        }


        String industryId =industryIds.toString().substring(1,industryIds.toString().length()-1).replace(" ", "");
        staff.put("industryIds", industryId);//关注行业领域结果集
        String raundId =raundIds.toString().substring(1,raundIds.toString().length()-1).replace(" ", "");
        staff.put("raundIds", raundId);//关注投资轮次结果集
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.ADDORUPDATEUSERINVESTPREFERENCEINFO,
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
                                    Bundle bundle = new Bundle();
                                    ReceiverUtils.sendReceiver(ReceiverUtils.CERTIFIEDINVESTOR_FINISH,bundle);
                                    finish();
                                    ToastHelper.toastMessage(SettingInvestmentPreferenceActivity.this, msg);
                                } else {
                                    ToastHelper.toastMessage(SettingInvestmentPreferenceActivity.this, msg);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, staff
        );
    }
    private void getIndustryRequest(){
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("dataType", "1");
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.FINDBASICINFO,
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
                                    if (!js.isNull("infoBebans")) {
                                        JSONArray array = js.optJSONArray("infoBebans");
                                        for (int i = 0; i < array.length(); i++) {
                                            arr.add(new UserIndustryEntity(array.optJSONObject(i)));
                                        }
                                        initDeta();
                                    }

                                } else {
                                    ToastHelper.toastMessage(SettingInvestmentPreferenceActivity.this, msg);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, staff
        );


    }

    private void getRaundRequest(){
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("dataType", "2");
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.FINDBASICINFO,
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
                                    if (!js.isNull("infoBebans")) {
                                        JSONArray array = js.optJSONArray("infoBebans");
                                        for (int i = 0; i < array.length(); i++) {
                                            arr2.add(new UserIndustryEntity(array.optJSONObject(i)));
                                        }
                                        initDate2();
                                    }

                                } else {
                                    ToastHelper.toastMessage(SettingInvestmentPreferenceActivity.this, msg);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, staff
        );

    }

    private void findUserInvestPreferenceRequest(){
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("tokenId", token);
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.FINDUSERINVESTPREFERENCEINFO,
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
                                    if(!js.isNull("preference")) {
                                        info = new InvestmentPreferenceEntity(js.optJSONObject("preference"));
                                        edt_mini_num.setText(info.getInvestSumMin()+"");//
                                        edt_maxi_num.setText(info.getInvestSumMax()+"");//
                                        if(info.getInvestMoneyUnit()==1){
                                            tv_currency_type.setText("人民币¥");//
                                        }else{
                                            tv_currency_type.setText("US$");//
                                        }

                                    }
                                    initInvestmentPreferenceData();
                                } else {
                                    ToastHelper.toastMessage(SettingInvestmentPreferenceActivity.this, msg);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, staff
        );
    }


}

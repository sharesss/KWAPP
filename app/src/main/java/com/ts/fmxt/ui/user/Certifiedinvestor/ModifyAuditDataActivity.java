package com.ts.fmxt.ui.user.Certifiedinvestor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.thindo.base.Adapter.FMBaseGroupAdapter;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.base.activity.FMBaseActivity;
import com.ts.fmxt.ui.user.login.dialog.PopupPhotoView;
import com.ts.fmxt.ui.user.view.PopupWheelCurrencyTypeView;
import com.ts.fmxt.ui.user.view.PopupWheelIndustryView;
import com.ts.fmxt.ui.user.view.PopupWheelIvestmentRundView;
import com.ts.fmxt.ui.user.view.PopupWheelTimeView;
import com.ts.fmxt.ui.user.view.WheelListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import http.data.AuthenticationEntity;
import http.data.ConsumerImageEntity;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.FileUtils;
import utils.ImageCacheUitl;
import utils.QiNiuUtils;
import utils.ReceiverUtils;
import utils.StringUtils;
import utils.Tools;
import utils.UISKipUtils;
import utils.helper.ToastHelper;
import widget.image.FMNetImageView;
import widget.popup.BaseDoubleEventPopup;
import widget.popup.PopupObject;
import widget.popup.dialog.MessageContentDialog;
import widget.popup.dialog.PopupUploadDialog;

/**
 * Created by kp on 2017/10/26.
 * 修改审核资料
 */

public class ModifyAuditDataActivity extends FMBaseActivity implements View.OnClickListener,WheelListener,
        ReceiverUtils.MessageReceiver{
    private int type,id;
    private TextView btn_login;
    private EditText
            edt_input_project_num,edt_historical_investment,edt_out_num,edt_project_name,edt_input_money,edt_return_multiples;
    private TextView tv_industry,tv_time,tv_currency_type,tv_investment_round,btn_nexts,tv_next_add;
    private PopupWheelTimeView mPopupWheelView;
    private PopupWheelIndustryView mPopupWheelIndustryView;
    private PopupWheelIvestmentRundView mPopupWheelIvestmentRundView;
    PopupWheelCurrencyTypeView mpopupWheelCurrencyTypeView;
    private AuthenticationEntity info;
    private PopupUploadDialog mPopupUploadDialog;
    private ConsumerImageAdapter mConsumerImageAdapter;
    private String token;
//    private String path;
    private FMNetImageView iv_upimage;
    private int onSelectTag = -1;
    private int conten =-1;
    private LinearLayout ll_image_layout;
    private List<Object> imageList = new ArrayList<>();
    private RelativeLayout rl_upimage;

    @Override
    public void onMessage(int receiverType, Bundle bundle) {
        if (receiverType == ReceiverUtils.REGISTER_IMAGE_UPLOADER) {
            if(mPopupUploadDialog!=null)
                mPopupUploadDialog.dismiss();
            String url = bundle.getString("data");
            String dirPath = "file://" + bundle.getString("dirPath");
//            iv_upimage.loadImage(path);
            ((ConsumerImageEntity) imageList.get(onSelectTag)).setPath(dirPath);
            ((ConsumerImageEntity) imageList.get(onSelectTag)).setUrl(url);
            mConsumerImageAdapter.notifyDataSetChanged();

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_audit_data);
        ReceiverUtils.addReceiver(this);
        type = getIntent().getIntExtra("type",0);
        id = getIntent().getIntExtra("id",0);
        initView();
        initImage();
        authenticationV2Request();

    }

    private void initView(){
        edt_input_project_num = (EditText) findViewById(R.id.edt_input_project_num);
        edt_historical_investment = (EditText) findViewById(R.id.edt_historical_investment);//历史总额
        edt_out_num = (EditText) findViewById(R.id.edt_out_num);//退出总数
        edt_project_name = (EditText) findViewById(R.id.edt_project_name);//项目名称
        tv_industry = (TextView) findViewById(R.id.tv_industry);//行业
        tv_time = (TextView) findViewById(R.id.tv_time);//时间
        edt_input_money = (EditText) findViewById(R.id.edt_input_money);//金额
        tv_currency_type = (TextView) findViewById(R.id.tv_currency_type);//币种
        tv_investment_round = (TextView) findViewById(R.id.tv_investment_round);//投资轮次
        edt_return_multiples = (EditText) findViewById(R.id.edt_return_multiples);//回报倍数
        rl_upimage = (RelativeLayout) findViewById(R.id.rl_upimage);
        btn_nexts = (TextView) findViewById(R.id.btn_nexts);
        tv_next_add = (TextView) findViewById(R.id.tv_next_add);
        mPopupUploadDialog=new PopupUploadDialog(this);
        mPopupUploadDialog.setContext(R.string.text_image_upload);
        iv_upimage = (FMNetImageView) findViewById(R.id.iv_upimage);
        ll_image_layout = (LinearLayout) findViewById(R.id.ll_image_layout);
        btn_nexts.setOnClickListener(this);
        iv_upimage.setOnClickListener(this);
        findViewById(R.id.ll_industry).setOnClickListener(this);
        findViewById(R.id.ll_time).setOnClickListener(this);
        findViewById(R.id.ll_investment_round).setOnClickListener(this);
        findViewById(R.id.btn_finish).setOnClickListener(this);
        findViewById(R.id.ll_currency_type).setOnClickListener(this);
        btn_login = (TextView) findViewById(R.id.btn_login);
        if(type==1){
            btn_login.setText("审核成功");
            btn_nexts.setText("更新认证");
        }else if(type==2){
            btn_login.setText("审核失败");
            btn_nexts.setText("重新提交认证");
        }else if(type==3){
            btn_login.setText("审核中");
            rl_upimage.setVisibility(View.GONE);
            iv_upimage.setVisibility(View.GONE);
            btn_nexts.setVisibility(View.GONE);
            findViewById(R.id.ll_industry).setOnClickListener(null);
            findViewById(R.id.ll_time).setOnClickListener(null);
            findViewById(R.id.ll_investment_round).setOnClickListener(null);
            findViewById(R.id.ll_currency_type).setOnClickListener(null);
            edt_input_project_num.setCursorVisible(false);
            edt_input_project_num.setFocusable(false);
            edt_input_project_num.setFocusableInTouchMode(false);
            edt_historical_investment.setCursorVisible(false);
            edt_historical_investment.setFocusable(false);
            edt_historical_investment.setFocusableInTouchMode(false);
            edt_out_num.setCursorVisible(false);
            edt_out_num.setFocusable(false);
            edt_out_num.setFocusableInTouchMode(false);
            edt_project_name.setCursorVisible(false);
            edt_project_name.setFocusable(false);
            edt_project_name.setFocusableInTouchMode(false);
            edt_input_money.setFocusableInTouchMode(false);
            edt_input_money.setFocusable(false);
            edt_input_money.setFocusableInTouchMode(false);
            edt_return_multiples.setFocusableInTouchMode(false);
            edt_return_multiples.setFocusable(false);
            edt_return_multiples.setFocusableInTouchMode(false);

        }
    }

    private void initImage() {
        imageList.clear();
        String[] imageUrls =getResources().getStringArray(R.array.consuer_image_list);//new  String [9];
        for (int i = 0; i <imageUrls.length; i++) {
            ConsumerImageEntity info = new ConsumerImageEntity();
            info.setPosition(i);
            info.setContext(imageUrls[i]);
            imageList.add(info);
        }
        mConsumerImageAdapter = new ConsumerImageAdapter(this, imageList);
        mConsumerImageAdapter.setAdapter(ll_image_layout);
    }

    private void formatData(){
        if (info == null) {
            return;
        }
        edt_input_project_num.setText(String.valueOf(info.getInvestProjectSum()+""));
        edt_historical_investment.setText(String.valueOf(info.getHistoryInvestSum()+""));
        edt_out_num.setText(String.valueOf(info.getExitProjectSum()+""));
        edt_project_name.setText(info.getCaseProjectName());
        tv_industry.setText(info.getCaseIndustryName());
        tv_time.setText(info.getCaseProjectTime());
        edt_input_money.setText(String.valueOf(info.getCaseInvestMoney()+""));
        tv_investment_round.setText(info.getCaseFinancingState());
        edt_return_multiples.setText(info.getCaseInvestReward()+"");
        industryId = String.valueOf(info.getCaseIndustryId());
        if(info.getCaseInvestMoneyUnit()==1){
            tv_currency_type.setText("RMB¥");
        }else{
            tv_currency_type.setText("US$");
        }

        String photos = info.getAssetsPhotos();
        if(!photos.equals("null")){
            String[] sArray=photos.split("\\,") ;
            for (int i = 0; i <sArray.length; i++) {
                String photo = sArray[i].trim();
                ((ConsumerImageEntity) imageList.get(i)).setUrl(photo);
                onSelectTag =i;
                conten = i;
            }
            tv_next_add.setVisibility(View.VISIBLE);
            mConsumerImageAdapter.notifyDataSetChanged();
        }

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_finish:
                finish();
                break;
            case R.id.ll_currency_type:
                mpopupWheelCurrencyTypeView = new PopupWheelCurrencyTypeView(this, tv_currency_type.getText
                        ().toString());
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
                mPopupWheelIvestmentRundView = new PopupWheelIvestmentRundView(this, tv_investment_round.getText
                        ().toString());
                mPopupWheelIvestmentRundView.setWheelListener(this);
                mPopupWheelIvestmentRundView.showPopupWindow();
                break;
            case R.id.iv_upimage:
                if(conten>=4){
                    ToastHelper.toastMessage(this,"最多只能上传5张图片");
                    return;
                }
                selectDrawable();
                break;
            case R.id.btn_nexts://下一步
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
                if(tv_investment_round.getText().toString().equals("")||tv_investment_round.getText().toString
                        ().equals("未填写")){
                    ToastHelper.toastMessage(this,"请选择轮次");
                    return;
                }
                if(edt_return_multiples.getText().toString().equals("")){
                    ToastHelper.toastMessage(this,"请输入投资回报倍数");
                    return;
                }
                if(onSelectTag<=0){
                    ToastHelper.toastMessage(this,"至少上传1张照片");
                    return;
                }
                authenticationRequest();
                break;
        }
    }

    private void selectDrawable() {
        PopupPhotoView popup = new PopupPhotoView(this, false);
        popup.showPopupWindow();
    }

    private void qiNiuTokenRequest() {
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.TOKEN, new
                OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String u) {
                        if (u.contains("token")) {
                            try {
                                JSONObject js = new JSONObject(u);
                                token = js.optString("token");
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                    }
                });
    }

    //图片结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    conten++;
                    onSelectTag = conten;
                    ConsumerImageEntity info = (ConsumerImageEntity) imageList.get(onSelectTag);
                    if (StringUtils.isEmpty(info.getQiniuToken()))//获取七牛Token
                        qiNiuTokenRequest();
                    startPhotoZoom(data.getData());
                    break;
                case 1:
                    if (Tools.hasSdcard()) {
                        File tempFile = new File(FileUtils.getRootPath() + PopupPhotoView.IMAGE_FILE_NAME);
                        conten++;
                        onSelectTag = conten;
                        ConsumerImageEntity infos = (ConsumerImageEntity) imageList.get(onSelectTag);
                        if (StringUtils.isEmpty(infos.getQiniuToken()))//获取七牛Token
                            qiNiuTokenRequest();
                        startPhotoZoom(Uri.fromFile(tempFile));
                    }
                    break;
                case 2:
                    if (data == null)
                        return;
                    Bundle extras = data.getExtras();
                    if (extras == null)
                        return;
                    Bitmap photo = extras.getParcelable("data");
                    ImageCacheUitl imageCacheUitl = ImageCacheUitl.getInstetn();
                    String path = imageCacheUitl.getSDCarPath() + PopupPhotoView.IMAGE_FILE_NAME;
                    Boolean flg = imageCacheUitl.savaImage(PopupPhotoView.IMAGE_FILE_NAME, photo);
                    if (flg) {
                        if(mPopupUploadDialog!=null)
                            mPopupUploadDialog.dismiss();
                        mPopupUploadDialog.showPopupWindow();
                        QiNiuUtils.getInstance().uploadImageRequest(path, token);
                    }
                    break;
            }
        }
    }

    class ConsumerImageAdapter extends FMBaseGroupAdapter {

        public ConsumerImageAdapter(Context context, List<Object> arrayList) {
            super(context, arrayList);
        }

        @Override
        protected View getView(int position, View convertView, ViewGroup parent) {

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_release_consumer_image_view,
                    null);
            FMNetImageView iv = (FMNetImageView) convertView.findViewById(R.id.iv_picture);
            RelativeLayout rl_picture = (RelativeLayout) convertView.findViewById(R.id.rl_picture);
            ImageView iv_del = (ImageView) convertView.findViewById(R.id.iv_del);
            if(type==3){
                iv_del.setVisibility(View.GONE);
            }
            ConsumerImageEntity info = (ConsumerImageEntity) getItem(position);
            if (!StringUtils.isEmpty(info.getUrl())){
                if (TextUtils.isEmpty(info.getPath())){
                    iv.loadImage(info.getUrl());
                }else {
                    iv.loadImage(info.getPath());
                }
                Log.i("url",info.getUrl());
                iv.setVisibility(View.VISIBLE);
                rl_picture.setVisibility(View.VISIBLE);
                iv_del.setOnClickListener(new onImageItemClick(position));
                tv_next_add.setVisibility(View.VISIBLE);
            }else{
                iv.setVisibility(View.GONE);
                rl_picture.setVisibility(View.GONE);
            }

            return convertView;
        }

        class onImageItemClick implements View.OnClickListener {
            private int position;

            public onImageItemClick(int position) {
                this.position = position;

            }

            @Override
            public void onClick(View v) {
                onSelectTag = position;
                ConsumerImageEntity info = (ConsumerImageEntity) getList().get(position);
                if (StringUtils.isEmpty(info.getQiniuToken()))//获取七牛Token
                    qiNiuTokenRequest();
                if (StringUtils.isEmpty(info.getUrl())) {
                    PopupPhotoView popup = new PopupPhotoView(ModifyAuditDataActivity.this, false,
                            info.getLocatstion());
                    popup.showPopupWindow();
                } else {
                    String uri = "";
                    String name = "";
                    for (int i = 0; i < imageList.size(); i++) {
                        ConsumerImageEntity item = (ConsumerImageEntity) imageList.get(i);
                        if (!StringUtils.isEmpty(item.getUrl())) {
                            uri = uri + item.getUrl() + ",";
                            name = name + item.getContext() + ",";
                        }
                    }
//                    if (!StringUtils.isEmpty(uri))
                    MessageContentDialog mPopupDialogWidget = new MessageContentDialog
                            (ModifyAuditDataActivity.this);

                    mPopupDialogWidget.setMessage("确定删除吗？");
                    mPopupDialogWidget.setRithButtonText(R.string.dl_ok);
                    mPopupDialogWidget.setOnEventClickListener(new BaseDoubleEventPopup.onEventClickListener() {

                        @Override
                        public void onEventClick(PopupObject obj) {
                            if (obj.getWhat() == 1) {
                                //删除单张照片
                                ((ConsumerImageEntity) imageList.get(onSelectTag)).setUrl("");
                                mConsumerImageAdapter.notifyDataSetChanged();
                                onSelectTag--;
                                conten--;
                                if(onSelectTag<0){
                                    tv_next_add.setVisibility(View.GONE);
                                }
                            }

                        }
                    });
                    mPopupDialogWidget.showPopupWindow();
                }
            }
        }
    }



    /**
     * 裁剪图片方法实现
     */
    public void startPhotoZoom(Uri uri) {
//       String url= uri.toString();
//       url= url.replaceAll("content","file");
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");// 设置裁剪
        intent.putExtra("aspectX", 1);// aspectX aspectY 是宽高的比例
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 320);// outputX outputY 是裁剪图片宽高
        intent.putExtra("outputY", 320);
        intent.putExtra("return-data", true);
        startActivityForResult(intent, 2);
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

    private void authenticationV2Request() {
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                Activity.MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("tokenId",token);


        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.AUTHENTICATIONV2,
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
                                if (stats.equals("1")) {
                                    if(!js.isNull("assets")){
                                        info =  new AuthenticationEntity(js.getJSONObject("assets"));
                                        formatData();
                                    }
                                } else {
                                    String msg = json.getString("msg");
                                    ToastHelper.toastMessage(ModifyAuditDataActivity.this, msg);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, staff
        );
    }

    List<Object> Figure = new ArrayList<>();
    private void authenticationRequest() {
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                Activity.MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");

        Map<String, String> staff = new HashMap<String, String>();
        staff.put("tokenId",token);
        staff.put("auditstate","2");
        staff.put("id",id+"");
        staff.put("investProjectSum",edt_input_project_num.getText().toString());
        staff.put("historyInvestSum",edt_historical_investment.getText().toString());
        staff.put("exitProjectSum",edt_out_num.getText().toString());
        staff.put("caseProjectName",edt_project_name.getText().toString());
        staff.put("caseIndustryId",industryId);
        staff.put("caseIndustryName",tv_industry.getText().toString());
        staff.put("caseProjectTime", tv_time.getText().toString());
        staff.put("caseInvestMoney",edt_input_money.getText().toString());
        staff.put("caseFinancingState", tv_investment_round.getText().toString());
        staff.put("caseInvestReward",edt_return_multiples.getText().toString());
        for (int i = 0; i < imageList.size(); i++) {
            ConsumerImageEntity info = (ConsumerImageEntity) imageList.get(i);
            Figure.add(info.getUrl());
        }
        String s = Figure.toString();
        String ss = s.substring(1,s.length() - 1).replaceAll(", null","").trim();
        String figure = ss.replaceAll(" ,","").trim();
        staff.put("assetsPhotos",figure);


        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.AUTHENTICATIONV2,
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
                                if (stats.equals("1")) {
                                    ToastHelper.toastMessage(ModifyAuditDataActivity.this, "提交成功");
                                    UISKipUtils.startCertifiedInvestorActivity(ModifyAuditDataActivity.this,1,1);
                                } else {
                                    String msg = json.getString("msg");
                                    ToastHelper.toastMessage(ModifyAuditDataActivity.this, msg);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, staff
        );
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ReceiverUtils.removeReceiver(this);
    }
}

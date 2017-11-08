package com.ts.fmxt.ui.user.Certifiedinvestor;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.thindo.base.Adapter.FMBaseGroupAdapter;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.base.activity.FMBaseActivity;
import com.ts.fmxt.ui.user.login.dialog.PopupPhotoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
 * Created by kp on 2017/10/24.
 * 上传照片，提交数据
 */

public class UploadPhotoActivity  extends FMBaseActivity implements View.OnClickListener, ReceiverUtils.MessageReceiver{
    private PopupUploadDialog mPopupUploadDialog;
    private ConsumerImageAdapter mConsumerImageAdapter;
    private String token;
    private String path;
    private FMNetImageView iv_upimage;
    private int onSelectTag = -1;
    private int conten =0;
    private LinearLayout ll_image_layout;
    private RelativeLayout rl_upimage;
    private List<Object> imageList = new ArrayList<>();
    private TextView tv_next_add;

    @Override
    public void onMessage(int receiverType, Bundle bundle) {
        if (receiverType == ReceiverUtils.REGISTER_IMAGE_UPLOADER) {
            if(mPopupUploadDialog!=null)
                mPopupUploadDialog.dismiss();
            path = bundle.getString("data");
            ((ConsumerImageEntity) imageList.get(onSelectTag)).setUrl(path);
            mConsumerImageAdapter.notifyDataSetChanged();
        }else if(receiverType == ReceiverUtils.CERTIFIEDINVESTOR_FINISH){
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_photo);
        ReceiverUtils.addReceiver(this);
        initView();
        initImage();
    }

    private void initView(){
        findViewById(R.id.btn_finish).setOnClickListener(this);
        mPopupUploadDialog=new PopupUploadDialog(this);
        mPopupUploadDialog.setContext(R.string.text_image_upload);
        iv_upimage = (FMNetImageView) findViewById(R.id.iv_upimage);
        ll_image_layout = (LinearLayout) findViewById(R.id.ll_image_layout);
        tv_next_add = (TextView) findViewById(R.id.tv_next_add);
        iv_upimage.setOnClickListener(this);
        rl_upimage = (RelativeLayout) findViewById(R.id.rl_upimage);
        findViewById(R.id.btn_nexts).setOnClickListener(this);

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

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_finish:
                finish();
                break;
            case R.id.iv_upimage:
                if(onSelectTag>=4){
                    ToastHelper.toastMessage(this,"最多只能上传5张图片");
                    return;
                }
                selectDrawable();
                break;
            case R.id.btn_nexts:
                if(imageList.size()<=0){
                    ToastHelper.toastMessage(this,"至少上传1张照片");
                    return;
                }
                authenticationV2Request();
                break;
        }
    }

    private void selectDrawable() {
        PopupPhotoView popup = new PopupPhotoView(this, false);
        popup.showPopupWindow();
    }

    private void qiNiuTokenRequest() {
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.TOKEN, new OkHttpClientManager.ResultCallback<String>() {

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

    List<Object> Figure = new ArrayList<>();
    private void authenticationV2Request() {
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                Activity.MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        SharedPreferences sharedPreference= getSharedPreferences("projectInfo",
                Activity.MODE_PRIVATE);
        String projectNum=sharedPreference.getString("projectNum", "");
        String historicalInvestment=sharedPreference.getString("historicalInvestment", "");
        String outNum=sharedPreference.getString("outNum", "");
        String projectName=sharedPreference.getString("projectName", "");
        String industry=sharedPreference.getString("industry", "");
        String industryId=sharedPreference.getString("industryId", "");
        String time=sharedPreference.getString("time", "");
        String inputMoney=sharedPreference.getString("inputMoney", "");
        String investmentRound=sharedPreference.getString("investmentRound", "");
        String roundId=sharedPreference.getString("roundId", "");
        String returnMultiples=sharedPreference.getString("returnMultiples", "");
        String currency_type=sharedPreference.getString("currency_type", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("tokenId",token);
        staff.put("auditstate","0");
        staff.put("investProjectSum",projectNum);
        staff.put("historyInvestSum",historicalInvestment);
        staff.put("exitProjectSum",outNum);
        staff.put("caseProjectName",projectName);
        staff.put("caseIndustryId",industryId);
        staff.put("caseIndustryName",industry);
        staff.put("caseProjectTime",time);
        staff.put("caseInvestMoney",inputMoney);
        staff.put("caseFinancingState",investmentRound);
        if(currency_type.equals("RMB¥")){
            staff.put("caseInvestMoneyUnit","1");
        }else{
            staff.put("caseInvestMoneyUnit","2");
        }


        staff.put("caseInvestReward",returnMultiples);
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
                                    ToastHelper.toastMessage(UploadPhotoActivity.this, "提交成功");
                                    UISKipUtils.startCertifiedInvestorActivity(UploadPhotoActivity.this,1,0);
                                } else {
                                    String msg = json.getString("msg");
                                    ToastHelper.toastMessage(UploadPhotoActivity.this, msg);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, staff
        );
    }

    //图片结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0:
                    startPhotoZoom(data.getData());
                    onSelectTag = conten;
                    ConsumerImageEntity infos = (ConsumerImageEntity) imageList.get(onSelectTag);
                    if (StringUtils.isEmpty(infos.getQiniuToken()))//获取七牛Token
                        qiNiuTokenRequest();
                    break;
                case 1:
                    if (Tools.hasSdcard()) {
                        File tempFile = new File(FileUtils.getRootPath() + PopupPhotoView.IMAGE_FILE_NAME);
                        startPhotoZoom(Uri.fromFile(tempFile));
                        onSelectTag = conten;
                        ConsumerImageEntity info = (ConsumerImageEntity) imageList.get(onSelectTag);
                        if (StringUtils.isEmpty(info.getQiniuToken()))//获取七牛Token
                            qiNiuTokenRequest();
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
                        conten++;
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

            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_release_consumer_image_view, null);
            FMNetImageView iv = (FMNetImageView) convertView.findViewById(R.id.iv_picture);
            RelativeLayout rl_picture = (RelativeLayout) convertView.findViewById(R.id.rl_picture);
            ImageView iv_del = (ImageView) convertView.findViewById(R.id.iv_del);
            ConsumerImageEntity info = (ConsumerImageEntity) getItem(position);
            if (!StringUtils.isEmpty(info.getUrl())){
                iv.loadImage(info.getUrl());
                iv.setVisibility(View.VISIBLE);
                rl_picture.setVisibility(View.VISIBLE);
                iv_del.setOnClickListener(new onImageItemClick(position));
                tv_next_add.setVisibility(View.VISIBLE);
                if(position==4){
                    rl_upimage.setVisibility(View.GONE);
                }
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
                    PopupPhotoView popup = new PopupPhotoView(UploadPhotoActivity.this, false, info.getLocatstion());
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
                    MessageContentDialog mPopupDialogWidget = new MessageContentDialog(UploadPhotoActivity.this);

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
                                if(onSelectTag<4){
                                    rl_upimage.setVisibility(View.VISIBLE);
                                }
                            }

                        }
                    });
                    mPopupDialogWidget.showPopupWindow();
                }
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ReceiverUtils.removeReceiver(this);
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


}

package com.ts.fmxt.ui.user.Certifiedinvestor;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.base.activity.FMBaseActivity;
import com.ts.fmxt.ui.user.login.dialog.PopupPhotoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

import static com.ts.fmxt.ui.user.Certifiedinvestor.ModifyAuditDataActivity.getBitmapFormUri;

/**
 * Created by kp on 2017/10/24.
 * 上传照片，提交数据
 */

public class UploadPhotoActivity  extends FMBaseActivity implements View.OnClickListener, ReceiverUtils.MessageReceiver{
    private PopupUploadDialog mPopupUploadDialog;
//    private ConsumerImageAdapter mConsumerImageAdapter;
    private String token;
//    private String path;
    private FMNetImageView iv_upimage;
    private int conten =-1;
    private LinearLayout ll_image_layout;
    private RelativeLayout rl_upimage;
//    private List<Object> imageList = new ArrayList<>();
    private TextView tv_next_add;

    @Override
    public void onMessage(int receiverType, Bundle bundle) {
        if (receiverType == ReceiverUtils.REGISTER_IMAGE_UPLOADER) {
            if(mPopupUploadDialog!=null)
                mPopupUploadDialog.dismiss();
            String url = bundle.getString("data");
            String dirPath = "file://" + bundle.getString("dirPath");
//            ConsumerImageEntity info = new ConsumerImageEntity();
//            info.setUrl(url);
//            info.setPath(dirPath);
//            imageList.add(info);
//            mConsumerImageAdapter.notifyDataSetChanged();
            addImageView(url,dirPath);

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
//        imageList.clear();
//        String[] imageUrls =getResources().getStringArray(R.array.consuer_image_list);//new  String [9];
//        for (int i = 0; i <imageUrls.length; i++) {
//            ConsumerImageEntity info = new ConsumerImageEntity();
//            info.setPosition(i);
//            info.setContext(imageUrls[i]);
//            imageList.add(info);
//        }
//        mConsumerImageAdapter = new ConsumerImageAdapter(this, imageList);
//        mConsumerImageAdapter.setAdapter(ll_image_layout);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_finish:
                finish();
                break;
            case R.id.iv_upimage:
                if(conten>=4){
                    ToastHelper.toastMessage(this,"最多只能上传5张图片");
                    return;
                }
                selectDrawable();
                break;
            case R.id.btn_nexts:
                if(ll_image_layout.getChildCount()==0){
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

    private void qiNiuTokenRequest(final Runnable runnable) {
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
                        if (runnable!=null){
                            runnable.run();
                        }
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
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < ll_image_layout.getChildCount(); i++) {
//                        ConsumerImageEntity item = (ConsumerImageEntity) imageList.get(i);
            View view = ll_image_layout.getChildAt(i);
            if (view.getTag() != null && !StringUtils.isEmpty((String) view.getTag())) {
                if (stringBuilder.length()>0){
                    stringBuilder.append(",");
                }
                stringBuilder.append((String) view.getTag());
            }
        }
//        String s = Figure.toString();
//        String ss = s.substring(1,s.length() - 1).replaceAll(", null","").trim();
//        String figure = ss.replaceAll(" ,","").trim();
        staff.put("assetsPhotos",stringBuilder.toString());


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
                                    Bundle bundle = new Bundle();
                                    ReceiverUtils.sendReceiver(ReceiverUtils.CERTIFIEDINVESTOR_FINISH,bundle);
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
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        if (resultCode != RESULT_CANCELED) {
            switch (requestCode) {
                case 0: {
                    if (data.getData() == null)
                        return;
//                    conten++;
//                    ConsumerImageEntity infos = (ConsumerImageEntity) imageList.get(imageList.size());
//                    if (StringUtils.isEmpty(infos.getQiniuToken()))//获取七牛Token
                    qiNiuTokenRequest(new Runnable() {
                        @Override
                        public void run() {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Uri mImageCaptureUri = data.getData();
                                    ll_image_layout.setVisibility(View.VISIBLE);
                                    Bitmap photoBmp = null;

                                    if (mImageCaptureUri != null) {
                                        try {
                                            photoBmp = getBitmapFormUri(UploadPhotoActivity.this, mImageCaptureUri);
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
//                    String path = ((ConsumerImageEntity) imageList.get(imageList.size())).getPath();
//                    if (!TextUtils.isEmpty(path)) {
//                        File file = new File(path);
//                        if (file.exists()) {
//                            file.delete();
//                        }
//                    }
                                    String fileName = System.currentTimeMillis() + ".png";
                                    Bitmap photo = photoBmp;
                                    ImageCacheUitl imageCacheUitl = ImageCacheUitl.getInstetn();
                                    String path = imageCacheUitl.getSDCarPath() + fileName;
                                    Boolean flg = imageCacheUitl.savaImage(fileName, photo);
                                    if (flg) {
                                        if (mPopupUploadDialog != null)
                                            mPopupUploadDialog.dismiss();
                                        mPopupUploadDialog.showPopupWindow();
//                        ((ConsumerImageEntity) imageList.get(conten)).setPath("");

                                        QiNiuUtils.getInstance().uploadImageRequest(path, token);

                                    }
                                }
                            });
                        }
                    });

                    break;
                }
                case 1:{
                    if (Tools.hasSdcard()) {
                        final File tempFile = new File(FileUtils.getRootPath() +"tempImage.jpg"); //+ ((ConsumerImageEntity) imageList.get(onSelectTag)).getLocatstion());
                        if (tempFile == null)
                            return;
//                        conten++;
//                        ConsumerImageEntity infos = (ConsumerImageEntity) imageList.get(conten);
//                        if (StringUtils.isEmpty(infos.getQiniuToken()))//获取七牛Token
                        qiNiuTokenRequest(new Runnable() {
                            @Override
                            public void run() {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        ll_image_layout.setVisibility(View.VISIBLE);
                                        Uri mImageUri = Uri.fromFile(tempFile);
                                        Bitmap photoBmps = null;

                                        if (mImageUri != null) {
                                            try {
                                                photoBmps = getBitmapFormUri(UploadPhotoActivity.this, mImageUri);
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
//                        String path = ((ConsumerImageEntity) imageList.get(conten)).getPath();
//                        if (!TextUtils.isEmpty(path)) {
//                            File file = new File(path);
//                            if (file.exists()) {
//                                file.delete();
//                            }
//                        }
                                        String fileName = System.currentTimeMillis()+".png";
                                        Bitmap photos = photoBmps;
                                        ImageCacheUitl imageCacheUitls = ImageCacheUitl.getInstetn();
                                        String  path = imageCacheUitls.getSDCarPath() + fileName;
                                        Boolean flgs = imageCacheUitls.savaImage(fileName, photos);
                                        if (flgs) {
                                            if (mPopupUploadDialog != null)
                                                mPopupUploadDialog.dismiss();
                                            mPopupUploadDialog.showPopupWindow();
//                            ((ConsumerImageEntity) imageList.get(conten)).setPath("");
                                            QiNiuUtils.getInstance().uploadImageRequest(path, token);

                                        }
                                    }
                                });
                            }
                        });

                    }
                    break;
                }
            }
        }
    }

    private void addImageView(String url,String path) {
        View convertView = LayoutInflater.from(UploadPhotoActivity.this).inflate(R.layout.adapter_release_consumer_image_view,
                null, false);
        ll_image_layout.addView(convertView);
        convertView.setTag(url);
//        int position = ll_image_layout.getChildCount()-1;
        FMNetImageView iv = (FMNetImageView) convertView.findViewById(R.id.iv_picture);
        RelativeLayout rl_picture = (RelativeLayout) convertView.findViewById(R.id.rl_picture);
        ImageView iv_del = (ImageView) convertView.findViewById(R.id.iv_del);
        if (!StringUtils.isEmpty(url)){
            if(!StringUtils.isEmpty(path)){
                iv.loadImage(path);
            }else{
                iv.loadImage(url);
            }
            iv.setVisibility(View.VISIBLE);
            rl_picture.setVisibility(View.VISIBLE);
            iv_del.setOnClickListener(new onImageItemClick(convertView));
            iv.setOnClickListener(new onImageItemClicks(convertView));
            tv_next_add.setVisibility(View.VISIBLE);

            if(ll_image_layout.getChildCount()>=5){
                rl_upimage.setVisibility(View.GONE);
            }
//            if(type==3){
//                iv_del.setVisibility(View.GONE);
//            }
        }

    }

    class onImageItemClicks implements View.OnClickListener {
        //        private int position;
        View convertView;

        public onImageItemClicks(View convertView) {
            this.convertView = convertView;

        }

        @Override
        public void onClick(View v) {
//            ConsumerImageEntity info = (ConsumerImageEntity) getList().get(position);
//            if (StringUtils.isEmpty(info.getQiniuToken()))//获取七牛Token
//                qiNiuTokenRequest();
//            if (StringUtils.isEmpty(info.getUrl())) {
//                PopupPhotoView popup = new PopupPhotoView(ModifyAuditDataActivity.this, false,
//                        info.getLocatstion());
//                popup.showPopupWindow();
//            } else {
            String uri = "";
            String name = "";
            for (int i = 0; i < ll_image_layout.getChildCount(); i++) {
//                        ConsumerImageEntity item = (ConsumerImageEntity) imageList.get(i);
                View view = ll_image_layout.getChildAt(i);
                if (view.getTag() != null && !StringUtils.isEmpty((String) view.getTag())) {
                    uri = uri + view.getTag() + ",";
                    name = name + i + ",";
                }
            }
            if (!StringUtils.isEmpty(uri)) {
                int position = ll_image_layout.indexOfChild(convertView) ;
                UISKipUtils.startPictureBrowseActivity(UploadPhotoActivity.this, uri, position, name);
            }

//            }
        }
    }
    class onImageItemClick implements View.OnClickListener {
        View convertView;

        public onImageItemClick(View convertView ) {
            this.convertView = convertView;

        }

        @Override
        public void onClick(View v) {
//            ConsumerImageEntity info = (ConsumerImageEntity) getList().get(position);
//            if (StringUtils.isEmpty(info.getQiniuToken()))//获取七牛Token
//                qiNiuTokenRequest();
//            if (StringUtils.isEmpty(info.getUrl())) {
//                PopupPhotoView popup = new PopupPhotoView(ModifyAuditDataActivity.this, false,
//                        info.getLocatstion());
//                popup.showPopupWindow();
//            } else {
//                    String uri = "";

//                    for (int i = 0; i < imageList.size(); i++) {
//                        ConsumerImageEntity item = (ConsumerImageEntity) imageList.get(i);
//                        if (!StringUtils.isEmpty(item.getUrl())) {
//                            uri = uri + item.getUrl() + ",";
////                            name = name + item.getContext() + ",";
//                        }
//                    }
//                    if (!StringUtils.isEmpty(uri))
            MessageContentDialog mPopupDialogWidget = new MessageContentDialog
                    (UploadPhotoActivity.this);

            mPopupDialogWidget.setMessage("确定删除吗？");
            mPopupDialogWidget.setRithButtonText(R.string.dl_ok);
            mPopupDialogWidget.setOnEventClickListener(new BaseDoubleEventPopup.onEventClickListener() {

                @Override
                public void onEventClick(PopupObject obj) {
                    if (obj.getWhat() == 1) {
//                                ll_image_layout.getChildCount()
                        ll_image_layout.removeView(convertView);
                        rl_upimage.setVisibility(View.VISIBLE);
//                                ConsumerImageEntity entity = imageList.remove(position);
//                                mConsumerImageAdapter.notifyDataSetChanged();
//                                if (!TextUtils.isEmpty(entity.getPath())) {
//                                    File file = new File(entity.getPath());
//                                    file.delete();
//                                }
                        //删除单张照片
////                                for (int i = 0; i < imageList.size(); i++) {
////                                    ConsumerImageEntity item = (ConsumerImageEntity) imageList.get(i);
////                                    if (item.getContext().equals(position+"")){
//                                        ((ConsumerImageEntity) imageList.get(position)).setUrl("");
//                                        ((ConsumerImageEntity) imageList.get(position)).setPath("");
////                                    }
//
////                                }
//                                if(imageList.size()>=0){
//                                    p = "";
//                                    photo.clear();
//                                    for (int i = 0; i < imageList.size(); i++) {
//                                        ConsumerImageEntity item = (ConsumerImageEntity) imageList.get(i);
//                                        photo.add(item.getUrl());
//                                        p =photo.toString();
//                                    }
//                                    String ss = p.substring(1,p.length() - 1).trim();
//                                    String figure = ss.replaceAll(" ,","").trim();
//
//                                    String[] sArray=figure.split("\\,") ;
//                                    for (int i = 0; i <sArray.length; i++) {
//                                        String photo = sArray[i].trim();
//                                        if(!photo.equals("")){
//                                            ((ConsumerImageEntity) imageList.get(i)).setUrl(photo);
//
//                                        }
//
//                                    }
//
//                                    if(sArray.length==4){
//                                        ((ConsumerImageEntity) imageList.get(sArray.length)).setUrl("");
//                                    }
//                                    if(sArray.length==3){
//                                        ((ConsumerImageEntity) imageList.get(3)).setUrl("");
//                                    }
//                                    if(sArray.length==2){
//                                        ((ConsumerImageEntity) imageList.get(2)).setUrl("");
//                                    }
//                                    if(sArray.length==1){
//                                        ((ConsumerImageEntity) imageList.get(1)).setUrl("");
//                                    }
//                                }
//                                conten--;
//                                mConsumerImageAdapter.notifyDataSetChanged();

//                                if(position<0){
//                                    tv_next_add.setVisibility(View.GONE);
//                                }
//                                if(position<4){
//                                    rl_upimage.setVisibility(View.VISIBLE);
//                                }
                    }

                }
            });
            mPopupDialogWidget.showPopupWindow();

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        ReceiverUtils.removeReceiver(this);
    }




}

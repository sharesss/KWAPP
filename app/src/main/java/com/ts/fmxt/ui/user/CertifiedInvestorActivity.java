package com.ts.fmxt.ui.user;/**
 * Created by A1 on 2017/7/31.
 */

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.base.activity.FMBaseActivity;
import com.ts.fmxt.ui.user.login.dialog.PopupPhotoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.FileUtils;
import utils.ImageCacheUitl;
import utils.QiNiuUtils;
import utils.ReceiverUtils;
import utils.Tools;
import utils.helper.ToastHelper;
import widget.image.FMNetImageView;
import widget.popup.dialog.PopupUploadDialog;
import widget.titlebar.NavigationView;

/**
 * created by kp at 2017/7/31
 * 认证投资人
 */
public class CertifiedInvestorActivity extends FMBaseActivity implements View.OnClickListener, ReceiverUtils.MessageReceiver{
    private FMNetImageView iv_image;
    private TextView btn_nexts,tv_isAdopt,tv_authentication_privilege,tv_reason;
    private String token;
    private String path;
    private PopupUploadDialog mPopupUploadDialog;
    private boolean ivPictureFlg = false;
    private int type;

    @Override
    public void onMessage(int receiverType, Bundle bundle) {
        if (receiverType == ReceiverUtils.REGISTER_IMAGE_UPLOADER) {
            if(mPopupUploadDialog!=null)
                mPopupUploadDialog.dismiss();
            ivPictureFlg = true;
            nextButton();
            path = bundle.getString("data");
            iv_image.loadImage(path);
        }
//        else if(receiverType==ReceiverUtils.REGISTER_FINISH){
//            finish();
//        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certified_investor);
        ReceiverUtils.addReceiver(this);
        type = getIntent().getIntExtra("type",-1);
        initView();
    }

    private void initView() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setTitle(R.string.title_certified_investor, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mPopupUploadDialog=new PopupUploadDialog(this);
        mPopupUploadDialog.setContext(R.string.text_image_upload);
        iv_image = (FMNetImageView) findViewById(R.id.iv_image);//上传照片
        btn_nexts = (TextView) findViewById(R.id.btn_nexts);//完成按钮
        tv_isAdopt = (TextView) findViewById(R.id.tv_isAdopt);//是否通过
        tv_authentication_privilege = (TextView) findViewById(R.id.tv_authentication_privilege);
        tv_reason = (TextView) findViewById(R.id.tv_reason);
        iv_image.setOnClickListener(this);
        btn_nexts.setOnClickListener(this);
        qiNiuTokenRequest();
        if(type!=0){
            certifiedInvestorRequest();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_image:
                selectDrawable();

                break;
            case R.id.btn_nexts:
                certifiedInvestorRequest();
                break;
        }

    }

    public void nextButton() {
        if (ivPictureFlg ) {//&&
            btn_nexts.setTextColor(getResourcesColor(R.color.white));
            btn_nexts.setBackground(getResources().getDrawable(R.drawable.bg_orange_shape));

        }else{
            btn_nexts.setTextColor(getResourcesColor(R.color.font_main_secondary));
            btn_nexts.setBackground(getResources().getDrawable(R.drawable.bg_gray_shape));
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
//                mTv.setText(u);//注意这里是UI线程
                if (u.contains("token")) {
                    try {
                        JSONObject js = new JSONObject(u);
                        token = js.optString("token");
//                        ToastHelper.toastMessage(NickNameRegeisterActivity.this, token);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            }
        });
    }

    //认证投资人
    private void certifiedInvestorRequest() {
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                Activity.MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("tokenId",token);
        if(type==0){
            staff.put("picture",path);
        }
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.AUTHENTICATION,
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
                                    ToastHelper.toastMessage(CertifiedInvestorActivity.this, msg);
                                    finish();
                                } else {
                                    ToastHelper.toastMessage(CertifiedInvestorActivity.this, msg);
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
                    break;
                case 1:
                    if (Tools.hasSdcard()) {
                        File tempFile = new File(FileUtils.getRootPath() + PopupPhotoView.IMAGE_FILE_NAME);
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

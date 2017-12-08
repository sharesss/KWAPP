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
import android.widget.LinearLayout;
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

import http.data.AuthenticationEntity;
import http.data.UserInfoEntity;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.FileUtils;
import utils.ImageCacheUitl;
import utils.QiNiuUtils;
import utils.ReceiverUtils;
import utils.Tools;
import utils.UISKipUtils;
import utils.helper.ToastHelper;
import widget.image.FMNetImageView;
import widget.popup.BaseDoubleEventPopup;
import widget.popup.PopupObject;
import widget.popup.dialog.MessageContentDialog;
import widget.popup.dialog.PopupUploadDialog;

/**
 * created by kp at 2017/7/31
 * 认证投资人
 */
public class CertifiedInvestorActivity extends FMBaseActivity implements View.OnClickListener, ReceiverUtils.MessageReceiver{
    private FMNetImageView iv_image;
    private TextView btn_nexts,tv_isAdopt,tv_authentication_privilege,tv_reason,isexamine,tv_details,btn_register,btn_cancel_update;
    private String token;
    private String path;
    private TextView tv_audit_status,tv_time,tv_times,iv_audit_status;
    private AuthenticationEntity info;
    private PopupUploadDialog mPopupUploadDialog;
    private LinearLayout ll_authentication;
    private boolean ivPictureFlg = false;
    private int type,state;
    private UserInfoEntity userInfo;

    @Override
    public void onMessage(int receiverType, Bundle bundle) {
        if (receiverType == ReceiverUtils.REGISTER_IMAGE_UPLOADER) {
            if(mPopupUploadDialog!=null)
                mPopupUploadDialog.dismiss();
            ivPictureFlg = true;
            nextButton();
            path = bundle.getString("data");
            iv_image.loadImage(path);
        }else if(receiverType == ReceiverUtils.CERTIFIEDINVESTOR_FINISH){
            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_certified_investor);
        ReceiverUtils.addReceiver(this);
        type = getIntent().getIntExtra("type",-1);
        state =  getIntent().getIntExtra("state",-1);
        initView();
    }

    private void initView() {
        mPopupUploadDialog=new PopupUploadDialog(this);
        mPopupUploadDialog.setContext(R.string.text_image_upload);
        iv_image = (FMNetImageView) findViewById(R.id.iv_image);//上传照片
        btn_nexts = (TextView) findViewById(R.id.btn_nexts);//完成按钮
        tv_isAdopt = (TextView) findViewById(R.id.tv_isAdopt);//是否通过
        tv_authentication_privilege = (TextView) findViewById(R.id.tv_authentication_privilege);
        tv_reason = (TextView) findViewById(R.id.tv_reason);
        iv_audit_status = (TextView) findViewById(R.id.iv_audit_status);
        tv_audit_status = (TextView) findViewById(R.id.tv_audit_status);
        tv_time = (TextView) findViewById(R.id.tv_time);
        tv_times = (TextView) findViewById(R.id.tv_times);
        ll_authentication = (LinearLayout) findViewById(R.id.ll_authentication);
        isexamine = (TextView) findViewById(R.id.tv_isexamine);
        tv_details = (TextView) findViewById(R.id.tv_details);
        btn_register = (TextView) findViewById(R.id.btn_register);
        btn_cancel_update = (TextView) findViewById(R.id.btn_cancel_update);
        btn_cancel_update.setOnClickListener(this);
        findViewById(R.id.btn_finish).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
        iv_image.setOnClickListener(this);
        btn_nexts.setOnClickListener(this);
        if(state==0){
            btn_nexts.setText("设置我的投资偏好");
        }else{
            btn_nexts.setText("查看提交的内容");
        }

        qiNiuTokenRequest();
        if(type!=0){
            certifiedInvestorRequest();
        }

    }

    private void formatData(){
        if (info == null) {
            return;
        }
        iv_image.loadImage(info.getPropertyphoto());
        iv_image.setOnClickListener(null);

        if(info.getAuditstate()==1){
            tv_isAdopt.setVisibility(View.GONE);
            ll_authentication.setVisibility(View.INVISIBLE);
            isexamine.setText("审核中");
            tv_details.setText("（三个工作日内完成审核）");
            tv_reason.setText("认证投资人后可进行小额跟投和老股拍卖");
            tv_times.setVisibility(View.VISIBLE);
            btn_cancel_update.setVisibility(View.GONE);
            tv_time.setText("提交时间："+info.getCreatetime());
            iv_audit_status.setBackground(getResources().getDrawable(R.mipmap.iv_audit));
            tv_audit_status.setText("提交成功，等待审核");
        }else if(info.getAuditstate()==2){
            tv_isAdopt.setVisibility(View.GONE);
            ll_authentication.setVisibility(View.INVISIBLE);
            isexamine.setText("审核成功");
            tv_details.setText("您已成为认证投资人");
            tv_time.setText("审核时间："+info.getCreatetime());
            iv_audit_status.setBackground(getResources().getDrawable(R.mipmap.iv_have_passed));
            tv_audit_status.setText("审核成功");
            tv_authentication_privilege.setText("认证投资人特权");
//            if(info.getAuditdesc().equals("")||info.getAuditdesc()==null){
//                tv_reason.setText("可进行小额跟投和老股拍卖");
//            }else{
//                tv_reason.setText(info.getAuditdesc());
//            }
            tv_reason.setText("可进行小额跟投和老股拍卖");
            tv_times.setVisibility(View.GONE);
            btn_register.setVisibility(View.GONE);
            btn_cancel_update.setVisibility(View.GONE);
            btn_nexts.setText("更新认证内容");
        }else if(info.getAuditstate()==3){
            tv_isAdopt.setVisibility(View.GONE);
            ll_authentication.setVisibility(View.INVISIBLE);
            isexamine.setText("审核失败");
            tv_details.setText("（请重新提交认证）");
            tv_authentication_privilege.setText("审核失败原因");
            tv_audit_status.setText("审核失败");
            tv_reason.setText(info.getAuditdesc());
            btn_register.setVisibility(View.GONE);
            final SharedPreferences sharedPreferences= getSharedPreferences("user",
                    MODE_PRIVATE);
            int isinvestauthen=sharedPreferences.getInt("isinvestauthen", -1);
            if(isinvestauthen==1){
                btn_cancel_update.setVisibility(View.VISIBLE);
            }
            tv_times.setVisibility(View.GONE);
            iv_audit_status.setBackground(getResources().getDrawable(R.mipmap.iv_not_pass));
            btn_nexts.setText("重新提交认证");
            tv_time.setText("审核时间："+info.getCreatetime());
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_finish:
                finish();
                break;
            case R.id.btn_register:
                MessageContentDialog mPopupDialogWidget = new MessageContentDialog(this);
                mPopupDialogWidget.setMessage("是否删除");
                mPopupDialogWidget.setOnEventClickListener(new BaseDoubleEventPopup.onEventClickListener() {

                    @Override
                    public void onEventClick(PopupObject obj) {
                        if (obj.getWhat() == 1)
                            AuthenticationDeleteRequest();
                    }
                });
                mPopupDialogWidget.showPopupWindow();

                break;
            case R.id.iv_image:
                selectDrawable();

                break;
            case R.id.btn_cancel_update:
                //取消上传
                CancelUpdateRequest();

                break;
            case R.id.btn_nexts:
                if(btn_nexts.getText().toString().equals("设置我的投资偏好")){
                    UISKipUtils.startSettingInvestmentPreferenceActivity(CertifiedInvestorActivity.this,0);
                    finish();
                }else if(btn_nexts.getText().toString().equals("更新认证内容")){
                    UISKipUtils.startModifyAuditDataActivity(CertifiedInvestorActivity.this,1,info.getId());
                    finish();
                }else if(btn_nexts.getText().toString().equals("重新提交认证")){
                    UISKipUtils.startModifyAuditDataActivity(CertifiedInvestorActivity.this,2,info.getId());
                    finish();
                }else if(btn_nexts.getText().toString().equals("查看提交的内容")){
                    UISKipUtils.startModifyAuditDataActivity(CertifiedInvestorActivity.this,3,info.getId());
                    finish();
                }
//                certifiedInvestorRequest();
                break;
        }

    }

    public void nextButton() {
        if (ivPictureFlg ) {//&&
            btn_nexts.setTextColor(getResourcesColor(R.color.white));
            btn_nexts.setBackground(getResources().getDrawable(R.drawable.bg_orange_5_shape));

        }else{
            btn_nexts.setTextColor(getResourcesColor(R.color.font_main_secondary));
            btn_nexts.setBackground(getResources().getDrawable(R.drawable.bg_gray_5_shape));
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
                                userInfoRequest();
                                if (stats.equals("1")) {
                                    if(type==0){
                                        String msg = json.getString("msg");
                                        ToastHelper.toastMessage(CertifiedInvestorActivity.this, msg);
                                        finish();
                                    }
                                    if(!js.isNull("assets")){
                                        info =  new AuthenticationEntity(js.getJSONObject("assets"));
                                        formatData();
                                    }

                                    SharedPreferences share = getSharedPreferences("user",MODE_PRIVATE);
                                    Bundle bundle = new Bundle();
                                    ReceiverUtils.sendReceiver(ReceiverUtils.REFRESH,bundle);

                                } else {
                                    String msg = json.getString("msg");
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
    //删除认证
    private void AuthenticationDeleteRequest(){
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                Activity.MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("tokenId",token);
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.AUTHENTICATIONDELETE,
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
                                        ToastHelper.toastMessage(CertifiedInvestorActivity.this, "删除成功");
                                        finish();
                                    Bundle bundle = new Bundle();
                                    ReceiverUtils.sendReceiver(ReceiverUtils.REFRESH,bundle);
                                } else {
                                    String msg = json.getString("msg");
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

    private void CancelUpdateRequest(){
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("assetsId",info.getId()+"");
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.AUTHENTICATIONDELETEV2,
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
                                    ToastHelper.toastMessage(CertifiedInvestorActivity.this, "删除成功");
                                    finish();
                                    Bundle bundle = new Bundle();
                                    ReceiverUtils.sendReceiver(ReceiverUtils.REFRESH,bundle);
                                } else {
                                    String msg = json.getString("msg");
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

    private void  userInfoRequest(){
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("tokenId",token);
        staff.put("userId","");

        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.INFORMATION,
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
                                    if(!js.isNull("information")){
                                        JSONObject jsonobj = js.optJSONObject("information");
                                        userInfo = new UserInfoEntity(jsonobj);
                                        SharedPreferences share = getSharedPreferences("user",MODE_PRIVATE);
                                        SharedPreferences.Editor editor = share.edit(); //使处于可编辑状态
                                        editor.putInt("isTruenameAuthen", userInfo.getIsTruenameAuthen());
                                        editor.putInt("isinvestauthen", userInfo.getIsinvestauthen());
                                        editor.putInt("auditstate", userInfo.getAuditstate());

                                        editor.commit();    //提交数据保存

                                    }
                                } else {
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

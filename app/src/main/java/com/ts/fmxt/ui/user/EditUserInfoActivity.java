package com.ts.fmxt.ui.user;/**
 * Created by A1 on 2017/7/26.
 */

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.squareup.okhttp.Request;
import com.ts.fmxt.FmxtApplication;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.base.activity.FMBaseActivity;
import com.ts.fmxt.ui.user.login.dialog.PopupPhotoView;
import com.ts.fmxt.ui.user.view.PopupWheelAddressView;
import com.ts.fmxt.ui.user.view.PopupWheelAgeView;
import com.ts.fmxt.ui.user.view.PopupWheelInComeView;
import com.ts.fmxt.ui.user.view.WheelListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import http.data.UserInfoEntity;
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
import widget.image.CircleImageView;
import widget.popup.BaseDoubleEventPopup;
import widget.popup.PopupObject;
import widget.popup.dialog.MessageContentDialog;
import widget.popup.dialog.PopupUploadDialog;
import widget.shortcutbadger.ShortcutBadger;
import widget.titlebar.NavigationView;

/**
 * created by kp at 2017/7/26
 * 编辑个人信息
 */
public class EditUserInfoActivity extends FMBaseActivity implements View.OnClickListener, ReceiverUtils.MessageReceiver, WheelListener {
    private NavigationView navigationView;
    private CircleImageView ivPortrait;
    private TextView tvAge,tvLocation,tvNickname,tvSex,tvAnnualIncome,tvPhoneNum,tvCompany,tvPosition,tvPersonalizedSignature;
    private String token;
    private String path;
    private PopupWheelAgeView mPopupWheelView;
    private PopupUploadDialog mPopupUploadDialog;
    private PopupWheelAddressView mPopupWheelAddressView;
    private PopupWheelInComeView mPopupWheelInComeView;
    private UserInfoEntity userInfo;
    private boolean isChange = false;

    @Override
    public void onMessage(int receiverType, Bundle bundle) {
        if (receiverType == ReceiverUtils.REGISTER_IMAGE_UPLOADER) {
            if(mPopupUploadDialog!=null)
                mPopupUploadDialog.dismiss();
            isChange = true;
            path = bundle.getString("data");
            ivPortrait.loadImage(path);
        }else if(receiverType==ReceiverUtils.MODIFY_PHONE){
            String str = bundle.getString("str");
            tvPhoneNum.setText(str);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_userinfo);
        ReceiverUtils.addReceiver(this);
        initView();
        personalRequest();
    }

    private void initView() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setTitle(R.string.title_edit_user_info, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isChange){
                   personalUpdateRequest();
                    finish();
                }else {
                    finish();
                }

            }
        });
        ivPortrait = (CircleImageView) findViewById(R.id.iv_portrait);
        findViewById(R.id.rl_head_portrait).setOnClickListener(this);
        findViewById(R.id.rl_age).setOnClickListener(this);
        findViewById(R.id.rl_location).setOnClickListener(this);
        findViewById(R.id.rl_nickname).setOnClickListener(this);
        findViewById(R.id.rl_gender).setOnClickListener(this);
        findViewById(R.id.rl_phone_num).setOnClickListener(this);
        findViewById(R.id.rl_annual_income).setOnClickListener(this);
        findViewById(R.id.rl_password).setOnClickListener(this);
        findViewById(R.id.rl_company).setOnClickListener(this);
        findViewById(R.id.rl_position).setOnClickListener(this);
        findViewById(R.id.rl_personalized_signature).setOnClickListener(this);
        findViewById(R.id.rl_safe_exit).setOnClickListener(this);
        tvAge = (TextView) findViewById(R.id.tv_age);
        tvLocation = (TextView) findViewById(R.id.tv_location);
        tvNickname = (TextView) findViewById(R.id.tv_nickname);
        tvSex = (TextView) findViewById(R.id.tv_sex);
        tvPhoneNum = (TextView) findViewById(R.id.tv_phone_num);
        tvCompany = (TextView) findViewById(R.id.tv_company);
        tvPosition = (TextView) findViewById(R.id.tv_position);
        tvPersonalizedSignature = (TextView) findViewById(R.id.tv_personalized_signature);
        mPopupUploadDialog=new PopupUploadDialog(this);
        mPopupUploadDialog.setContext(R.string.text_image_upload);
        tvAnnualIncome = (TextView) findViewById(R.id.tv_annual_income);
        qiNiuTokenRequest();
    }
    private void formatData(UserInfoEntity userInfo){
        if(userInfo==null){
            return;
        }
        if (!StringUtils.isEmpty(userInfo.getHeadpic())){
            ivPortrait.loadImage(userInfo.getHeadpic());
        }
        if(!userInfo.getNickname().equals("null")){
            tvNickname.setText(userInfo.getNickname());
        }
        if(userInfo.getSex()==1){
            tvSex.setText("男");
        }else if(userInfo.getSex()==0){
            tvSex.setText("女");
        }
        if(userInfo.getAge()!=0){
            tvAge.setText(userInfo.getAge()+"");
        }else{
            tvAge.setText("未填写");
        }

        if(!userInfo.getAccount().equals("null")&&!userInfo.getAccount().equals("")){
            tvPhoneNum.setText(userInfo.getAccount());
        }else{
            tvPhoneNum.setText("未填写");
        }

        if(!userInfo.getResidence().equals("null")){
            tvLocation.setText(userInfo.getResidence());
        }else{
            tvLocation.setText("未填写");
        }

        if(!userInfo.getCompany().equals("null")){
            tvCompany.setText(userInfo.getCompany());
        }else{
            tvCompany.setText("未填写");
        }

        if(!userInfo.getPosition().equals("null")){
            tvPosition.setText(userInfo.getPosition());
        }else{
            tvPosition.setText("未填写");
        }

        if(!userInfo.getAnnualincome().equals("null")){
            tvAnnualIncome.setText(userInfo.getAnnualincome());
        }else{
            tvAnnualIncome.setText("未填写");
        }

        if(!userInfo.getSignature().equals("null")){
            tvPersonalizedSignature.setText(userInfo.getSignature());
        }else{
            tvPersonalizedSignature.setText("未填写");
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //图片
            case R.id.rl_head_portrait:
                selectDrawable();
                break;
            case R.id.rl_age://年龄
                mPopupWheelView = new PopupWheelAgeView(this);
                mPopupWheelView.setWheelListener(this);
                mPopupWheelView.showPopupWindow();
                break;
            case R.id.rl_location://位置
                mPopupWheelAddressView = new PopupWheelAddressView(this);
                mPopupWheelAddressView.setWheelListener(this);
                mPopupWheelAddressView.showPopupWindow();
                break;
            case R.id.rl_nickname://修改昵称
                String nickname = tvNickname.getText().toString();
                UISKipUtils.startEditNickName(EditUserInfoActivity.this, nickname, 100);
                break;
            case R.id.rl_company://修改公司名称
                String compay = tvCompany.getText().toString();
                UISKipUtils.startEditNickName(EditUserInfoActivity.this, compay, 200);
                break;
            case R.id.rl_position://修改职称
                String position = tvPosition.getText().toString();
                UISKipUtils.startEditNickName(EditUserInfoActivity.this, position, 300);
                break;
            case R.id.rl_personalized_signature://修改个性签名
                String autograph = tvPersonalizedSignature.getText().toString();
                UISKipUtils.startEditAutograph(EditUserInfoActivity.this, autograph, 400);
                break;
            case R.id.rl_gender://性别
                final Dialog builder_sex = new Dialog(EditUserInfoActivity.this, R.style.Dialog);
                builder_sex.setContentView(R.layout.dialog_sex);
                WindowManager.LayoutParams lp=builder_sex.getWindow().getAttributes();
                lp.dimAmount=0.7f;
                builder_sex.getWindow().getDecorView().setPadding(0, 0, 0, 0);
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                builder_sex.getWindow().setGravity(Gravity.BOTTOM);
                final TextView man  = (TextView) builder_sex.findViewById(R.id.tv_man);
                TextView woman  = (TextView) builder_sex.findViewById(R.id.tv_woman);
                man.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvSex.setText("男");
                        builder_sex.dismiss();
                        isChange = true;
                    }
                });
                woman.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        tvSex.setText("女");
                        builder_sex.dismiss();
                        isChange = true;
                    }
                });
                builder_sex.show();
                break;
            case R.id.rl_phone_num://修改手机
                String phone = tvPhoneNum.getText().toString();
                UISKipUtils.startEditphone(EditUserInfoActivity.this, phone);
                break;
            case R.id.rl_annual_income://年收入
                mPopupWheelInComeView = new PopupWheelInComeView(this, tvAnnualIncome.getText().toString());
                mPopupWheelInComeView.setWheelListener(this);
                mPopupWheelInComeView.showPopupWindow();
                break;
            case R.id.rl_password://修改密码
                UISKipUtils.startEditPassWord(EditUserInfoActivity.this);
                break;
            case R.id.rl_safe_exit://安全退出
//                if(FMWession.getInstance().isLogin()){
//                    UISKipUtils.startLoginActivity(this);
//                    finish();
//                }else{
                    onLogoutClick();

//                }
                break;
        }
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
                case 100:
                    String name = data.getStringExtra("name");
                    tvNickname.setText(name);
                    break;
                case 200:
                    String compay = data.getStringExtra("name");
                    tvCompany.setText(compay);
                    isChange = true;
                    break;
                case 300:
                    String position = data.getStringExtra("name");
                    tvPosition.setText(position);
                    isChange = true;
                    break;
                case 400:
                    String Signature = data.getStringExtra("name");
                    tvPersonalizedSignature.setText(Signature);
                    isChange = true;
                    break;
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

    private void selectDrawable() {
        PopupPhotoView popup = new PopupPhotoView(this, false);
        popup.showPopupWindow();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ReceiverUtils.removeReceiver(this);
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

    private String age="未填写";
    @Override
    public void completeCall(String text, String text2, int type) {
        switch (type) {
            case 1://年龄
                age =text;
                tvAge.setText(text2);
                isChange = true;
                break;
            case 2://年薪
                tvAnnualIncome.setText(text);
                isChange = true;
                break;
            case 3://省份
                String str = String.format("%s %s", text, text2);
                tvLocation.setText(str);
                isChange = true;
                break;

        }
    }

    @Override
    public void clearCall(int type) {
        String str = getResourcesStr(R.string.user_info_nodata);
        switch (type) {
            case 1://年龄
                age =str;
                isChange = true;
                tvAge.setText(str);
                break;
            case 2://年薪
                isChange = true;
                tvAnnualIncome.setText(str);
                break;
            case 3://省份
                isChange = true;
                tvLocation.setText(str);
                break;
        }
    }

    //退出登录
    private void onLogoutClick() {
        MessageContentDialog mPopupDialogWidget = new MessageContentDialog(this);
        mPopupDialogWidget.setMessage("是否退出登录");
        mPopupDialogWidget.setOnEventClickListener(new BaseDoubleEventPopup.onEventClickListener() {

            @Override
            public void onEventClick(PopupObject obj) {
                if (obj.getWhat() == 1) {
                    outSys();
                }

            }
        });
        mPopupDialogWidget.showPopupWindow();

    }

    private void outSys() {
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                MODE_PRIVATE);
        sharedPreferences.edit().clear().commit();
        ShortcutBadger.applyCount(FmxtApplication.getContext(), 0);
        UISKipUtils.startMainFrameActivity(this);
        EMClient.getInstance().logout(true);
        finish();
    }

    private void personalRequest(){
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("tokenId", token);
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.PERSONAL,
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
                                    if (!js.isNull("information")) {
                                        JSONObject jsonobj = js.optJSONObject("information");
                                        userInfo = new UserInfoEntity(jsonobj);
                                        formatData(userInfo);
                                    }

                                } else {
                                    ToastHelper.toastMessage(EditUserInfoActivity.this, msg);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, staff
        );
    }
        //提交修改数据
    private void personalUpdateRequest(){
        SharedPreferences sharedPreferences= getSharedPreferences("user",
                MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("tokenId",token);
        if(path!=null){
            staff.put("picture",path);
        }
//        String  name = tvNickname.getText().toString();
//        if(!name.equals("未填写")){
//            staff.put("nickname",name);
//        }
        String sex = tvSex.getText().toString();
        if(sex.equals("男")){
            staff.put("sex","1");
        }else{
            staff.put("sex","0");
        }
        if(!age.equals("未填写")){
            staff.put("birthYears",age);
            staff.put("updateType","1");
        }else if(age.equals("未填写")){
            staff.put("birthYears","");
            staff.put("updateType","1");
        }

        String Location = tvLocation.getText().toString();
        if(!Location.equals("未填写")){
            staff.put("residence",Location);
        }else {
            staff.put("residence","");
        }
        String Company =tvCompany.getText().toString();
        if(!Company.equals("未填写")){
            staff.put("company",Company);
        }else{
            staff.put("company","");
        }
        String Position = tvPosition.getText().toString();
        if(!Position.equals("未填写")){
            staff.put("position",Position);
        }else{
            staff.put("position","");
        }
        String annualincome = tvAnnualIncome.getText().toString();
        if(!annualincome.equals("未填写")){
            staff.put("annualincome",annualincome);
        }else {
            staff.put("annualincome","");
        }
//        String Signature =tvPersonalizedSignature.getText().toString();
//        if(!Signature.equals("未填写")){
//            staff.put("signature",Signature);
//        }
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.PERSONALUPDATE,
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
                                    ToastHelper.toastMessage(EditUserInfoActivity.this, msg);
                                    finish();
                                    Bundle bundle = new Bundle();
                                    ReceiverUtils.sendReceiver(ReceiverUtils.REFRESH,bundle);
                                } else {
                                    ToastHelper.toastMessage(EditUserInfoActivity.this, msg);
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

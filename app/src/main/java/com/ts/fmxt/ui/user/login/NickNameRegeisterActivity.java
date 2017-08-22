package com.ts.fmxt.ui.user.login;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.thindo.base.NetworkAPI.BaseResponse;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.base.activity.FMBaseActivity;
import com.ts.fmxt.ui.user.login.dialog.PopupPhotoView;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import http.data.RegisterEntity;
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
import widget.ContainsEmojiEditText;
import widget.image.FMRadiusNetImageView;
import widget.popup.dialog.PopupUploadDialog;


/**
 * Created by kp on 2016/5/17.
 * 注册昵称
 */
public class NickNameRegeisterActivity extends FMBaseActivity implements View.OnClickListener, ReceiverUtils.MessageReceiver, ContainsEmojiEditText.ContainsEmojiEditTextInterface {
    private FMRadiusNetImageView ivPicture;
    private EditText edNickname;
    private TextView tv_sex_woman;
    private TextView tv_sex_man;
    private TextView btnNexts;
    private String path;
    private RegisterEntity mRegisterEntity;
    private String token;
    private ContainsEmojiEditText mNickname;
    private boolean ivPictureFlg = false;
    private boolean sexFlg = false;
    private boolean nicknameFlg = false;
    private PopupUploadDialog mPopupUploadDialog;

    @Override
    public void onMessage(int receiverType, Bundle bundle) {
        if (receiverType == ReceiverUtils.REGISTER_IMAGE_UPLOADER) {
            if(mPopupUploadDialog!=null)
                mPopupUploadDialog.dismiss();
            ivPictureFlg = true;
            nextButton();
            path = bundle.getString("data");
            ivPicture.loadImage(path);
            findViewById(R.id.tv_lib).setVisibility(View.GONE);
        }
        else if(receiverType==ReceiverUtils.REGISTER_FINISH){
            finish();
        }
    }

    public void nextButton() {
        if (nicknameFlg && sexFlg ) {//&& ivPictureFlg
            btnNexts.setTextColor(getResourcesColor(R.color.white));
            btnNexts.setBackground(getResources().getDrawable(R.drawable.bg_orange_shape));
        }else{
            btnNexts.setTextColor(getResourcesColor(R.color.font_main_secondary));
            btnNexts.setBackground(getResources().getDrawable(R.drawable.bg_gray_shape));
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_number);
        ReceiverUtils.addReceiver(this);
        mNickname = (ContainsEmojiEditText) findViewById(R.id.ed_nickname);
        mNickname.setContainsEmojiEditTextInterface(this);
        mNickname.setIsStringFilter(true);
        ivPicture = (FMRadiusNetImageView) findViewById(R.id.iv_picture);
        edNickname = (EditText) findViewById(R.id.ed_nickname);
        tv_sex_woman = (TextView) findViewById(R.id.tv_sex_woman);
        tv_sex_man = (TextView) findViewById(R.id.tv_sex_man);
        btnNexts = (TextView) findViewById(R.id.btn_nexts);

        findViewById(R.id.btn_finish).setOnClickListener(this);
        findViewById(R.id.tv_login).setOnClickListener(this);

        tv_sex_man.setOnClickListener(this);
        tv_sex_woman.setOnClickListener(this);
        btnNexts.setOnClickListener(this);
        ivPicture.setOnClickListener(this);
        tv_sex_woman.setSelected(true);
        tv_sex_man.setSelected(true);
        mRegisterEntity = new RegisterEntity();
        mPopupUploadDialog=new PopupUploadDialog(this);
        mPopupUploadDialog.setContext(R.string.text_image_upload);
        qiNiuTokenRequest();
    }

    //判断
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            //图片
            case R.id.iv_picture:
                selectDrawable();
                break;
            case R.id.tv_sex_man://男
                changeSex(true);
                break;
            case R.id.tv_sex_woman:  //女
                changeSex(false);
                break;
            case R.id.btn_finish:  //返回
                finish();
                break;
            case R.id.tv_login:  //登录
                UISKipUtils.startLoginActivity(this);
                break;
            case R.id.btn_nexts:
                if (nicknameFlg && sexFlg ) {
                    nicknameHeavyRequest();

                }
                break;


        }

    }

    //性别改变
    private void changeSex(boolean flg) {
        sexFlg = true;
        nextButton();
        mRegisterEntity.setSex(flg ? 1 : 0);
        Drawable select_on_sex = getResources().getDrawable(R.mipmap.dues_set_s);
        select_on_sex.setBounds(0, 0, select_on_sex.getMinimumWidth(), select_on_sex.getMinimumHeight());

        Drawable select_off_sex = getResources().getDrawable(R.mipmap.dues_set_n);
        select_off_sex.setBounds(0, 0, select_off_sex.getMinimumWidth(), select_off_sex.getMinimumHeight());

        tv_sex_man.setCompoundDrawables(flg ? select_on_sex : select_off_sex, null, null, null);
        tv_sex_woman.setCompoundDrawables(flg ? select_off_sex : select_on_sex, null, null, null);
    }

    private void selectDrawable() {
        PopupPhotoView popup = new PopupPhotoView(this, false);
        popup.showPopupWindow();
    }

    private void qiNiuTokenRequest() {
     OkHttpClientManager.postAsyn(HttpPathManager.HOST+ HttpPathManager.TOKEN, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(String u) {
//                mTv.setText(u);//注意这里是UI线程
                if (u.contains("token")) {
                    try {
                        JSONObject js  = new JSONObject(u);
                        token =  js.optString("token");
//                        ToastHelper.toastMessage(NickNameRegeisterActivity.this, token);
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
    public void onStart(BaseResponse baseResponse) {

    }

    @Override
    public void onFailure(BaseResponse baseResponse) {

    }

    @Override
    public void onSuccess(BaseResponse response) {
        if (response.getStatus() == BaseResponse.SUCCEED) {
            switch (response.getRequestType()) {
                case 1:

                    break;
                case 8://路径
                    path = response.getData().toString();
                    ivPicture.loadImage(path);
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

    @Override
    public void onTextChangedInterface(String str) {
        nicknameFlg = StringUtils.isEmpty(str) ? false : true;
        nextButton();
    }

    private boolean StringFilter(String str) throws PatternSyntaxException {
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);

        boolean flg = m.find();
        return m.find();
    }

    private void nicknameHeavyRequest(){
        String mNickname = edNickname.getText().toString().trim();
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("nickName",  mNickname);
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.NICKNAMEHEAVY,
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
                                    String mNickname = edNickname.getText().toString().trim();
                                    mRegisterEntity.setNickName(mNickname);
                                    mRegisterEntity.setPortraitUri(path);
                                    UISKipUtils.startPhoneRegisterActivity(NickNameRegeisterActivity.this, mRegisterEntity);

                                } else {
                                    ToastHelper.toastMessage(NickNameRegeisterActivity.this, msg);
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

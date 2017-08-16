package com.ts.fmxt.ui.user.login.dialog;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;

import com.ts.fmxt.R;

import java.io.File;

import utils.FileUtils;
import utils.Tools;
import widget.popup.BaseEventPopup;


/**
 * 修改头像
 */
public class PopupPhotoView extends BaseEventPopup implements OnClickListener {
    public static final String IMAGE_FILE_NAME = "tempImage.jpg";
    private boolean is_showDelete;
    private ImageView bt;
    private Button btDelect;
    private DelectListener DelectListener;
    private int postion;
    private String uri = IMAGE_FILE_NAME;

    //是否显示删除  默认传false即可
    public PopupPhotoView(Activity context, boolean is_showDelete) {
        super(context);
        this.uri = "tempImage.jpg";
        this.is_showDelete = is_showDelete;
        btDelect.setVisibility(is_showDelete ? View.VISIBLE : View.GONE);
        bt.setVisibility(is_showDelete ? View.VISIBLE : View.GONE);
    }

    public PopupPhotoView(Activity context, boolean is_showDelete, String uri) {
        super(context);
        this.uri = uri;
        this.is_showDelete = is_showDelete;
        btDelect.setVisibility(is_showDelete ? View.VISIBLE : View.GONE);
        bt.setVisibility(is_showDelete ? View.VISIBLE : View.GONE);
    }

    @Override
    public View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.popup_photo_view, null);
    }

    public void hiddlePoho() {
        getView().findViewById(R.id.bt_photograph).setVisibility(View.GONE);
    }

    @Override
    public void findEventByID() {
        getView().findViewById(R.id.other_cancel).setOnClickListener(this);
        getView().findViewById(R.id.bt_photoalbum).setOnClickListener(this);
        getView().findViewById(R.id.bt_photograph).setOnClickListener(this);
        getView().findViewById(R.id.app_widget).setOnClickListener(this);
        bt = (ImageView) getView().findViewById(R.id.bt);
        btDelect = (Button) getView().findViewById(R.id.bt_delect);
        btDelect.setOnClickListener(this);

    }

    @Override
    public void findTitleByID() {

    }

    @Override
    public void findMessageByID() {

    }

    @Override
    public Animation loadAnim() {
        return null;
    }

    @Override
    public View getStartAnimViewGroup() {
        return getView().findViewById(R.id.popup_parent_layout);
    }

    @Override
    public void onClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.bt_photograph://拍照
                Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (Tools.hasSdcard()) {// 判断存储卡是否可以用，可用进行存储
                    intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(FileUtils.getRootPath(), uri)));
                }

                getContext().startActivityForResult(intentFromCapture, 1);
                break;
            case R.id.bt_photoalbum://相册
                Intent intentFromGallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
//                Intent intentFromGallery = new Intent();
//                intentFromGallery.setType("image/*"); // 设置文件类型
//                intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
                intentFromGallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                getContext().startActivityForResult(intentFromGallery, 0);
                break;
            case R.id.bt_delect:
                if (DelectListener != null) DelectListener.OnDelectListener(postion);
                break;

                default:
                break;
        }
    }

    public void setOnDelectListener(DelectListener DelectListener) {
        this.DelectListener = DelectListener;
        if (is_showDelete) {
            bt.setVisibility(View.VISIBLE);
            btDelect.setVisibility(View.VISIBLE);
        }
    }

    public void setPostion(int postion) {
        this.postion = postion;
    }

    public interface DelectListener {
        void OnDelectListener(int postion);
    }

    public void setIs_showDelete(boolean is_showDelete) {
        this.is_showDelete = is_showDelete;
        btDelect.setVisibility(is_showDelete ? View.VISIBLE : View.GONE);
        bt.setVisibility(is_showDelete ? View.VISIBLE : View.GONE);
    }
}

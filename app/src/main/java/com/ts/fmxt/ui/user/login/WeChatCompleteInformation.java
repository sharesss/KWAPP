package com.ts.fmxt.ui.user.login;

import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ts.fmxt.R;
import com.ts.fmxt.ui.base.activity.FMBaseActivity;

import utils.Tools;
import widget.ContainsEmojiEditText;
import widget.image.FMRadiusNetImageView;

/**
 * created by kp at 2017/7/25
 * 补全账户信息
 */
public class WeChatCompleteInformation extends FMBaseActivity implements View.OnClickListener {
    private FMRadiusNetImageView ivPicture;
    private EditText etdRegisterPhone,edtRegisteSmsCode;
    private ContainsEmojiEditText edtRegisterPsw;
    private ImageView iv_eye;
    private TextView tvName;
    private boolean flg = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wechat_complete_information);
        initView();
    }

    private void initView() {
        ivPicture = (FMRadiusNetImageView) findViewById(R.id.iv_picture);
        etdRegisterPhone = (EditText) findViewById(R.id.register_phone);
        edtRegisteSmsCode = (EditText) findViewById(R.id.register_sms_code);
        edtRegisterPsw = (ContainsEmojiEditText) findViewById(R.id.register_psw);
        tvName = (TextView) findViewById(R.id.tv_name);
        iv_eye = findViewByIds(R.id.iv_eye);
        iv_eye.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_eye:
                iv_eye.setSelected(!flg);
                flg = !flg;
                edtRegisterPsw.setInputType(!flg ? InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD : InputType.TYPE_CLASS_TEXT);
                break;
            case R.id.tv_send_code:
                if (Tools.isFastDoubleClick())
                    return;
//                if (sendCodeFlg == false && registerPhoneFlg == true)
//                    randomCodeRequest();
                break;
            case R.id.btn_nexts:
                if (Tools.isFastDoubleClick())
                    return;
//                if (registerPhoneFlg && sendCodeFlg) {
//                    VerifyRandomCodeRequest();
//                }
                break;
            case R.id.btn_finish:
                finish();
                break;
            case R.id.tv_protocol://协议
//                UISKipUtils.startFMBrowserActivity(this, getResourcesStr(R.string.html_fm_agreement), getResourcesStr(R.string.text_fm_agreement));
                break;
        }
    }
}

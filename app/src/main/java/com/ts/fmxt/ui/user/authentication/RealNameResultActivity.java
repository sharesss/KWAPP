package com.ts.fmxt.ui.user.authentication;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.thindo.base.NetworkAPI.BaseResponse;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.base.activity.FMBaseActivity;

import utils.DateFormatUtils;
import utils.SpannableUtils;
import utils.Tools;
import widget.titlebar.NavigationView;


/**
 * 实名认证
 */
public class RealNameResultActivity extends FMBaseActivity implements View.OnClickListener {
    private NavigationView navigationView;
    private TextView tv_desc;
    private int type;
    private ImageView iv_stauts;
    private TextView tv_status;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real_name_result_view);
        type = getIntent().getIntExtra("type", 0);
        tv_desc = (TextView) findViewById(R.id.tv_desc);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setTitle(getResourcesStr(R.string.user_realname), new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_stauts = (ImageView) findViewById(R.id.iv_stauts);
        tv_status = (TextView) findViewById(R.id.tv_status);
        tv_desc.setText(SpannableUtils.getSpannableStr(getResourcesStr(R.string.text_authorization_1), getResourcesStr(R.string.text_authorization_2), getResourcesColor(R.color.font_main_third), 0f));
         String success= String.format("你已经完成实名认证\n认证时间：%s", DateFormatUtils.formatDateStr(DateFormatUtils.LIST_ITEM));
        String error="认证失败，请稍后再试";
        tv_status.setText(type == 1 ? success : error);
        iv_stauts.setImageDrawable(getResources().getDrawable(type == 1 ? R.mipmap.icon_authorization_on : R.mipmap.icon_authorization_off));
    }


    @Override
    public void onSuccess(BaseResponse response) {
        super.onSuccess(response);
        if (response.getStatus() == BaseResponse.SUCCEED) {

        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_authorization:
                if (Tools.isFastDoubleClick())
                    return;

                break;
        }
    }
}
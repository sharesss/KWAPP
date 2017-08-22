package com.ts.fmxt.ui.user;/**
 * Created by A1 on 2017/7/28.
 */

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.ts.fmxt.R;
import com.ts.fmxt.ui.base.activity.FMBaseActivity;

import widget.image.CircleImageView;

/**
 * created by kp at 2017/7/28
 * 他人信息
 */
public class OtherInfomationActivity extends FMBaseActivity implements View.OnClickListener{
    private CircleImageView ivPortrait;
    private TextView tvUserName,tvFmIdentity,tvCompanyName,tvAutograph;
    private int userid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_other_infomation);
        userid = getIntent().getIntExtra("userid",0);
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_finish).setOnClickListener(this);
        findViewById(R.id.rl_items_to_follow).setOnClickListener(this);
        findViewById(R.id.rl_collection_items).setOnClickListener(this);
        ivPortrait = (CircleImageView) findViewById(R.id.iv_portrait);
        tvUserName = (TextView) findViewById(R.id.tv_user_name);
        tvFmIdentity = findViewByIds(R.id.tv_fm_identity);
        tvCompanyName = (TextView) findViewById(R.id.tv_company_name);
        tvAutograph = (TextView) findViewById(R.id.tv_autograph);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_finish:
                finish();
                break;
        }

    }
}
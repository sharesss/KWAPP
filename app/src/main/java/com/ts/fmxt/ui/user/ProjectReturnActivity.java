package com.ts.fmxt.ui.user;

import android.os.Bundle;
import android.view.View;

import com.ts.fmxt.R;
import com.ts.fmxt.ui.base.activity.FMBaseActivity;

/**
 * Created by A1 on 2017/8/16.
 */

public class ProjectReturnActivity extends FMBaseActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_project_return);
        initView();
    }

    private void initView() {
        findViewById(R.id.btn_finish).setOnClickListener(this);
        findViewById(R.id.btn_register).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

    }
}

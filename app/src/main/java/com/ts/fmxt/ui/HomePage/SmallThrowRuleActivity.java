package com.ts.fmxt.ui.HomePage;

import android.os.Bundle;
import android.view.View;

import com.ts.fmxt.R;
import com.ts.fmxt.ui.base.activity.FMBaseActivity;

import widget.titlebar.NavigationView;

/**
 * Created by kp on 2017/9/27.
 * 小投规则
 */

public class SmallThrowRuleActivity extends FMBaseActivity {
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_small_throw_rule);
        initView();
    }

    private void initView() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);

            navigationView.setTitle("小投规则", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

    }


}

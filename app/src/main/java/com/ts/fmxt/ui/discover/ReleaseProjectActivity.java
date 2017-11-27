package com.ts.fmxt.ui.discover;

import android.os.Bundle;
import android.view.View;

import com.ts.fmxt.R;
import com.ts.fmxt.ui.base.activity.FMBaseActivity;

import widget.titlebar.NavigationView;

/**
 * Created by kp on 2017/11/21.
 * 发布流程
 */

public class ReleaseProjectActivity extends FMBaseActivity {
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_release_project);
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setTitle("发布流程", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}

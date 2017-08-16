package com.ts.fmxt.ui.user;/**
 * Created by A1 on 2017/7/27.
 */

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.ts.fmxt.R;
import com.ts.fmxt.ui.base.activity.FMBaseActivity;

import widget.FMEdittext;
import widget.FMNumEdittext;
import widget.titlebar.NavigationView;

/**
 * created by kp at 2017/7/27
 * 个性签名
 */
public class EditAutographActivity extends FMBaseActivity {
    private NavigationView navigationView;
    private String name ;
    private int type;
    private FMNumEdittext edtEdit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_autograph);
        name = getIntent().getStringExtra("name");
        type = getIntent().getIntExtra("type",0);
        initView();
    }

    private void initView() {
        navigationView = (NavigationView) findViewById(R.id.navigation_view);
        navigationView.setTitle(R.string.title_personalized_signature, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        edtEdit = (FMNumEdittext) findViewById(R.id.edt_edit);
        if(!name.equals("未填写")){
            edtEdit.setText(name);
        }


        edtEdit.setMaxNum(30);
        edtEdit.setTextWatcherCallBack(new FMEdittext.TextWatcherCallBack() {

            @Override
            public void afterTextChangedText(String string) {
                navigationView.showRightButtom(R.string.bt_complete, R.color.black,
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Intent intent = new Intent();
                                String editinfo = edtEdit.getTextVal().toString();
                                intent.putExtra("name", editinfo); //将计算的值回传回去
                                setResult(type, intent);
                                finish(); //结束当前的activity的生命周期
                            }
                        });
            }
        });

    }

}

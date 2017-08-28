package com.ts.fmxt.ui.discover.view;/**
 * Created by A1 on 2017/8/12.
 */

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.discover.DiscoverDetailsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import http.data.InvestBPListEntity;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.ReceiverUtils;
import utils.UISKipUtils;
import utils.helper.ToastHelper;
import widget.popup.BaseDoubleEventPopup;
import widget.popup.PopupObject;
import widget.popup.dialog.MessageContentDialog;

/**
 * created by kp at 2017/8/12
 * 滑动布局
 */
public class seekBar extends RelativeLayout implements SeekBar.OnSeekBarChangeListener{
    private Context context;
    private TextView tv_seekbar_distance,tv_peoplenum,tv_fraction,tv_ceek,tv_result;
    private SeekBar sb_bp;
    private RelativeLayout rl_sp_bp;
    private RelativeLayout ll_pb;
    private ProgressBar pb_yellowindex;
    private InvestBPListEntity info;
    private int investIds;
    DiscoverDetailsActivity activity;
    public seekBar(Context context) {
        super(context);
        initNavigation();
        this.context = context;
    }

    public seekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        initNavigation();
        this.context = context;
    }

    public seekBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initNavigation();
        this.context = context;
    }

    private void initNavigation() {
        LayoutInflater.from(getContext()).inflate(R.layout.seek_bar_view, this);
        sb_bp = (SeekBar)findViewById(R.id.sb_bp);
        sb_bp.setOnSeekBarChangeListener(this);
        tv_seekbar_distance = (TextView) findViewById(R.id.tv_seekbar_distance);
        ll_pb = (RelativeLayout) findViewById(R.id.ll_pb);
        rl_sp_bp = (RelativeLayout) findViewById(R.id.rl_sp_bp);
        pb_yellowindex = (ProgressBar) findViewById(R.id.pb_yellowindex);
        tv_fraction = (TextView) findViewById(R.id.tv_fraction);
        tv_peoplenum = (TextView) findViewById(R.id.tv_peoplenum);
        tv_ceek = (TextView) findViewById(R.id.tv_ceek);
        tv_result  = (TextView) findViewById(R.id.tv_result);

    }

    public void formatData(final InvestBPListEntity info,  int investId ){
       this.investIds= investId;
        if(info==null){
            return;
        }

        this.info =info;
        if(info.isScore()==1){
            rl_sp_bp.setVisibility(View.GONE);
            ll_pb.setVisibility(View.VISIBLE);
            pb_yellowindex.setProgress(info.getScore());
            tv_ceek.setText("此BP评价结果");
        }
        tv_fraction.setText(info.getScore()+"分");
        tv_peoplenum.setText(info.getPeopleNum()+"人");
        tv_result.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences= getContext().getSharedPreferences("user",
                        Activity.MODE_PRIVATE);
                String token=sharedPreferences.getString("token", "");
                int isTruenameAuthen=sharedPreferences.getInt("isTruenameAuthen", -1);
                if (token.equals("")) {
                    MessageContentDialog mPopupDialogWidget = new MessageContentDialog((Activity) context);
                    mPopupDialogWidget.setMessage("您还未登录，是否去登录？");
                    mPopupDialogWidget.setOnEventClickListener(new BaseDoubleEventPopup.onEventClickListener() {

                        @Override
                        public void onEventClick(PopupObject obj) {
                            if (obj.getWhat() == 1)
                                UISKipUtils.startLoginActivity((Activity) context);
                        }
                    });
                    mPopupDialogWidget.showPopupWindow();

                    return;
                }
                if(isTruenameAuthen==0){
                    MessageContentDialog mPopupDialogWidget = new MessageContentDialog((Activity) context);
                    mPopupDialogWidget.setMessage("您还实名认证，是否去认证？");
                    mPopupDialogWidget.setOnEventClickListener(new BaseDoubleEventPopup.onEventClickListener() {

                        @Override
                        public void onEventClick(PopupObject obj) {
                            if (obj.getWhat() == 1)
                                UISKipUtils.startRealNameActivity((Activity) context);
                        }
                    });
                    mPopupDialogWidget.showPopupWindow();
                    return;
                }
                investBPGradeRequest(investIds);

            }
        });
    }

    // 数值改变
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

        tv_seekbar_distance.setVisibility(View.VISIBLE);
        tv_seekbar_distance.setText(String.valueOf(progress));
        int position =sb_bp.getProgress(); //seekbar当前进度
        float x = seekBar.getWidth();//seekbar的当前位置
        float seekbarWidth = sb_bp.getX() ; //seekbar的宽度
        float width = (position*x)/11+seekbarWidth; //seekbar当前位置的宽度
        tv_result.setVisibility(View.VISIBLE);
        tv_seekbar_distance.setX(width);
    }
    // 开始拖动
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        SharedPreferences sharedPreferences= getContext().getSharedPreferences("user",
                Activity.MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        int isTruenameAuthen=sharedPreferences.getInt("isTruenameAuthen", -1);
            if (token.equals("")) {
                MessageContentDialog mPopupDialogWidget = new MessageContentDialog((Activity) context);
                mPopupDialogWidget.setMessage("您还未登录，是否去登录？");
                mPopupDialogWidget.setOnEventClickListener(new BaseDoubleEventPopup.onEventClickListener() {

                    @Override
                    public void onEventClick(PopupObject obj) {
                        if (obj.getWhat() == 1)
                            UISKipUtils.startLoginActivity((Activity) context);
                    }
                });
                mPopupDialogWidget.showPopupWindow();
                return;
            }
        if(isTruenameAuthen==0){
            MessageContentDialog mPopupDialogWidget = new MessageContentDialog((Activity) context);
            mPopupDialogWidget.setMessage("您还实名认证，是否去认证？");
            mPopupDialogWidget.setOnEventClickListener(new BaseDoubleEventPopup.onEventClickListener() {

                @Override
                public void onEventClick(PopupObject obj) {
                    if (obj.getWhat() == 1)
                        UISKipUtils.startRealNameActivity((Activity) context);
                }
            });
            mPopupDialogWidget.showPopupWindow();
            return;
        }
    }
    // 停止拖动
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        SharedPreferences sharedPreferences= getContext().getSharedPreferences("user",
                Activity.MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        int isTruenameAuthen=sharedPreferences.getInt("isTruenameAuthen", -1);
        if (token.equals("")) {
            seekBar.setProgress(0);
            tv_result.setVisibility(View.GONE);
            tv_seekbar_distance.setVisibility(View.GONE);
            return;
        }
        if(isTruenameAuthen==0){
            seekBar.setProgress(0);
            tv_result.setVisibility(View.GONE);
            tv_seekbar_distance.setVisibility(View.GONE);
            return;
        }

    }

    private void investBPGradeRequest(int investId){
        SharedPreferences sharedPreferences= getContext().getSharedPreferences("user",
                Activity.MODE_PRIVATE);
        String fen = tv_seekbar_distance.getText().toString();
        String token=sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("tokenId",token);
        staff.put("investId",String.valueOf(investId));
        staff.put("investBPid",String.valueOf(info.getId()));
        staff.put("grade",fen);

        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.INVESTBPGRADE,
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
                                    rl_sp_bp.setVisibility(View.GONE);
                                    ll_pb.setVisibility(View.VISIBLE);
                                    pb_yellowindex.setProgress(info.getScore());
                                    tv_ceek.setText("此BP评价结果");
                                    String fen = tv_seekbar_distance.getText().toString();
                                    int SumTotal = info.getSumTotal()+Integer.valueOf(fen);
                                    int PeopleNum = info.getPeopleNum()+1;
                                    double Score = SumTotal/PeopleNum;
                                    int exponent = (new Double(Score)).intValue();
                                    pb_yellowindex.setProgress(exponent);
                                    tv_fraction.setText(exponent+"分");
                                    tv_peoplenum.setText(PeopleNum+"人");
                                    Bundle bundle = new Bundle();
                                    ReceiverUtils.sendReceiver(ReceiverUtils.SEEKBAR,bundle);
                                } else {
                                    ToastHelper.toastMessage(getContext(), msg);
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

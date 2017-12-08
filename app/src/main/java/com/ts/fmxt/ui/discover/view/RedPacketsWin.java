package com.ts.fmxt.ui.discover.view;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.discover.view.holder.MZHolderCreator;
import com.ts.fmxt.ui.discover.view.holder.MZViewHolder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import http.data.ParticipationsEntity;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.UISKipUtils;

/**
 * Created by kp on 2017/11/23.
 */

public class RedPacketsWin  extends PopupWindow {
    private View mMenuView;
    private static Activity context;
    private MZBannerView mMZBanner;
    private TextView tv_circleX;
    public String url;
    private static List arr;
    private String ids;
    public static final int []RES = new int[]{R.mipmap.yellowback,R.mipmap.redback,R.mipmap.greenback,R.mipmap.yellowback,R.mipmap.redback,R.mipmap.greenback,R.mipmap.yellowback,R.mipmap.redback,R.mipmap.greenback,R.mipmap.yellowback,R.mipmap.redback,R.mipmap.greenback};
    public RedPacketsWin(Activity context,List arrayList){
        this.context = context;
        this.arr = arrayList;
        initParam();

    }

    private void initParam() {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        mMenuView = inflater.inflate(R.layout.pop_window_red_packets, null);
        initView();

        // 设置SelectPicPopupWindow的View
        this.setContentView(mMenuView);
        // 设置按钮监听
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.PopupAnimation);
        // 实例化一个ColorDrawable颜色为半透明 0xb0000000
        ColorDrawable dw = new ColorDrawable(0xb0000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);
        this.setOutsideTouchable(true);
        // 显示窗口
        setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        setHeight(RelativeLayout.LayoutParams.MATCH_PARENT);
    }

    private void initView() {
        tv_circleX = (TextView) mMenuView.findViewById(R.id.tv_circleX);
        mMZBanner = (MZBannerView) mMenuView.findViewById(R.id.banner);
        // 设置页面点击事件
        mMZBanner.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int position) {
//                Toast.makeText(context,"click page:"+position,Toast.LENGTH_LONG).show();
            }
        });

        List<Integer> list = new ArrayList<>();
        List<String> id = new ArrayList<>();
        for(int i=0;i<arr.size();i++){
            ParticipationsEntity info = (ParticipationsEntity) arr.get(i);
            id.add(info.getId()+"");
            list.add(RES[i]);
        }
        String s = id.toString();
        String ss = s.substring(1,s.length() - 1).replaceAll(", null","").trim();
        ids = ss.replaceAll(" ","").trim();
        // 设置数据
        mMZBanner.setPages(list, new MZHolderCreator<BannerViewHolder>() {
            @Override
            public BannerViewHolder createViewHolder() {
                return new BannerViewHolder();
            }
        });

        tv_circleX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
                ParticipationRequest();
            }
        });

//        tv_collection = (TextView) mMenuView.findViewById(R.id.tv_collection);
    }

    public void dissmiss(){
        dismiss();
    }

    public class BannerViewHolder implements MZViewHolder<Integer> {
        private ImageView mImageView,oppen_image;
        private TextView tv_project_name,tv_share_amount,tv_return_rate;
        AnimationDrawable animationDrawable;
        ParticipationsEntity info;
        @Override
        public View createView(Context context) {
            // 返回页面布局文件
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item,null);
            mImageView = (ImageView) view.findViewById(R.id.banner_image);
            oppen_image = (ImageView) view.findViewById(R.id.oppen_image);
            tv_project_name = (TextView) view.findViewById(R.id.tv_project_name);
            tv_share_amount = (TextView) view.findViewById(R.id.tv_share_amount);
            tv_return_rate = (TextView) view.findViewById(R.id.tv_return_rate);
            return view;
        }

        @Override
        public void onBind(Context context, int position, Integer data) {
            // 数据绑定
            info = (ParticipationsEntity) arr.get(position);
            mImageView.setImageResource(data);
            tv_project_name.setText("恭喜 "+info.getInvestProjectName()+" 项目最新分红");
            tv_share_amount.setText(info.getBonusShareAmount()+"");
            tv_return_rate.setText("平均年回报率"+info.getAnnualizedReturn()+"%");
            oppen_image.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //创建旋转动画
                    animationDrawable = (AnimationDrawable)oppen_image.getBackground();
                    animationDrawable.start();//启动动画
                    Message message = handler.obtainMessage(1);     // Message
                    handler.sendMessageDelayed(message, 2500);

                }
            });
        }

        final Handler handler = new Handler() {

            public void handleMessage(Message msg) {         // handle message
                switch (msg.what) {
                    case 1:
                        animationDrawable.stop();
                        UISKipUtils.startDiscoverDetailsActivity(context,info.getInvestId(),1);
                        dissmiss();
                }
                super.handleMessage(msg);
            }
        };
    }

    private void ParticipationRequest(){
        SharedPreferences sharedPreferences= context.getSharedPreferences("user",
                context.MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("tokenId", String.valueOf(token));
        staff.put("participationId", ids);
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.UPDATEINVESTPROJECTPARTICIPATIONREMINDSTATE,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String result) {
                        try {
                            JSONObject js = new JSONObject(result);
                            if(!js.isNull("statsMsg")){
                                JSONObject json = js.optJSONObject("statsMsg");
                                String stats = json.getString("stats");
                                String msg = json.getString("msg");
                                if(stats.equals("1")){
                                   dismiss();
                                }else{
//                                    ToastHelper.toastMessage(context,msg);
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

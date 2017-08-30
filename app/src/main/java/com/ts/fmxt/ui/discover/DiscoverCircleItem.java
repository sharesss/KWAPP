package com.ts.fmxt.ui.discover;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.discover.view.CircleBar;
import com.ts.fmxt.ui.discover.view.RedCircleBar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import http.data.ConsumerEntity;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.UISKipUtils;
import utils.helper.ToastHelper;

import static android.content.Context.MODE_PRIVATE;

/**
 */

public class DiscoverCircleItem implements BaseViewItem,View.OnClickListener {
    private ConsumerEntity info;
    private ViewHolder viewHolder;
    private int type;
    private Resources resources;
    private DiscoverDetailsActivity activity;
    private int investId;

    public DiscoverCircleItem(ConsumerEntity info, DiscoverDetailsActivity activity,int investId) {
        this.info = info;
        this.activity = activity;
        this.investId = investId;
    }

    @Override
    public int getViewType() {
        return getClass().hashCode();
    }

    @Override
    public View createView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.discircle_item_layout, parent, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        viewHolder = (ViewHolder) holder;
        resources = viewHolder.ivCirclebar.getResources();
        viewHolder.ivCirclebar.startCustomAnimation();
        float progress = progressPercentage(info.getVoteNum(), info.getDokels());
        float percentage = num(info.getVoteNum(), info.getDokels());
        int progres = Math.round(percentage);
        viewHolder.ivCirclebar.setText(String.valueOf(progres));//中间的数字
        viewHolder.ivCirclebar.setSweepAngle(progress);//进度
        viewHolder.ivRedCirclebar.startCustomAnimation();

        viewHolder.ivRedCirclebar.startCustomAnimation();
        float inxe = progressPercentage(info.getVoteNum(), info.getNotDokels());
        float percen = num(info.getVoteNum(), info.getNotDokels());
        int progre = Math.round(percen);
        viewHolder.ivRedCirclebar.setText(String.valueOf(progre));//中间的数字
        viewHolder.ivRedCirclebar.setSweepAngle(inxe);//进度
        if (info.getIsVote() == 0) {
            viewHolder.ll_dokels.setOnClickListener(this);
            viewHolder.ll_notdokels.setOnClickListener(this);
        } else if (info.getIsVote() == 1) {
            viewHolder.tvWorth.setBackground(resources.getDrawable(R.drawable.bg_gray_circle));
            viewHolder.tvWorth.setTextColor(resources.getColor(R.color.gray));
            viewHolder.tvNoworth.setBackground(resources.getDrawable(R.drawable.bg_gray_circle));
            viewHolder.tvNoworth.setTextColor(resources.getColor(R.color.gray));
            type = 1;
        } else if (info.getIsVote() == 2) {
            viewHolder.tvWorth.setBackground(resources.getDrawable(R.drawable.bg_gray_circle));
            viewHolder.tvWorth.setTextColor(resources.getColor(R.color.gray));
            viewHolder.tvNoworth.setBackground(resources.getDrawable(R.drawable.bg_gray_circle));
            viewHolder.tvNoworth.setTextColor(resources.getColor(R.color.gray));
            type = 2;
        }

    }
    public int getType() {
        return type;
    }
    @Override
    public RecyclerViewHolder createViewHolder(ViewGroup parent) {
        View view = createView(parent);
        return new ViewHolder(view);
    }

    @Override
    public void onClick(View view) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",
                MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        switch (view.getId()) {
            case R.id.ll_dokels:
                if (token.equals("")) {
                    UISKipUtils.startLoginActivity(activity);
                    return;
                }
                type = 1;
                IsWorthRequest(type);
                break;

            case R.id.ll_notdokels://不值得投
                if (token.equals("")) {
                    UISKipUtils.startLoginActivity(activity);
                    return;
                }
                type = 2;
                IsWorthRequest(type);
                break;
        }
    }

    private class ViewHolder extends RecyclerViewHolder {
        CircleBar ivCirclebar;
        RedCircleBar ivRedCirclebar;
        TextView tvWorth, tvNoworth;
        LinearLayout ll_dokels,ll_notdokels;
        private ViewHolder(View view) {
            super(view);
            ivCirclebar = (CircleBar) view.findViewById(R.id.iv_circlebar);
            ivRedCirclebar = (RedCircleBar) view.findViewById(R.id.iv_redcirclebar);
            tvWorth = (TextView)view.findViewById(R.id.tv_worth);
            tvNoworth = (TextView) view.findViewById(R.id.tv_noworth);
            ll_dokels = (LinearLayout) view.findViewById(R.id.ll_dokels);
            ll_notdokels = (LinearLayout) view.findViewById(R.id.ll_notdokels);
        }
    }

    private float progressPercentage(float max, float min) {
        // TODO Auto-generated method stub
        float percentage = min / max;
        float progress = percentage * 360;
        return progress;

    }

    private float num(float max, float min) {
        // TODO Auto-generated method stub
        float percentage = min / max;
        float progress = percentage * 100;
        return progress;

    }

    //是否值得投
    public void IsWorthRequest(int voteType) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",
                MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("investId", String.valueOf(investId));
        staff.put("voteType", String.valueOf(voteType));
        staff.put("tokenId", String.valueOf(token));

        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.SAVEINVESTPROJECTVOTE,
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
                                    viewHolder.tvWorth.setBackground(resources.getDrawable(R.drawable.bg_gray_circle));
                                    viewHolder.tvWorth.setTextColor(resources.getColor(R.color.gray));
                                    viewHolder.tvNoworth.setBackground(resources.getDrawable(R.drawable.bg_gray_circle));
                                    viewHolder.tvNoworth.setTextColor(resources.getColor(R.color.gray));
                                    viewHolder.ll_dokels.setOnClickListener(null);
                                    viewHolder.ll_notdokels.setOnClickListener(null);
                                } else {
                                    ToastHelper.toastMessage(activity, msg);

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

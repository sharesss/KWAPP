package com.ts.fmxt.ui.discover;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.ts.fmxt.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import http.data.ConsumerCommentEntity;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.SpannableUtils;
import utils.UISKipUtils;
import utils.helper.ToastHelper;
import widget.image.CircleImageView;

/**
 */

public class DisCommentItem implements BaseViewItem {
    private ConsumerCommentEntity entity;
    private DiscoverDetailsActivity activity;
    private int type;

    public DisCommentItem(ConsumerCommentEntity entity, DiscoverDetailsActivity activity, int type) {
        this.entity = entity;
        this.activity = activity;
        this.type = type;
    }

    @Override
    public int getViewType() {
        return getClass().hashCode();
    }

    @Override
    public View createView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_comment_view, parent, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        Resources resources = viewHolder.iv_portrait.getResources();
        viewHolder.iv_portrait.loadImage(entity.getHeadPic());
        viewHolder.tv_time.setText(entity.getTime());

        viewHolder.tv_name.setText(entity.getNickName());
        viewHolder.tv_reply.setText(SpannableUtils.getSpannableLatterStr("回复", entity.getParentUserName(), resources.getColor(R.color.font_main_secondary), 0f));
        viewHolder.tv_reply.setVisibility(entity.getParentId() > 0 ? View.VISIBLE : View.GONE);
        viewHolder.tv_comment.setText(entity.getContent());
        viewHolder.iv_portrait.setOnClickListener(new onItemClick(position));
        viewHolder.tv_likeCount.setText(String.valueOf(entity.getLikeCount()));
        viewHolder.tv_isInvestAuthen.setVisibility(entity.getIsInvestAuthen() > 0 ? View.VISIBLE : View.GONE);
        viewHolder.tv_isfounder.setVisibility(entity.getIsFounder() > 0 ? View.VISIBLE : View.GONE);
        Drawable likeDrawble = resources.getDrawable(entity.getIsLike() == 1 ? R.mipmap.like_icon_s : R.mipmap.like_icon_n);
        likeDrawble.setBounds(0, 0, likeDrawble.getMinimumWidth(), likeDrawble.getMinimumHeight());
        viewHolder.tv_likeCount.setCompoundDrawables(likeDrawble, null, null, null);
        viewHolder.iv_portrait.setOnClickListener(new onItemClick(position));
        viewHolder.tv_replys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                activity.replys(entity.getNickName(), entity);
            }
        });
        viewHolder.tv_likeCount.setOnClickListener(new ClickListener(position));

    }

    @Override
    public RecyclerViewHolder createViewHolder(ViewGroup parent) {
        View view = createView(parent);
        return new ViewHolder(view);
    }

    private class ViewHolder extends RecyclerViewHolder {
        private TextView tv_comment, tv_name, tv_time, tv_reply;
        private CircleImageView iv_portrait;
        private TextView tv_likeCount, tv_isInvestAuthen, tv_isfounder, tv_replys;

        private ViewHolder(View convertView) {
            super(convertView);

            iv_portrait = (CircleImageView) convertView.findViewById(R.id.iv_portrait);
            tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            tv_reply = (TextView) convertView.findViewById(R.id.tv_reply);
            tv_comment = (TextView) convertView.findViewById(R.id.tv_comment);
//            iv_cat = (FMNetImageView) convertView.findViewById(R.id.iv_cat);
            tv_likeCount = (TextView) convertView.findViewById(R.id.tv_likeCount);
            tv_isInvestAuthen = (TextView) convertView.findViewById(R.id.tv_isInvestAuthen);
            tv_isfounder = (TextView) convertView.findViewById(R.id.tv_isfounder);
            tv_replys = (TextView) convertView.findViewById(R.id.tv_replys);
        }
    }

    class onItemClick implements View.OnClickListener {
        private int position;

        public onItemClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (!activity.checkLogin()) {
                return;
            }

            UISKipUtils.startOtherInfomation(activity, entity.getUserId());


        }
    }

    public class ClickListener implements View.OnClickListener {
        private int position;

        public ClickListener(int position) {
            this.position = position;

        }

        @Override
        public void onClick(View v) {
            if (entity.getIsLike() == 0) {
                isLikeRequest(entity, 1);
            } else {
                isLikeRequest(entity, 0);
            }

        }
    }

    private void isLikeRequest(ConsumerCommentEntity info, int state) {
        SharedPreferences sharedPreferences = activity.getSharedPreferences("user",
                activity.MODE_PRIVATE);
        String token = sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("investCommentId", String.valueOf(info.getId()));
        staff.put("tokenId", String.valueOf(token));
        staff.put("state", String.valueOf(state));
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.SAVEINVESTCOMMENTLIKE,
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
                                    activity.CommentRequest(0);
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

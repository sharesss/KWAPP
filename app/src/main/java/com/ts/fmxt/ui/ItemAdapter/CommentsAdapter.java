package com.ts.fmxt.ui.ItemAdapter;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.thindo.base.Adapter.FMBaseAdapter;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.discover.CommentActivity;
import com.ts.fmxt.ui.discover.view.KeyMapDailog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import http.data.ConsumerCommentEntity;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.SpannableUtils;
import utils.UISKipUtils;
import utils.helper.ToastHelper;
import widget.image.CircleImageView;
import widget.image.FMNetImageView;

import static com.ts.fmxt.R.id.tv_likeCount;
import static com.ts.fmxt.R.id.tv_replys;

/**
 * Created by kp on 2017/8/11.
 */
public class CommentsAdapter extends FMBaseAdapter implements KeyMapDailog.SendBackListener{
    private KeyMapDailog dialog;
    private CommentActivity activity;
    private int type;
    private int isAuction;
    public CommentsAdapter(Activity mContext, List<Object> data, int type, int isAuction) {
        super(mContext, data);
        this.activity = (CommentActivity) mContext;
        this.type= type;
        this.isAuction = isAuction;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = View.inflate(getContext(), R.layout.adapter_comment_view, null);
            mHolder.iv_portrait = (CircleImageView) convertView.findViewById(R.id.iv_portrait);
            mHolder.tv_time = (TextView) convertView.findViewById(R.id.tv_time);
            mHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            mHolder.tv_reply = (TextView) convertView.findViewById(R.id.tv_reply);
            mHolder.tv_comment = (TextView) convertView.findViewById(R.id.tv_comment);
            mHolder.iv_cat = (FMNetImageView) convertView.findViewById(R.id.iv_cat);
            mHolder.tv_likeCount = (TextView) convertView.findViewById(tv_likeCount);
            mHolder.tv_isInvestAuthen = (TextView) convertView.findViewById(R.id.tv_isInvestAuthen);
            mHolder.tv_VInvestAuthen= (TextView) convertView.findViewById(R.id.tv_VInvestAuthen);
            mHolder.tv_isfounder = (TextView) convertView.findViewById(R.id.tv_isfounder);
            mHolder.tv_replys = (TextView) convertView.findViewById(tv_replys);
            convertView.setTag(mHolder);
        } else {
            mHolder = (CommentsAdapter.ViewHolder) convertView.getTag();
        }
        ConsumerCommentEntity item = (ConsumerCommentEntity) getItem(position);
        mHolder.iv_portrait.loadImage(item.getHeadPic());
        mHolder.tv_time.setText(item.getTime());

        mHolder.tv_name.setText(item.getNickName());
        mHolder.tv_reply.setText(SpannableUtils.getSpannableLatterStr("回复", item.getParentUserName(), getResourColor(R.color.font_main_secondary), 0f));
        mHolder.tv_reply.setVisibility(item.getParentId() > 0 ? View.VISIBLE : View.GONE);
        mHolder.tv_comment.setText(item.getContent());
        mHolder.iv_portrait.setOnClickListener(new onItemClick(position));
        mHolder.tv_likeCount.setText(String.valueOf(item.getLikeCount()));
        mHolder.tv_isInvestAuthen.setVisibility(item.getIsInvestAuthen() == 1 ? View.VISIBLE : View.GONE);
        mHolder.tv_VInvestAuthen.setVisibility(item.getIsInvestAuthen() == 2 ? View.VISIBLE : View.GONE);
        mHolder.tv_isfounder.setVisibility(item.getIsFounder() > 0 ? View.VISIBLE : View.GONE);
        Drawable likeDrawble = getContext().getResources().getDrawable(item.getIsLike() == 1 ? R.mipmap.like_icon_s : R.mipmap.like_icon_n);
        likeDrawble.setBounds(0, 0, likeDrawble.getMinimumWidth(), likeDrawble.getMinimumHeight());
        mHolder.tv_likeCount.setCompoundDrawables(likeDrawble, null, null, null);
        mHolder.iv_portrait.setOnClickListener(new onItemClick(position));
        mHolder.tv_replys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ConsumerCommentEntity item = (ConsumerCommentEntity) getItem(position);
                activity.replys(item.getNickName(), item);
            }
        });
        mHolder.tv_likeCount.setOnClickListener( new ClickListener(position));
        if(isAuction==1){
            mHolder.tv_likeCount.setVisibility(View.GONE);
            mHolder.tv_replys.setVisibility(View.GONE);
        }
        return convertView;
    }

    @Override
    public void sendBack(String inputText) {

    }

    class onItemClick implements View.OnClickListener {
        private int position;

        public onItemClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            ConsumerCommentEntity info = (ConsumerCommentEntity) getItem(position);
            SharedPreferences sharedPreferences= getContext().getSharedPreferences("user",
                    getContext().MODE_PRIVATE);
            String token=sharedPreferences.getString("token", "");
            if (token.equals("")) {
                UISKipUtils.startLoginActivity((Activity) getContext());
                return;
            } else {
                UISKipUtils.startOtherInfomation((Activity) getContext(), info.getUserId());
            }

        }
    }

    private void isLikeRequest(ConsumerCommentEntity info,int  state){
        SharedPreferences sharedPreferences= getContext().getSharedPreferences("user",
                getContext().MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
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
                                    activity.CommentRequest(type);
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

    public class ClickListener implements View.OnClickListener {
        private int position;

        public ClickListener(int position) {
            this.position = position;

        }

        @Override
        public void onClick(View v) {
            ConsumerCommentEntity item = (ConsumerCommentEntity) getItem(position);
            if(item.getIsLike()==0){
                isLikeRequest(item,1);
                }else{
                isLikeRequest(item,0);
                }
//            ((ConsumerEntity) getList().get(position)).setIsLike(1);
//            ((ConsumerEntity) getList().get(position)).addPraiseNum();

//            ConsumerPraiseRequest request = new ConsumerPraiseRequest();
//            request.setRequestType(7);
//            request.setBillId(String.valueOf(((ConsumerEntity) getList().get(position)).getId()));
//            request.executePost(false);
            notifyDataSetChanged();

        }
    }
    public class ViewHolder {
        private TextView tv_comment, tv_name, tv_time, tv_reply;
        private CircleImageView iv_portrait;
        private FMNetImageView iv_cat;
        private TextView tv_likeCount,tv_isInvestAuthen,tv_isfounder,tv_replys,tv_VInvestAuthen;
    }
}

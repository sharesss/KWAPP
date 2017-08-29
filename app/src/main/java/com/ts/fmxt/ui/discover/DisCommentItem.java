package com.ts.fmxt.ui.discover;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ts.fmxt.R;

import http.data.ConsumerCommentEntity;
import widget.image.CircleImageView;

/**
 */

public class DisCommentItem implements BaseViewItem {
    ConsumerCommentEntity entity;

    public DisCommentItem(ConsumerCommentEntity entity) {
        this.entity = entity;
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
        viewHolder.iv_portrait.loadImage(entity.getHeadPic());
        viewHolder.tv_time.setText(entity.getTime());

        viewHolder.tv_name.setText(entity.getNickName());
//        viewHolder.tv_reply.setText(SpannableUtils.getSpannableLatterStr("回复", entity.getParentUserName(), getResourColor(R.color.font_main_secondary), 0f));
        viewHolder.tv_reply.setVisibility(entity.getParentId() > 0 ? View.VISIBLE : View.GONE);
        viewHolder.tv_comment.setText(entity.getContent());
        viewHolder.iv_portrait.setOnClickListener(new onItemClick(position));
        viewHolder.tv_likeCount.setText(String.valueOf(entity.getLikeCount()));
        viewHolder.tv_isInvestAuthen.setVisibility(entity.getIsInvestAuthen() > 0 ? View.VISIBLE : View.GONE);
        viewHolder.tv_isfounder.setVisibility(entity.getIsFounder() > 0 ? View.VISIBLE : View.GONE);
//        Drawable likeDrawble = getContext().getResources().getDrawable(entity.getIsLike() == 1 ? R.mipmap.like_icon_s : R.mipmap.like_icon_n);
//        likeDrawble.setBounds(0, 0, likeDrawble.getMinimumWidth(), likeDrawble.getMinimumHeight());
//        viewHolder.tv_likeCount.setCompoundDrawables(likeDrawble, null, null, null);
        viewHolder.iv_portrait.setOnClickListener(new onItemClick(position));
        viewHolder.tv_replys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                ConsumerCommentEntity item = (ConsumerCommentEntity) getItem(position);
//                activity.replys(item.getNickName(), item);
            }
        });
        viewHolder.tv_likeCount.setOnClickListener( new ClickListener(position));

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
//            ConsumerCommentEntity info = (ConsumerCommentEntity) getItem(position);
//            SharedPreferences sharedPreferences= getContext().getSharedPreferences("user",
//                    getContext().MODE_PRIVATE);
//            String token=sharedPreferences.getString("token", "");
//            if (token.equals("")) {
//                UISKipUtils.startLoginActivity((Activity) getContext());
//                return;
//            } else {
//                UISKipUtils.startOtherInfomation((Activity) getContext(), info.getUserId());
//            }

        }
    }

        public class ClickListener implements View.OnClickListener {
        private int position;

        public ClickListener(int position) {
            this.position = position;

        }

        @Override
        public void onClick(View v) {
//            ConsumerCommentEntity item = (ConsumerCommentEntity) getItem(position);
//            if(item.getIsLike()==0){
//                isLikeRequest(item,1);
//                }else{
//                isLikeRequest(item,0);
//                }
//
//            notifyDataSetChanged();

        }
    }
}

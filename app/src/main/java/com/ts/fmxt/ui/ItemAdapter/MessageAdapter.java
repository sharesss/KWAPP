package com.ts.fmxt.ui.ItemAdapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.thindo.base.Adapter.FMBaseAdapter;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.user.MessageActivity;

import java.util.ArrayList;

import http.data.MessageData;
import http.data.MessageEntity;
import utils.SpannableUtils;
import widget.testlistview.view.CstSwipeDelMenuViewGroup;

/**
 * Created by kp on 2017/8/19.
 * 通知适配器
 */

public class MessageAdapter extends FMBaseAdapter {
    private MessageActivity activity;
    public MessageAdapter(Context context, ArrayList<Object> listData) {
        super(context, listData);
        this.activity= (MessageActivity) context;
//        this.mMessageFragment = mMessageFragment;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder mHolder = null;
        if (convertView == null) {
            mHolder = new ViewHolder();
            convertView = View.inflate(getContext(), R.layout.item_message, null);
            mHolder.tvName = (TextView) convertView.findViewById(R.id.tv_name);
            mHolder.tv_isInvestAuthen = (TextView) convertView.findViewById(R.id.tv_isInvestAuthen);
            mHolder.tv_type = (TextView) convertView.findViewById(R.id.tv_type);
            mHolder.tv_message_details = (TextView) convertView.findViewById(R.id.tv_message_details);
            mHolder.tvTime = (TextView) convertView.findViewById(R.id.tv_time);
            mHolder.tv_del = (TextView) convertView.findViewById(R.id.tv_del);
            mHolder.iv_red_status = (ImageView) convertView.findViewById(R.id.iv_red_status);
            mHolder.menuViewGroup = (CstSwipeDelMenuViewGroup) convertView.findViewById(R.id.rly_parent);
            mHolder.rl_item = (RelativeLayout) convertView.findViewById(R.id.rl_item);


            convertView.setTag(mHolder);
        } else {
            mHolder = (MessageAdapter.ViewHolder) convertView.getTag();
        }
        mHolder.menuViewGroup.smoothClose();
        mHolder.menuViewGroup.setPosition(position);
        mHolder.menuViewGroup.setMenuOnItemClick(activity);
        MessageEntity info = (MessageEntity) getItem(position);

        Drawable drawable = getContext().getResources().getDrawable(R.mipmap.icon_message_comment);

        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mHolder.tvName.setCompoundDrawables(drawable, null, null, null);
        mHolder.tvName.setText(SpannableUtils.getSpannableStr(info.getUserName(), "", getResourColor(R.color.font_main_secondary), 0.85f));
        mHolder.tv_isInvestAuthen.setVisibility(info.getIsInvestAuthen()==1 ? View.VISIBLE : View.GONE);
        mHolder.tvTime.setText(info.getTiem());
        mHolder.tv_message_details.setText(info.getCount());
        mHolder.iv_red_status.setVisibility(info.getReadFlag() == 0 ? View.VISIBLE : View.GONE);
        mHolder.tv_del.setOnClickListener(new DelMsg(position, mHolder.rl_item));
        mHolder.rl_item.setOnClickListener(new OnItemClick(position));
        return convertView;
    }

    class OnItemClick implements View.OnClickListener {
        private int position;

        public OnItemClick(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            if (!MessageData.getInstance().isDelTag())
            activity.onMenuOnItemClick(position);
        }
    }

    class DelMsg implements View.OnClickListener {
        private int position;
        private RelativeLayout rl_item;

        public DelMsg(int position, RelativeLayout rl_item) {
            this.position = position;
            this.rl_item = rl_item;
        }

        @Override
        public void onClick(View v) {

            MessageEntity info = (MessageEntity) getItem(position);
            activity.cleanNotifyRequest(String.valueOf(info.getType()), info.getMessageId());

        }
    }

    private class ViewHolder {
        private TextView tvName, tvTime, tv_del,tv_isInvestAuthen,tv_type,tv_message_details;
        private ImageView iv_red_status;
        private CstSwipeDelMenuViewGroup menuViewGroup;
        private RelativeLayout rl_item;


    }
}

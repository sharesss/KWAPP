package com.ts.fmxt.ui.im.widget.chatrow;

import android.app.Activity;
import android.content.Context;
import android.text.Spannable;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.LatLng;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.im.utils.EaseSmileUtils;

import utils.StringUtils;
import widget.image.CircleImageView;

/**
 * Created by kp on 2017/10/12.
 * 出价消息扩展
 */

public class ChatRowAuctionBidding extends EaseChatRow{

    private TextView tvMaxmoney,tvMoney,tvAddres;
    private CircleImageView ivBrandIcon;
    private Activity context;
    private int cashCouponId;
    private EMTextMessageBody locBody;
    private TextView contentView;

    public ChatRowAuctionBidding(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
        this.context = (Activity) context;
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_received_auction_bidding : R.layout.ease_row_sent_auction_bidding, this);

    }

    @Override
    protected void onFindViewById() {
        ivBrandIcon = (CircleImageView) findViewById(R.id.iv_brand_icon);
        tvMaxmoney = (TextView) findViewById(R.id.tv_maxmoney);
        tvMoney = (TextView) findViewById(R.id.tv_money);
        tvAddres = (TextView) findViewById(R.id.tv_addres);
        contentView = (TextView) findViewById(R.id.tv_chatcontent);
    }


    @Override
    protected void onSetUpView() {
        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        String msg = "";
        String auction_MsgType = message.getStringAttribute("auction_MsgType", "");
        if (!StringUtils.isEmpty(auction_MsgType)) {
            tv_sys_msg.setVisibility(View.VISIBLE);
            contentView.setVisibility(View.GONE);
            userAvatarView.setVisibility(View.GONE);
            bubbleLayout.setVisibility(View.GONE);
            if (ackedView != null)
                ackedView.setVisibility(View.GONE);
            tv_sys_msg.setText(message.direct() == EMMessage.Direct.SEND ? "聊天通道已开启,快去liao一liao" : "咦,又有小伙伴想认识你~");
        } else {
            if (ackedView != null)
                ackedView.setVisibility(View.GONE);
            tv_sys_msg.setVisibility(View.GONE);
            contentView.setVisibility(View.VISIBLE);
            userAvatarView.setVisibility(View.VISIBLE);
            bubbleLayout.setVisibility(View.VISIBLE);
            msg = txtBody.getMessage();
            Spannable span = EaseSmileUtils.getSmiledText(context, msg);
            // 设置内容
            contentView.setText(span, TextView.BufferType.SPANNABLE);

        }
        // handle sending message
        if (message.direct() == EMMessage.Direct.SEND) {
            setMessageSendCallback();
            switch (message.status()) {
                case CREATE:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.GONE);
                    break;
                case SUCCESS:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.GONE);
                    break;
                case FAIL:
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.GONE);
                    break;
                case INPROGRESS:
                    progressBar.setVisibility(View.VISIBLE);
                    statusView.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        }else{
            if(!message.isAcked() && message.getChatType() == EMMessage.ChatType.Chat){
                try {
                    EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
                } catch (HyphenateException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    protected void onUpdateView() {
        adapter.notifyDataSetChanged();
    }

    @Override
    protected void onBubbleClick() {
//        UISkipUtils.satrtUserPrivilegeCoupons(context, cashCouponId, getResources().getString(R.string.html_privilege_securities_info), getResources().getString(R.string.text_privilege_securities_title));
    }

    /*
	 * listener for map clicked
	 */
    protected class MapClickListener implements View.OnClickListener {

        LatLng location;
        String address;

        public MapClickListener(LatLng loc, String address) {
            location = loc;
            this.address = address;

        }

        @Override
        public void onClick(View v) {

        }
    }

}

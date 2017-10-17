package com.ts.fmxt.ui.im.widget.chatrow;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.LatLng;
import com.ts.fmxt.R;

import utils.ReceiverUtils;

/**
 * Created by kp on 2017/10/12.
 * 出价消息扩展
 */

public class ChatRowAuctionBidding extends EaseChatRow{

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
        contentView = (TextView) findViewById(R.id.tv_chatcontent);
    }


    @Override
    protected void onSetUpView() {
        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        String msg = "";
        String auction_MsgType = message.getStringAttribute("auction_MsgType", "");
        String auction_addPrice= message.getStringAttribute("auction_addPrice", "");
        contentView.setText(auction_addPrice);
        ReceiverUtils.sendReceiver(ReceiverUtils.IMREFRESH,null);
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

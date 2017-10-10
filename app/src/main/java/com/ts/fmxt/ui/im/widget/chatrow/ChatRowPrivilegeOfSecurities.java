package com.ts.fmxt.ui.im.widget.chatrow;/**
 * Created by A1 on 2017/6/8.
 */

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
import com.ts.fmxt.ui.im.domain.PrivilegeOfSecuritiesEntity;

import widget.image.CircleImageView;


/**
 * created by kp at 2017/6/8
 * 消息扩展体，特权卷
 */
public class ChatRowPrivilegeOfSecurities extends EaseChatRow{

    private TextView tvMaxmoney,tvMoney,tvAddres;
    private CircleImageView ivBrandIcon;
    private Activity context;
    private int cashCouponId;
    private EMTextMessageBody locBody;

    public ChatRowPrivilegeOfSecurities(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
        this.context = (Activity) context;
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_received_privilege_securities : R.layout.ease_row_sent_privilege_securities, this);

    }

    @Override
    protected void onFindViewById() {
        ivBrandIcon = (CircleImageView) findViewById(R.id.iv_brand_icon);
        tvMaxmoney = (TextView) findViewById(R.id.tv_maxmoney);
        tvMoney = (TextView) findViewById(R.id.tv_money);
        tvAddres = (TextView) findViewById(R.id.tv_addres);
    }


    @Override
    protected void onSetUpView() {
        locBody = (EMTextMessageBody) message.getBody();
        String addres = message.getStringAttribute(PrivilegeOfSecuritiesEntity.kSendCouponMessageSuccessLocationKey, "");
        String maxmoney = message.getStringAttribute(PrivilegeOfSecuritiesEntity.kSendCouponMessageSuccessTotalMoneyKey, "");
        String money = message.getStringAttribute(PrivilegeOfSecuritiesEntity.kSendCouponMessageSuccessDiscountMoneyKey, "");
        String icon = message.getStringAttribute(PrivilegeOfSecuritiesEntity.kSendCouponMessageSuccessLogoKey, "");
        cashCouponId= message.getIntAttribute(PrivilegeOfSecuritiesEntity.kSendCouponMessageSuccessCashCouponIdKey, 0);
        tvMaxmoney.setText(maxmoney);
        tvMoney.setText(money);
        ivBrandIcon.loadImage(icon);
        tvAddres.setText(addres);
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

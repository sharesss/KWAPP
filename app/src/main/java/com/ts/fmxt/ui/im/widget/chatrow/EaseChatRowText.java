package com.ts.fmxt.ui.im.widget.chatrow;

import android.content.Context;
import android.text.Spannable;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.TextView.BufferType;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.im.utils.EaseSmileUtils;

import utils.StringUtils;

public class EaseChatRowText extends EaseChatRow {

    private TextView contentView;

    public EaseChatRowText(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_received_message : R.layout.ease_row_sent_message, this);
    }

    @Override
    protected void onFindViewById() {
        contentView = (TextView) findViewById(R.id.tv_chatcontent);
    }

    @Override
    public void onSetUpView() {
        EMTextMessageBody txtBody = (EMTextMessageBody) message.getBody();
        String msg = "";
        String kSendGiftMessageSuccessSystemTip = message.getStringAttribute("kSendGiftMessageSuccessSystemTip", "");
        if (!StringUtils.isEmpty(kSendGiftMessageSuccessSystemTip)) {
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
            contentView.setText(span, BufferType.SPANNABLE);

        }
        handleTextMessage();

    }

    protected void handleTextMessage() {
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
                    progressBar.setVisibility(View.GONE);
                    statusView.setVisibility(View.GONE);
                    break;
                default:
                    break;
            }
        } else {
            if (!message.isAcked() && message.getChatType() == ChatType.Chat) {
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
        // TODO Auto-generated method stub

    }


}

package com.ts.fmxt.ui.im.widget.chatrow;

import android.content.Context;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.hyphenate.chat.EMMessage;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.im.EaseUI;
import com.ts.fmxt.ui.im.domain.EaseEmojicon;

import utils.StringUtils;


/**
 * big emoji icons
 */
public class EaseChatRowBigExpression extends EaseChatRowText {

    private ImageView imageView;


    public EaseChatRowBigExpression(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ? R.layout.ease_row_received_bigexpression : R.layout.ease_row_sent_bigexpression, this);
    }

    @Override
    protected void onFindViewById() {
        percentageView = (TextView) findViewById(R.id.percentage);
        imageView = (ImageView) findViewById(R.id.image);
    }


    @Override
    public void onSetUpView() {
        String kLLEmotionTypeFacialCustomGif = message.getStringAttribute("kLLEmotionTypeFacialCustomGif", null);
        imageView.setVisibility(View.GONE);
        if (StringUtils.isEmpty(kLLEmotionTypeFacialCustomGif)) {
            imageView.setVisibility(View.VISIBLE);
            String emojiconId = message.getStringAttribute("codeId", null);
            EaseEmojicon emojicon = null;
            if (EaseUI.getInstance().getEmojiconInfoProvider() != null) {
                emojicon = EaseUI.getInstance().getEmojiconInfoProvider().getEmojiconInfo(emojiconId);
            }
            if (emojicon != null) {
                if (emojicon.getBigIcon() != 0) {
                    Glide.with(activity).load(emojicon.getBigIcon()).placeholder(R.mipmap.ease_default_expression).into(imageView);
                } else if (emojicon.getBigIconPath() != null) {
                    Glide.with(activity).load(emojicon.getBigIconPath()).placeholder(R.mipmap.ease_default_expression).into(imageView);
                } else {
                    imageView.setImageResource(R.mipmap.ease_default_expression);
                }
            }
        }else{
        }
        handleTextMessage();
    }
}

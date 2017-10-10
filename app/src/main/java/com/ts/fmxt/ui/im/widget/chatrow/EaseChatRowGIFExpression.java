package com.ts.fmxt.ui.im.widget.chatrow;

import android.content.Context;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.hyphenate.chat.EMMessage;
import com.ts.fmxt.R;

import utils.StringUtils;
import widget.image.FMNetImageView;



/**
 * big emoji icons
 */
public class EaseChatRowGIFExpression extends EaseChatRowText {

    private FMNetImageView imageView;


    public EaseChatRowGIFExpression(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

    @Override
    protected void onInflateView() {
        inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
                R.layout.ease_row_received_bigexpression : R.layout.ease_row_sent_bigexpression, this);
    }

    @Override
    protected void onFindViewById() {
        percentageView = (TextView) findViewById(R.id.percentage);
        imageView = (FMNetImageView) findViewById(R.id.image);
    }


    @Override
    public void onSetUpView() {
        String kLLEmotionTypeFacialCustomGif = message.getStringAttribute("kLLEmotionTypeFacialCustomGif", null);
        if(!StringUtils.isEmpty(kLLEmotionTypeFacialCustomGif))
        imageView.loadImage(kLLEmotionTypeFacialCustomGif);
        handleTextMessage();
    }
}

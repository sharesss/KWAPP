package com.ts.fmxt.ui.im.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.ts.fmxt.R;
import com.ts.fmxt.ui.im.domain.EaseEmojicon;
import com.ts.fmxt.ui.im.domain.EaseEmojicon.Type;
import com.ts.fmxt.ui.im.utils.EaseSmileUtils;

import java.util.List;

import widget.image.FMNetImageView;


public class EmojiconGridAdapter extends ArrayAdapter<EaseEmojicon> {

    private Type emojiconType;


    public EmojiconGridAdapter(Context context, int textViewResourceId, List<EaseEmojicon> objects, EaseEmojicon.Type emojiconType) {
        super(context, textViewResourceId, objects);
        this.emojiconType = emojiconType;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            if (emojiconType == Type.BIG_EXPRESSION) {
                convertView = View.inflate(getContext(), R.layout.ease_row_big_expression, null);
            } else {
                convertView = View.inflate(getContext(), R.layout.ease_row_expression, null);
            }
        }

        FMNetImageView imageView = (FMNetImageView) convertView.findViewById(R.id.iv_expression);
        TextView textView = (TextView) convertView.findViewById(R.id.tv_name);
        EaseEmojicon emojicon = getItem(position);
        if (textView != null && emojicon.getName() != null) {
            textView.setText(emojicon.getName());
        }

        if (emojicon.getType() == EaseEmojicon.Type.FM_EXPRESSION) {
            imageView.loadImage(emojicon.getUrl());
        } else {
            if (EaseSmileUtils.DELETE_KEY.equals(emojicon.getEmojiText())) {
                imageView.setImageResource(R.mipmap.ease_delete_expression);
            } else {
                if (emojicon.getIcon() != 0) {
                    imageView.setImageResource(emojicon.getIcon());
                } else if (emojicon.getIconPath() != null) {
//                    Glide.with(getContext()).load(emojicon.getIconPath()).placeholder(R.mipmap.ease_default_expression).into(imageView);
                }
            }
        }
        return convertView;
    }

}

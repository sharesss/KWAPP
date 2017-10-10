package com.ts.fmxt.ui.im.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.im.EaseUI;
import com.ts.fmxt.ui.im.domain.EaseUser;

import static android.content.Context.MODE_PRIVATE;


public class EaseUserUtils {
    
    static EaseUI.EaseUserProfileProvider userProvider;
    
    static {
        userProvider = EaseUI.getInstance().getUserProfileProvider();
    }
    
    /**
     * get EaseUser according username
     * @param username
     * @return
     */
    public static EaseUser getUserInfo(String username){
        if(userProvider != null)
            return userProvider.getUser(username);
        
        return null;
    }
    
    /**
     * set user avatar
     * @param username
     */
    public static void setUserAvatar(Context context, String username, ImageView imageView){
    	EaseUser user = getUserInfo(username);
        if(user != null){
            try {
//                int avatarResId = Integer.parseInt(user.getAvatar());
//                Glide.with(context).load(avatarResId).into(imageView);
            } catch (Exception e) {
                //use default avatar
                Glide.with(context).load(user.getAvatar()).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.ease_default_avatar).into(imageView);
            }
            SharedPreferences sharedPreferences = context.getSharedPreferences("ImInfo",
                    MODE_PRIVATE);
            String headpic = sharedPreferences.getString("headpic", "");
            Glide.with(context).load(headpic).into(imageView);
        }else{
            Glide.with(context).load(R.mipmap.ease_default_avatar).into(imageView);
        }
    }
    public static void setUserAvatar(Context context, String username, ImageView imageView, Boolean flg){
        if(username!= null ){
            try {
                int avatarResId = Integer.parseInt(username);
                Glide.with(context).load(avatarResId).into(imageView);
            } catch (Exception e) {
                //use default avatar
                Glide.with(context).load(username).diskCacheStrategy(DiskCacheStrategy.ALL).placeholder(R.mipmap.ease_default_avatar).into(imageView);
            }
        }else{
            if(flg){
                SharedPreferences sharedPreferences = context.getSharedPreferences("ImInfo",
                        MODE_PRIVATE);
                String headpic = sharedPreferences.getString("headpic", "");
                Glide.with(context).load(headpic).into(imageView);
            }else {
                Glide.with(context).load(R.mipmap.ease_default_avatar).into(imageView);
            }
        }
    }

    /**
     * set user's nickname
     */
    public static void setUserNick(String username, TextView textView){
        if(textView != null){
        	EaseUser user = getUserInfo(username);
        	if(user != null && user.getNick() != null){
        		textView.setText(user.getNick());
        	}else{
        		textView.setText(username);
        	}
        }
    }
    
}

package com.ts.fmxt;/**
 * Created by A1 on 2017/7/20.
 */

import android.app.Application;
import android.content.Context;

import cn.sharesdk.framework.ShareSDK;
import widget.image.cache.ImageCacheLoader;


/**
 * created by kp at 2017/7/20
 * instruction
 */
public class FmxtApplication extends Application {
    public static boolean ISRELEASE ;//true=开发 或者测试地址  false=正式地址
    public final static String APP_VERSION = "1.0.0";//每次发版本记得升级
    private static Context context;
    public static final String APP_ID = "wx5d69b438816ee7ea";
    //图片浏览器参数

    public static boolean ISSHOWDOC=true;//是否显示底部的小圆点
    public static boolean RoundCorners=true;//是否圆角

    public static Context getContext() {
        return FmxtApplication.context;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        FmxtApplication.context = getApplicationContext();
        ImageCacheLoader.getInstance().initImageLoader(getContext(), R.mipmap.ic_launcher);
        ShareSDK.initSDK(this);
    }
}
package com.ts.fmxt;/**
 * Created by A1 on 2017/7/20.
 */

import android.app.Application;
import android.content.Context;

import com.ts.fmxt.ui.im.IMHelper;

import java.util.HashSet;
import java.util.Set;

import cn.jpush.android.api.JPushInterface;
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
    public static final String APP_ID = "wx8b7d7b9b4d470443";
    public final static String CreditAPPID="1004396";
    public static String IM_PSW = "EB92EBA134966B62BBB5F409756BBCA3";
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
        IMHelper.getInstance().init(context);
        //初始化sdk
        JPushInterface.setDebugMode(true);//正式版的时候设置false，关闭调试
        JPushInterface.init(this);
        //建议添加tag标签，发送消息的之后就可以指定tag标签来发送了
        Set<String> set = new HashSet<>();
        set.add("andfixdemo");//名字任意，可多添加几个,能区别就好了
        JPushInterface.setTags(this, set, null);//设置标签
    }
}
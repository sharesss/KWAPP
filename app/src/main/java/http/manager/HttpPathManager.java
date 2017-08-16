package http.manager;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.ts.fmxt.FmxtApplication;

public class HttpPathManager {

//     public static String HOST = "http://touzhi.tunnel.qydev.com/investment-app-api/";//曾本地服务器
    public static String HOST = "http://192.168.12.233:8080/investment-app-api/";//阿峰本地服务器
//    public static String HOST = "https://fmb.fmsecret.cn/newfmb-app-server/rest";//正式
//        public static String HOST = "https://wx.fmsecret.cn/investment-app-api/";//测试

    //      public static String HOST = "http://fmb.fmsecret.cn/newfmb-app-server/rest";`


//    public static String HOST = getHttpHead();
    /**
     * 发布类型
     *
     * @return
     */
    public static boolean isReleaseVersion() {
        try {
            Context context = FmxtApplication.getContext();
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            boolean value = bundle.getBoolean("APP_PUBLISH_RELEASE");
            return value;

        } catch (PackageManager.NameNotFoundException e) {
            Log.e("bbtu", "can not get publish type!!!!");
        }
        return true;
    }


    /**
     * 网络请求头
     */
    public static String getHttpHead() {
        try {
            Context context = FmxtApplication.getContext();
            ApplicationInfo ai = context.getPackageManager().getApplicationInfo(
                    context.getPackageName(), PackageManager.GET_META_DATA);
            Bundle bundle = ai.metaData;
            String value = bundle.getString("APP_PUBLISH_HTTP_VALUE");
            return value;

        } catch (PackageManager.NameNotFoundException e) {
            Log.e("bbtu", "can not get publish type!!!!");
        }
        return "";
    }
    /**
     * 接口类名
     */
    public static String USER = "user/";
    public static String MY = "my/";
    public static String CITY = "city/";
    public static String INVEST = "invest/";
    public static String INVESTCOMMENT = "investComment/";
    public static String MESSAGE = "message/";

    /**
     * 接口方法名
     */
    public static String SENTOBTAIN = "sentObtain";
    public static String TOKEN = "token";
    public static String REGISTER = USER+"register";
    public static String SMSCHECK = "smsCheck";
    public static String LOGIN = USER+"login";
    public static String AUTHENTICATION = INVEST+"authentication";
    public static String INFORMATION = MY+"information";
    public static String PERSONAL =MY+"personal";
    public static String PERSONALUPDATE =MY+"personalUpdate";
    public static String CITYLIST =CITY+"getCityList";
    public static String MODIFYPASSWORD = MY+"modifyPassword";
    public static String CHANGENUMBER = MY+"changeNumber";
    public static String INVESTLIST = INVEST+"investList";
    public static String INVESTDETAIL = INVEST+"investDetail";
    public static String INVESTBPLIST = INVEST+"investBPList";
    public static String INVESTCOMMENTLIST = INVESTCOMMENT+"investCommentList";
    public static String SAVEINVESTCOMMENT = INVESTCOMMENT+"saveInvestComment";
    public static String SAVEINVESTCOMMENTLIKE = INVESTCOMMENT+"saveInvestCommentLike";
    public static String INVESTBPGRADE =  INVEST+"investBPGrade";
    public static String GETINVESTPROJECTFOLLOW =  INVEST+"getInvestProjectFollow";
    public static String EDITINVESTPROJECTCOLLECT =  INVEST+"editInvestProjectCollect";
    public static String GETINVESTPROJECTCOLLECT =  INVEST+"getInvestProjectCollect";
    public static String MESSAGELIST =  MESSAGE+"messageList";


}

package http.manager;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.ts.fmxt.FmxtApplication;

public class HttpPathManager {
//     public static String HOST = "http://touzhi.tunnel.qydev.com/investment-app-api/";//曾本地服务器
//    public static String HOST = "http://192.168.1.115:8080/investment-app-api/";//阿峰本地服务器
//         public static String HOST = "http://kzf.tunnel.qydev.com/investment-app-api/";//阿峰本地服务器映射
    public static String HOST = "https://fmb.fmsecret.cn/investment-app-api/";//正式
//        public static String HOST = "https://t.fmsecret.cn/investment-app-api/";//测试

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
    public static String ZHIMA = "zhima/";
    public static String SYSTEM ="system/";
    public static String STOCK ="stock/";
    public static String INVESTRENEWAL="investRenewal/";

    /**
     * 接口方法名
     */
    public static String SENTOBTAIN = "sentObtain";
    public static String TOKEN = "token";
    public static String REGISTER = USER+"register";
    public static String NICKNAMEHEAVY = USER+"nicknameHeavy";
    public static String SMSCHECK = "smsCheck";
    public static String LOGIN = USER+"login";
    public static String AUTHENTICATION = INVEST+"authentication";
    public static String INFORMATION = MY+"information";
    public static String FINDPASSWORD = MY+"findPassword";
    public static String PERSONAL =MY+"personal";
    public static String PERSONALUPDATE =MY+"personalUpdate";
    public static String CITYLIST =CITY+"getCityList";
    public static String MODIFYPASSWORD = MY+"modifyPassword";
    public static String CHANGENUMBER = MY+"changeNumber";
    public static String INVESTLIST = INVEST+"investList";
    public static String INVESTDETAIL = INVEST+"investDetail";
    public static String INVESTBPLIST = INVEST+"investBPList";
    public static String FINDPROJECTPARTICIPATION = INVESTRENEWAL+"findProjectParticipation";
    public static String FINDINVESTPROJECTPARTICIPATIONREMIND = INVESTRENEWAL+"findInvestProjectParticipationRemind";
    public static String UPDATEINVESTPROJECTPARTICIPATIONREMINDSTATE = INVESTRENEWAL+"updateInvestProjectParticipationRemindState";
    public static String INVESTCOMMENTLIST = INVESTCOMMENT+"investCommentList";
    public static String SAVEINVESTCOMMENT = INVESTCOMMENT+"saveInvestComment";
    public static String SAVEINVESTCOMMENTLIKE = INVESTCOMMENT+"saveInvestCommentLike";
    public static String INVESTBPGRADE =  INVEST+"investBPGrade";
    public static String GETINVESTPROJECTFOLLOW =  INVEST+"getInvestProjectFollow";
    public static String EDITINVESTPROJECTCOLLECT =  INVEST+"editInvestProjectCollect";
    public static String GETINVESTPROJECTCOLLECT =  INVEST+"getInvestProjectCollect";
    public static String MESSAGELIST =  MESSAGE+"messageList";
    public static String UPDATEMESSAGEREADFLAG =  MESSAGE+"updateMessageReadFlag";
    public static String UPDATEMESSAGEINFORMFLAG =  MESSAGE+"updateMessageInformFlag";

    public static String SAVEINVESTPROJECTVOTE =  INVEST+"saveInvestProjectVote";
    public static String FINDINVESTRESERVEINFO =  INVEST+"findInvestReserveInfo";
    public static String FINDINVESTPROJECTREARDINFO =  INVEST+"findInvestProjectReardInfo";
    public static String INVESTAMOUNTLIST =  INVEST+"investAmountList";
    public static String SAVEINVESTPROJECTNOTICE =  INVEST+"saveInvestProjectNotice";
    public static String SAVEUSERINVESTPROJECTFOLLOW =  INVEST+"saveUserInvestProjectFollow";
    public static String AUTHENTICATIONDELETE =  MY+"authenticationDelete";
    public static String MESSAGECOUNT =  MESSAGE+"messageCount";
    public static String WEIXINAUTHORIZATION =  USER+"weiXinAuthorization";
    public static String WEIXINBINDMOBILE =  USER+"weiXinBindMobile";
    public static String FINDINFORMINFO =  SYSTEM+"findInformInfo";
    public static String SAVEINVESTINFORMINFO =  SYSTEM+"saveInvestInformInfo";
    public static String getGenerateParams =  ZHIMA+"getGenerateParams";
    public static String ZHIMASCOREGET =  ZHIMA+"zhimaScoreGet";
    public static String FINDUSERCREDITPROPERTYRECORD =  MY+"findUserCreditPropertyRecord";
    public static String FINDSTOCKEQUITYHOME =  STOCK+"findStockEquityHome";
    public static String FINDSTOCKEQUITYDETAILS =  STOCK+"findStockEquityDetails";
    public static String FINDSTOCKEQITYSALESROOMINITINFO =  STOCK+"findStockEqitySalesroomInitInfo";
    public static String FINDSTOCKEQUITYAUCTIONPAYINFO =  STOCK+"findStockEquityAuctionPayInfo";
    public static String FINDSTOCKEQUITYREALINFO =  STOCK+"findStockEquityRealInfo";
    public static String SAVEUSERSTOCKEQUITYREMIND =  STOCK+"saveUserStockEquityRemind";
    public static String  SAVESTOCKEQUITYAUCTIONBID = STOCK+"saveStockEquityAuctionBid";
    public static String  FINDBASICINFO = STOCK+"findBasicInfo";
    public static String  AUTHENTICATIONV2 = INVEST+"authenticationV2";
    public static String  ADDORUPDATEUSERINVESTPREFERENCEINFO = MY+"addOrUpdateUserInvestPreferenceInfo";
    public static String FINDUSERINVESTPREFERENCEINFO = MY+"findUserInvestPreferenceInfo";
    public static String AUTHENTICATIONDELETEV2 = MY+"authenticationDeleteV2";




}

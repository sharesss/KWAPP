package utils;/**
 * Created by A1 on 2017/7/20.
 */

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.thindo.base.Utils.AppManager;
import com.ts.fmxt.ui.MainFrameActivity;
import com.ts.fmxt.ui.discover.DiscoverDetailsActivity;
import com.ts.fmxt.ui.user.AboutusActivity;
import com.ts.fmxt.ui.user.BindNewPhoneActivity;
import com.ts.fmxt.ui.user.CertifiedInvestorActivity;
import com.ts.fmxt.ui.user.CollectionProjectActivity;
import com.ts.fmxt.ui.user.EditActivity;
import com.ts.fmxt.ui.user.EditAutographActivity;
import com.ts.fmxt.ui.user.EditPassWordActivity;
import com.ts.fmxt.ui.user.EditPhoneActivity;
import com.ts.fmxt.ui.user.EditUserInfoActivity;
import com.ts.fmxt.ui.user.FollowProjectActivity;
import com.ts.fmxt.ui.user.MessageActivity;
import com.ts.fmxt.ui.user.OtherInfomationActivity;
import com.ts.fmxt.ui.user.UserAgreementActivity;
import com.ts.fmxt.ui.user.login.ForgetPswActivity;
import com.ts.fmxt.ui.user.login.LoginActivity;
import com.ts.fmxt.ui.user.login.NickNameRegeisterActivity;
import com.ts.fmxt.ui.user.login.RandomCodeActivity;

import http.data.RegisterEntity;

/**
 * created by kp at 2017/7/20
 * instruction
 */
public class UISKipUtils {

    /**
     * 退出程序
     */
    public static void Exit() {
        AppManager.getInstance().exitApp();
    }

    /**
     * 主页
     *
     * @param context
     */
    public static void startMainFrameActivity(Activity context) {
        Intent intent = new Intent(context, MainFrameActivity.class);
        context.startActivity(intent);
    }

    /**
     * 登录
     *
     * @param context
     */
    public static void startLoginActivity(Activity context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    /**
     * 注册
     *
     * @param context
     */
    public static void startRegister(Activity context) {
        Intent intent = new Intent(context, NickNameRegeisterActivity.class);
        context.startActivity(intent);
    }

    //注册---手机号
    public static void startPhoneRegisterActivity(Activity context, RegisterEntity mRegisterEntity) {
        Intent intent = new Intent(context, RandomCodeActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("entity", mRegisterEntity);
        intent.putExtra("bundle", bundle);
        context.startActivity(intent);
    }

    //忘记密码
    public static void startForgetActivity(Activity context) {
        Intent intent = new Intent(context, ForgetPswActivity.class);
        context.startActivity(intent);
    }

    /**
     * 编辑个人资料
     *
     * @param context
     */
    public static void startEditUserInfoActivity(Activity context) {
        Intent intent = new Intent(context, EditUserInfoActivity.class);
        Bundle bundle = new Bundle();
        context.startActivity(intent);
    }
    /**
     * 编辑用户昵称
     *
     * @param context
     * @param type  区分跳转的状态
     */
    public static void startEditNickName(Activity context,String name,int type){
        Intent intent = new Intent(context, EditActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("type", type);
        context.startActivityForResult(intent, type);
    }

    /**
     * 验证当前手机号
     *
     * @param context
     *
     */
    public static void startEditphone(Activity context,String phone){
        Intent intent = new Intent(context, EditPhoneActivity.class);
        intent.putExtra("phone", phone);
        context.startActivity(intent);
    }
    /**
     * 绑定新手机
     * @param context
     *
     */
    public static void startBindNewPhone(Activity context){
        Intent intent = new Intent(context, BindNewPhoneActivity.class);
        context.startActivity(intent);
    }

    /**
     * 修改密码
     * @param context
     *
     */
    public static void startEditPassWord(Activity context){
        Intent intent = new Intent(context, EditPassWordActivity.class);
        context.startActivity(intent);
    }

    /**
     * 签名
     * @param context
     *
     */
    public static void startEditAutograph(Activity context,String name,int type){
        Intent intent = new Intent(context, EditAutographActivity.class);
        intent.putExtra("name", name);
        intent.putExtra("type", type);
        context.startActivityForResult(intent, type);
    }

    /**
     * 他人信息
     * @param context
     *
     */
    public static void startOtherInfomation(Activity context){
        Intent intent = new Intent(context, OtherInfomationActivity.class);
        context.startActivity(intent);
    }

    /**
     * 想跟投的项目
     * @param context
     *
     */
    public static void startFollowProject(Activity context){
        Intent intent = new Intent(context, FollowProjectActivity.class);
        context.startActivity(intent);
    }

    /**
     * 收藏的项目
     * @param context
     *
     */
    public static void startCollectionProject(Activity context){
        Intent intent = new Intent(context,CollectionProjectActivity.class);
        context.startActivity(intent);
    }

    /**
     * 认证投资人
     * @param context
     *
     */
    public static void startCertifiedInvestorActivity(Activity context,int type){
        Intent intent = new Intent(context, CertifiedInvestorActivity.class);
        intent.putExtra("type", type);
        context.startActivity(intent);
    }
    public static void startMessageActivity(Activity context){
        Intent intent = new Intent(context, MessageActivity.class);
        context.startActivity(intent);
    }

    public static void startDiscoverDetailsActivity(Activity context,int id){
        Intent intent = new Intent(context, DiscoverDetailsActivity.class);
        intent.putExtra("id", id);
        context.startActivity(intent);
    }

    public static void satrtUserAgreement(Activity context,String url, String title){
        Intent intent = new Intent(context, UserAgreementActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    public static void satrtAboutus(Activity context,String url, String title){
        Intent intent = new Intent(context, AboutusActivity.class);
        intent.putExtra("url", url);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

}

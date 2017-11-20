package utils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

import com.ts.fmxt.FmxtApplication;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import http.Logger;
/**
 * @author kp
 * @Description:描述:封装的广播发送和接收
 */

public class ReceiverUtils {

    private static Logger logger = new Logger("ReceiverUtils");
    private final static String RECEIVER_ACTION = "utils.receiver";
    private final static String ACTION_TYPE_NAME = "ReceiverTypeName";
    private final static String ACTION_BUNDLE = "bundle";

    //请在以下位置声明不同类型的ReceiverType 如： public final static int LOGIN_SUCCESS = 1;//登陆成功

    public final static int REGISTER_IMAGE_UPLOADER= 11;//注册上传头像
    public final static int REGISTER_FINISH= 12;//注册流程结束关闭
    public final static int MODIFY_PHONE= 13;//注册流程结束关闭
    public final static int REFRESH= 14;//注册流程结束关闭
    public final static int SEEKBAR= 15;//投票刷新详情界面
    public final static int WX_PLAY =16;//微信支付完成
    public final static int REPORT =17;//展示举报的其他输入框
    public final static int GONE =18;//取消展示举报的其他输入框
    public final static int IMREFRESH = 19;     //出价成功
    public final static int AUCTION_STATUS = 20;     //出价成功
    public final static int CERTIFIEDINVESTOR_FINISH = 21;     //认证结束








    static class MessageObserverReceiver extends BroadcastReceiver {

        public MessageObserverReceiver() {
            FmxtApplication.getContext().registerReceiver(this, new IntentFilter(RECEIVER_ACTION));
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            int receiverType = intent.getIntExtra(ACTION_TYPE_NAME, 0);
            Bundle bundle = intent.getBundleExtra(ACTION_BUNDLE);
            for (MessageReceiver receiver : receiverList) {
                receiver.onMessage(receiverType, bundle);
            }
        }

    }

    private static MessageObserverReceiver broadCast = new MessageObserverReceiver();

    /**
     * 发送广播
     */
    public static void sendReceiver(int receiverType, Bundle bundle) {
        if (!receiverList.isEmpty()) {
            logger.i("SendReceiver : " + String.valueOf(receiverType));
            Intent intent = new Intent(RECEIVER_ACTION);
            intent.putExtra(ACTION_TYPE_NAME, receiverType);
            intent.putExtra(ACTION_BUNDLE, bundle);
            FmxtApplication.getContext().sendBroadcast(intent);
        }
    }

    /**
     * 添加广播监听器
     */
    public static void addReceiver(MessageReceiver receiver) {
        receiverList.add(receiver);
        logger.i(" --- addReceiver --- ");
    }

    /**
     * 移除广播监听器
     */
    public static void removeReceiver(MessageReceiver receiver) {
        receiverList.remove(receiver);
        logger.i(" --- removeReceiver --- ");
    }

    public interface MessageReceiver {

        public void onMessage(int receiverType, Bundle bundle);
    }

    private static List<MessageReceiver> receiverList;

    static {
        receiverList = Collections.synchronizedList(new ArrayList<MessageReceiver>());
    }
}

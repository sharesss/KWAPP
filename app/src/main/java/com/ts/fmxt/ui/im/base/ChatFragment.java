package com.ts.fmxt.ui.im.base;

import android.app.Activity;
import android.content.ClipData;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.util.PathUtil;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.im.Constant;
import com.ts.fmxt.ui.im.ContextMenuActivity;
import com.ts.fmxt.ui.im.EaseChatFragment;
import com.ts.fmxt.ui.im.EaseConstant;
import com.ts.fmxt.ui.im.ForwardMessageActivity;
import com.ts.fmxt.ui.im.IMHelper;
import com.ts.fmxt.ui.im.ImageGridActivity;
import com.ts.fmxt.ui.im.VideoCallActivity;
import com.ts.fmxt.ui.im.VoiceCallActivity;
import com.ts.fmxt.ui.im.domain.AuctionBiddingEntity;
import com.ts.fmxt.ui.im.domain.EmojiconExampleGroupData;
import com.ts.fmxt.ui.im.domain.EmojiconFMGroupData;
import com.ts.fmxt.ui.im.domain.PrivilegeOfSecuritiesEntity;
import com.ts.fmxt.ui.im.domain.RobotUser;
import com.ts.fmxt.ui.im.widget.chatrow.ChatRowAuctionBidding;
import com.ts.fmxt.ui.im.widget.chatrow.ChatRowPrivilegeOfSecurities;
import com.ts.fmxt.ui.im.widget.chatrow.EaseChatRow;
import com.ts.fmxt.ui.im.widget.chatrow.EaseCustomChatRowProvider;
import com.ts.fmxt.ui.im.widget.emojicon.EaseEmojiconMenu;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;
import java.util.Map;

import utils.ReceiverUtils;
import utils.sharePreferences.FMWession;

import static android.content.Context.MODE_PRIVATE;

public class ChatFragment extends EaseChatFragment implements EaseChatFragment.EaseChatFragmentHelper, ReceiverUtils.MessageReceiver {
    // constant start from 11 to avoid conflict with constant in base class
    private static final int ITEM_VIDEO = 11;
    private static final int ITEM_FILE = 12;
    private static final int ITEM_VOICE_CALL = 13;
    private static final int ITEM_VIDEO_CALL = 14;

    private static final int REQUEST_CODE_SELECT_VIDEO = 11;
    private static final int REQUEST_CODE_SELECT_FILE = 12;
    private static final int REQUEST_CODE_GROUP_DETAIL = 13;
    private static final int REQUEST_CODE_CONTEXT_MENU = 14;
    private static final int REQUEST_CODE_SELECT_AT_USER = 15;


    private static final int MESSAGE_TYPE_SENT_VOICE_CALL = 1;
    private static final int MESSAGE_TYPE_RECV_VOICE_CALL = 2;
    private static final int MESSAGE_TYPE_SENT_VIDEO_CALL = 3;
    private static final int MESSAGE_TYPE_RECV_VIDEO_CALL = 4;

    //red packet code : 红包功能使用的常量
    private static final int MESSAGE_TYPE_RECV_RED_PACKET = 5;
    private static final int MESSAGE_TYPE_SEND_RED_PACKET = 6;
    private static final int MESSAGE_TYPE_SEND_RED_PACKET_ACK = 7;
    private static final int MESSAGE_TYPE_RECV_RED_PACKET_ACK = 8;
    private static final int REQUEST_CODE_SEND_RED_PACKET = 16;
    private static final int ITEM_RED_PACKET = 16;
    //end of red packet code
    /**
     * 特权卷
     */
    static final int ITEM_PRIVILEGE_OF_SECURITIES = 90;
    static final int REQUEST_CODE_SELEST_ITEM_PRIVILEGE_OF_SECURITIES = 91;
    static final int MESSAGE_ITEM_RECV_PRIVILEGE_OF_SECURITIES = 92;
    static final int MESSAGE_ITEM_SENT_PRIVILEGE_OF_SECURITIES = 93;
    /**
     * 出价
     */
    static final int MESSAGE_TYPE_RECV_AuctionBidding = 94;
    static final int MESSAGE_TYPE_SENT_AuctionBidding = 95;
    /**
     * if it is chatBot
     */
    private boolean isRobot;
    private Activity mActivity;
    private Boolean isHaveData = true;//为了避免多次收到广播加的判断
    String price ;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ReceiverUtils.addReceiver(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        this.mActivity = context;
    }

    @Override
    public void onMessage(int receiverType, Bundle bundle) {
        if (receiverType == ReceiverUtils.IMREFRESH) {
//                if(bundle.getString("type").equals("1")){
//                    isRobot=true;
//                }
//            price = bundle.getString("price");
//            sendMsg();
        }

    }

    @Override
    protected void setUpView() {
        setChatFragmentListener(this);


        if (chatType == Constant.CHATTYPE_SINGLE) {
            Map<String, RobotUser> robotMap = IMHelper.getInstance().getRobotList();
            if (robotMap != null && robotMap.containsKey(toChatUsername)) {
//                isRobot = true;
            }
        }
        super.setUpView();
        // set click listener
        titleBar.setLeftLayoutClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
//                ReceiverUtils.sendReceiver(ReceiverUtils.INDEX_IM, null);

            }
        });
        ((EaseEmojiconMenu) inputMenu.getEmojiconMenu()).addEmojiconGroup(EmojiconExampleGroupData.getData());
        //疯蜜自定义表情
        ((EaseEmojiconMenu) inputMenu.getEmojiconMenu()).addEmojiconGroup(EmojiconFMGroupData.getData());
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            inputMenu.getPrimaryMenu().getEditText().addTextChangedListener(new TextWatcher() {

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    if (count == 1 && "@".equals(String.valueOf(s.charAt(start)))) {
//                        startActivityForResult(new Intent(getActivity(), PickAtUserActivity.class).
//                                putExtra("groupId", toChatUsername), REQUEST_CODE_SELECT_AT_USER);
                    }
                }

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });
        }
//        String path = FMWession.getInstance().getSendGifUri();
//        if (!StringUtils.isEmpty(path)) {
//            sendSysImageMsg(path);//发送打赏图片信息
//
//        }
    }

//    private void sendSysImageMsg(final String path) {
//        com.nostra13.universalimageloader.core.ImageLoader.getInstance().loadImage(path, new ImageLoadingListener() {
//            @Override
//            public void onLoadingStarted(String imageUri, View view) {
//            }
//
//            @Override
//            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
//            }
//
//            @Override
//            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//                String imagePath = FileUtil.saveBitmap(loadedImage, DateFormatUtils.formatDateStr("yyyyMMHHddmmss"));
//                EMMessage imageMsg = EMMessage.createImageSendMessage(imagePath, false, toChatUsername);
//                imageMsg.setAttribute("kSendGiftMessageSuccessURLTip", path);
//                imageMsg.setAttribute("kSendGiftMessageSuccessNumberTip", FMWession.getInstance().getSendGifNum());
//                EMClient.getInstance().chatManager().sendMessage(imageMsg);
//                FMWession.getInstance().setSendGifUri("");
//                new AsyncMainTask().execute();
//            }
//
//            @Override
//            public void onLoadingCancelled(String imageUri, View view) {
//            }
//        });
//
//    }

    private void sendPrivilegeOfSecuritiesMsg() {
        isHaveData = false;
        SharedPreferences share = mActivity.getSharedPreferences("ee",
                MODE_PRIVATE);
        if (share != null) {
            EMMessage imageMsg = EMMessage.createTxtSendMessage("特权卷", toChatUsername);
            imageMsg.setAttribute(PrivilegeOfSecuritiesEntity.kSendCouponMessageSuccess, "kSendCouponMessageSuccessTip");
            imageMsg.setAttribute(PrivilegeOfSecuritiesEntity.kSendCouponMessageSuccessStoreIdIdKey, share.getInt("storeId", 0));
            imageMsg.setAttribute(PrivilegeOfSecuritiesEntity.kSendCouponMessageSuccessLocationKey, share.getString("storeAddress", ""));
            imageMsg.setAttribute(PrivilegeOfSecuritiesEntity.kSendCouponMessageSuccessDiscountMoneyKey, share.getInt("couponBackMoney", 0));
            imageMsg.setAttribute(PrivilegeOfSecuritiesEntity.kSendCouponMessageSuccessTotalMoneyKey, share.getInt("couponFullMoney", 0));
            imageMsg.setAttribute(PrivilegeOfSecuritiesEntity.kSendCouponMessageSuccessCashCouponIdKey, share.getInt("userCashCouponId", 0));
            imageMsg.setAttribute(PrivilegeOfSecuritiesEntity.kSendCouponMessageSuccessLogoKey, share.getString("brandLogo", ""));
            EMClient.getInstance().chatManager().sendMessage(imageMsg);
            SharedPreferences.Editor editor = share.edit();
            editor.clear();
            FMWession.getInstance().setSendGifName("");
            new AsyncMainTask().execute();

        }
    }

    private class AsyncMainTask extends AsyncTask<String, Object, Integer> {

        @Override
        protected Integer doInBackground(String... params) {
            SystemClock.sleep(1000);
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            sendSysTextMsg();//发送文字信息
            refeshIMUI();
        }
    }

    private void sendSysTextMsg() {
        EMMessage message = EMMessage.createTxtSendMessage("系统消息", toChatUsername);
        message.setAttribute("kSendGiftMessageSuccessSystemTip", "kSendGiftMessageSuccessSystemTip");
        message.setAttribute("kSendGiftMessageSuccessNAMETip", FMWession.getInstance().getSendGifName());
        EMClient.getInstance().chatManager().sendMessage(message);
        FMWession.getInstance().setSendGifName("");
    }

    private void refeshIMUI() {
        String otherAvatarUri = String.format("im_avatar_%s", toChatUsername);
        String uri = FMWession.getInstance().getIM(otherAvatarUri);
        messageList.refresh(uri);
    }


    @Override
    protected void registerExtendMenuItem() {
        //use the menu in base class
        super.registerExtendMenuItem();
//        inputMenu.registerExtendMenuItem("特权劵", R.drawable.ic_privilege_securities, ITEM_PRIVILEGE_OF_SECURITIES, extendMenuItemClickListener);
        //extend menu items
//        inputMenu.registerExtendMenuItem(R.string.attach_video, R.drawable.em_chat_video_selector, ITEM_VIDEO, extendMenuItemClickListener);
//        inputMenu.registerExtendMenuItem(R.string.attach_file, R.drawable.em_chat_file_selector, ITEM_FILE, extendMenuItemClickListener);
//        if (chatType == Constant.CHATTYPE_SINGLE) {
//            inputMenu.registerExtendMenuItem(R.string.attach_voice_call, R.drawable.em_chat_voice_call_selector, ITEM_VOICE_CALL, extendMenuItemClickListener);
//            inputMenu.registerExtendMenuItem(R.string.attach_video_call, R.drawable.em_chat_video_call_selector, ITEM_VIDEO_CALL, extendMenuItemClickListener);
//        }
        //聊天室暂时不支  持红包功能
        //red packet code : 注册红包菜单选项
//        if (chatType != Constant.CHATTYPE_CHATROOM) {
//            inputMenu.registerExtendMenuItem(R.string.attach_red_packet, R.drawable.em_chat_red_packet_selector, ITEM_RED_PACKET, extendMenuItemClickListener);
//        }
        //end of red packet code
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_CONTEXT_MENU) {
            switch (resultCode) {
                case ContextMenuActivity.RESULT_CODE_COPY: // copy
                    clipboard.setPrimaryClip(ClipData.newPlainText(null,
                            ((EMTextMessageBody) contextMenuMessage.getBody()).getMessage()));
                    break;
                case ContextMenuActivity.RESULT_CODE_DELETE: // delete
                    conversation.removeMessage(contextMenuMessage.getMsgId());
                    messageList.refresh();
                    break;

                case ContextMenuActivity.RESULT_CODE_FORWARD: // forward
                    Intent intent = new Intent(getActivity(), ForwardMessageActivity.class);
                    intent.putExtra("forward_msg_id", contextMenuMessage.getMsgId());
                    startActivity(intent);

                    break;

                default:
                    break;
            }
        }
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE_SELECT_VIDEO: //send the video
                    if (data != null) {
                        int duration = data.getIntExtra("dur", 0);
                        String videoPath = data.getStringExtra("path");
                        File file = new File(PathUtil.getInstance().getImagePath(), "thvideo" + System.currentTimeMillis());
                        try {
                            FileOutputStream fos = new FileOutputStream(file);
                            Bitmap ThumbBitmap = ThumbnailUtils.createVideoThumbnail(videoPath, 3);
                            ThumbBitmap.compress(CompressFormat.JPEG, 100, fos);
                            fos.close();
                            sendVideoMessage(videoPath, file.getAbsolutePath(), duration);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case REQUEST_CODE_SELECT_FILE: //send the file
                    if (data != null) {
                        Uri uri = data.getData();
                        if (uri != null) {
                            sendFileByUri(uri);
                        }
                    }
                    break;
                case REQUEST_CODE_SELECT_AT_USER:
                    if (data != null) {
                        String username = data.getStringExtra("username");
                        inputAtUsername(username, false);
                    }
                    break;
                //red packet code : 发送红包消息到聊天界面
                case REQUEST_CODE_SEND_RED_PACKET:
//                    if (data != null){
//                        sendMessage(RedPacketUtil.createRPMessage(getActivity(), data, toChatUsername));
//                    }
                    break;
                //end of red packet code
                default:
                    break;
            }
        }

        if (resultCode == REQUEST_CODE_SELEST_ITEM_PRIVILEGE_OF_SECURITIES) {// 特权劵回调

//            ExpiredEntity eeinfo = (ExpiredEntity) data.getSerializableExtra("ee");
//            EMMessage message = EMMessage.createTxtSendMessage("特权卷",toChatUsername);
//            message.setAttribute(PrivilegeOfSecuritiesEntity.kSendCouponMessageSuccess, "kSendCouponMessageSuccessTip");
//            message.setAttribute(PrivilegeOfSecuritiesEntity.kSendCouponMessageSuccessStoreIdIdKey, eeinfo.getStoreId());
//            message.setAttribute(PrivilegeOfSecuritiesEntity.kSendCouponMessageSuccessLocationKey, eeinfo.getStoreAddress());
//            message.setAttribute(PrivilegeOfSecuritiesEntity.kSendCouponMessageSuccessDiscountMoneyKey, eeinfo.getCouponBackMoney());
//            message.setAttribute(PrivilegeOfSecuritiesEntity.kSendCouponMessageSuccessTotalMoneyKey, eeinfo.getCouponFullMoney());
//            message.setAttribute(PrivilegeOfSecuritiesEntity.kSendCouponMessageSuccessCashCouponIdKey, eeinfo.getId());
//            message.setAttribute(PrivilegeOfSecuritiesEntity.kSendCouponMessageSuccessLogoKey, eeinfo.getBrandLogo());
//            EMClient.getInstance().chatManager().sendMessage(message);
//            FMWession.getInstance().setSendGifName("");
            new AsyncMainTask().execute();
        }

    }

    @Override
    public void onSetMessageAttributes(EMMessage message) {

        SharedPreferences sharedPreferences =getActivity().getSharedPreferences("ImInfo",
                MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId",0);
        String name = sharedPreferences.getString("name", "");
        String headpic = sharedPreferences.getString("headpic", "");
        message.setAttribute(AuctionBiddingEntity.auction_userID, userId+"");
        message.setAttribute(EaseConstant.EXTRA_USER_IMG, headpic);
        message.setAttribute(EaseConstant.EXTRA_USER_NAME, name);
//        if(price!=null){
//            message.setAttribute(AuctionBiddingEntity.auction_addPrice,"出价"+price+"元");
//        }
//        if (isRobot) {
//            //set message extension
//            message.setAttribute(AuctionBiddingEntity.auction_MsgType, "1");
//        }else{
//            message.setAttribute(AuctionBiddingEntity.auction_MsgType, "0");
//        }
    }

    public void sendMsg() {
        SharedPreferences sharedPreferences = this.getActivity().getSharedPreferences("ImInfo",
                getActivity().MODE_PRIVATE);
        int userId = sharedPreferences.getInt("userId",0);
        String name = sharedPreferences.getString("name", "");
        String headpic = sharedPreferences.getString("headpic", "");
        EMMessage message = EMMessage.createTxtSendMessage("出价",toChatUsername);
        message.setAttribute(AuctionBiddingEntity.auction_userID, userId+"");
        message.setAttribute(EaseConstant.EXTRA_USER_IMG, headpic);
        message.setAttribute(EaseConstant.EXTRA_USER_NAME, name);
        if(price!=null){
            message.setAttribute(AuctionBiddingEntity.auction_addPrice,"出价"+price+"元");
        }
        message.setAttribute(AuctionBiddingEntity.auction_MsgType, "1");
        //send message
        EMClient.getInstance().chatManager().sendMessage(message);
//            messageList.refreshSelectLast();

    }

    @Override
    public EaseCustomChatRowProvider onSetCustomChatRowProvider() {
        return new CustomChatRowProvider();
    }


    @Override
    public void onEnterToChatDetails() {
        if (chatType == Constant.CHATTYPE_GROUP) {
            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
            if (group == null) {
                Toast.makeText(getActivity(), R.string.gorup_not_found, Toast.LENGTH_SHORT).show();
                return;
            }
//            startActivityForResult(
//                    (new Intent(getActivity(), GroupDetailsActivity.class).putExtra("groupId", toChatUsername)),
//                    REQUEST_CODE_GROUP_DETAIL);
        } else if (chatType == Constant.CHATTYPE_CHATROOM) {
            //startActivityForResult(new Intent(getActivity(), ChatRoomDetailsActivity.class).putExtra("roomId", toChatUsername), REQUEST_CODE_GROUP_DETAIL);
        }
    }

    @Override
    public void onAvatarClick(String username) {
        //handling when user click avatar
//        Intent intent = new Intent(getActivity(), UserProfileActivity.class);
//        intent.putExtra("username", username);
//        startActivity(intent);
    }

    @Override
    public void onAvatarLongClick(String username) {
        inputAtUsername(username);
    }


    @Override
    public boolean onMessageBubbleClick(EMMessage message) {
        //消息框点击事件，demo这里不做覆盖，如需覆盖，return true
        //red packet code : 拆红包页面
//        if (message.getBooleanAttribute(RedPacketConstant.MESSAGE_ATTR_IS_RED_PACKET_MESSAGE, false)){
//            RedPacketUtil.openRedPacket(getActivity(), chatType, message, toChatUsername, messageList);
//            return true;
//        }
        //end of red packet code
        return false;
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {
        //red packet code : 处理红包回执透传消息
//        for (EMMessage message : messages) {
//            EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
//            String action = cmdMsgBody.action();//获取自定义action
//            if (action.equals(RedPacketConstant.REFRESH_GROUP_RED_PACKET_ACTION)){
//                RedPacketUtil.receiveRedPacketAckMessage(message);
//                messageList.refresh();
//            }
//        }
        //end of red packet code
        super.onCmdMessageReceived(messages);
    }

    @Override
    public void onMessageBubbleLongClick(EMMessage message) {
        // no message forward when in chat room
//        startActivityForResult((new Intent(getActivity(), ContextMenuActivity.class)).putExtra("message",message)
//                        .putExtra("ischatroom", chatType == EaseConstant.CHATTYPE_CHATROOM),
//                REQUEST_CODE_CONTEXT_MENU);
    }

    @Override
    public boolean onExtendMenuItemClick(int itemId, View view) {
        switch (itemId) {
            case ITEM_VIDEO:
                Intent intent = new Intent(getActivity(), ImageGridActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT_VIDEO);
                break;
            case ITEM_FILE: //file
                selectFileFromLocal();
                break;
            case ITEM_VOICE_CALL:
                startVoiceCall();
                break;
            case ITEM_VIDEO_CALL:
                startVideoCall();
                break;
            //red packet code : 进入发红包页面
            case ITEM_RED_PACKET:
//                   RedPacketUtil.startRedPacketActivityForResult(this, chatType, toChatUsername, REQUEST_CODE_SEND_RED_PACKET);
                break;
            //end of red packet code
            case ITEM_PRIVILEGE_OF_SECURITIES:
//                Intent in  = new Intent(getActivity(), SelectPrivilegedVolumeActivity.class);
//                in.putExtra("type",1);
//                in.putExtra("userId", Integer.valueOf(FMUserId));
//                startActivityForResult(in, REQUEST_CODE_SELEST_ITEM_PRIVILEGE_OF_SECURITIES);

                break;
            default:
                break;
        }
        //keep exist extend menu
        return false;
    }

    /**
     * select file
     */
    protected void selectFileFromLocal() {
        Intent intent = null;
        if (Build.VERSION.SDK_INT < 19) { //api 19 and later, we can't use this way, demo just select from images
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("*/*");
            intent.addCategory(Intent.CATEGORY_OPENABLE);

        } else {
            intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_SELECT_FILE);
    }

    /**
     * make a voice call
     */
    protected void startVoiceCall() {
        if (!EMClient.getInstance().isConnected()) {
            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
        } else {
            startActivity(new Intent(getActivity(), VoiceCallActivity.class).putExtra("username", toChatUsername)
                    .putExtra("isComingCall", false));
            // voiceCallBtn.setEnabled(false);
            inputMenu.hideExtendMenuContainer();
        }
    }

    /**
     * make a video call
     */
    protected void startVideoCall() {
        if (!EMClient.getInstance().isConnected())
            Toast.makeText(getActivity(), R.string.not_connect_to_server, Toast.LENGTH_SHORT).show();
        else {
            startActivity(new Intent(getActivity(), VideoCallActivity.class).putExtra("username", toChatUsername)
                    .putExtra("isComingCall", false));
            // videoCallBtn.setEnabled(false);
            inputMenu.hideExtendMenuContainer();
        }
    }

    /**
     * chat row provider
     */
    private final class CustomChatRowProvider implements EaseCustomChatRowProvider {
        @Override
        public int getCustomChatRowTypeCount() {
            //here the number is the message type in EMMessage::Type
            //which is used to count the number of different chat row
//            if(IMHelper.getInstance().isPrivilegeOfSecurities(message)){
//                //发送特权卷
//                return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_ITEM_RECV_PRIVILEGE_OF_SECURITIES : MESSAGE_ITEM_SENT_PRIVILEGE_OF_SECURITIES;
//            }
            return 8;
        }

        @Override
        public int getCustomChatRowType(EMMessage message) {
            if (message.getType() == EMMessage.Type.TXT) {
                //voice call
                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false)) {
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VOICE_CALL : MESSAGE_TYPE_SENT_VOICE_CALL;
                } else if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)) {
                    //video call
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_VIDEO_CALL : MESSAGE_TYPE_SENT_VIDEO_CALL;
                } else if (IMHelper.getInstance().isPrivilegeOfSecurities(message)) {
//                    发送特权卷
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_ITEM_RECV_PRIVILEGE_OF_SECURITIES : MESSAGE_ITEM_SENT_PRIVILEGE_OF_SECURITIES;
                } else if (IMHelper.getInstance().isAuctionBidding(message)) {
//                    出价
                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_AuctionBidding : MESSAGE_TYPE_SENT_AuctionBidding;
                }

                //red packet code : 红包消息和红包回执消息的chat row type
//                else if (message.getBooleanAttribute(RedPacketConstant.MESSAGE_ATTR_IS_RED_PACKET_MESSAGE, false)) {
//                    //发送红包消息
//                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_RED_PACKET : MESSAGE_TYPE_SEND_RED_PACKET;
//                } else if (message.getBooleanAttribute(RedPacketConstant.MESSAGE_ATTR_IS_RED_PACKET_ACK_MESSAGE, false)) {
//                    //领取红包消息
//                    return message.direct() == EMMessage.Direct.RECEIVE ? MESSAGE_TYPE_RECV_RED_PACKET_ACK : MESSAGE_TYPE_SEND_RED_PACKET_ACK;
//                }
                //end of red packet code
            }
            return 0;
        }

        @Override
        public EaseChatRow getCustomChatRow(EMMessage message, int position, BaseAdapter adapter) {
            if (message.getType() == EMMessage.Type.TXT) {
                if (IMHelper.getInstance().isPrivilegeOfSecurities(message)) {
                    return new ChatRowPrivilegeOfSecurities(getActivity(), message, position, adapter);
                } else if (IMHelper.getInstance().isAuctionBidding(message)) {
                    return new ChatRowAuctionBidding(getActivity(), message, position, adapter);
                }

//                // voice call or video call
//                if (message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VOICE_CALL, false) ||
//                        message.getBooleanAttribute(Constant.MESSAGE_ATTR_IS_VIDEO_CALL, false)){
//                    return new ChatRowVoiceCall(getActivity(), message, position, adapter);
//                }
//                //red packet code : 红包消息和红包回执消息的chat row
//                else if (message.getBooleanAttribute(RedPacketConstant.MESSAGE_ATTR_IS_RED_PACKET_MESSAGE, false)) {//发送红包消息
//                    return new ChatRowRedPacket(getActivity(), message, position, adapter);
//                } else if (message.getBooleanAttribute(RedPacketConstant.MESSAGE_ATTR_IS_RED_PACKET_ACK_MESSAGE, false)) {//open redpacket message
//                    return new ChatRowRedPacketAck(getActivity(), message, position, adapter);
//                }
                //end of red packet code
            }
            return null;
        }

    }

}

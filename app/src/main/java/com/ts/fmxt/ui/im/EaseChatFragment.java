package com.ts.fmxt.ui.im;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v4.widget.SwipeRefreshLayout.OnRefreshListener;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.EMChatRoomChangeListener;
import com.hyphenate.EMMessageListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMGroup;
import com.hyphenate.chat.EMImageMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.ChatType;
import com.hyphenate.chat.EMTextMessageBody;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.PathUtil;
import com.squareup.okhttp.Request;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.im.base.EaseBaseFragment;
import com.ts.fmxt.ui.im.domain.AuctionBiddingEntity;
import com.ts.fmxt.ui.im.domain.EaseEmojicon;
import com.ts.fmxt.ui.im.domain.EaseUser;
import com.ts.fmxt.ui.im.model.EaseAtMessageHelper;
import com.ts.fmxt.ui.im.utils.EaseCommonUtils;
import com.ts.fmxt.ui.im.utils.EaseUserUtils;
import com.ts.fmxt.ui.im.widget.EaseAlertDialog;
import com.ts.fmxt.ui.im.widget.EaseChatExtendMenu;
import com.ts.fmxt.ui.im.widget.EaseChatInputMenu;
import com.ts.fmxt.ui.im.widget.EaseChatMessageList;
import com.ts.fmxt.ui.im.widget.EaseVoiceRecorderView;
import com.ts.fmxt.ui.im.widget.chatrow.EaseCustomChatRowProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import http.data.ChatRoomInfoEntity;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.ReceiverUtils;
import utils.StringUtils;
import utils.helper.ToastHelper;
import utils.sharePreferences.FMWession;
import widget.image.CircleImageView;
import widget.image.FMNetImageView;

import static android.content.Context.MODE_PRIVATE;


/**
 * you can new an EaseChatFragment to use or you can inherit it to expand.
 * You need call setArguments to pass chatType and userId
 * <br/>
 * <br/>
 * you can see ChatActivity in demo for your reference
 */
public class EaseChatFragment extends EaseBaseFragment implements EMMessageListener, ReceiverUtils.MessageReceiver {
    protected static final String TAG = "EaseChatFragment";
    protected static final int REQUEST_CODE_MAP = 1;
    protected static final int REQUEST_CODE_CAMERA = 2;
    protected static final int REQUEST_CODE_LOCAL = 3;

    /**
     * params to fragment
     */
    protected Bundle fragmentArgs;
    protected int chatType;
    protected String toChatUsername;
    public EaseChatMessageList messageList;
    protected EaseChatInputMenu inputMenu;

    protected EMConversation conversation;

    protected InputMethodManager inputManager;
    protected ClipboardManager clipboard;

    protected Handler handler = new Handler();
    protected File cameraFile;
    protected EaseVoiceRecorderView voiceRecorderView;
    protected SwipeRefreshLayout swipeRefreshLayout;
    protected ListView listView;

    protected boolean isloading;
    protected boolean haveMoreData = true;
    protected int pagesize = 20;
    protected GroupListener groupListener;
    protected EMMessage contextMenuMessage;

    static final int ITEM_GIF = 1;
    static final int ITEM_TAKE_PICTURE = 2;
    static final int ITEM_PICTURE = 3;
    static final int ITEM_LOCATION = 4;


    protected int[] itemStrings = { R.string.attach_picture,R.string.attach_take_pic};
    protected int[] itemdrawables = {R.drawable.ease_chat_image_selector, R.drawable.ease_chat_takepic_selector
    };
    protected int[] itemIds = {ITEM_PICTURE, ITEM_TAKE_PICTURE, ITEM_LOCATION};
    private EMChatRoomChangeListener chatRoomChangeListener;
    private boolean isMessageListInited;
    protected MyItemClickListener extendMenuItemClickListener;
    protected String FMUserId;
    private FMNetImageView mGif,iv_auction_picture;
    private RelativeLayout rl_gif_animation;
    private TextView tv_send_gif_num;
    private TextView tv_quit,tv_count_down,tv_time,tv_follw_num,tv_starting_price;
    private LinearLayout ll_count_down;
    private LinearLayout ll_auction_result,ll_auction_successful;//拍卖结果
    private int time ;
    private long lasttime;
    private ChatRoomInfoEntity mChatRoomInfoEntity;
    private CircleImageView iv_portrait,iv_highest_bid,iv_auction_result_portrait;
    private TextView tv_name,tv_nickname,tv_price,tv_auction_result_name;
    private TextView iv_suction_starting_price,tv_auction_name;//拍卖未开始UI
    private RelativeLayout rl_highest_bid_view;//拍卖开始UI
    private TextView tv_auction_result_project_name,tv_deal_price,tv_auction_failure;
    String price ;
    private Activity mActivity;
    final Handler handlers = new Handler() {

        public void handleMessage(Message msg) {         // handle message
            switch (msg.what) {
                case 1:
                    if(time>0){
                        tv_count_down.setText(time+"");
                        time--;
                        Message msg0 = handlers.obtainMessage(1);
                        handlers.sendMessageDelayed(msg0, 1000);
                    }else{
                        findStockEqitySalesroomInitInfoRequest();
                        ll_count_down.setVisibility(View.GONE);
                    }
                    break;
                case 2:
                    long lasttime = (long) msg.obj;
                    if(lasttime<0){
                        findStockEqitySalesroomInitInfoRequest();
                        return;
                    }
                    if(lasttime<= 10){
                        ll_count_down.setVisibility(View.VISIBLE);
                            time = 10;
                            Message message = handlers.obtainMessage(1);     // Message
                            handlers.sendMessageDelayed(message, 1000);
                    }
                    tv_time.setText("距离开始："+showtime(lasttime));
                    Message msg0 = handlers.obtainMessage(2);
                    msg0.obj = --lasttime;
                    handlers.sendMessageDelayed(msg0, 1000);
                    break;
                case 3:
                    long lasttimes = (long) msg.obj;
                    if(lasttimes<=0){
                        findStockEqitySalesroomInitInfoRequest();
                        return;
                    }
                    tv_time.setText("距离结束："+showtime(lasttimes));
                    Message msgs = handlers.obtainMessage(3);
                    msgs.obj = --lasttimes;
                    handlers.sendMessageDelayed(msgs, 1000);
                    break;

            }
            super.handleMessage(msg);
        }
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ReceiverUtils.addReceiver(this);
        return inflater.inflate(R.layout.ease_fragment_chat, container, false);
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        this.mActivity = context;
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {

        fragmentArgs = getArguments();
        // check if single chat or group chat
        chatType = 3;//fragmentArgs.getInt(EaseConstant.EXTRA_CHAT_TYPE, EaseConstant.CHATTYPE_SINGLE)
        // userId you are chat with or group id
        toChatUsername = fragmentArgs.getString(EaseConstant.EXTRA_USER_ID);
        FMUserId = fragmentArgs.getString("FMUserId");
//        FMWession.getInstance().setIMOtherId(toChatUsername, FMUserId);
        super.onActivityCreated(savedInstanceState);
    }
    @Override
    public void onMessage(int receiverType, Bundle bundle) {
        if (receiverType == ReceiverUtils.IMREFRESH) {
                findStockEqitySalesroomInitInfoRequest();
        }

    }
    /**
     * init view
     */
    protected void initView() {
        // hold to record voice
        //noinspection ConstantConditions
        voiceRecorderView = (EaseVoiceRecorderView) getView().findViewById(R.id.voice_recorder);
        mGif = (FMNetImageView) getView().findViewById(R.id.iv_gif);
        iv_auction_picture= (FMNetImageView) getView().findViewById(R.id.iv_auction_picture);
        rl_gif_animation = (RelativeLayout) getView().findViewById(R.id.rl_gif_animation);
        tv_send_gif_num = (TextView) getView().findViewById(R.id.tv_send_gif_num);
        // message list layout
        messageList = (EaseChatMessageList) getView().findViewById(R.id.message_list);
        if (chatType != EaseConstant.CHATTYPE_SINGLE)
            messageList.setShowUserNick(true);
        listView = messageList.getListView();

        extendMenuItemClickListener = new MyItemClickListener();
        inputMenu = (EaseChatInputMenu) getView().findViewById(R.id.input_menu);
        tv_quit = (TextView) getView().findViewById(R.id.tv_quit);
        tv_auction_result_name = (TextView) getView().findViewById(R.id.tv_auction_result_name);
        tv_starting_price = (TextView) getView().findViewById(R.id.tv_starting_price);
        ll_count_down = (LinearLayout) getView().findViewById(R.id.ll_count_down);
        tv_count_down = (TextView) getView().findViewById(R.id.tv_count_down);
        tv_time = (TextView) getView().findViewById(R.id.tv_time);
        tv_follw_num = (TextView) getView().findViewById(R.id.tv_follw_num);
        iv_portrait = (CircleImageView) getView().findViewById(R.id.iv_portrait);
        iv_highest_bid = (CircleImageView) getView().findViewById(R.id.iv_highest_bid);
        iv_auction_result_portrait = (CircleImageView) getView().findViewById(R.id.iv_auction_result_portrait);
        tv_name = (TextView) getView().findViewById(R.id.tv_name);
        tv_nickname= (TextView) getView().findViewById(R.id.tv_nickname);
        tv_price = (TextView) getView().findViewById(R.id.tv_price);
        tv_deal_price = (TextView) getView().findViewById(R.id.tv_deal_price);
        iv_suction_starting_price = (TextView) getView().findViewById(R.id.iv_suction_starting_price);
        tv_auction_name = (TextView) getView().findViewById(R.id.tv_auction_name);
        tv_auction_failure= (TextView) getView().findViewById(R.id.tv_auction_failure);
        rl_highest_bid_view = (RelativeLayout) getView().findViewById(R.id.rl_highest_bid_view);
        ll_auction_result = (LinearLayout) getView().findViewById(R.id.ll_auction_result);
        ll_auction_successful = (LinearLayout) getView().findViewById(R.id.ll_auction_successful);
        tv_auction_result_project_name = (TextView) getView().findViewById(R.id.tv_auction_result_project_name);

//        registerExtendMenuItem();
        // init input menu
        inputMenu.init(null);
        inputMenu.setChatInputMenuListener(new EaseChatInputMenu.ChatInputMenuListener() {

            @Override
            public void onSendMessage(String content) {
                sendTextMessage(content);
            }
            @Override
            public void onSendPriceMessage(String content) {
                sendPriceMessage(content);
            }

            @Override
            public boolean onPressToSpeakBtnTouch(View v, MotionEvent event) {
                return voiceRecorderView.onPressToSpeakBtnTouch(v, event, new EaseVoiceRecorderView.EaseVoiceRecorderCallback() {

                    @Override
                    public void onVoiceRecordComplete(String voiceFilePath, int voiceTimeLength) {
                        sendVoiceMessage(voiceFilePath, voiceTimeLength);
                    }
                });
            }

            @Override
            public void onBigExpressionClicked(EaseEmojicon emojicon) {
                if(emojicon.getType()== EaseEmojicon.Type.FM_EXPRESSION){//自定义表情
                    sendFMMessage(emojicon.getSendGifUrl(),emojicon.getIdentityCode());
                }else {
                    sendBigExpressionMessage(emojicon.getName(), emojicon.getIdentityCode());
                }
            }
        });
        swipeRefreshLayout = messageList.getSwipeRefreshLayout();
        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);

        inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        clipboard = (ClipboardManager) getActivity().getSystemService(Context.CLIPBOARD_SERVICE);
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        tv_quit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().finish();
                EMClient.getInstance().chatroomManager().leaveChatRoom(toChatUsername);
            }
        });


        findStockEqitySalesroomInitInfoRequest();
    }

    private void findStockEqitySalesroomInitInfoRequest(){
        SharedPreferences sharedPreferences= mActivity.getSharedPreferences("ImInfo",
               MODE_PRIVATE);
        int stockId=sharedPreferences.getInt("stockId", 0);
        Map<String, String> staff = new HashMap<String, String>();
            staff.put("stockId", String.valueOf(stockId));

        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.FINDSTOCKEQITYSALESROOMINITINFO,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String result) {
                        try {
                            JSONObject js = new JSONObject(result);
                            if (!js.isNull("statsMsg")) {
                                JSONObject json = js.optJSONObject("statsMsg");
                                String stats = json.getString("stats");
                                String msg = json.getString("msg");
                                if(stats.equals("1")){
                                    if (!js.isNull("equitySalesroom")) {
                                        JSONObject jsonobj = js.optJSONObject("equitySalesroom");
                                        mChatRoomInfoEntity = new ChatRoomInfoEntity(jsonobj);
                                        initData();
                                    }
//                            ToastHelper.toastMessage(getContext(), msg);
                                }else{
                                    ToastHelper.toastMessage(getActivity(),msg);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, staff
        );
    }

    private void initData()  {
        if (mChatRoomInfoEntity == null) {
            return;
        }
        time = Integer.parseInt(String.valueOf(mChatRoomInfoEntity.getStockStartTime()));
        lasttime = mChatRoomInfoEntity.getStockStartTime();
        if (mChatRoomInfoEntity.getUserHeadpic() != null) {
            iv_portrait.loadImage(mChatRoomInfoEntity.getUserHeadpic());

        }
        tv_name.setText(mChatRoomInfoEntity.getUserNickname());
        if (mChatRoomInfoEntity.getHeadpic() != null) {
            iv_highest_bid.loadImage(mChatRoomInfoEntity.getHeadpic());
            iv_auction_result_portrait.loadImage(mChatRoomInfoEntity.getHeadpic());
        }
        tv_auction_result_name.setText(mChatRoomInfoEntity.getNickname());
        tv_nickname.setText(mChatRoomInfoEntity.getNickname());
        tv_price.setText(mChatRoomInfoEntity.getOfferPrice() + "元");
        try {
            EMCursorResult<EMChatRoom> result = EMClient.getInstance().chatroomManager().fetchPublicChatRoomsFromServer(100, null);
            int attentionNumber = result.getData().size();
            tv_follw_num.setText(attentionNumber+"人关注");
        } catch (HyphenateException e) {
            e.printStackTrace();
        }
        SharedPreferences sharedPreferences= mActivity.getSharedPreferences("ImInfo",
                MODE_PRIVATE);
        int startingPrice=sharedPreferences.getInt("startingPrice", 0);
        String picture = sharedPreferences.getString("picture", "");
        String title = sharedPreferences.getString("title", "");
        tv_starting_price.setText("¥"+startingPrice);
        iv_auction_picture.loadImage(picture);
        tv_auction_result_project_name.setText(title);
        tv_deal_price.setText("¥"+mChatRoomInfoEntity.getOfferPrice() );
        if (mChatRoomInfoEntity.getStockEquityState() == 0) {
            ll_auction_result.setVisibility(View.GONE);
            //拍卖未开始
            rl_highest_bid_view.setVisibility(View.GONE);
            iv_suction_starting_price.setText("起拍价"+startingPrice+"元");
            iv_suction_starting_price.setVisibility(View.VISIBLE);
            tv_auction_name.setVisibility(View.VISIBLE);
            if(lasttime<=360){
                Message msg = handlers.obtainMessage(2);
                msg.obj = lasttime;
                handlers.sendMessage(msg);
            }

        } else if (mChatRoomInfoEntity.getStockEquityState() == 1) {
            //拍卖进行中
            if(mChatRoomInfoEntity.getOfferPrice()==0){
                iv_suction_starting_price.setText("起拍价"+startingPrice+"元");
                iv_suction_starting_price.setVisibility(View.VISIBLE);
                tv_auction_name.setVisibility(View.VISIBLE);
                rl_highest_bid_view.setVisibility(View.GONE);
            }else{
                iv_suction_starting_price.setVisibility(View.GONE);
                tv_auction_name.setVisibility(View.GONE);
                rl_highest_bid_view.setVisibility(View.VISIBLE);
            }
            Message msg = handlers.obtainMessage(3);
            msg.obj = lasttime;
            handlers.sendMessage(msg);
            ll_count_down.setVisibility(View.GONE);
        } else if (mChatRoomInfoEntity.getStockEquityState() == 2) {
            //拍卖已结束
            tv_time.setVisibility(View.GONE);
            ll_count_down.setVisibility(View.GONE);
            ll_auction_result.setVisibility(View.VISIBLE);
            if(mChatRoomInfoEntity.getOfferPrice()==0){
                ll_auction_successful.setVisibility(View.GONE);
                tv_auction_failure.setVisibility(View.VISIBLE);
            }else{
                ll_auction_successful.setVisibility(View.VISIBLE);
                tv_auction_failure.setVisibility(View.GONE);
            }

        }
//        Bundle bundle = new Bundle();
//        bundle.putInt("type", mChatRoomInfoEntity.getStockEquityState());
//        ReceiverUtils.sendReceiver(ReceiverUtils.AUCTION_STATUS,bundle);
        SharedPreferences shares = mActivity.getSharedPreferences("ImInfo",MODE_PRIVATE);
        SharedPreferences.Editor editors = shares.edit(); //使处于可编辑状态
        editors.putInt("type",  mChatRoomInfoEntity.getStockEquityState());
        editors.putInt("offerPrice",mChatRoomInfoEntity.getOfferPrice());
        editors.commit();    //提交数据保存
    }
    public String showtime(long lasttime) {
        int day = (int) (lasttime / (3600 * 24));
        lasttime = (lasttime % (3600 * 24));
        int hour = (int) (lasttime / (3600));
        lasttime = (lasttime % (3600));
        int min = (int) (lasttime / (60));
        int second = (int) (lasttime % (60));
//      if(hour>0){
//            String text = (hour + "时"+ min + "分" + second + "秒");
//            return text;
//        }else{
            String text = ( min + "分" + second + "秒");
            return text;
//        }
        //day + "天" +  +

    }


    protected void setUpView() {

        if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            onConversationInit();
            onMessageListInit();
            onChatRoomViewCreation();
        }

        titleBar.setTitle("");//FMWession.getInstance().getIMOtherUserName()
        titleBar.setLeftLayoutClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        titleBar.setRightLayoutClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (chatType == EaseConstant.CHATTYPE_SINGLE) {
                    emptyHistory();
                } else {
                    toGroupDetails();
                }
            }
        });


        setRefreshLayoutListener();


    }

    /**
     * register extend menu, item id need > 3 if you override this method and keep exist item
     */
    protected void registerExtendMenuItem() {
//        for (int i = 0; i < itemStrings.length; i++) {
//            inputMenu.registerExtendMenuItem(itemStrings[i], itemdrawables[i], itemIds[i], extendMenuItemClickListener);
//        }
//        inputMenu.registerExtendMenuItem("特权卷",R.drawable.ic_launcher,ITEM_PRIVILEGE_OF_SECURITIES,extendMenuItemClickListener);

    }


    protected void onConversationInit() {
        conversation = EMClient.getInstance().chatManager().getConversation(toChatUsername, EaseCommonUtils.getConversationType(chatType), true);
        conversation.markAllMessagesAsRead();
        // the number of messages loaded into conversation is getChatOptions().getNumberOfMessagesLoaded
        // you can change this number
        final List<EMMessage> msgs = conversation.getAllMessages();
        int msgCount = msgs != null ? msgs.size() : 0;
        if (msgCount < conversation.getAllMsgCount() && msgCount < pagesize) {
            String msgId = null;
            if (msgs != null && msgs.size() > 0) {
                msgId = msgs.get(0).getMsgId();
            }
            conversation.loadMoreMsgFromDB(msgId, pagesize - msgCount);
        }

    }

    protected void onMessageListInit() {
        messageList.init(toChatUsername, chatType, chatFragmentHelper != null ?
                chatFragmentHelper.onSetCustomChatRowProvider() : null);
        setListItemClickListener();

        messageList.getListView().setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                hideKeyboard();
                inputMenu.hideExtendMenuContainer();
                return false;
            }
        });

        isMessageListInited = true;
    }

    protected void setListItemClickListener() {
        messageList.setItemClickListener(new EaseChatMessageList.MessageListItemClickListener() {

            @Override
            public void onUserAvatarClick(String username) {
                if (chatFragmentHelper != null) {
                    chatFragmentHelper.onAvatarClick(username);
                }
            }

            @Override
            public void onUserAvatarLongClick(String username) {
                if (chatFragmentHelper != null) {
                    chatFragmentHelper.onAvatarLongClick(username);
                }
            }

            @Override
            public void onResendClick(final EMMessage message) {
                new EaseAlertDialog(getActivity(), R.string.resend, R.string.confirm_resend, null, new EaseAlertDialog.AlertDialogUser() {
                    @Override
                    public void onResult(boolean confirmed, Bundle bundle) {
                        if (!confirmed) {
                            return;
                        }
                        resendMessage(message);
                    }
                }, true).show();
            }

            @Override
            public void onBubbleLongClick(EMMessage message) {
                contextMenuMessage = message;
                if (chatFragmentHelper != null) {
                    chatFragmentHelper.onMessageBubbleLongClick(message);
                }
            }

            @Override
            public boolean onBubbleClick(EMMessage message) {
                if (chatFragmentHelper == null) {
                    return false;
                }
                return chatFragmentHelper.onMessageBubbleClick(message);
            }

        });
    }

    protected void setRefreshLayoutListener() {
        swipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {

            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (listView.getFirstVisiblePosition() == 0 && !isloading && haveMoreData) {
                            List<EMMessage> messages;
                            try {
                                if (chatType == EaseConstant.CHATTYPE_SINGLE) {
                                    messages = conversation.loadMoreMsgFromDB(messageList.getItem(0).getMsgId(),
                                            pagesize);
                                } else {
                                    messages = conversation.loadMoreMsgFromDB(messageList.getItem(0).getMsgId(),
                                            pagesize);
                                }
                            } catch (Exception e1) {
                                swipeRefreshLayout.setRefreshing(false);
                                return;
                            }
                            if (messages.size() > 0) {
                                messageList.refreshSeekTo(messages.size() - 1);
                                if (messages.size() != pagesize) {
                                    haveMoreData = false;
                                }
                            } else {
                                haveMoreData = false;
                            }

                            isloading = false;

                        } else {
                            Toast.makeText(getActivity(), getResources().getString(R.string.no_more_messages),
                                    Toast.LENGTH_SHORT).show();
                        }
                        swipeRefreshLayout.setRefreshing(false);
                    }
                }, 600);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_CAMERA) { // capture new image
                if (cameraFile != null && cameraFile.exists())
                    sendImageMessage(cameraFile.getAbsolutePath());
            } else if (requestCode == REQUEST_CODE_LOCAL) { // send local image
                if (data != null) {
                    Uri selectedImage = data.getData();
                    if (selectedImage != null) {
                        sendPicByUri(selectedImage);
                    }
                }
            } else if (requestCode == REQUEST_CODE_MAP) { // location
                double latitude = data.getDoubleExtra("latitude", 0);
                double longitude = data.getDoubleExtra("longitude", 0);
                String locationAddress = data.getStringExtra("address");
                if (locationAddress != null && !locationAddress.equals("")) {
                    sendLocationMessage(latitude, longitude, locationAddress);
                } else {
                    Toast.makeText(getActivity(), R.string.unable_to_get_loaction, Toast.LENGTH_SHORT).show();
                }

            }
//            else if (requestCode == REQUEST_CODE_SELEST_ITEM_PRIVILEGE_OF_SECURITIES) { // 特权劵回调
//                double latitude = data.getDoubleExtra("latitude", 0);
//                double longitude = data.getDoubleExtra("longitude", 0);
//                String locationAddress = data.getStringExtra("address");
//                if (locationAddress != null && !locationAddress.equals("")) {
//                    sendLocationMessage(latitude, longitude, locationAddress);
//                } else {
//                    Toast.makeText(getActivity(), R.string.unable_to_get_loaction, Toast.LENGTH_SHORT).show();
//                }

//            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isMessageListInited)
            messageList.refresh();
        EaseUI.getInstance().pushActivity(getActivity());
        // register the event listener when enter the foreground
        EMClient.getInstance().chatManager().addMessageListener(this);

        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            EaseAtMessageHelper.get().removeAtMeGroup(toChatUsername);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        // unregister this event listener when this activity enters the
        // background
        EMClient.getInstance().chatManager().removeMessageListener(this);

        // remove activity from foreground activity list
        EaseUI.getInstance().popActivity(getActivity());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (groupListener != null) {
            EMClient.getInstance().groupManager().removeGroupChangeListener(groupListener);
        }

        if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            EMClient.getInstance().chatroomManager().leaveChatRoom(toChatUsername);
        }

        if (chatRoomChangeListener != null) {
            EMClient.getInstance().chatroomManager().removeChatRoomChangeListener(chatRoomChangeListener);
        }

    }

    public void onBackPressed() {
        if (inputMenu.onBackPressed()) {
            getActivity().finish();
            if (chatType == EaseConstant.CHATTYPE_GROUP) {
                EaseAtMessageHelper.get().removeAtMeGroup(toChatUsername);
                EaseAtMessageHelper.get().cleanToAtUserList();
            }
            if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
                EMClient.getInstance().chatroomManager().leaveChatRoom(toChatUsername);
            }
        }
    }

    protected void onChatRoomViewCreation() {
        final ProgressDialog pd = ProgressDialog.show(getActivity(), "", "Joining......");
        EMClient.getInstance().chatroomManager().joinChatRoom(toChatUsername, new EMValueCallBack<EMChatRoom>() {

            @Override
            public void onSuccess(final EMChatRoom value) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (getActivity().isFinishing() || !toChatUsername.equals(value.getId()))
                            return;
                        pd.dismiss();
                        EMChatRoom room = EMClient.getInstance().chatroomManager().getChatRoom(toChatUsername);
                        if (room != null) {
                            SharedPreferences sharedPreferences = mActivity.getSharedPreferences("ImInfo",
                                    MODE_PRIVATE);
                            String title = sharedPreferences.getString("title", "");
                            tv_auction_name.setText("拍品【"+title+"】");

                            titleBar.setTitle(title);
                            titleBar.setLeftLayoutClickListener(new OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    getActivity().finish();
                                    EMClient.getInstance().chatroomManager().leaveChatRoom(toChatUsername);
                                }
                            });
                        } else {
                            titleBar.setTitle(toChatUsername);
                        }
                        addChatRoomChangeListenr();
                        onConversationInit();
                        onMessageListInit();
                    }
                });
            }

            @Override
            public void onError(final int error, String errorMsg) {
                // TODO Auto-generated method stub
                EMLog.d(TAG, "join room failure : " + error);
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        pd.dismiss();
                    }
                });
                getActivity().finish();
            }
        });
    }


    protected void addChatRoomChangeListenr() {
        chatRoomChangeListener = new EMChatRoomChangeListener() {

            @Override
            public void onChatRoomDestroyed(String roomId, String roomName) {
                if (roomId.equals(toChatUsername)) {
                    showChatroomToast(" room : " + roomId + " with room name : " + roomName + " was destroyed");
                    getActivity().finish();

                }
            }

            @Override
            public void onMemberJoined(String roomId, String participant) {
                showChatroomToast("member : " + participant + " join the room : " + roomId);
            }

            @Override
            public void onMemberExited(String roomId, String roomName, String participant) {
                showChatroomToast("member : " + participant + " leave the room : " + roomId + " room name : " + roomName);
            }

            @Override
            public void onMemberKicked(String roomId, String roomName, String participant) {
                if (roomId.equals(toChatUsername)) {
                    String curUser = EMClient.getInstance().getCurrentUser();
                    if (curUser.equals(participant)) {
                        EMClient.getInstance().chatroomManager().leaveChatRoom(toChatUsername);
                        getActivity().finish();
                    } else {
                        showChatroomToast("member : " + participant + " was kicked from the room : " + roomId + " room name : " + roomName);
                    }
                }
            }

        };


        EMClient.getInstance().chatroomManager().addChatRoomChangeListener(chatRoomChangeListener);
    }

    protected void showChatroomToast(final String toastContent) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                Toast.makeText(getActivity(), toastContent, Toast.LENGTH_SHORT).show();
            }
        });
    }

    // implement methods in EMMessageListener
    @Override
    public void onMessageReceived(List<EMMessage> messages) {
        for (EMMessage message : messages) {
            String username = null;
            // group message
            if (message.getChatType() == ChatType.GroupChat || message.getChatType() == ChatType.ChatRoom) {
                username = message.getTo();
            } else {
                // single chat message
                username = message.getFrom();
            }

            // if the message is for current conversation
            if (username.equals(toChatUsername)) {
                messageList.refreshSelectLast();
                EaseUI.getInstance().getNotifier().vibrateAndPlayTone(message);
            } else {
                EaseUI.getInstance().getNotifier().onNewMsg(message);
            }
        }
    }

    @Override
    public void onCmdMessageReceived(List<EMMessage> messages) {

    }

    @Override
    public void onMessageReadAckReceived(List<EMMessage> messages) {
        if (isMessageListInited) {
            messageList.refresh();
        }
    }

    @Override
    public void onMessageDeliveryAckReceived(List<EMMessage> messages) {
        if (isMessageListInited) {
            messageList.refresh();
        }
    }

    @Override
    public void onMessageChanged(EMMessage emMessage, Object change) {
        if (isMessageListInited) {
            messageList.refresh();
        }
    }



    private void showAnimation() {
        Animation scaleAnimation = AnimationUtils.loadAnimation(getActivity(), R.anim.im_send_gif_anim);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mGif.setImageBitmap(null);
                tv_send_gif_num.setText("");
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        mGif.loadImage(FMWession.getInstance().getSendGifAnimUri());
        tv_send_gif_num.setText(String.format("X %s",FMWession.getInstance().getSendGifNum()));
        rl_gif_animation.startAnimation(scaleAnimation);//开始动画

    }

    private class AsyncMainTask extends AsyncTask<String, Object, Integer> {


        @Override
        protected Integer doInBackground(String... params) {
            SystemClock.sleep(800);
            return null;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            String otherAvatarUri = String.format("im_avatar_%s", toChatUsername);
            String uri = FMWession.getInstance().getIM(otherAvatarUri);
            messageList.refresh("http://www.imgeek.org/uploads/article/20160713/ebfe4e94f2e844766672b447f0794899.png");
        }
    }

    /**
     * handle the click event for extend menu
     */
    class MyItemClickListener implements EaseChatExtendMenu.EaseChatExtendMenuItemClickListener {

        @Override
        public void onClick(int itemId, View view) {
            if (chatFragmentHelper != null) {
                if (chatFragmentHelper.onExtendMenuItemClick(itemId, view)) {
                    return;
                }
            }
            switch (itemId) {
                case ITEM_TAKE_PICTURE:
                    selectPicFromCamera();
                    break;
                case ITEM_PICTURE:
                    selectPicFromLocal();
                    break;
                case ITEM_LOCATION:
                    // startActivityForResult(new Intent(getActivity(), EaseBaiduMapActivity.class), REQUEST_CODE_MAP);
                    break;

                default:
                    break;
            }
        }

    }

    /**
     * input @
     *      昵称
     * @param username
     */
    protected void inputAtUsername(String username, boolean autoAddAtSymbol) {
        if (EMClient.getInstance().getCurrentUser().equals(username) ||
                chatType != EaseConstant.CHATTYPE_GROUP) {
            return;
        }
        EaseAtMessageHelper.get().addAtUser(username);
        EaseUser user = EaseUserUtils.getUserInfo(username);
        if (user != null) {
            username = user.getNick();
        }
        if (autoAddAtSymbol)
            inputMenu.insertText("@" + username + " ");
        else
            inputMenu.insertText(username + " ");
    }


    /**
     * input @
     *      头像
     * @param username
     */
    protected void inputAtUsername(String username) {
        inputAtUsername(username, true);
    }


    //send message
    protected void sendTextMessage(String content) {
        if (EaseAtMessageHelper.get().containsAtUsername(content)) {
            sendAtMessage(content);
        } else {
            EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
            sendMessage(message);
        }
    }

    /**
     * send @ message, only support group chat message
     *
     * @param content
     */
    @SuppressWarnings("ConstantConditions")
    private void sendAtMessage(String content) {
        if (chatType != EaseConstant.CHATTYPE_GROUP) {
            EMLog.e(TAG, "only support group chat message");
            return;
        }
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
        if (EMClient.getInstance().getCurrentUser().equals(group.getOwner()) && EaseAtMessageHelper.get().containsAtAll(content)) {
            message.setAttribute(EaseConstant.MESSAGE_ATTR_AT_MSG, EaseConstant.MESSAGE_ATTR_VALUE_AT_MSG_ALL);
        } else {
            message.setAttribute(EaseConstant.MESSAGE_ATTR_AT_MSG,
                    EaseAtMessageHelper.get().atListToJsonArray(EaseAtMessageHelper.get().getAtMessageUsernames(content)));
        }
        sendMessage(message);

    }


    protected void sendBigExpressionMessage(String name, String identityCode) {
      // EMMessage message = EaseCommonUtils.createExpressionMessage(toChatUsername, name, identityCode);
        EMMessage message = EaseCommonUtils.createTuzkiMessage(toChatUsername, name, identityCode);
        sendMessage(message);
    }
    protected void sendFMMessage(String url, String identityCode) {
        EMMessage message = EaseCommonUtils.createFMMessage(toChatUsername, url, identityCode);
        sendMessage(message);
    }

    protected void sendVoiceMessage(String filePath, int length) {
        EMMessage message = EMMessage.createVoiceSendMessage(filePath, length, toChatUsername);
        sendMessage(message);
    }

    protected void sendImageMessage(String imagePath) {
        EMMessage message = EMMessage.createImageSendMessage(imagePath, false, toChatUsername);
        sendMessage(message);
    }

    protected void sendLocationMessage(double latitude, double longitude, String locationAddress) {
        EMMessage message = EMMessage.createLocationSendMessage(latitude, longitude, locationAddress, toChatUsername);
        sendMessage(message);
    }

    protected void sendVideoMessage(String videoPath, String thumbPath, int videoLength) {
        EMMessage message = EMMessage.createVideoSendMessage(videoPath, thumbPath, videoLength, toChatUsername);
        sendMessage(message);
    }

    protected void sendFileMessage(String filePath) {
        EMMessage message = EMMessage.createFileSendMessage(filePath, toChatUsername);
        sendMessage(message);
    }
    /**
     * 出价的消息
     *
     * @param content
     */
    protected void sendPriceMessage(String content) {
        EMMessage message = EMMessage.createTxtSendMessage(content, toChatUsername);
        message.setAttribute(AuctionBiddingEntity.auction_addPrice, content);
        message.setAttribute(AuctionBiddingEntity.auction_MsgType, "1");
        sendMessage(message);
    }

    protected void sendMessage(EMMessage message) {
        if (message == null) {
            return;
        }
        if (chatFragmentHelper != null) {
            //set extension
            chatFragmentHelper.onSetMessageAttributes(message);
        }
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            message.setChatType(ChatType.GroupChat);
        } else if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            message.setChatType(ChatType.ChatRoom);
        }
        //send message
        EMClient.getInstance().chatManager().sendMessage(message);
        //refresh ui
        if (isMessageListInited) {
            messageList.refreshSelectLast();
        }
    }


    public void resendMessage(EMMessage message) {
        message.setStatus(EMMessage.Status.CREATE);
        EMClient.getInstance().chatManager().sendMessage(message);
        messageList.refresh();
    }

    //===================================================================================


    /**
     * send image
     *
     * @param selectedImage
     */
    protected void sendPicByUri(Uri selectedImage) {
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = getActivity().getContentResolver().query(selectedImage, filePathColumn, null, null, null);
        if (cursor != null) {
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();
            cursor = null;

            if (picturePath == null || picturePath.equals("null")) {
                Toast toast = Toast.makeText(getActivity(), R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;
            }
            sendImageMessage(picturePath);
        } else {
            File file = new File(selectedImage.getPath());
            if (!file.exists()) {
                Toast toast = Toast.makeText(getActivity(), R.string.cant_find_pictures, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                return;

            }
            sendImageMessage(file.getAbsolutePath());
        }

    }

    /**
     * send file
     *
     * @param uri
     */
    protected void sendFileByUri(Uri uri) {
        String filePath = null;
        if ("content".equalsIgnoreCase(uri.getScheme())) {
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = null;

            try {
                cursor = getActivity().getContentResolver().query(uri, filePathColumn, null, null, null);
                int column_index = cursor.getColumnIndexOrThrow("_data");
                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(column_index);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (cursor != null) {
                    cursor.close();
                }
            }
        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
            filePath = uri.getPath();
        }
        if (filePath == null) {
            return;
        }
        File file = new File(filePath);
        if (!file.exists()) {
            Toast.makeText(getActivity(), R.string.File_does_not_exist, Toast.LENGTH_SHORT).show();
            return;
        }
        //limit the size < 10M
        if (file.length() > 10 * 1024 * 1024) {
            Toast.makeText(getActivity(), R.string.The_file_is_not_greater_than_10_m, Toast.LENGTH_SHORT).show();
            return;
        }
        sendFileMessage(filePath);
    }

    /**
     * capture new image
     */
    protected void selectPicFromCamera() {
        if (!EaseCommonUtils.isSdcardExist()) {
            Toast.makeText(getActivity(), R.string.sd_card_does_not_exist, Toast.LENGTH_SHORT).show();
            return;
        }

        cameraFile = new File(PathUtil.getInstance().getImagePath(), EMClient.getInstance().getCurrentUser()
                + System.currentTimeMillis() + ".jpg");
        //noinspection ResultOfMethodCallIgnored
        cameraFile.getParentFile().mkdirs();
        startActivityForResult(
                new Intent(MediaStore.ACTION_IMAGE_CAPTURE).putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(cameraFile)),
                REQUEST_CODE_CAMERA);
    }

    /**
     * select local image
     */
    protected void selectPicFromLocal() {
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");

        } else {
            intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        }
        startActivityForResult(intent, REQUEST_CODE_LOCAL);
    }


    /**
     * clear the conversation history
     */
    protected void emptyHistory() {
        String msg = getResources().getString(R.string.Whether_to_empty_all_chats);
        new EaseAlertDialog(getActivity(), null, msg, null, new EaseAlertDialog.AlertDialogUser() {

            @Override
            public void onResult(boolean confirmed, Bundle bundle) {
                if (confirmed) {
                    EMClient.getInstance().chatManager().deleteConversation(toChatUsername, true);
                    messageList.refresh();
                }
            }
        }, true).show();
    }

    /**
     * open group detail
     */
    protected void toGroupDetails() {
        if (chatType == EaseConstant.CHATTYPE_GROUP) {
            EMGroup group = EMClient.getInstance().groupManager().getGroup(toChatUsername);
            if (group == null) {
                Toast.makeText(getActivity(), R.string.gorup_not_found, Toast.LENGTH_SHORT).show();
                return;
            }
            if (chatFragmentHelper != null) {
                chatFragmentHelper.onEnterToChatDetails();
            }
        } else if (chatType == EaseConstant.CHATTYPE_CHATROOM) {
            if (chatFragmentHelper != null) {
                chatFragmentHelper.onEnterToChatDetails();
            }
        }
    }

    /**
     * hide
     */
    protected void hideKeyboard() {
        if (getActivity().getWindow().getAttributes().softInputMode != WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (getActivity().getCurrentFocus() != null)
                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                        InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * forward message
     *
     * @param forward_msg_id
     */
    protected void forwardMessage(String forward_msg_id) {
        final EMMessage forward_msg = EMClient.getInstance().chatManager().getMessage(forward_msg_id);
        EMMessage.Type type = forward_msg.getType();
        switch (type) {
            case TXT:
                if (forward_msg.getBooleanAttribute(EaseConstant.MESSAGE_ATTR_IS_BIG_EXPRESSION, false)|| !StringUtils.isEmpty(forward_msg.getStringAttribute("type", ""))) {
                    sendBigExpressionMessage(((EMTextMessageBody) forward_msg.getBody()).getMessage(),
                            forward_msg.getStringAttribute(EaseConstant.MESSAGE_ATTR_EXPRESSION_ID, null));
                } else {
                    // get the content and send it
                    String content = ((EMTextMessageBody) forward_msg.getBody()).getMessage();
                    sendTextMessage(content);
                }
                break;
            case IMAGE:
                // send image
                String filePath = ((EMImageMessageBody) forward_msg.getBody()).getLocalUrl();
                if (filePath != null) {
                    File file = new File(filePath);
                    if (!file.exists()) {
                        // send thumb nail if original image does not exist
                        filePath = ((EMImageMessageBody) forward_msg.getBody()).thumbnailLocalPath();
                    }
                    sendImageMessage(filePath);
                }
                break;
            default:
                break;
        }

        if (forward_msg.getChatType() == EMMessage.ChatType.ChatRoom) {
            EMClient.getInstance().chatroomManager().leaveChatRoom(forward_msg.getTo());
        }
    }

    /**
     * listen the group event
     */
    class GroupListener extends EaseGroupRemoveListener {

        @Override
        public void onUserRemoved(final String groupId, String groupName) {
            getActivity().runOnUiThread(new Runnable() {

                public void run() {
                    if (toChatUsername.equals(groupId)) {
                        Toast.makeText(getActivity(), R.string.you_are_group, Toast.LENGTH_LONG).show();
                        Activity activity = getActivity();
                        if (activity != null && !activity.isFinishing()) {
                            activity.finish();
                        }
                    }
                }
            });
        }

        @Override
        public void onGroupDestroyed(final String groupId, String groupName) {
            // prompt group is dismissed and finish this activity
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    if (toChatUsername.equals(groupId)) {
                        Toast.makeText(getActivity(), R.string.the_current_group, Toast.LENGTH_LONG).show();
                        Activity activity = getActivity();
                        if (activity != null && !activity.isFinishing()) {
                            activity.finish();
                        }
                    }
                }
            });
        }

    }


    protected EaseChatFragmentHelper chatFragmentHelper;

    public void setChatFragmentListener(EaseChatFragmentHelper chatFragmentHelper) {
        this.chatFragmentHelper = chatFragmentHelper;
    }

    public interface EaseChatFragmentHelper {
        /**
         * set message attribute
         */
        void onSetMessageAttributes(EMMessage message);

        /**
         * enter to chat detail
         */
        void onEnterToChatDetails();

        /**
         * on avatar clicked
         *
         * @param username
         */
        void onAvatarClick(String username);

        /**
         * on avatar long pressed
         *
         * @param username
         */
        void onAvatarLongClick(String username);

        /**
         * on message bubble clicked
         */
        boolean onMessageBubbleClick(EMMessage message);

        /**
         * on message bubble long pressed
         */
        void onMessageBubbleLongClick(EMMessage message);

        /**
         * on extend menu item clicked, return true if you want to override
         *
         * @param view
         * @param itemId
         * @return
         */
        boolean onExtendMenuItemClick(int itemId, View view);

        /**
         * on set custom chat row provider
         *
         * @return
         */
        EaseCustomChatRowProvider onSetCustomChatRowProvider();
    }

}

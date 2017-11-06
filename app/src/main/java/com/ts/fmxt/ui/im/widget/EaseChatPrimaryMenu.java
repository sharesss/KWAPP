package com.ts.fmxt.ui.im.widget;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.okhttp.Request;
import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;
import com.ts.fmxt.FmxtApplication;
import com.ts.fmxt.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import http.data.WeiXinPayEntity;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.ReceiverUtils;
import utils.Tools;
import utils.helper.ToastHelper;
import widget.weixinpay.playUitls;

import static android.content.Context.MODE_PRIVATE;


/**
 * primary menu
 *
 */
public class EaseChatPrimaryMenu extends EaseChatPrimaryMenuBase implements OnClickListener , ReceiverUtils.MessageReceiver {
    private int investId;//股票ID
    private EditText editText;
    private View buttonSetModeKeyboard;
    private RelativeLayout edittext_layout;
    private View buttonSetModeVoice;
    private View buttonSend;
    private View buttonPressToSpeak;
    private ImageView faceNormal;
    private ImageView faceChecked;
    private Button buttonMore;
    private int type;
    private WeiXinPayEntity entity;
    private IWXAPI api;
    int isApply;

    @Override
    public void onMessage(int receiverType, Bundle bundle) {
        if (receiverType == ReceiverUtils.WX_PLAY) {
            isApply = 1;
            if(type==0){
                    buttonMore.setText("等待开拍");
                    editText.clearFocus();
                    editText.setFocusable(false);
            }else if(type==1){
                    buttonMore.setText("出价竞拍");
            }
        }

    }


    public EaseChatPrimaryMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        api = WXAPIFactory.createWXAPI(context, FmxtApplication.APP_ID, true);
        //将应用appid注册到微信
        api.registerApp(FmxtApplication.APP_ID);
        ReceiverUtils.addReceiver(this);
        init(context, attrs);
    }

    public EaseChatPrimaryMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
        ReceiverUtils.addReceiver(this);
        api = WXAPIFactory.createWXAPI(context, FmxtApplication.APP_ID, true);
        //将应用appid注册到微信
        api.registerApp(FmxtApplication.APP_ID);
    }

    public EaseChatPrimaryMenu(Context context) {
        super(context);
        ReceiverUtils.addReceiver(this);
        api = WXAPIFactory.createWXAPI(context, FmxtApplication.APP_ID, true);
        //将应用appid注册到微信
        api.registerApp(FmxtApplication.APP_ID);
        init(context, null);
    }

    private void init(final Context context, AttributeSet attrs) {
        Context context1 = context;

        LayoutInflater.from(context).inflate(R.layout.ease_widget_chat_primary_menu, this);
        editText = (EditText) findViewById(R.id.et_sendmessage);
        buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard);
        edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
        buttonSetModeVoice = findViewById(R.id.btn_set_mode_voice);
        buttonSend = findViewById(R.id.btn_send);
        buttonPressToSpeak = findViewById(R.id.btn_press_to_speak);
        faceNormal = (ImageView) findViewById(R.id.iv_face_normal);
        faceChecked = (ImageView) findViewById(R.id.iv_face_checked);
        RelativeLayout faceLayout = (RelativeLayout) findViewById(R.id.rl_face);
        faceLayout.setVisibility(View.GONE);
        buttonMore = (Button) findViewById(R.id.btn_more);
        edittext_layout.setBackgroundResource(R.mipmap.ease_input_bar_bg_normal);
        SharedPreferences sharedPreferences= getContext().getSharedPreferences("ImInfo",
                MODE_PRIVATE);
        investId=sharedPreferences.getInt("investId", 0);
        isApply=sharedPreferences.getInt("isApply", 0);
        type=sharedPreferences.getInt("type", 0);
            if(type==0){
                if(isApply==0){
                    buttonMore.setText("预交保证金");
                    buttonMore.setBackground(getContext().getResources().getDrawable(R.drawable.bg_orange_5_shape));
                }else{
                    buttonMore.setText("等待开拍");
                    editText.clearFocus();
                    editText.setFocusable(false);
                }
            }else if(type==1){
                if(isApply==0){
                    buttonMore.setText("预交保证金");
                    buttonMore.setBackground(getContext().getResources().getDrawable(R.drawable.bg_orange_5_shape));
                }else{
                    buttonMore.setText("出价竞拍");
                    buttonMore.setBackground(getContext().getResources().getDrawable(R.drawable.bg_orange_5_shape));
                }

            }else if(type==2){
                buttonMore.setText("拍卖结束");
                editText.clearFocus();
                editText.setFocusable(false);
            }
        buttonSend.setOnClickListener(this);
        buttonSetModeKeyboard.setOnClickListener(this);
        buttonSetModeVoice.setOnClickListener(this);
        buttonMore.setOnClickListener(this);
        faceLayout.setOnClickListener(this);
        editText.setOnClickListener(this);
        editText.requestFocus();
        
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
//                    edittext_layout.setBackgroundResource(R.mipmap.ease_input_bar_bg_active);
                } else {
//                    edittext_layout.setBackgroundResource(R.mipmap.ease_input_bar_bg_normal);
                }

            }
        });
        // listen the text change
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    buttonMore.setVisibility(View.GONE);
                    buttonSend.setVisibility(View.VISIBLE);
                } else {
                    buttonMore.setVisibility(View.VISIBLE);
                    buttonSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        buttonPressToSpeak.setOnTouchListener(new View.OnTouchListener() {
            
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(listener != null){
                    return listener.onPressToSpeakBtnTouch(v, event);
                }
                return false;
            }
        });
    }
    
    /**
     * set recorder view when speak icon is touched
     * @param voiceRecorderView
     */
    public void setPressToSpeakRecorderView(EaseVoiceRecorderView voiceRecorderView){
        EaseVoiceRecorderView voiceRecorderView1 = voiceRecorderView;
    }

    /**
     * append emoji icon to editText
     * @param emojiContent
     */
    public void onEmojiconInputEvent(CharSequence emojiContent){
        editText.append(emojiContent);
    }
    
    /**
     * delete emojicon
     */
    public void onEmojiconDeleteEvent(){
        if (!TextUtils.isEmpty(editText.getText())) {
            KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
            editText.dispatchKeyEvent(event);
        }
    }
    
    /**
     * on clicked event
     * @param view
     */
    @Override
    public void onClick(View view){
        int id = view.getId();
        if (id == R.id.btn_send) {
            if(listener != null){
                String s = editText.getText().toString();
                editText.setText("");
                listener.onSendBtnClicked(s);
            }
        } else if (id == R.id.btn_set_mode_voice) {
            setModeVoice();
            showNormalFaceImage();
            if(listener != null)
                listener.onToggleVoiceBtnClicked();
        } else if (id == R.id.btn_set_mode_keyboard) {
            setModeKeyboard();
            showNormalFaceImage();
            if(listener != null)
                listener.onToggleVoiceBtnClicked();
        } else if (id == R.id.btn_more) {
            buttonSetModeKeyboard.setVisibility(View.GONE);
            edittext_layout.setVisibility(View.VISIBLE);
            buttonPressToSpeak.setVisibility(View.GONE);
            showNormalFaceImage();

            SharedPreferences sharedPreferences= getContext().getSharedPreferences("ImInfo",
                    MODE_PRIVATE);
            int offerPrice=sharedPreferences.getInt("offerPrice", 0);
            int startingPrice=sharedPreferences.getInt("startingPrice", 0);
            int priceRisingRate=sharedPreferences.getInt("priceRisingRate", -1);
            if(buttonMore.getText().toString().equals("预交保证金")){
                if (Tools.isFastDoubleClick()) {
                    ToastHelper.toastMessage(getContext(), "请勿重复操作");
                    return;
                }
                boolean sIsWXAppInstalledAndSupported = api.isWXAppInstalled() && api.isWXAppSupportAPI();
                if (!sIsWXAppInstalledAndSupported) {
                    ToastHelper.toastMessage(getContext(),"您还没安装微信");
                    return;
                }
                WechatPay();
                return;
            }
            if(buttonMore.getText().toString().equals("等待开拍")||buttonMore.getText().toString().equals("拍卖结束")){
                return;
            }
            if(buttonMore.getText().toString().equals("出价竞拍")){
                if(offerPrice!=0){
                    startingPrice =offerPrice;
                }
                Price_Dialog price_dialog = new Price_Dialog();
                price_dialog.show((Activity) getContext(), startingPrice, priceRisingRate, new Price_Dialog.PostLinstener() {
                    @Override
                    public void post(int price) {
                        saveStockEquityAuctionBidRequest(price);

                    }
                });
            }

        } else if (id == R.id.et_sendmessage) {
            if(buttonMore.getText().toString().equals("等待开拍")||buttonMore.getText().toString().equals("拍卖结束")){
                return;
            }
            edittext_layout.setBackgroundResource(R.mipmap.ease_input_bar_bg_active);
            faceNormal.setVisibility(View.VISIBLE);
            faceChecked.setVisibility(View.INVISIBLE);

            if(listener != null)
                listener.onEditTextClicked();
        } else if (id == R.id.rl_face) {
//            if(buttonMore.getText().toString().equals("等待开拍")||buttonMore.getText().toString().equals("拍卖结束")){
//                return;
//            }
//            toggleFaceImage();
//            if(listener != null){
//                listener.onToggleEmojiconClicked();
//            }
        } else {
        }
    }
    
    
    /**
     * show voice icon when speak bar is touched
     * 
     */
    protected void setModeVoice() {
        hideKeyboard();
        edittext_layout.setVisibility(View.GONE);
        buttonSetModeVoice.setVisibility(View.GONE);
        buttonSetModeKeyboard.setVisibility(View.VISIBLE);
        buttonSend.setVisibility(View.GONE);
        buttonMore.setVisibility(View.VISIBLE);
        buttonPressToSpeak.setVisibility(View.VISIBLE);
        faceNormal.setVisibility(View.VISIBLE);
        faceChecked.setVisibility(View.INVISIBLE);

    }

    /**
     * show keyboard
     */
    protected void setModeKeyboard() {
        edittext_layout.setVisibility(View.VISIBLE);
        buttonSetModeKeyboard.setVisibility(View.GONE);
        buttonSetModeVoice.setVisibility(View.VISIBLE);
        // mEditTextContent.setVisibility(View.VISIBLE);
        editText.requestFocus();
        // buttonSend.setVisibility(View.VISIBLE);
        buttonPressToSpeak.setVisibility(View.GONE);
        if (TextUtils.isEmpty(editText.getText())) {
            buttonMore.setVisibility(View.VISIBLE);
            buttonSend.setVisibility(View.GONE);
        } else {
            buttonMore.setVisibility(View.GONE);
            buttonSend.setVisibility(View.VISIBLE);
        }

    }
    
    
    protected void toggleFaceImage(){
        if(faceNormal.getVisibility() == View.VISIBLE){
            showSelectedFaceImage();
        }else{
            showNormalFaceImage();
        }
    }
    
    private void showNormalFaceImage(){
        faceNormal.setVisibility(View.VISIBLE);
        faceChecked.setVisibility(View.INVISIBLE);
    }
    
    private void showSelectedFaceImage(){
        faceNormal.setVisibility(View.INVISIBLE);
        faceChecked.setVisibility(View.VISIBLE);
    }
    

    @Override
    public void onExtendMenuContainerHide() {
        showNormalFaceImage();
    }

    @Override
    public void onTextInsert(CharSequence text) {
       int start = editText.getSelectionStart();
       Editable editable = editText.getEditableText();
       editable.insert(start, text);
       setModeKeyboard();
    }

    @Override
    public EditText getEditText() {
        return editText;
    }

    private void saveStockEquityAuctionBidRequest(final int price){
        SharedPreferences sharedPreferences= getContext().getSharedPreferences("ImInfo",
                MODE_PRIVATE);
        int stockId=sharedPreferences.getInt("stockId", 0);
        SharedPreferences sharedPreference= getContext().getSharedPreferences("user",
                MODE_PRIVATE);
        String token=sharedPreference.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("stockId", String.valueOf(stockId));
        staff.put("tokenId", String.valueOf(token));
        staff.put("offerPrice", String.valueOf(price));

        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.SAVESTOCKEQUITYAUCTIONBID,
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
                                    SharedPreferences sharedPreferencesw = getContext().getSharedPreferences("ImInfo",
                                            MODE_PRIVATE);
                                    int userId = sharedPreferencesw.getInt("userId",0);
                                    SharedPreferences sharedPreferences = getContext().getSharedPreferences("ImInfo",
                                            MODE_PRIVATE);
                                    String name = sharedPreferences.getString("name", "");
                                    String headpic = sharedPreferences.getString("headpic", "");
                                    if(listener != null){
//                                        Bundle bundle = new Bundle();
//                                        bundle.putString("type", "1");
//                                        bundle.putString("price", "出价"+price+"元");
//                                        ReceiverUtils.sendReceiver(ReceiverUtils.IMREFRESH,bundle);
                                        SharedPreferences share = getContext().getSharedPreferences("ImInfo",MODE_PRIVATE);
                                        SharedPreferences.Editor editor = share.edit(); //使处于可编辑状态
                                        editor.putInt("startingPrice",price);
                                        editor.commit();    //提交数据保存
//                                        EMMessage message = EMMessage.createTxtSendMessage("出价",null);
//                                        message.setAttribute(AuctionBiddingEntity.auction_MsgType, "1");
//                                        message.setAttribute(AuctionBiddingEntity.auction_addPrice,"出价"+price+"元");
//                                        message.setAttribute(AuctionBiddingEntity.auction_end, "");
//                                        message.setAttribute(AuctionBiddingEntity.auction_userNickName, name);
//                                        message.setAttribute(AuctionBiddingEntity.auction_userHeadPic, headpic);
//                                        message.setAttribute(AuctionBiddingEntity.auction_userID, userId+"");
//                                        EMClient.getInstance().chatManager().sendMessage(message);
                                        listener.onSendPriceMessage("出价"+price+"元");
                                    }
                                }else{
                                    ToastHelper.toastMessage(getContext(),msg);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, staff
        );
    }
    private void WechatPay(){
        SharedPreferences sharedPreferences= getContext().getSharedPreferences("user",
                MODE_PRIVATE);
        String token=sharedPreferences.getString("token", "");
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("investId", String.valueOf(investId));
        staff.put("tokenId", String.valueOf(token));
        staff.put("body", "支付保证金");
        staff.put("totalFee",String.valueOf(1));//2000*100
        staff.put("roleType", "2");
        staff.put("clientType", "2");
        staff.put("orderType", "2");

        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.SAVEUSERINVESTPROJECTFOLLOW,
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
                                if (stats.equals("1")) {
                                    if(!js.isNull("unifiedOrder")){
                                        entity =   new WeiXinPayEntity(js.optJSONObject("unifiedOrder"));
                                    }
                                    playUitls.getInstance().weixinPlay(entity, getContext());

                                } else {
                                    ToastHelper.toastMessage(getContext(), msg);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, staff
        );

    }


}

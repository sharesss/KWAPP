package com.ts.fmxt.ui.im;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.hyphenate.util.EasyUtils;
import com.thindo.base.Utils.AppManager;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.MainFrameActivity;
import com.ts.fmxt.ui.im.base.ChatFragment;
import com.ts.fmxt.ui.im.parse.UserProfileManager;
import com.ts.fmxt.ui.im.runtimepermissions.PermissionsManager;

import utils.ReceiverUtils;

/**
 * IM
 */
public class ChatActivity extends BaseActivity implements ReceiverUtils.MessageReceiver{
    public static ChatActivity activityInstance;
    private EaseChatFragment chatFragment;
    String toChatUsername;
//    private ExpiredEntity ee;

    @Override
    protected void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        AppManager.getInstance().addActStack(this);
        setContentView(R.layout.chat_activity);
        activityInstance = this;
        ReceiverUtils.addReceiver(this);
        UserProfileManager mUserProfileManager=new UserProfileManager();

//        mUserProfileManager.updateCurrentUserNickName(FMWession.getInstance().getUserInfo().getNickName());
//        mUserProfileManager.setCurrentUserAvatar(FMWession.getInstance().getUserInfo().getHeadPic());
        toChatUsername = getIntent().getExtras().getString("userId");
        chatFragment = new ChatFragment();
        chatFragment.setArguments(getIntent().getExtras());
        getSupportFragmentManager().beginTransaction().add(R.id.container, chatFragment).commit();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        activityInstance = null;
        ReceiverUtils.removeReceiver(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        // make sure only one chat activity is opened
        String username = intent.getStringExtra("userId");
        if (toChatUsername.equals(username))
            super.onNewIntent(intent);
        else {
            finish();
            startActivity(intent);
        }

    }

    @Override
    public void onBackPressed() {
        chatFragment.onBackPressed();
        if (EasyUtils.isSingleActivity(this)) {
            Intent intent = new Intent(this, MainFrameActivity.class);
            startActivity(intent);
        }
    }

    public String getToChatUsername() {
        return toChatUsername;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }

        @Override
        public void onMessage(int receiverType, Bundle bundle) {
//            if (receiverType == ReceiverUtils.INDEX_IM) {
//                 finish();
//            }
    }
}

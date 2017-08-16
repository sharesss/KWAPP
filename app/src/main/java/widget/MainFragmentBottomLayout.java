package widget;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.ts.fmxt.R;

import http.data.CallbackInfo;
import utils.UISKipUtils;
import utils.helper.UIHelper;
import widget.viewbadger.BadgeView;


/**
 * @author Randy
 * @Description:描述:主页底部
 * @date 2015年11月16日 下午11:19:45
 */
public class MainFragmentBottomLayout extends RelativeLayout implements OnClickListener {

    private BottomItemOnClick mOnItemClick;

    public MixedTextDrawView mNearby, mAccount, mConsumer;
    private BadgeView mBadgeView;

    public MainFragmentBottomLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initView();
    }

    public MainFragmentBottomLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public MainFragmentBottomLayout(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_bottom_view, this);
        mNearby = (MixedTextDrawView) findViewById(R.id.bt_nearby);
        mAccount = (MixedTextDrawView) findViewById(R.id.bt_account);
        mConsumer = (MixedTextDrawView) findViewById(R.id.bt_consumer);

        mNearby.setOnClickListener(this);
        mAccount.setOnClickListener(this);
        mConsumer.setOnClickListener(this);

        mBadgeView = new BadgeView(getContext(), findViewById(R.id.tv_msg_num));
        mBadgeView.setTextColor(getResources().getColor(R.color.white));
        mBadgeView.setTextSize(10);
        mBadgeView.setBadgeMargin(UIHelper.dipToPx(getContext(), 5));
        mBadgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
    }

    @Override
    public void onClick(View v) {
        CallbackInfo info = new CallbackInfo();
        switch (v.getId()) {
            case R.id.bt_nearby:
                changeStatus();
                info.setPosition(0);
                mNearby.notifyMixedTextDraw(true);
                break;

            case R.id.bt_account:
                SharedPreferences sharedPreferences= getContext().getSharedPreferences("user",
                        getContext().MODE_PRIVATE);
                String token=sharedPreferences.getString("token", "");
                if (token.equals("")) {
                UISKipUtils.startLoginActivity((Activity) getContext());
                    return;
                }
                changeStatus();
                info.setPosition(2);//代言
                //     info.setPosition(1);//F豆
                mAccount.notifyMixedTextDraw(true);
                break;

            case R.id.bt_consumer:
                changeStatus();
                info.setPosition(1);
                mConsumer.notifyMixedTextDraw(true);
                break;
            default:
                break;
        }
        if (mOnItemClick != null) {
            mOnItemClick.onItemClick(info);
        }
    }

    /*恢复状态未选中*/
    private void changeStatus() {
        mNearby.notifyMixedTextDraw(false);
        mAccount.notifyMixedTextDraw(false);
        mConsumer.notifyMixedTextDraw(false);
    }

    public void setOnItemClick(BottomItemOnClick mOnItemClick) {
        this.mOnItemClick = mOnItemClick;
    }

    /*底部按钮事件回调*/
    public interface BottomItemOnClick {
        public void onItemClick(CallbackInfo mCallbackInfo);
    }

    public void currentIndex() {
        changeStatus();
        mConsumer.notifyMixedTextDraw(true);
    }

    //新消息切换背景
    public void setMesBg(boolean isMsg) {
//        if (isMsg) {
//            mAccount.setDrawable(R.drawable.tab_mine_msg);
//            mAccount.setDrawableLight(R.drawable.tab_mine_msg2);
//        } else {
//            mAccount.setDrawable(R.drawable.tab_mine_icon_0ff);
//            mAccount.setDrawableLight(R.drawable.tab_mine_on);
//        }
    }

    public void showMsg(int num) {
        if (num > 0) {
            mBadgeView.setText(String.valueOf(num));
            mBadgeView.show();
        } else {
            mBadgeView.hide();
        }
    }
}

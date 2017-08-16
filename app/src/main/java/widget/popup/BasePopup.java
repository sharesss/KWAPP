package widget.popup;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.ts.fmxt.R;

import http.Logger;


public abstract class BasePopup implements PopupPage {

    protected Logger logger = new Logger(this.getClass().getSimpleName());
    protected View popupView;
    protected PopupWindow mPopupWindow;
    private Activity context;

    public BasePopup(Activity context) {
        this(context, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    public BasePopup(Activity context, int width, int height) {

        this.context = context;

        popupView = setContentView(LayoutInflater.from(context));
        popupView.setFocusableInTouchMode(true);

        mPopupWindow = new PopupWindow(popupView, width, height);
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        mPopupWindow.setBackgroundDrawable(new BitmapDrawable());
        mPopupWindow.setFocusable(true);
        mPopupWindow.setOutsideTouchable(true);
        mPopupWindow.setAnimationStyle(0);
    }

    public abstract Animation loadAnim();

    public abstract View getStartAnimViewGroup();

    @Override
    public View getView() {
        return popupView;
    }

    public Activity getContext() {
        return context;
    }

    public void showPopupWindow() {
        showPopupWindow(R.id.AppWidget);
    }

    public void showPopupWindow(int resource) {
        try {
            Activity context = getContext();
            mPopupWindow.showAtLocation(context.findViewById(resource), Gravity.CENTER
                    | Gravity.CENTER_HORIZONTAL, 0, 0);

            if (loadAnim() != null && getStartAnimViewGroup() != null) {
                getStartAnimViewGroup().startAnimation(loadAnim());
            }
        } catch (Exception e) {
            logger.w(e);
        }
    }

    public void showPopupWindow(View view) {
        try {
            mPopupWindow.showAsDropDown(view);
            if (loadAnim() != null && getStartAnimViewGroup() != null) {
                getStartAnimViewGroup().startAnimation(loadAnim());
            }
        } catch (Exception e) {
            logger.w(e);
        }
    }

    public void showPopupWindos(View v, int gravity, int x, int y) {
        try {
            mPopupWindow.showAtLocation(v, gravity, x, y);
            if (loadAnim() != null && getStartAnimViewGroup() != null) {
                getStartAnimViewGroup().startAnimation(loadAnim());
            }
        } catch (Exception e) {
            logger.w(e);
        }
    }

    /**
     * 设置弹框大小
     */
    public void setPopupLayoutParams(int width, int height) {
        mPopupWindow = new PopupWindow(popupView, width, height);
    }

    /**
     * Popup是否显示状态
     */
    public boolean isShowPopup() {
        if (mPopupWindow.isShowing()) {
            return true;
        }
        return false;
    }

    /**
     * 关闭Popup
     */
    public void dismiss() {
        try {
            mPopupWindow.dismiss();
        } catch (Exception e) {
            logger.w(e);
        }
    }

    /**
     * 设置输入法弹出 上移布局，不影响操作使用
     */
    public void inputMethodUpload() {
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    /**
     * 屏蔽back键
     */
    public void shieldBack() {
        mPopupWindow.setBackgroundDrawable(null);
    }

    /**
     * popup Dismiss接口监听
     */
    public void setOnDismissPopupListener(final OnDismissPoppupListener listener) {
        if (listener != null) {
            mPopupWindow.setOnDismissListener(new OnDismissListener() {

                @Override
                public void onDismiss() {
                    listener.onDismissPopup();
                }
            });
        }
    }

    public interface OnDismissPoppupListener {

        public void onDismissPopup();
    }


    /**
     * 设置点击事件关闭Popup
     */
    protected void setCancelPopupListener(View view) {
        view.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dismiss();
            }
        });
    }

    protected Animation getTranslateAnimation(int durationMillis, int start) {
        return getTranslateAnimation(start, 0, durationMillis);
    }

    /**
     * 生成TranslateAnimation
     *
     * @param durationMillis 动画显示时间
     * @param start          初始位置
     * @return
     */
    protected Animation getTranslateAnimation(int start, int end, int durationMillis) {
        Animation translateAnimation = new TranslateAnimation(0, 0, start, end);
        translateAnimation.setDuration(durationMillis);
        translateAnimation.setFillEnabled(true);
        translateAnimation.setFillAfter(true);
        translateAnimation.setFillBefore(true);
        return translateAnimation;
    }

    /**
     * 生成ScaleAnimation
     *
     * @return
     */
    protected Animation getScaleAnimation() {
        Animation scaleAnimation = new ScaleAnimation(0.7f, 1f, 0.7f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        scaleAnimation.setDuration(200);
        scaleAnimation.setFillEnabled(true);
        scaleAnimation.setFillAfter(true);
        scaleAnimation.setFillBefore(true);
        return scaleAnimation;
    }

    /**
     * 返回String值
     */
    public String getResourcesStr(int strResources) {
        return context.getResources().getString(strResources);
    }

    /**
     * 返回Color值
     */
    public int getResourcesColor(int colorResources) {
        return context.getResources().getColor(colorResources);
    }

    /**
     * 获取控件的文字内容（仅限于TextView及其子类）
     */
    public String getViewStr(TextView textView) {
        return textView.getText().toString().trim();
    }


}

package utils.helper;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.Handler;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import java.util.List;


/**
 * @author meteorshower
 * 
 * 
 */
public class UIHelper {

	/** dip转px */
	public static int dipToPx(Context context, float dip) {
		return (int) (dip * context.getResources().getDisplayMetrics().density + 0.5f);
	}

	/** px转dip */
	public static int pxToDip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/** 将sp值转换为px值 */
	public static int sp2px(Context context, float spValue) {
		final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	/** 获取屏幕分辨率：宽 */
	public static int getScreenPixWidth(Context context) {
		return context.getResources().getDisplayMetrics().widthPixels;
	}

	/** 获取屏幕分辨率：高 */
	public static int getScreenPixHeight(Context context) {
		return context.getResources().getDisplayMetrics().heightPixels;
	}

	/** 隐藏软键盘 */
	public static void hideInputMethod(View view) {
		InputMethodManager imm = (InputMethodManager) view.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
		}
	}

	/** 显示软键盘 */
	public static void showInputMethod(View view) {
		InputMethodManager imm = (InputMethodManager) view.getContext()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		if (imm != null) {
			imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
		}
	}

	/** 显示软键盘 */
	public static void showInputMethod(Context context) {
		InputMethodManager imm = (InputMethodManager) context
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
	}

	/** 多少时间后显示软键盘 */
	public static void showInputMethod(final View view, long delayMillis) {
		// 显示输入法
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				UIHelper.showInputMethod(view);
			}
		}, delayMillis);
	}

	/*
	 * 判断手机是否在锁屏状态 true锁屏 false未锁屏
	 */
	public static boolean isScreenLocked(Context c) {
		KeyguardManager mKeyguardManager = (KeyguardManager) c
				.getSystemService(Context.KEYGUARD_SERVICE);
		boolean bResult = !mKeyguardManager.inKeyguardRestrictedInputMode();

		return bResult;
	}

	// 判断程序在后台运行
	public static boolean isTopActivity(Context c) {
		String packageName = "com.weiju";
		ActivityManager activityManager = (ActivityManager) c
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> tasksInfo = activityManager.getRunningTasks(1);
		// Log.i("aa", tasksInfo.get(0).topActivity.getPackageName());
		if (tasksInfo.size() > 0) {
			// 应用程序位于堆栈的顶层
			if (packageName.equals(tasksInfo.get(0).topActivity
					.getPackageName())) {
				return true;
			}
		}
		return false;
	}
}

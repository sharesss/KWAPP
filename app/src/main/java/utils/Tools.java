package utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;

public class Tools {

	public static void startActivity(Context context, Class<? extends Activity> cls) {
		Intent intent = new Intent(context, cls);
		context.startActivity(intent);
	}

	/**
	 * 检查是否存在SDCard
	 * @return
	 */
	public static boolean hasSdcard(){
		String state = Environment.getExternalStorageState();
		if(state.equals(Environment.MEDIA_MOUNTED)){
			return true;
		}else{
			return false;
		}
	}
	private static long lastClickTime;

	/** 防止按钮双击 */
	public static boolean isFastDoubleClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 500) {
			lastClickTime = time;
			return true;
		}
		lastClickTime = time;
		
		return false;
	}
	/** 防止按钮双击 */
	public static boolean isFastDoubleLongClick() {
		long time = System.currentTimeMillis();
		long timeD = time - lastClickTime;
		if (0 < timeD && timeD < 1000) {
			lastClickTime = time;
			return true;
		}
		lastClickTime = time;
		
		return false;
	}
}

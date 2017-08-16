package utils.helper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ts.fmxt.FmxtApplication;
import com.ts.fmxt.R;

import utils.StringUtils;


public class ToastHelper {
	
	private static Toast mToast;
	
	public static void toastMessage(Context context,int stringId){
		if(context == null)
			context = FmxtApplication.getContext();
		toastMessage(context,context.getResources().getString(stringId));
	}
	
	public static void toastMessage(Context context,String message){
		if(context == null)
			context = FmxtApplication.getContext();
		toastMessage(context,message,0);
	}

	public static void toastMessage(Context context,String message,int iconResourcesId){
		
		if(StringUtils.isEmpty(message)){
			return;
		}
		
		if(mToast != null)
			mToast.cancel();
		
//		if(mToast == null)
			mToast = new Toast(context);
		
		View view = LayoutInflater.from(context).inflate(R.layout.toast_view, null);
		TextView tvMsg = (TextView) view.findViewById(R.id.toast_text);
		ImageView ivIcon = (ImageView) view.findViewById(R.id.toast_image);
		if(iconResourcesId != 0){
			ivIcon.setVisibility(View.VISIBLE);
			ivIcon.setImageResource(iconResourcesId);
		}else{
			ivIcon.setVisibility(View.GONE);
		}
		
		tvMsg.setText(message);
		
		mToast.setView(view);
		mToast.show();
	}
}

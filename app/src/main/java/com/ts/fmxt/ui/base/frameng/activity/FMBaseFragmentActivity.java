package com.ts.fmxt.ui.base.frameng.activity;

import android.os.Bundle;

import com.thindo.base.NetworkAPI.BaseResponse;
import com.thindo.base.NetworkAPI.OnResponseListener;
import com.thindo.base.UI.Activity.BaseFragmentActivity;
import com.ts.fmxt.R;

import utils.StringUtils;
import widget.dialog.FMBProgressDialog;
import widget.titlebar.NavigationView;


/**
 * @author meteorshower
 *
 */
public class FMBaseFragmentActivity extends BaseFragmentActivity implements OnResponseListener {

	protected FMBProgressDialog dialog;
	protected NavigationView navigationView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		dialog = new FMBProgressDialog(this);
	}

	@Override
	public void setContentView(int layoutResID) {
		super.setContentView(layoutResID);
		navigationView = (NavigationView) findViewById(R.id.navigation_view);
	}

	@Override
	public void onStart(BaseResponse response) {
		if(response.isShowProgress()){
			dialog.show();
		}
	}

	@Override
	public void onFailure(BaseResponse response) {
		if(response.isShowProgress()){
			dialog.dismiss();
		}
		String error;
		if(StringUtils.isEmpty(response.getError_msg())){
			error = getResourcesStr(R.string.msg_error_msg);
		}else{
			error = response.getError_msg();
		}
		//ToastHelper.toastMessage(this, error);
	}

	@Override
	public void onSuccess(BaseResponse response) {
		if(response.isShowProgress()){
			dialog.dismiss();
		}
	}

}

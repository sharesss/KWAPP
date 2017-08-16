package com.ts.fmxt.ui.base.frameng;

import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.thindo.base.NetworkAPI.BaseResponse;
import com.thindo.base.NetworkAPI.OnResponseListener;
import com.thindo.base.Widget.refresh.RefreshScrollView;
import com.ts.fmxt.R;

import utils.StringUtils;
import widget.dialog.FMBProgressDialog;


public abstract class FMBaseFragment extends Fragment implements OnResponseListener {

	protected FMBProgressDialog dialog;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		dialog = new FMBProgressDialog(getActivity());
		return super.onCreateView(inflater, container, savedInstanceState);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onStart(BaseResponse response) {
		if (response.isShowProgress()) {
			dialog.show();
		}
	}

	@Override
	public void onFailure(BaseResponse response) {
		try {
			if (response.isShowProgress()) {
				dialog.dismiss();
			}
			String error;
			if (StringUtils.isEmpty(response.getError_msg())) {
				error = getResourcesStr(R.string.msg_error_msg);
			} else {
				error = response.getError_msg();
			}
			//ToastHelper.toastMessage(getActivity(), error);
		} catch (Exception e) {
		}

	}

	public void stop(){
		dialog.dismiss();
	}

	@Override
	public void onSuccess(BaseResponse response) {
		if (response.isShowProgress()) {
			dialog.dismiss();
		}
//		if (response.getStatus() == 5) {//冻结账户
//			FMWession.clear(getActivity());
//			FMWession.getInstance().setToken("");
//			FMWession.getInstance().setLoginInfo("");
//			FMWession.getInstance().setUserLike("");
//			FMWession.getInstance().setUserInfo("");
//			EMClient.getInstance().logout(true);
//			ReceiverUtils.sendReceiver(ReceiverUtils.OUT_LOGIN, null);
//			ShortcutBadger.applyCount(FMBApplication.getContext(), 0);
//			UISkipUtils.showMsgPopup(getActivity(), response.getError_msg());
//			return;
//		}
	}

	/** 获取文本资源 */
	public String getResourcesStr(int resourcesId) {
		String str="";
		try {
			str=getActivity().getResources().getString(resourcesId);
		} catch (Exception e) {
			// TODO: handle exception
		}
		return str;
	}

	/** 获取颜色资源 */
	public int getResourcesColor(int resourcesId) {
		return getActivity().getResources().getColor(resourcesId);
	}

	public void notRefreshFlag(View v, int resourcesId) {
		notRefreshFlag(v, resourcesId, 0);
	}

	/** 屏蔽刷新方法 */
	public void notRefreshFlag(View v, int resourcesId, int layoutId) {
		RefreshScrollView refreshSV = (RefreshScrollView) v
				.findViewById(resourcesId);
		refreshSV.setPullNoRefreshMode();
		if (layoutId != 0) {
			ScrollView sv = refreshSV.getRefreshableView();
			sv.removeAllViews();
			LayoutInflater.from(getActivity()).inflate(layoutId, sv);
		}
	}

	@Override
	public void onResume() {
		super.onResume();
//		MobclickAgent.onPageStart("STBaseFragment"); // 统计页面
	}

	@Override
	public void onPause() {
		super.onPause();
//		MobclickAgent.onPageEnd("STBaseFragment");
	}

	/**
	 * 检测网络是否正常
	 * @return
	 */
	public boolean cheakNetwork(){
		boolean flg=false;
		ConnectivityManager con=(ConnectivityManager)getActivity().getSystemService(getActivity().CONNECTIVITY_SERVICE);
		boolean wifi=con.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnectedOrConnecting();
		boolean internet=con.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).isConnectedOrConnecting();
		if(wifi|internet){
			flg=true;
		}
		return flg;
	}
}

package com.ts.fmxt.ui.base.activity;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ScrollView;

import com.thindo.base.UI.Interfaces.STBaseScroll;
import com.thindo.base.Utils.AppManager;
import com.thindo.base.Widget.refresh.RefreshScrollView;
import com.thindo.base.Widget.refresh.base.RefreshBase;


public abstract class FMBaseScrollActivityV2 extends AppCompatActivity implements STBaseScroll {

	private RefreshScrollView scrollView;
	public ScrollView acutoScrollView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		AppManager.getInstance().addActStack(this);
		//黑色导航栏
		Window window = getWindow();
		ViewGroup decorViewGroup = (ViewGroup) window.getDecorView();
		View statusBarView = new View(window.getContext());
		int statusBarHeight = getStatusBarHeight(window.getContext());
		FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, statusBarHeight);
		params.gravity = Gravity.TOP;
		statusBarView.setLayoutParams(params);
		statusBarView.setBackgroundColor(getResources().getColor(com.thindo.base.R.color.black));
		decorViewGroup.addView(statusBarView);   //黑色背景
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		AppManager.getInstance().removeAct(this);
	}


	private int getStatusBarHeight(Context context) {
		int statusBarHeight = 0;
		Resources res = context.getResources();
		int resourceId = res.getIdentifier("status_bar_height", "dimen", "android");
		if (resourceId > 0) {
			statusBarHeight = res.getDimensionPixelSize(resourceId);
		}
		return statusBarHeight;
	}
	
	public void bindRefreshScrollAdapter(int resourcesId, int layoutId){
		bindRefreshScrollAdapter(resourcesId, layoutId, true);
	}
	
	public void bindRefreshScrollAdapter(int resourcesId, int layoutId, boolean enableLoadMore){

		scrollView = (RefreshScrollView) findViewById(resourcesId);
		if(layoutId != 0){
			acutoScrollView = scrollView.getRefreshableView();
			acutoScrollView.removeAllViews();
			LayoutInflater.from(this).inflate(layoutId, acutoScrollView);
		}
		
		scrollView.setMode(RefreshBase.Mode.PULL_FROM_START);
		scrollView.setOnRefreshListener(new RefreshBase.OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(RefreshBase<ScrollView> refreshView) {
				FMBaseScrollActivityV2.this.onReload();
			}
		});
		
		if(enableLoadMore){
			scrollView.setMode(RefreshBase.Mode.BOTH);
			scrollView.setOnRefreshListener(new RefreshBase.OnRefreshListener2<ScrollView>() {

				@Override
				public void onPullDownToRefresh(RefreshBase<ScrollView> refreshView) {
					FMBaseScrollActivityV2.this.onReload();
				}

				@Override
				public void onPullUpToRefresh(RefreshBase<ScrollView> refreshView) {
					FMBaseScrollActivityV2.this.loadMore();
				}
			});
		}
	}


	
	public abstract void onReload();

	public abstract void loadMore();
	
	@Override
	public RefreshScrollView getRefreshScrollView() {
		return scrollView;
	}

	@Override
	public ScrollView getScrollView() {
		return acutoScrollView;
	}

	@Override
	public void startRefreshState() {
		if(scrollView != null)
			scrollView.setRefreshing(true);
		else if(acutoScrollView != null)
			FMBaseScrollActivityV2.this.onReload();
	}

	@Override
	public void stopRefreshState() {
		if(scrollView != null)
			scrollView.onRefreshComplete();
	}

	@Override
	public void setBackground(int resourcesId) {
		if(scrollView != null)
			scrollView.setBackgroundResource(resourcesId);
		else if(acutoScrollView != null)
			acutoScrollView.setBackgroundResource(resourcesId);
	}

	@Override
	public void notRefreshFlag() {
		scrollView.setPullNoRefreshMode();
 		scrollView.setMode(RefreshBase.Mode.DISABLED);
//		scrollView.setOnRefreshListener(null);
	}




}

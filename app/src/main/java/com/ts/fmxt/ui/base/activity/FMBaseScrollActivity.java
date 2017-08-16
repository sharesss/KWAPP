package com.ts.fmxt.ui.base.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ScrollView;

import com.thindo.base.NetworkAPI.BaseResponse;
import com.thindo.base.UI.Interfaces.STBaseScroll;
import com.thindo.base.Widget.refresh.RefreshScrollView;
import com.thindo.base.Widget.refresh.base.RefreshBase;


public abstract class FMBaseScrollActivity extends FMBaseActivity implements STBaseScroll {

	private RefreshScrollView scrollView;
	public ScrollView acutoScrollView;


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
				FMBaseScrollActivity.this.onReload();
			}
		});
		
		if(enableLoadMore){
			scrollView.setMode(RefreshBase.Mode.BOTH);
			scrollView.setOnRefreshListener(new RefreshBase.OnRefreshListener2<ScrollView>() {

				@Override
				public void onPullDownToRefresh(RefreshBase<ScrollView> refreshView) {
					FMBaseScrollActivity.this.onReload();
				}

				@Override
				public void onPullUpToRefresh(RefreshBase<ScrollView> refreshView) {
					FMBaseScrollActivity.this.loadMore();
				}
			});
		}
	}
	@Override
	public void onFailure(BaseResponse response) {
		super.onFailure(response);
		if(!cheakNetwork()) {
			scrollView.setVisibility(View.GONE);
			if(mNetworkView!=null)
				mNetworkView.setVisibility(View.VISIBLE);
		}
		stopRefreshState();
	}

	@Override
	public void onSuccess(BaseResponse response) {
		super.onSuccess(response);
		if(mNetworkView!=null)
			mNetworkView.setVisibility(View.GONE);
		scrollView.setVisibility(View.VISIBLE);
		stopRefreshState();
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
			FMBaseScrollActivity.this.onReload();
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
	@Override
	public void networkErrorReload() {

	}



}

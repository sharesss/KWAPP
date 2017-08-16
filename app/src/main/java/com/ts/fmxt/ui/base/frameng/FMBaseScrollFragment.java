package com.ts.fmxt.ui.base.frameng;

import android.view.LayoutInflater;
import android.widget.ScrollView;

import com.thindo.base.NetworkAPI.BaseResponse;
import com.thindo.base.UI.Interfaces.STBaseScroll;
import com.thindo.base.Widget.refresh.RefreshScrollView;
import com.thindo.base.Widget.refresh.base.RefreshBase;


public abstract class FMBaseScrollFragment extends FMBaseFragment implements STBaseScroll {

	private RefreshScrollView scrollView;
	private ScrollView acutoScrollView;
	
	public void bindRefreshScrollAdapter(RefreshScrollView scrollView, int layoutId){
		bindRefreshScrollAdapter(scrollView, layoutId, true);
	}
	
	/** Bind RefreshScrollView控件 */
	public void bindRefreshScrollAdapter(RefreshScrollView scrollView, int layoutId, boolean enableLoadMore){
		
		this.scrollView = scrollView;
		bindScrollAdapter(scrollView.getRefreshableView(), layoutId);
		
		scrollView.setMode(RefreshBase.Mode.PULL_FROM_START);
		scrollView.setOnRefreshListener(new RefreshBase.OnRefreshListener<ScrollView>() {

			@Override
			public void onRefresh(RefreshBase<ScrollView> refreshView) {
				FMBaseScrollFragment.this.onReload();
			}
		});
		
		if(enableLoadMore){
			scrollView.setMode(RefreshBase.Mode.BOTH);
			scrollView.setOnRefreshListener(new RefreshBase.OnRefreshListener2<ScrollView>() {

				@Override
				public void onPullDownToRefresh(RefreshBase<ScrollView> refreshView) {
					FMBaseScrollFragment.this.onReload();
				}

				@Override
				public void onPullUpToRefresh(RefreshBase<ScrollView> refreshView) {
					FMBaseScrollFragment.this.loadMore();
				}
			});
		}
	}
	
	/** Bind ScrollView控件 */
	protected void bindScrollAdapter(ScrollView acutoScrollView,int layoutId){
		this.acutoScrollView = acutoScrollView;
		if(layoutId != 0){
			acutoScrollView.removeAllViews();
			LayoutInflater.from(getActivity()).inflate(layoutId, this.acutoScrollView);
		}
	}
	
	@Override
	public void onFailure(BaseResponse response) {
		super.onFailure(response);
		stopRefreshState();
	}

	@Override
	public void onSuccess(BaseResponse response) {
		super.onSuccess(response);
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
	}

	@Override
	public void stop(){
		if(scrollView != null)
		scrollView.onRefreshComplete();
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
		if(scrollView != null)
			scrollView.setPullNoRefreshMode();
	}

	public void noRefresh(){
		scrollView.setMode(RefreshBase.Mode.DISABLED);
	}
	public void refreshStatus(){
		scrollView.setMode(RefreshBase.Mode.PULL_FROM_START);
	}

}

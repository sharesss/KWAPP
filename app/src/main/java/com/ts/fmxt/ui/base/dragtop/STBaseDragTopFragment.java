package com.ts.fmxt.ui.base.dragtop;

import android.view.View;

import com.thindo.base.Widget.refresh.base.RefreshBase;
import com.ts.fmxt.ui.base.frameng.FMBaseFragment;

import utils.dragtop.DragTopLayout;
import utils.dragtop.RefreshDragTopLayout;
import utils.dragtop.event.EventBus;


public abstract class STBaseDragTopFragment extends FMBaseFragment {

	private RefreshDragTopLayout dragTopLayout;
	private DragTopLayout acutoDragTopLayout;
	private boolean enableLoadMore = false;
	
	protected void bindRefreshDragTopAdapter(RefreshDragTopLayout dragTopLayout){
		bindRefreshDragTopAdapter(dragTopLayout, false);
	}
	
	protected void bindRefreshDragTopAdapter(RefreshDragTopLayout dragTopLayout, boolean enableLoadMore){
		this.dragTopLayout = dragTopLayout;
		this.acutoDragTopLayout = this.dragTopLayout.getRefreshableView();
		this.enableLoadMore = enableLoadMore;
		
		this.acutoDragTopLayout.setTopVisibility(View.GONE);
		this.acutoDragTopLayout.setContentVisibily(View.GONE);

		this.dragTopLayout.setMode(RefreshBase.Mode.PULL_FROM_START);
		this.dragTopLayout.setOnRefreshListener(new RefreshBase.OnRefreshListener<DragTopLayout>() {

			@Override
			public void onRefresh(RefreshBase<DragTopLayout> refreshView) {
				STBaseDragTopFragment.this.onReload();
			}
		});
		
		if(enableLoadMore){
			this.dragTopLayout.setMode(RefreshBase.Mode.BOTH);
			this.dragTopLayout.setOnRefreshListener(new RefreshBase.OnRefreshListener2<DragTopLayout>() {

				@Override
				public void onPullDownToRefresh(RefreshBase<DragTopLayout> refreshView) {
					STBaseDragTopFragment.this.onReload();
				}

				@Override
				public void onPullUpToRefresh(RefreshBase<DragTopLayout> refreshView) {
					STBaseDragTopFragment.this.loadMore();
				}
			});
		}
	}
	
	public abstract void onReload();

	public abstract void loadMore();
	
	/** 启动刷新 */
	protected void startRefreshState(){
		if(dragTopLayout != null){
			dragTopLayout.setMode(RefreshBase.Mode.PULL_FROM_START);
			dragTopLayout.setRefreshing(true);
		}
	}
	
	/** 停止刷新 */
	protected void stopRefreshState(){
		if(dragTopLayout != null){
			dragTopLayout.onRefreshComplete();
			if(enableLoadMore && dragTopLayout.getMode() != RefreshBase.Mode.BOTH)
				dragTopLayout.setMode(RefreshBase.Mode.BOTH);
		}
		
		if(acutoDragTopLayout.getTopView().getVisibility() != View.VISIBLE)
			this.acutoDragTopLayout.setTopVisibility(View.VISIBLE);
		if(acutoDragTopLayout.getContentView().getVisibility() != View.VISIBLE)
			this.acutoDragTopLayout.setContentVisibily(View.VISIBLE);
	}
	
	protected RefreshDragTopLayout getRefreshDragTopLayout(){
		return dragTopLayout;
	}
	
	protected DragTopLayout getDragTopLayout(){
		return acutoDragTopLayout;
	}
	
	// Handle scroll event from fragments
	public void onEvent(Boolean b) {
		acutoDragTopLayout.setTouchMode(b);
	}
	
	//Set eventbus as scroll widget mode
	protected void postEnventBus(boolean eventBus){
		EventBus.getDefault().post(eventBus);
	}
	
	@Override
	public void onPause() {
		super.onPause();
		EventBus.getDefault().unregister(this);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		EventBus.getDefault().register(this);
	}

}

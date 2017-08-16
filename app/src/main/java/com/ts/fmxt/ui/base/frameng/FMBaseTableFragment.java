package com.ts.fmxt.ui.base.frameng;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.thindo.base.NetworkAPI.BaseResponse;
import com.thindo.base.UI.Interfaces.STBaseTable;
import com.thindo.base.Widget.refresh.RefreshListView;
import com.thindo.base.Widget.refresh.base.RefreshBase;
import com.thindo.base.Widget.refresh.footer.RefreshFooterView;

import java.util.ArrayList;

import http.data.TableList;
import utils.dragtop.AttachUtil;
import utils.dragtop.event.EventBus;
import utils.helper.ToastHelper;
import utils.helper.UIHelper;
import widget.NetworkView;


public abstract class FMBaseTableFragment extends FMBaseFragment implements STBaseTable , NetworkView.NetworkViewListener {

    protected ArrayList<Object> arrayList = new ArrayList<Object>();
    private RefreshListView listView;
    protected BaseAdapter tableAdapter;
    public ListView acutoListView;
    private RefreshFooterView refreshFooterView;
    private boolean enableLoadMore = false;
    private View emptyView = null;
    public NetworkView mNetworkView;

    protected void bindRefreshAdapter(RefreshListView listView, BaseAdapter _tableAdapter) {
        bindRefreshAdapter(listView, _tableAdapter, true);
    }

    /**
     * 添加自定义底部样式
     */
    public void bindRefreshFooterAdapter(RefreshListView listView, BaseAdapter _tableAdapter) {
        this.listView = listView;
        this.acutoListView = listView.getRefreshableView();
        addFooterClickRefreshView();
        bindRefreshAdapter(listView, _tableAdapter, false);
    }

    /**
     * Bind 刷新控件
     */
    protected void bindRefreshAdapter(RefreshListView listView, BaseAdapter _tableAdapter, boolean enableLoadMore) {
        this.enableLoadMore = enableLoadMore;
        this.tableAdapter = _tableAdapter;
        this.listView = listView;
        this.acutoListView = listView.getRefreshableView();

        this.acutoListView.setAdapter(tableAdapter);
        this.listView.setMode(RefreshBase.Mode.PULL_FROM_START);
        this.listView.setOnRefreshListener(new RefreshListView.OnRefreshListener<ListView>() {

            @Override
            public void onRefresh(RefreshBase<ListView> refreshView) {
                FMBaseTableFragment.this.onReload();
            }
        });

        if (enableLoadMore) {
            this.listView.setMode(RefreshBase.Mode.BOTH);
            this.listView.setOnRefreshListener(new RefreshListView.OnRefreshListener2<ListView>() {

                @Override
                public void onPullDownToRefresh(RefreshBase<ListView> refreshView) {
                    FMBaseTableFragment.this.onReload();
                }

                @Override
                public void onPullUpToRefresh(RefreshBase<ListView> refreshView) {
                    FMBaseTableFragment.this.loadMore();
                }
            });
        }

        listView.setOnItemClickListener(new OnBaseItemClickListener());
    }

    protected void bindListAdapter(ListView listView, BaseAdapter _tableAdapter) {
        bindListAdapter(listView, _tableAdapter, false);
    }

    /**
     * 添加自定义底部样式
     */
    protected void bindListFooterAdapter(ListView listView, BaseAdapter _tableAdapter, boolean scrollEventBus) {
        this.acutoListView = listView;
        addFooterClickRefreshView();
        bindListAdapter(listView, _tableAdapter, scrollEventBus);
    }

    /**
     * Bind 基础控件
     */
    protected void bindListAdapter(ListView listView, BaseAdapter _tableAdapter, boolean scrollEventBus) {
        this.tableAdapter = _tableAdapter;
        this.acutoListView = listView;

        this.acutoListView.setAdapter(tableAdapter);
        this.acutoListView.setOnItemClickListener(new OnBaseItemClickListener());

        if (scrollEventBus) {
            this.acutoListView.setOnScrollListener(new OnScrollListener() {

                @Override
                public void onScrollStateChanged(AbsListView view, int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {
                    EventBus.getDefault().post(AttachUtil.isAdapterViewAttach(view));
                }
            });
        }
    }

    private class OnBaseItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            // 去掉headview和footview
            // 除了控件自带的1个headerview外，如果有其他自定义的header，则position做此处理
            int fristCount = acutoListView.getHeaderViewsCount();

            int dataCount = FMBaseTableFragment.this.tableAdapter.getCount();

            // int lastCount = listView.getFooterViewsCount();

            if (position >= fristCount + dataCount || position < fristCount)
                return;

            FMBaseTableFragment.this.onItemClick(arg0, arg1, position - fristCount, arg3);
        }
    }

    public abstract void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3);

    public abstract void onReload();

    public abstract void loadMore();

    @Override
    public void onStart(BaseResponse response) {
        super.onStart(response);
    }

    @Override
    public void onFailure(BaseResponse response) {
        super.onFailure(response);
        if ( !cheakNetwork()) {
//            mNetworkView.setVisibility(View.VISIBLE);
            arrayList.clear();
            tableAdapter.notifyDataSetChanged();
        }
        stopRefreshState();
    }

    @Override
    public void onSuccess(BaseResponse response) {
        super.onSuccess(response);
        if (response.getRequestType() == 0) {
            if (BaseResponse.SUCCEED == response.getStatus()) {
                TableList tableList = (TableList) response.getData();

                if (!response.isLoadMore()) {
                    arrayList.clear();
                }
                stopRefreshState();
//
                arrayList.addAll(tableList.getArrayList());
                tableAdapter.notifyDataSetChanged();

                if (refreshFooterView != null) {
                    if (arrayList.size() == 0 && refreshFooterView.getVisibility() != View.GONE)
                        refreshFooterView.setVisibility(View.GONE);
                    else if (arrayList.size() > 0 && refreshFooterView.getVisibility() != View.VISIBLE)
                        refreshFooterView.setVisibility(View.VISIBLE);
                    if (tableList.getArrayList() != null && tableList.getArrayList().size() < 10)
                        refreshFooterView.setVisibility(View.GONE);
                    if (tableList.getArrayList() != null && tableList.getArrayList().size() > 10)
                        refreshFooterView.setVisibility(View.GONE);
                }
            } else {
                stopRefreshState();
                ToastHelper.toastMessage(getActivity(), response.getError_msg());
            }
        }
    }

    @Override
    public RefreshListView getRefreshListView() {
        return listView;
    }

    @Override
    public ListView getListView() {
        return acutoListView;
    }

    @Override
    public void startRefreshState() {
       if(acutoListView!=null)
          acutoListView.setEmptyView(null);

        if (listView != null) {
            listView.setMode(RefreshBase.Mode.PULL_FROM_START);
            listView.setRefreshing(true);
        } else if (acutoListView != null)
            FMBaseTableFragment.this.onReload();
    }

    @Override
    public void stop(){
        if (emptyView != null)
            acutoListView.setEmptyView(emptyView);

        if (listView != null) {
            listView.onRefreshComplete();
            if (enableLoadMore && listView.getMode() != RefreshBase.Mode.BOTH)
                listView.setMode(RefreshBase.Mode.BOTH);
        }
        if (refreshFooterView != null)
            refreshFooterView.stopRefreshFooter();
    }
    @Override
    public void stopRefreshState() {

        if (emptyView != null)
            acutoListView.setEmptyView(emptyView);

        if (listView != null) {
            listView.onRefreshComplete();
            if (enableLoadMore && listView.getMode() != RefreshBase.Mode.BOTH)
                listView.setMode(RefreshBase.Mode.BOTH);
        }
        if (refreshFooterView != null)
            refreshFooterView.stopRefreshFooter();
    }

    @Override
    public void addHeaderView(View headerView) {
        if (acutoListView != null)
            acutoListView.addHeaderView(headerView);
    }

    @Override
    public void addFooterView(View footerView) {
        if (acutoListView != null)
            acutoListView.addFooterView(footerView);
    }

    @Override
    public void setBackground(int resourcesId) {
        if (listView != null)
            listView.setBackgroundResource(resourcesId);
        else if (acutoListView != null)
            acutoListView.setBackgroundResource(resourcesId);
    }

    @Override
    public void setDivider(int dividerId, int dividerHeight) {
        if (acutoListView != null) {
            acutoListView.setDivider(getActivity().getResources().getDrawable(dividerId));
            acutoListView.setDividerHeight(UIHelper.dipToPx(getActivity(), dividerHeight));
        }
    }

    @Override
    public void setScrollBar(boolean scrollBar) {
        if (acutoListView != null) {
            acutoListView.setVerticalScrollBarEnabled(false);
            acutoListView.setHorizontalScrollBarEnabled(false);
        }
    }

    @Override
    public RefreshFooterView getRefreshFooterView() {
        return refreshFooterView;
    }

    private void addFooterClickRefreshView() {
        if (listView != null)
            listView.setMode(RefreshBase.Mode.PULL_FROM_START);
        refreshFooterView = new RefreshFooterView(getActivity());
        refreshFooterView.setOnRefreshFooterClickListener(new RefreshFooterView.OnRefreshFooterClickListener() {

            @Override
            public void onRefreshFooterClick() {
                FMBaseTableFragment.this.loadMore();
            }
        });
        addFooterView(refreshFooterView);
        refreshFooterView.setVisibility(View.GONE);
    }

    @Override
    public void setEmptyView(View view) {
        this.emptyView = view;
    }

    public void setNetworkView(NetworkView mNetworkView) {
        this.mNetworkView = mNetworkView;
        this.mNetworkView.setmCallBack(this);
    }

    @Override
    public void networkErrorReload() {
        startRefreshState();
    }
}

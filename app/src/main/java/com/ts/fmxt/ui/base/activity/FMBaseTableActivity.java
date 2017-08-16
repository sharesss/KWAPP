package com.ts.fmxt.ui.base.activity;

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

import http.Logger;
import utils.dragtop.AttachUtil;
import utils.dragtop.event.EventBus;
import utils.helper.UIHelper;


public abstract class FMBaseTableActivity extends FMBaseActivity implements STBaseTable {

    private Logger logger = new Logger(this.getClass().getSimpleName());
    protected ArrayList<Object> arrayList = new ArrayList<Object>();
    private RefreshListView listView;
    protected BaseAdapter tableAdapter;
    public ListView acutoListView;
    private RefreshFooterView refreshFooterView;
    private boolean enableLoadMore = false;
    public View emptyView;


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
                FMBaseTableActivity.this.onReload();
            }
        });

        if (enableLoadMore) {
            this.listView.setMode(RefreshBase.Mode.BOTH);
            this.listView.setOnRefreshListener(new RefreshListView.OnRefreshListener2<ListView>() {

                @Override
                public void onPullDownToRefresh(
                        RefreshBase<ListView> refreshView) {
                    FMBaseTableActivity.this.onReload();
                }

                @Override
                public void onPullUpToRefresh(
                        RefreshBase<ListView> refreshView) {
                    FMBaseTableActivity.this.loadMore();
                }
            });
        }

        this.listView.setOnItemClickListener(new OnBaseItemClickListener());
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
                public void onScrollStateChanged(AbsListView view,
                                                 int scrollState) {

                }

                @Override
                public void onScroll(AbsListView view, int firstVisibleItem,
                                     int visibleItemCount, int totalItemCount) {
                    EventBus.getDefault().post(
                            AttachUtil.isAdapterViewAttach(view));
                }
            });
        }
    }

    /**
     * 绑定无数据刷新有刷新操作体验
     */
    public void bindNoRefreshListAdapter(RefreshListView listView, BaseAdapter _tableAdapter) {
        this.tableAdapter = _tableAdapter;
        this.listView = listView;
        this.acutoListView = listView.getRefreshableView();
        this.acutoListView.setAdapter(tableAdapter);
        this.listView.setMode(RefreshBase.Mode.BOTH);
        this.listView.setOnItemClickListener(new OnBaseItemClickListener());
        this.listView.setPullNoRefreshMode();
    }

    private class OnBaseItemClickListener implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
            // 去掉headview和footview
            // 除了控件自带的1个headerview外，如果有其他自定义的header，则position做此处理
            int fristCount = acutoListView.getHeaderViewsCount();

            int dataCount = FMBaseTableActivity.this.tableAdapter.getCount();

            // int lastCount = listView.getFooterViewsCount();

            if (position >= fristCount + dataCount || position < fristCount)
                return;

            FMBaseTableActivity.this.onItemClick(arg0, arg1, position - fristCount, arg3);
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
        stopRefreshState();
        if (mNetworkView != null && !cheakNetwork()) {
            mNetworkView.setVisibility(View.VISIBLE);
            arrayList.clear();
            tableAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onSuccess(BaseResponse response) {
        super.onSuccess(response);
        if (mNetworkView != null)
            mNetworkView.setVisibility(View.GONE);
        if (response.getRequestType() == 0) {
            if (BaseResponse.SUCCEED == response.getStatus()) {
//                TableList tableList = (TableList) response.getData();
//                if (!response.isLoadMore()) {
//                    arrayList.clear();
//                }
                stopRefreshState();

//                logger.i("TableList Size : " + tableList.getArrayList().size());
//                arrayList.addAll(tableList.getArrayList());
//                tableAdapter.notifyDataSetChanged();

                if (refreshFooterView != null) {
                    if (arrayList.size() == 0 && refreshFooterView.getVisibility() != View.GONE)
                        refreshFooterView.setVisibility(View.GONE);
                    else if (arrayList.size() > 0 && refreshFooterView.getVisibility() != View.VISIBLE)
                        refreshFooterView.setVisibility(View.VISIBLE);
                }
                if (enableLoadMore) {
                    this.listView.setMode(arrayList.size() < 10 ? RefreshBase.Mode.PULL_FROM_START : RefreshBase.Mode.BOTH);
                }

            } else {
                stopRefreshState();
              //  ToastHelper.toastMessage(this, response.getError_msg());
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
        if (acutoListView != null) {
            acutoListView.setEmptyView(null);
        }

        if (listView != null) {
            listView.setMode(RefreshBase.Mode.PULL_FROM_START);
            listView.setRefreshing(true);
        } else if (acutoListView != null)
            FMBaseTableActivity.this.onReload();
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

    public void removeHeaderView(View headerView) {
        if (acutoListView != null)
            acutoListView.removeHeaderView(headerView);
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
            acutoListView.setDivider(this.getResources().getDrawable(dividerId));
            acutoListView.setDividerHeight(UIHelper.dipToPx(this, dividerHeight));
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
        refreshFooterView = new RefreshFooterView(this);
        refreshFooterView.setOnRefreshFooterClickListener(new RefreshFooterView.OnRefreshFooterClickListener() {

            @Override
            public void onRefreshFooterClick() {
                FMBaseTableActivity.this.loadMore();
            }
        });
        addFooterView(refreshFooterView);
        refreshFooterView.setVisibility(View.GONE);
    }

    @Override
    public void setEmptyView(View view) {
        this.emptyView = view;
    }

    @Override
    public void networkErrorReload() {
        startRefreshState();
    }

}

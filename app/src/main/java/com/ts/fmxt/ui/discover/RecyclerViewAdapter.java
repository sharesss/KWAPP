package com.ts.fmxt.ui.discover;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by liangzhenxiong on 2017/4/6 0006.
 * 主适配器
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<BaseViewItem> mdata;
    private Context mContext;
//    private LongSparseArray sparseArray = new LongSparseArray();

    /**
     * Same as QuickAdapter#QuickAdapter(Context,int) but with
     * some initialization data.
     *
     * @param data A new list is created out of this one to avoid mutable list
     */
    public RecyclerViewAdapter(Context mContext, List<BaseViewItem> data) {
        mdata = data;
        this.mContext = mContext;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerView.ViewHolder viewHolder = null;
        for (BaseViewItem viewItem : mdata) {
            if (viewItem != null && viewItem.getViewType() == viewType) {
                viewHolder = viewItem.createViewHolder(parent);
                if (viewHolder == null) {
                    View contentview = viewItem.createView(parent);
                    if (contentview != null) {
                        viewHolder = new RecyclerViewHolder(contentview);
                        break;
                    }
                } else {
                    break;
                }
            }
        }
        if (viewHolder == null) {
            viewHolder = new RecyclerViewHolder(new View(mContext));
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        mdata.get(position).onBindViewHolder(holder, position);
    }


    @Override
    public int getItemViewType(int position) {
        return mdata.get(position).getViewType();
    }

    @Override
    public int getItemCount() {
        return mdata.size();
    }
}

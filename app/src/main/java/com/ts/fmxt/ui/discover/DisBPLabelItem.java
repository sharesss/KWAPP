package com.ts.fmxt.ui.discover;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ts.fmxt.R;

/**
 */

public class DisBPLabelItem implements BaseViewItem, View.OnClickListener {
    private int cont = 0;
    DiscoverDetailsActivity activity;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_bpresult:
                activity.InvestBPListRequest(true);
                activity.adapter.notifyDataSetChanged();
                break;
        }
    }


    public DisBPLabelItem(int cont, DiscoverDetailsActivity activity) {
        this.cont = cont;
        this.activity = activity;
    }

    @Override
    public int getViewType() {
        return getClass().hashCode();
    }

    @Override
    public View createView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.disbplabelitem_layout, parent, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;
        if (activity.oncheckBP) {
            viewHolder.tvBpresult.setVisibility(View.GONE);
            viewHolder.tvResult.setVisibility(View.VISIBLE);
        } else {
            if (cont >= 6) {
                viewHolder.tvBpresult.setVisibility(View.VISIBLE);
            } else {
                viewHolder.tvBpresult.setVisibility(View.GONE);
            }
        }
        if (activity != null) {
            viewHolder.tvBpresult.setOnClickListener(this);
        }

    }

    @Override
    public RecyclerViewHolder createViewHolder(ViewGroup parent) {
        View view = createView(parent);
        return new ViewHolder(view);
    }

    private class ViewHolder extends RecyclerViewHolder {
        TextView tvBpresult, tvResult;


        private ViewHolder(final View covert) {
            super(covert);
            tvBpresult = (TextView) covert.findViewById(R.id.tv_bpresult);
            tvResult = (TextView) covert.findViewById(R.id.tv_result);
        }
    }
}

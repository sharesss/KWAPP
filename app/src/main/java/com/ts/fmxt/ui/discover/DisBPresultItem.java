package com.ts.fmxt.ui.discover;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ts.fmxt.R;

import http.data.InvestBPListEntity;

/**
 */

public class DisBPresultItem implements BaseViewItem {
    InvestBPListEntity info;

    public DisBPresultItem(InvestBPListEntity info) {
        this.info = info;
    }

    @Override
    public int getViewType() {
        return getClass().hashCode();
    }

    @Override
    public View createView(ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_bp_result, parent, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        ViewHolder viewHolder = (ViewHolder) holder;

        viewHolder.tv_brand_name.setText(info.getBpname());
        viewHolder.tv_fraction.setText(info.getScore() + "分");
        viewHolder.tv_peoplenum.setText(info.getPeopleNum() + "人");
        viewHolder.pb_yellowindex.setProgress(info.getScore());
    }

    @Override
    public RecyclerViewHolder createViewHolder(ViewGroup parent) {
        View view = createView(parent);
        return new ViewHolder(view);
    }

    private class ViewHolder extends RecyclerViewHolder {
        private TextView tv_brand_name;
        private TextView tv_fraction, tv_peoplenum;
        private ProgressBar pb_yellowindex;

        protected ViewHolder(View convertView) {
            super(convertView);
            tv_brand_name = (TextView) convertView.findViewById(R.id.tv_name);
            tv_fraction = (TextView) convertView.findViewById(R.id.tv_fraction);
            tv_peoplenum = (TextView) convertView.findViewById(R.id.tv_peoplenum);
            pb_yellowindex = (ProgressBar) convertView.findViewById(R.id.pb_yellowindex);

        }
    }
}

package com.ts.fmxt.ui.ItemAdapter;/**
 * Created by A1 on 2017/8/14.
 */

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.thindo.base.Adapter.FMBaseAdapter;
import com.ts.fmxt.R;

import java.util.List;

import http.data.InvestBPListEntity;

/**
 * created by kp at 2017/8/14
 * bp结果
 */
public class BpresultAdapter extends FMBaseAdapter {
    private List list;
    public BpresultAdapter(Context context, List arrayList) {
        super(context, arrayList);
        this.list = arrayList;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder ViewHolder = null;
        if (convertView == null) {
            ViewHolder = new ViewHolder();
            convertView = View.inflate(getContext(), R.layout.adapter_bp_result, null);
            ViewHolder.tv_brand_name = (TextView) convertView.findViewById(R.id.tv_name);
            ViewHolder.tv_fraction = (TextView) convertView.findViewById(R.id.tv_fraction);
            ViewHolder.tv_peoplenum= (TextView) convertView.findViewById(R.id.tv_peoplenum);
            ViewHolder.pb_yellowindex = (ProgressBar) convertView.findViewById(R.id.pb_yellowindex);
            convertView.setTag(ViewHolder);
        } else {
            ViewHolder = (ViewHolder) convertView.getTag();
        }
        InvestBPListEntity info = (InvestBPListEntity) list.get(position);
        ViewHolder.tv_brand_name.setText(info.getBpname());
        ViewHolder.tv_fraction.setText(info.getScore()+"分");
        ViewHolder.tv_peoplenum.setText(info.getPeopleNum()+"人");
        ViewHolder.pb_yellowindex.setProgress(info.getScore());


        return convertView;
    }

    class ViewHolder {
        private TextView tv_brand_name;
        private TextView tv_fraction,tv_peoplenum;
        private ProgressBar pb_yellowindex;


    }
}

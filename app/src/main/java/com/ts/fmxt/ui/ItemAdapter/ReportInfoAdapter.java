package com.ts.fmxt.ui.ItemAdapter;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thindo.base.Adapter.FMBaseAdapter;
import com.ts.fmxt.R;

import java.util.List;

import http.data.ReportInfoEntity;
import utils.ReceiverUtils;

/**
 * Created by A1 on 2017/9/5.
 */

public class ReportInfoAdapter extends FMBaseAdapter {
    private List list;
    private int sele =-1;
    private static int isSelePosition;
    private static String name;

    public ReportInfoAdapter(Activity context, List arrayList) {
        super(context, arrayList);
        this.list = arrayList;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder ViewHolder = null;
        if (convertView == null) {
            ViewHolder = new ViewHolder();
            convertView = View.inflate(getContext(), R.layout.item_report_info, null);
            ViewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
            ViewHolder.tv_asset = (TextView) convertView.findViewById(R.id.tv_asset);
            convertView.setTag(ViewHolder);
        } else {
            ViewHolder = (ViewHolder) convertView.getTag();
        }
        if(position==sele){
            ViewHolder.tv_asset.setBackground(getContext().getResources().getDrawable(R.mipmap.report_down));

        }else{
            ViewHolder.tv_asset.setBackground(getContext().getResources().getDrawable(R.mipmap.report_up));
        }
        ReportInfoEntity info = (ReportInfoEntity) list.get(position);
        ViewHolder.tv_name.setText(info.getName());
        ViewHolder.tv_asset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sele=position;
                ReportInfoEntity info = (ReportInfoEntity) list.get(sele);
                isSelePosition = info.getId();
                name = info.getName();
                if(name.equals("其他")){
                    Bundle bundle = new Bundle();
                    ReceiverUtils.sendReceiver(ReceiverUtils.REPORT,bundle);
                }else {
                    Bundle bundle = new Bundle();
                    ReceiverUtils.sendReceiver(ReceiverUtils.GONE,bundle);
                }
                notifyDataSetChanged();

            }
        });
        return convertView;
    }
    public static int getIsSelePosition(){
        return isSelePosition;
    }

    public static String  getName(){
        return name;
    }


    class ViewHolder {
        private TextView tv_name,tv_asset;


    }
}


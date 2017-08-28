package com.ts.fmxt.ui.ItemAdapter;/**
 * Created by A1 on 2017/8/8.
 */

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thindo.base.Adapter.FMBaseAdapter;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.discover.view.seekBar;

import java.util.List;

import http.data.InvestBPListEntity;
import widget.image.FMNetImageView;

/**
 * created by kp at 2017/8/8
 * 12项BP适配器
 */
public class ItemBpAdapter extends FMBaseAdapter {
    private List list;
    private int investId;

    public ItemBpAdapter(Activity context, List arrayList, int investId) {
        super(context, arrayList);
        this.list = arrayList;
        this.investId = investId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder ViewHolder = null;
        if (convertView == null) {
            ViewHolder = new ViewHolder();
            convertView = View.inflate(getContext(), R.layout.item_bp, null);
            ViewHolder.iv_image  = (FMNetImageView) convertView.findViewById(R.id.iv_image);
            ViewHolder.tv_brand_name = (TextView) convertView.findViewById(R.id.tv_brand_name);
            ViewHolder.tv_brand_details = (TextView) convertView.findViewById(R.id.tv_brand_details);
            ViewHolder.tv_index = (TextView) convertView.findViewById(R.id.tv_index);
            ViewHolder.tv_money = (TextView) convertView.findViewById(R.id.tv_money);
            ViewHolder.seekBar = (seekBar) convertView.findViewById(R.id.seekBar);
            convertView.setTag(ViewHolder);
        } else {
            ViewHolder = (ViewHolder) convertView.getTag();
        }
        InvestBPListEntity info = (InvestBPListEntity) list.get(position);
        ViewHolder.tv_brand_name.setText(info.getBpname());
        ViewHolder.tv_brand_details.setText(info.getBpdeion());
        ViewHolder.iv_image.loadImage(info.getBpphoto());
        ViewHolder.iv_image.setVisibility(!info.getBpphoto().equals("") ? View.VISIBLE : View.GONE);
        if(investId!=0){
            ViewHolder.seekBar.formatData(info,investId);
        }
        return convertView;
    }

    class ViewHolder {
        private FMNetImageView iv_image;
        private TextView tv_brand_name,tv_index,tv_seekbar_distance;
        private TextView tv_brand_details,tv_money;
        private  seekBar seekBar;


    }
}

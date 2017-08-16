package com.ts.fmxt.ui.ItemAdapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.ts.fmxt.R;

import java.util.List;

import widget.wheelview.adapters.AbstractWheelTextAdapter;


/**
 * 滚轮适配器
 */
public class WheelAdapter extends AbstractWheelTextAdapter {

    Context mContext;
    List<String> mDatas;

    /**
     *
     * @param context 上下文
     * @param datas 数据
     * @param currentIndex 当前索引
     * @param maxSize 字体最大值
     * @param minSize 字体最小值
     */
    public WheelAdapter(Context context, List<String> datas, int currentIndex, int maxSize, int minSize) {
        super(context, R.layout.adapter_wheel_view,NO_RESOURCE,currentIndex,maxSize,minSize);
        mContext = context;
        mDatas = datas;
        setItemTextResource(R.id.tempValue);
    }

    @Override
    public CharSequence getItemText(int index) {
        return mDatas.get(index);
    }

    @Override
    public int getItemsCount() {
        return mDatas.size();
    }


    @Override
    public View getItem(int index, View convertView, ViewGroup parent) {

        View view  = super.getItem(index,convertView,parent);

        return view;
//        return super.getItem(index, convertView, parent);
    }


    public  String getText(int index){
        return mDatas.get(index);
    }
}

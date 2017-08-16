package com.ts.fmxt.ui.user.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.TextView;

import com.ts.fmxt.R;
import com.ts.fmxt.ui.ItemAdapter.WheelAdapter;

import java.util.ArrayList;
import java.util.List;

import utils.StringUtils;
import widget.popup.BaseEventPopup;
import widget.wheelview.OnWheelChangedListener;
import widget.wheelview.OnWheelScrollListener;
import widget.wheelview.WheelView;


/**
 * 年收入
 */
public class PopupWheelInComeView extends BaseEventPopup implements OnClickListener  {

    private static int maxsize = 18;//最大字体

    private static int minsize = 14;//最小字体
    private int mYearCurrentIndex = 2, mMonthCurrentIndex, mDayCurrentIndex;
    private WheelListener mWheelListener;
    private WheelAdapter mAdapter;
    List<Object> data = new ArrayList<>();
    List<String> mArrayList = new ArrayList<>();
    private WheelAdapter mWheelAdapter;
    private int mCurrentIndex=0;
    private WheelView mWheelView;
//    private UserInComeEntity info;
    private String money;

    public PopupWheelInComeView(Activity context, String money) {
        super(context);
        String[] array=getContext().getResources().getStringArray(R.array.annual_salary);
        for(int i=0;i<array.length;i++){
            if(!StringUtils.isEmpty(money)){
                if(money.equals(array[i])){
                    mCurrentIndex=i;
                }
            }
            mArrayList.add(array[i]);
        }
        this.money=array[mCurrentIndex];
        getView().findViewById(R.id.tv_finish).setOnClickListener(this);
        getView().findViewById(R.id.tv_clean).setOnClickListener(this);

        mAdapter = new WheelAdapter(getContext(), mArrayList, mCurrentIndex, maxsize, minsize);

        mWheelView = (WheelView) getView().findViewById(R.id.year);
        mWheelView.setVisibility(View.VISIBLE);
        initListener();
        mWheelView.setViewAdapter(mAdapter);
        mWheelView.setCurrentItem(mCurrentIndex);

    }

    public void setWheelListener(WheelListener mWheelListener) {
        this.mWheelListener = mWheelListener;
    }


    @Override
    public View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.popup_wheel_view, null);
    }

    @Override
    public void findEventByID() {

    }

    @Override
    public void findTitleByID() {

    }

    @Override
    public void findMessageByID() {

    }

    @Override
    public Animation loadAnim() {
        return null;
    }

    @Override
    public View getStartAnimViewGroup() {
        return getView().findViewById(R.id.popup_parent_layout);
    }

    @Override
    public void showPopupWindow() {
        super.showPopupWindow(R.id.AppWidget);
    }

    @Override
    public void setOnDismissPopupListener(OnDismissPoppupListener listener) {
        super.setOnDismissPopupListener(listener);

    }

    @Override
    public void onClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.tv_finish:
                if (mWheelListener != null)
                    mWheelListener.completeCall(money,"", 2);

                break;
            case R.id.tv_clean:
                if (mWheelListener != null)
                    mWheelListener.clearCall(2);
                break;

        }
    }

    private void initListener() {
        mWheelView.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int currentItem = wheel.getCurrentItem();
                String text = (String) mAdapter.getItemText(currentItem);
                setItemTextSize(text, mAdapter);
            }
        });
        mWheelView.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                int currentItem = wheel.getCurrentItem();
                String text = (String) mAdapter.getItemText(currentItem);
                setItemTextSize(text, mAdapter);
                money=text;

            }
        });
    }

    /**
     * 设置字体大小
     *
     * @param currentItemText
     * @param birthYearAdapter
     */
    private void setItemTextSize(String currentItemText, WheelAdapter birthYearAdapter) {
        //获取所有的View
        ArrayList<View> arrayLists = birthYearAdapter.getTextViews();

        int size = arrayLists.size();
        //当前条目的内容
        String currentText;
        for (int i = 0; i < size; i++) {
            TextView textview = (TextView) arrayLists.get(i);
            currentText = textview.getText().toString().trim();

            if (currentItemText.equals(currentText)) {
                textview.setTextSize(maxsize);
            } else {
                textview.setTextSize(minsize);
            }
        }

    }



}

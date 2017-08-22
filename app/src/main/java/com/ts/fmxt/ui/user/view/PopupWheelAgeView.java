package com.ts.fmxt.ui.user.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.TextView;

import com.ts.fmxt.R;
import com.ts.fmxt.ui.ItemAdapter.WheelAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import utils.StringUtils;
import utils.helper.ToastHelper;
import widget.popup.BaseEventPopup;
import widget.wheelview.OnWheelChangedListener;
import widget.wheelview.OnWheelScrollListener;
import widget.wheelview.WheelView;


/**
 * 三级联动
 */
public class PopupWheelAgeView extends BaseEventPopup implements OnClickListener {

    private static int maxsize = 18;//最大字体

    private static int minsize = 18;//最小字体
    private int mYearCurrentIndex, mMonthCurrentIndex, mDayCurrentIndex;
    private WheelListener mWheelListener;

    List<String> mMonthArrayList = new ArrayList<>();
    List<String> mYearArrayList = new ArrayList<>();
    List<String> mDayArrayList = new ArrayList<>();
    private WheelAdapter mYearAdapter, mMonthAdapter, mDayAdapter;

    private SimpleDateFormat yearDate = new SimpleDateFormat("yyyy");
    private WheelView yearView, monthView, dayView;
    private String yearStr = "2017年", monthStr = "01月", dataStr = "01日";

    public PopupWheelAgeView(Activity context) {
        super(context);

        int year = Integer.valueOf(yearDate.format(new Date()));
        for (int i = year - 18; i > year - 101; i--) {
            mYearArrayList.add(String.format("%s年", String.valueOf(i)));
        }
        for (int i = 1; i <= 18; i++) {
            mMonthArrayList.add(String.format("%s月", String.valueOf(i)));
        }

        yearStr = getYear() + "年";
        monthStr = getMonth() + "月";
        getView().findViewById(R.id.tv_finish).setOnClickListener(this);
        getView().findViewById(R.id.tv_clean).setOnClickListener(this);

        mYearAdapter = new WheelAdapter(getContext(), mYearArrayList, mYearCurrentIndex, maxsize, minsize);
        mMonthAdapter = new WheelAdapter(getContext(), mMonthArrayList, mMonthCurrentIndex, maxsize, minsize);

        yearView = (WheelView) getView().findViewById(R.id.year);
        yearView.setVisibility(View.VISIBLE);
        yearView.setVisibleItems(5);
        yearView.setViewAdapter(mYearAdapter);

        monthView = (WheelView) getView().findViewById(R.id.month);
        monthView.setVisibleItems(5);
        monthView.setVisibility(View.VISIBLE);
        monthView.setViewAdapter(mMonthAdapter);
        monthView.setCurrentItem(Integer.valueOf(getMonth()) - 1);
        dayView = (WheelView) getView().findViewById(R.id.day);
        dayView.setVisibility(View.VISIBLE);
        dayView.setVisibleItems(5);
        notifyDay();

        initListener();

    }

    public void setWheelListener(WheelListener mWheelListener) {
        this.mWheelListener = mWheelListener;
    }

    private void notifyDay() {
        mDayArrayList.clear();
        String tempY = yearStr.substring(0, yearStr.length() - 1);
        String tempM = monthStr.substring(0, monthStr.length() - 1);
        for (int i = 1; i <= callDays(tempY, tempM); i++) {
            mDayArrayList.add(String.format("%s日", String.valueOf(i)));
        }
        mDayAdapter = new WheelAdapter(getContext(), mDayArrayList, mDayCurrentIndex, maxsize, minsize);
        dayView.setViewAdapter(mDayAdapter);
        dayView.setCurrentItem(0);
    }

    /**
     * 计算每月多少天
     *
     * @param month
     * @param year
     */
    public int callDays(String year, String month) {
        boolean leayyear = false;
        int day = 30;
        if (StringUtils.isEmpty(month))
            month = getMonth();
        if (StringUtils.isEmpty(year))
            year = getYear();
        if (Integer.parseInt(year) % 4 == 0) {
            leayyear = true;
        } else {
            leayyear = false;
        }
        for (int i = 1; i <= 12; i++) {
            switch (Integer.parseInt(month)) {
                case 1:
                case 3:
                case 5:
                case 7:
                case 8:
                case 10:
                case 12:
                    day = 31;
                    break;
                case 2:
                    if (leayyear) {
                        day = 29;
                    } else {
                        day = 28;
                    }
                    break;
                case 4:
                case 6:
                case 9:
                case 11:
                    day = 30;
                    break;
            }
        }
        if (year.equals(getYear()) && month.equals(getMonth())) {
            day = getDay();
        }
        return day;
    }

    public String getMonth() {
        Calendar c = Calendar.getInstance();
        return String.valueOf(c.get(Calendar.MONTH) + 1);
    }

    public int getDay() {
        Calendar c = Calendar.getInstance();
        return c.get(Calendar.DATE);
    }

    public String getYear() {
        Calendar c = Calendar.getInstance();
        return String.valueOf(c.get(Calendar.YEAR));
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
        super.showPopupWindow();
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
                if (mWheelListener != null) {
                    int temp = Integer.valueOf(yearStr.substring(0, yearStr.length() - 1));
                    int thisY = Integer.valueOf(getYear());
                    int callYear = thisY - temp;
                    if (callYear == 0) {
                        yearStr = mYearArrayList.get(0);
                        callYear = 18;
                    }
                    String date = String.format("%s-%s-%s", yearStr.substring(0, yearStr.length() - 1), monthStr.substring(0, monthStr.length() - 1), dataStr.substring(0, dataStr.length() - 1));
                    if (callYear < 18) {
                        ToastHelper.toastMessage(getContext(), "年龄必须大于18岁");
                        return;
                    }
                    mWheelListener.completeCall(date, String.valueOf(callYear), 1);
                }
                break;
            case R.id.tv_clean:
                mWheelListener.clearCall(1);
                break;

        }
    }


    private void initListener() {
        //===========================年分滚轮监听===========================================================
        yearView.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int currentItem = wheel.getCurrentItem();
                String text = (String) mYearAdapter.getItemText(currentItem);
                setItemTextSize(text, mYearAdapter);
            }
        });
        yearView.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                int currentItem = wheel.getCurrentItem();
                String text = (String) mYearAdapter.getItemText(currentItem);
                setItemTextSize(text, mYearAdapter);
                yearStr = text;
                notifyDay();
            }
        });

//===========================月分滚轮监听===========================================================
        monthView.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int currentItem = wheel.getCurrentItem();
                String text = (String) mMonthAdapter.getItemText(currentItem);
                setItemTextSize(text, mMonthAdapter);
            }
        });


        monthView.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                //获取当前的索引
                int currentItem = wheel.getCurrentItem();
                String text = (String) mMonthAdapter.getItemText(currentItem);
                setItemTextSize(text, mMonthAdapter);
                monthStr = text;
                notifyDay();
            }
        });
//===========================日分滚轮监听===========================================================
        dayView.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int currentItem = wheel.getCurrentItem();
                String text = (String) mDayAdapter.getItemText(currentItem);
                setItemTextSize(text, mDayAdapter);
            }
        });


        dayView.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                //获取当前的索引
                int currentItem = wheel.getCurrentItem();
                String text = (String) mDayAdapter.getItemText(currentItem);
                setItemTextSize(text, mDayAdapter);
                dataStr = text;
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

package com.ts.fmxt.ui.user.view;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.widget.TextView;

import com.squareup.okhttp.Request;
import com.ts.fmxt.R;
import com.ts.fmxt.ui.ItemAdapter.WheelAdapter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import http.data.TableList;
import http.data.UserCityEntity;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.helper.ToastHelper;
import widget.popup.BaseEventPopup;
import widget.wheelview.OnWheelChangedListener;
import widget.wheelview.OnWheelScrollListener;
import widget.wheelview.WheelView;


/**
 * 常居住地
 */
public class PopupWheelAddressView extends BaseEventPopup implements OnClickListener {

    private static int maxsize = 18;//最大字体

    private static int minsize = 14;//最小字体
    private int mYearCurrentIndex, mMonthCurrentIndex, mDayCurrentIndex;
    private WheelListener mWheelListener;

    List<Object> data = new ArrayList<>();
    List<String> mProvinceArrayList = new ArrayList<>();
    List<String> mCityArrayList = new ArrayList<>();
    private WheelAdapter mProvinceAdapter, mCityAdapter;

    private WheelView provinceView, cityView;
    private String provinceStr, cityStr;

    public PopupWheelAddressView(Activity context) {
        super(context);


        getView().findViewById(R.id.tv_finish).setOnClickListener(this);
        getView().findViewById(R.id.tv_clean).setOnClickListener(this);


        provinceView = (WheelView) getView().findViewById(R.id.year);
        provinceView.setVisibility(View.VISIBLE);
        provinceView.setVisibleItems(5);


        cityView = (WheelView) getView().findViewById(R.id.month);
        cityView.setVisibleItems(5);
        cityView.setVisibility(View.VISIBLE);
        cityView.setViewAdapter(mCityAdapter);
        initListener();


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
        super.showPopupWindow();
        getCityListRequest();
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
                    mWheelListener.completeCall(provinceStr,cityStr, 3);
                }
                break;
            case R.id.tv_clean:
                mWheelListener.clearCall(3);
                break;

        }
    }


    private void notifyCity(int position) {
        UserCityEntity info = (UserCityEntity) data.get(position);
        provinceStr = info.getName();
        cityStr = (String) info.getList().get(0);
        mCityArrayList.clear();
        mCityArrayList.addAll(info.getList());
        mCityAdapter = new WheelAdapter(getContext(), mCityArrayList, mMonthCurrentIndex, maxsize, minsize);
        cityView.setViewAdapter(mCityAdapter);
        cityView.setCurrentItem(0);
    }

    private void initListener() {
        //===========================省分滚轮监听===========================================================
        provinceView.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int currentItem = wheel.getCurrentItem();
                String text = (String) mProvinceAdapter.getItemText(currentItem);
                setItemTextSize(text, mProvinceAdapter);
            }
        });
        provinceView.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                int currentItem = wheel.getCurrentItem();
                String text = (String) mProvinceAdapter.getItemText(currentItem);
                setItemTextSize(text, mProvinceAdapter);
                notifyCity(currentItem);
                provinceStr = text;
            }
        });

//===========================市滚轮监听===========================================================
        cityView.addChangingListener(new OnWheelChangedListener() {
            @Override
            public void onChanged(WheelView wheel, int oldValue, int newValue) {
                int currentItem = wheel.getCurrentItem();
                String text = (String) mCityAdapter.getItemText(currentItem);
                setItemTextSize(text, mCityAdapter);
            }
        });


        cityView.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(WheelView wheel) {

            }

            @Override
            public void onScrollingFinished(WheelView wheel) {
                int currentItem = wheel.getCurrentItem();
                String text = (String) mCityAdapter.getItemText(currentItem);
                setItemTextSize(text, mCityAdapter);
                cityStr = text;
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

    private void getCityListRequest(){
        OkHttpClientManager.postAsyn(HttpPathManager.HOST+HttpPathManager.CITYLIST, new OkHttpClientManager.ResultCallback<String>() {
            @Override
            public void onError(Request request, Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(String u) {
                TableList tableList = new TableList();
                try {
                    JSONObject js = new JSONObject(u);
                    if (!js.isNull("statsMsg")) {
                        JSONObject json = js.optJSONObject("statsMsg");
                        String stats = json.getString("stats");
                        String msg = json.getString("msg");
                        if (stats.equals("1")) {
                            if (!js.isNull("areas")) {
                                JSONArray array = js.optJSONArray("areas");
                                for (int i = 0; i < array.length(); i++) {
                                    tableList.getArrayList().add(new UserCityEntity(array.optJSONObject(i)));
                                }
                            }
                            if (tableList != null) {
                                data.clear();
                                data.addAll(tableList.getArrayList());
                                for (int i = 0; i < tableList.getArrayList().size(); i++) {
                                    UserCityEntity info = (UserCityEntity) tableList.getArrayList().get(i);
                                    mProvinceArrayList.add(info.getName());
                                }
                                mProvinceAdapter = new WheelAdapter(getContext(), mProvinceArrayList, mYearCurrentIndex, maxsize, minsize);
                                provinceView.setViewAdapter(mProvinceAdapter);
                                notifyCity(0);
                            }

                        } else {
                            ToastHelper.toastMessage(getContext(), msg);
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

    }


}

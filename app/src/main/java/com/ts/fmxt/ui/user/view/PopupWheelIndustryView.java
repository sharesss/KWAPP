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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import http.data.TableList;
import http.data.UserIndustryEntity;
import http.manager.HttpPathManager;
import http.manager.OkHttpClientManager;
import utils.StringUtils;
import utils.helper.ToastHelper;
import widget.popup.BaseEventPopup;
import widget.wheelview.OnWheelChangedListener;
import widget.wheelview.OnWheelScrollListener;
import widget.wheelview.WheelView;


/**
 * 行业
 */
public class PopupWheelIndustryView extends BaseEventPopup implements OnClickListener  {

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
    private String money,moneys,id;

    public PopupWheelIndustryView(Activity context, String money) {
        super(context);
        this.moneys = money;
        getView().findViewById(R.id.tv_finish).setOnClickListener(this);
        getView().findViewById(R.id.tv_clean).setOnClickListener(this);
        getIndustryRequest();

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
                    mWheelListener.completeCall(money,id, 2);

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
                UserIndustryEntity info = (UserIndustryEntity) data.get(currentItem);
                money = info.getName();
                id =  info.getId()+"";


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

    private void getIndustryRequest(){
        Map<String, String> staff = new HashMap<String, String>();
        staff.put("dataType", "1");
        OkHttpClientManager.postAsyn(HttpPathManager.HOST + HttpPathManager.FINDBASICINFO,
                new OkHttpClientManager.ResultCallback<String>() {

                    @Override
                    public void onError(Request request, Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onResponse(String result) {
                        TableList tableList = new TableList();
                        try {
                            JSONObject js = new JSONObject(result);
                            if (!js.isNull("statsMsg")) {
                                JSONObject json = js.optJSONObject("statsMsg");
                                String stats = json.getString("stats");
                                String msg = json.getString("msg");
                                if (stats.equals("1")) {
                                    if (!js.isNull("infoBebans")) {
                                        JSONArray array = js.optJSONArray("infoBebans");
                                        for (int i = 0; i < array.length(); i++) {
                                            tableList.getArrayList().add(new UserIndustryEntity(array.optJSONObject(i)));
                                        }
                                    }
                                    if (tableList != null) {
                                        data.clear();
                                        data.addAll(tableList.getArrayList());
                                        for(int i=0;i<tableList.getArrayList().size();i++){
                                            if(!StringUtils.isEmpty(moneys)){
                                                if(moneys.equals(tableList.getArrayList().get(i))){
                                                    mCurrentIndex=i;
                                                }

                                            }
                                            UserIndustryEntity info = (UserIndustryEntity) tableList.getArrayList().get(i);
                                            mArrayList.add(info.getName());
                                        }
                                        mAdapter = new WheelAdapter(getContext(), mArrayList, mCurrentIndex, maxsize, minsize);

                                        mWheelView = (WheelView) getView().findViewById(R.id.year);
                                        mWheelView.setVisibility(View.VISIBLE);
                                        initListener();
                                        mWheelView.setViewAdapter(mAdapter);
                                        mWheelView.setCurrentItem(mCurrentIndex);
                                        UserIndustryEntity info = (UserIndustryEntity) data.get(0);
                                        money = info.getName();
                                        id =  info.getId()+"";
                                    }

                                } else {
                                    ToastHelper.toastMessage(getContext(), msg);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, staff
        );


    }

}

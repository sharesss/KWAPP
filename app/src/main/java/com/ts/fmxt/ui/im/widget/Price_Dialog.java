package com.ts.fmxt.ui.im.widget;

import android.app.Activity;
import android.app.Dialog;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.ts.fmxt.R;


/**
 * Created by liangzhenxiong on 2017/10/11.
 */

public class Price_Dialog {
    private int floorprice;
    private int rangeprice;
    private TextView tv_price;
    private long time;

    public Price_Dialog() {

    }

    public interface PostLinstener {
        void post(int price);
    }

    private int thisprice;
    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1) {
                Message message = handler.obtainMessage(1);
                handler.sendMessageDelayed(message, 400);
                thisprice = thisprice + Price_Dialog.this.rangeprice;
                tv_price.setText(String.format("¥ %1$d.00", thisprice));
            } else if (msg.what == 0) {
                Message message = handler.obtainMessage(0);
                handler.sendMessageDelayed(message, 400);
                if (thisprice <= Price_Dialog.this.floorprice) {
                    return;
                }
                thisprice = thisprice - Price_Dialog.this.rangeprice;
                tv_price.setText(String.format("¥ %1$d.00", thisprice));
            }
        }
    };

    public void show(Activity activity, int floorprice, int rangeprice, final PostLinstener postLinstener) {
        if (activity == null) {
            return;
        }
        if (floorprice < 0) {
            floorprice = 0;
        }
        if (rangeprice < 0) {
            rangeprice = 1;
        }
        this.floorprice = floorprice;
        this.rangeprice = rangeprice;
        Dialog dialog = new Dialog(activity, R.style.DialogStyle);
        dialog.setContentView(R.layout.price_dialog_layout);
        dialog.setCancelable(true);
        thisprice = floorprice;
        tv_price = (TextView) dialog.findViewById(R.id.tv_price);
        tv_price.setText(String.format("¥ %1$d.00", floorprice));
        TextView tv_range = (TextView) dialog.findViewById(R.id.tv_range);
        TextView tv_floor = (TextView) dialog.findViewById(R.id.tv_floor);
        tv_range.setText(String.format("加价幅度 ¥：%1$d", this.rangeprice));
        tv_floor.setText(String.format("起拍价 ¥：%1$d", floorprice));


        TextView tv_del = (TextView) dialog.findViewById(R.id.tv_del);
        TextView tv_add = (TextView) dialog.findViewById(R.id.tv_add);
        TextView tv_post = (TextView) dialog.findViewById(R.id.tv_post);
        tv_del.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    handler.removeCallbacksAndMessages(null);
                    if (System.currentTimeMillis() - time > 300) {
                        return true;
                    }
                    if (thisprice <= Price_Dialog.this.floorprice) {
                        return true;
                    }
                    thisprice = thisprice - Price_Dialog.this.rangeprice;
                    tv_price.setText(String.format("¥ %1$d.00", thisprice));
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.sendEmptyMessageDelayed(0, 800);
                    time = System.currentTimeMillis();
                }
                return true;
            }
        });
        tv_add.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    handler.removeCallbacksAndMessages(null);
                    if (System.currentTimeMillis() - time > 300) {
                        return true;
                    }
                    thisprice = thisprice + Price_Dialog.this.rangeprice;
                    tv_price.setText(String.format("¥ %1$d.00", thisprice));
                } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    handler.sendEmptyMessageDelayed(1, 800);
                    time = System.currentTimeMillis();
                }
                return true;
            }
        });
        tv_post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (postLinstener != null) {
                    postLinstener.post(thisprice);
                }
            }
        });
        Window dialogWindow = dialog.getWindow();
        try {
            dialogWindow.setGravity(Gravity.BOTTOM);
            WindowManager.LayoutParams lp = dialogWindow.getAttributes();
            lp.width = ViewGroup.MarginLayoutParams.MATCH_PARENT;
            dialogWindow.setAttributes(lp);
            dialog.show();
        } catch (Exception e) {
            // TODO: handle exception
        }
    }
}

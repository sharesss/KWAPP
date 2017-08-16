package widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ts.fmxt.R;


/**
 *网络异常类
 */
public class NetworkView extends LinearLayout {

    private TextView bt_confirm, tv_msg;
    private NetworkViewListener mCallBack;

    public NetworkView(Context context) {
        super(context);
        initView();
    }

    public NetworkView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public NetworkView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_network_view, this);
        tv_msg = (TextView) findViewById(R.id.tv_msg);
        bt_confirm = (TextView) findViewById(R.id.bt_confirm);
        bt_confirm.setOnClickListener(new OnClickListener(){

            @Override
            public void onClick(View v) {
                if (mCallBack != null)
                    mCallBack.networkErrorReload();
            }
        });
    }

    public void setMsgText(String text) {
        tv_msg.setText(text);
    }

    public void setButtonText(String text) {
        bt_confirm.setText(text);
        bt_confirm.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCallBack != null)
                    mCallBack.networkErrorReload();
            }
        });
    }

    public void setmCallBack(NetworkViewListener mCallBack) {
        this.mCallBack = mCallBack;
    }

    public interface NetworkViewListener {
        public void networkErrorReload();
    }
}

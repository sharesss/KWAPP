package widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ts.fmxt.R;


/**
 * 空数据显示View
 */
public class EmptyView extends RelativeLayout {

    private TextView tv_empty;
    private ImageView iv_empty;


    public EmptyView(Context context) {
        super(context);
        initView();
    }

    public EmptyView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public EmptyView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.widget_empty_view, this);
        iv_empty = (ImageView) findViewById(R.id.iv_empty);
        tv_empty = (TextView) findViewById(R.id.tv_empty);
    }

    public void setEmptyResource(int resourceId) {
        iv_empty.setImageResource(resourceId);
    }

    public void setEmptyText(int resourceId) {
        tv_empty.setText(getResources().getString(resourceId));
    }
    public void setEmptyText(String text) {
        tv_empty.setText(text);
    }


}

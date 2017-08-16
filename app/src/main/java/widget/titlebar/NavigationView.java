package widget.titlebar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ts.fmxt.R;


public class NavigationView extends RelativeLayout {

    public TextView tv_title, iv_back, tv_right, ivBacks;
    private ImageView iv_rigth;
    private ImageView ivRigth2;
    private RelativeLayout app_widget;

    public NavigationView(Context context) {
        super(context);
        initNavigation();
    }

    public NavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initNavigation();
    }

    public NavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initNavigation();
    }

    private void initNavigation() {
        LayoutInflater.from(getContext()).inflate(R.layout.navigation_view, this);
        iv_back = (TextView) findViewById(R.id.iv_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_right = (TextView) findViewById(R.id.tv_right);
        iv_rigth = (ImageView) findViewById(R.id.iv_rigth);
        ivRigth2 = (ImageView) findViewById(R.id.iv_rigth2);

        ivBacks = (TextView) findViewById(R.id.iv_backs);

        app_widget = (RelativeLayout) findViewById(R.id.app_widget);
    }

    public void setTitle(int resourcesId) {
        tv_title.setText(getResources().getString(resourcesId));
    }

    public void setTitle(String str) {
        tv_title.setText(str);
    }

    public void setTitle(int resourcesId, View.OnClickListener onClickListener) {
        tv_title.setText(getResources().getString(resourcesId));
        if (iv_back != null)
            iv_back.setOnClickListener(onClickListener);

    }

    public void setTitle(String str, View.OnClickListener onClickListener) {
        tv_title.setText(str);
        if (iv_back != null)
            iv_back.setOnClickListener(onClickListener);

    }

    public void setLeft(int dra_id, View.OnClickListener onClickListener) {

        ivBacks.setBackgroundResource(dra_id);
        if (ivBacks != null)
            ivBacks.setOnClickListener(onClickListener);

    }

    public void showRightButtom(int resourcesId, View.OnClickListener onClickListener) {
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(getResources().getString(resourcesId));
        tv_right.setTextColor(getResources().getColor(R.color.white));
        if (onClickListener != null)
            tv_right.setOnClickListener(onClickListener);

    }
    public void showRightButtom(int resourcesId,int textColor, View.OnClickListener onClickListener) {
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(getResources().getString(resourcesId));
        tv_right.setTextColor(getResources().getColor(textColor));
        if (onClickListener != null)
            tv_right.setOnClickListener(onClickListener);

    }

    public void showRightButtonTextAndDrawable(int draId, String text) {
        tv_right.setCompoundDrawablesWithIntrinsicBounds(getResources().getDrawable(draId), null, null, null);
        tv_right.setCompoundDrawablePadding(10);
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(text);

    }

    public void showRightButtomAndTextColor(int resourcesId, int textColor, View.OnClickListener onClickListener) {
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setTextColor(getResources().getColor(textColor));
        tv_right.setText(getResources().getString(resourcesId));
        if (onClickListener != null)
            tv_right.setOnClickListener(onClickListener);

    }

    public void showRightImage(int resourcesId, View.OnClickListener onClickListener) {
        iv_rigth.setVisibility(View.VISIBLE);
        iv_rigth.setImageResource(resourcesId);
        if (onClickListener != null)
            iv_rigth.setOnClickListener(onClickListener);

    }

    public void showRightImages(int resourcesId, View.OnClickListener onClickListener) {
        ivRigth2.setVisibility(View.VISIBLE);
        ivRigth2.setImageResource(resourcesId);
        if (onClickListener != null)
            ivRigth2.setOnClickListener(onClickListener);

    }

    public void setRightColor(int color) {
        tv_right.setTextColor(getResources().getColor(color));
    }

    public TextView getIv_back() {
        return iv_back;
    }

    public void setBackground(int resourcesId) {
        app_widget.setBackgroundResource(resourcesId);
    }

    public void hiddelBack() {
        iv_back.setVisibility(View.GONE);
    }


    public TextView getTv_title() {
        return tv_title;
    }


}

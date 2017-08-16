package widget.titlebar;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ts.fmxt.R;

import widget.viewbadger.BadgeView;


public class ImageNavigationView extends RelativeLayout implements View.OnClickListener {

    private TextView tv_title;
    private ImageCallBack mImageCallBack;
    private ImageView iv_right;
    private PopupWindow popupWindow;
    private View view;
    private int widthPixels;
    public TextView ivBack;
    private ImageView iv_line;

    public ImageNavigationView(Context context) {
        super(context);
        initNavigation();
    }

    public ImageNavigationView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initNavigation();
    }

    public ImageNavigationView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initNavigation();
    }

    private void initNavigation() {
        LayoutInflater.from(getContext()).inflate(R.layout.image_navigation_view, this);
        tv_title = (TextView) findViewById(R.id.tv_title);
        findViewById(R.id.iv_title_left).setOnClickListener(this);

        iv_line = (ImageView) findViewById(R.id.iv_line);
        ivBack = (TextView) findViewById(R.id.iv_back);

        iv_right = (ImageView) findViewById(R.id.iv_right);
        iv_right.setOnClickListener(this);

    }

    public void GoneBack() {
        ivBack.setVisibility(GONE);
    }

    public void setTitle(int resourcesId) {
        tv_title.setText(getResources().getString(resourcesId));
    }

    public void setTitle(String title) {
        tv_title.setText(title);
    }

    @Override
    public void onClick(View v) {
        if (mImageCallBack == null)
            return;
        switch (v.getId()) {
//            case R.id.iv_left:
//                mImageCallBack.onClickImage(0);
//                break;
            case R.id.iv_right:
                mImageCallBack.onClickImage(1);
                break;


        }
    }

    public void setmImageCallBack(ImageCallBack mImageCallBack) {
        this.mImageCallBack = mImageCallBack;
    }

    public void hiddelRigth() {
        findViewById(R.id.iv_right).setVisibility(View.GONE);
    }

    public void hiddelLeft() {
        findViewById(R.id.iv_title_left).setVisibility(View.GONE);
    }


    public void showLeft(int dra_id, View.OnClickListener OnClickListener) {
        ImageView ImageView = (android.widget.ImageView) findViewById(R.id.iv_title_left);
        ImageView.setImageResource(dra_id);
        ImageView.setVisibility(VISIBLE);
        ImageView.setOnClickListener(OnClickListener);
    }

    public void showRight(int dra_id, View.OnClickListener OnClickListener) {
        iv_right.setImageResource(dra_id);
        iv_right.setVisibility(VISIBLE);
        iv_right.setOnClickListener(OnClickListener);
    }

    public void showRigthTag(boolean flg) {
        findViewById(R.id.tv_num).setVisibility(flg ? View.VISIBLE : View.GONE);
    }
    BadgeView mBadgeView=null;
    public void showRigthNum(int num) {
        if(mBadgeView==null)
           mBadgeView=new BadgeView(getContext(),findViewById(R.id.tv_num));
        mBadgeView.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);
        mBadgeView.setTextSize(10);
        mBadgeView.setText(String.valueOf(num));
        if(num>0)
             mBadgeView.show();
          else
            mBadgeView.hide();


    }

    public void setLine(int color) {
        iv_line.setBackgroundColor(color);
    }

    public interface ImageCallBack {
        public void onClickImage(int type);
    }


}

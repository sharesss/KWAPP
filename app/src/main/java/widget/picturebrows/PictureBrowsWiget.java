package widget.picturebrows;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Scroller;

import com.ts.fmxt.FmxtApplication;
import com.ts.fmxt.R;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import utils.helper.UIHelper;
import widget.image.FMRadiusNetImageView;


public class PictureBrowsWiget extends RelativeLayout {


    private int dotsBottomMargin = 5; //点veiw的BottomMargin
    private int currentPosition = 0; // 当前页面的位置
    private int skipLongTime = 1; // 自动滚动页面的间隔时间
    private boolean isAutoPlaying; // 自动播放
    private boolean isOneshot = false;    //是否只播放一次，false：需要多次播放，循环执行；true：只播放一次
    private boolean isFirstStart = true;  //是否第一次滚动页面
    private PictureType pictureType = PictureType.NORMAL; // 图片模式

    private ScheduledExecutorService scheduledExecutorService; // 执行自动播放任务
    private ViewPager viewPager;
    private List<PictureBrowsInfo> list = new ArrayList<PictureBrowsInfo>();
    private List<FlagPagerItem> listFlagItem;
    private OnBannerChangeListener onBannerChangeListener;
    private OnBannerItemClickListener onBannerItemClickListener;
    private OrgBannerPagerAdapter adapter;
    private int drawableDots = R.mipmap.point_normal;
    private int higth = 500;

    private LinearLayout dotsPage;

    public PictureBrowsWiget(Context context) {
        super(context);
        init();
    }

    public PictureBrowsWiget(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
        init();
    }

    public PictureBrowsWiget(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    //初始化
    private void init() {
        viewPager = new ViewPager(getContext());
        initViewPagerScroll();
        DisplayMetrics dm = getResources().getDisplayMetrics();
        dotsBottomMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dotsBottomMargin, dm);
        PictureBrowsWiget.this.setLayoutParams(new LayoutParams(dm.widthPixels, dm.widthPixels / 2));
        viewPager.setLayoutParams(new LayoutParams(dm.widthPixels, UIHelper.dipToPx(getContext(),higth )));
        viewPager.setId(R.id.controls1);
        addView(viewPager);
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
    }

    //初始化点
    private void initDots() {
        if (dotsPage != null)
            dotsPage.removeAllViews();
        dotsPage = new LinearLayout(getContext());
        LayoutParams lp = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, -1);
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL, -1);
        lp.bottomMargin = dotsBottomMargin;
        dotsPage.setLayoutParams(lp);
        addView(dotsPage);

        int paddingImage = UIHelper.dipToPx(getContext(), 4);
        for (int i = 0; i < adapter.getCount(); i++) {
            ImageView dotItem = new ImageView(getContext());
            dotItem.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            dotItem.setScaleType(ScaleType.MATRIX);
            dotItem.setPadding(paddingImage, paddingImage, paddingImage, paddingImage);
            dotItem.setImageResource(drawableDots);
            dotItem.setEnabled(i != 0); // true为白色可以点击； false为红色不可点击；
            dotItem.setOnClickListener(getOnClickDotListener());
            dotItem.setTag(i);
            dotItem.setTag(i);
            dotsPage.addView(dotItem);
        }
        currentPosition = 0;
    }

    private OnClickListener getOnClickDotListener() {
        return new OnClickListener() {

            @Override
            public void onClick(View v) {
                int position = (Integer) v.getTag();
                setCurrentPage(position);
                setCurrentDot(position);
            }
        };
    }

    private void setCurrentPage(int position) {
        if (position < 0 || position >= adapter.getCount()) {
            return;
        }
        viewPager.setCurrentItem(position);
    }


    private void setCurrentDot(int position) {
        if (position < 0 || position >= adapter.getCount() || dotsPage == null) {
            return;
        }

        for (int i = 0; i < dotsPage.getChildCount(); i++) {
            ImageView iv = (ImageView) dotsPage.getChildAt(i);
            iv.setEnabled(i != position);
        }

        currentPosition = position;
    }

    private void initViewPagerScroll() {
        try {
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            FixedSpeedScroller scroller = new FixedSpeedScroller(viewPager.getContext());
            scroller.setmDuration(skipLongTime);
            mScroller.set(viewPager, scroller);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public void setAdapterByObj(List<Object> list) {
        List<PictureBrowsInfo> arrayList = new ArrayList<PictureBrowsInfo>();
        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                arrayList.add((PictureBrowsInfo) list.get(i));
            }
        }
        setAdapter(arrayList);
    }

    public void setAdapter(List<PictureBrowsInfo> list) {
        if (list == null || list.size() == 0)
            return;
        setList(list);
        initViewPagerAdapter();
    }

    public void setAdapter(String[] arrays) {
        if (arrays == null || arrays.length == 0)
            return;
        List<PictureBrowsInfo> listBanner = new ArrayList<PictureBrowsInfo>();
        for (String arg : arrays) {
            PictureBrowsInfo eb = new PictureBrowsInfo();
            eb.setImageUrl(arg);
            eb.setUrlName("");
            listBanner.add(eb);
        }
        setList(listBanner);
        initViewPagerAdapter();
    }

    public void setAdapter(int[] arrays) {
        if (arrays == null || arrays.length == 0)
            return;
        List<PictureBrowsInfo> listBanner = new ArrayList<PictureBrowsInfo>();
        for (int arg : arrays) {
            PictureBrowsInfo eb = new PictureBrowsInfo();
            eb.setImageUrl(arg);
            eb.setUrlName("");
            listBanner.add(eb);
        }
        setList(listBanner);
        initViewPagerAdapter();
    }

    /**
     * 设置监听滑动时间
     *
     * @param listener
     */
    public void setOnBannaerChangeListener(OnBannerChangeListener listener) {
        this.onBannerChangeListener = listener;
    }

    /**
     * Item点击事件
     *
     * @param listener
     */
    public void setOnBannerItemClickListener(OnBannerItemClickListener listener) {
        this.onBannerItemClickListener = listener;
    }

    /**
     * 初始化ViewPager对象
     */
    private void initViewPagerAdapter() {
        if (listFlagItem == null)
            listFlagItem = new ArrayList<FlagPagerItem>();
        else
            listFlagItem.clear();

        FlagPagerItem item = null;
        for (PictureBrowsInfo banner : getList()) {
            item = new FlagPagerItem();
            FMRadiusNetImageView iv = new FMRadiusNetImageView(getContext());
            iv.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
//			switch(pictureType){
//			case CENTER:
//				iv.setScaleType(ScaleType.CENTER);
//				break;
//			case CENTER_CROP:
//				iv.setScaleType(ScaleType.CENTER_CROP);
//				break;
//			case CENTER_INSIDE:
//				iv.setScaleType(ScaleType.CENTER_INSIDE);
//				break;
//			case FIT_CENTER:
//				iv.setScaleType(ScaleType.FIT_CENTER);
//				break;
//			case FIT_END:
//				iv.setScaleType(ScaleType.FIT_END);
//				break;
//			case FIT_START:
//				iv.setScaleType(ScaleType.FIT_START);
//				break;
//			case FIT_XY:
//				iv.setScaleType(ScaleType.FIT_XY);
//				break;
//			case MATRIX:
//				iv.setScaleType(ScaleType.MATRIX);
//				break;
//			}
            iv.setScaleType(ScaleType.FIT_XY);
            item.setIv(iv);
            listFlagItem.add(item);
        }

        adapter = new OrgBannerPagerAdapter();
        viewPager.setAdapter(adapter);
        viewPager.addOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                setCurrentDot(position);
                if (onBannerChangeListener != null)
                    onBannerChangeListener.onBannerSelected(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                isAutoPlaying = false;
                if (onBannerChangeListener != null)
                    onBannerChangeListener.onBannerScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                isAutoPlaying = true;
                if (onBannerChangeListener != null)
                    onBannerChangeListener.onBannerScrollStateChanged(state);
            }
        });

        if (isAutoPlaying) {
            scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(),
                    getSkipLongTime(), getSkipLongTime(), TimeUnit.SECONDS);
        }
//        if (FMBApplication.ISSHOWDOC)
//            initDots();
    }

    private class ScrollTask implements Runnable {
        public void run() {
            if (isAutoPlaying) {
                sendBannerMsgHandler(SkipBannerModel.flagCurrentBanner);
                return;
            }
            if (isFirstStart) {
                isFirstStart = false;
                isAutoPlaying = true;
                sendBannerMsgHandler(SkipBannerModel.flagCurrentBanner);
            }
        }
    }

    private class OrgBannerPagerAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return getList().size();
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

//        @Override
//        public void destroyItem(ViewGroup container, int position, Object object) {
//            container.removeView(listFlagItem.get(position).getIv());
//        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            // container.removeView(listFlagItem.get(position).getIv());
            container.removeView((View) object);
        }

        public Object getItem(int position) {
            return getList().get(position);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            FlagPagerItem item = listFlagItem.get(position);
            if (!item.isFlag()) {
                FMRadiusNetImageView iv = item.getIv();
                LayoutParams lp = new LayoutParams(
                        LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                iv.setLayoutParams(lp); //使设置好的布局参数应用到控件
                PictureBrowsInfo info = (PictureBrowsInfo) getItem(position);
                if (info.getImageUrl() instanceof String) {
                    iv.loadImage(String.valueOf(((String) info.getImageUrl()).trim()), FmxtApplication.RoundCorners ? 30 : 0, R.mipmap.ic_launcher);
                } else if (info.getImageUrl() instanceof Integer) {
                    iv.setImageResource(Integer.parseInt(String.valueOf(info.getImageUrl())));
                } else {
                    iv.setImageResource(R.mipmap.ic_launcher);
                }

                item.setFlag(true);
                listFlagItem.set(position, item);
                if (onBannerItemClickListener != null) {
                    iv.setOnClickListener(new OnClickListener() {

                        @Override
                        public void onClick(View v) {
                            onBannerItemClickListener.onItemClick(position, v);
                        }
                    });
                }
                container.addView(iv, 0);
            } else {
                container.addView(item.getIv(), 0);
            }
            return item.getIv();
        }

    }

    public interface OnBannerChangeListener {

        public void onBannerSelected(int position);

        public void onBannerScrolled(int position, float positionOffset, int positionOffsetPixels);

        public void onBannerScrollStateChanged(int state);
    }

    public interface OnBannerItemClickListener {

        public void onItemClick(int position, View arg0);
    }

    public List<PictureBrowsInfo> getList() {
        return list;
    }

    private void setList(List<PictureBrowsInfo> list) {
        if (list != null && list.size() > 0) {
            this.list.clear();
            this.list.addAll(list);
        }
    }

    private int getSkipLongTime() {
        return skipLongTime;
    }

    /**
     * 切换时间间隔
     * 单位：s
     *
     * @param
     */
    public void setSkipLongTime(int skipLongTimeInS) {
        this.skipLongTime = skipLongTimeInS;
    }

    private class FlagPagerItem {

        private FMRadiusNetImageView iv;
        private boolean flag = false;

        public FMRadiusNetImageView getIv() {
            return iv;
        }

        public void setIv(FMRadiusNetImageView iv) {
            this.iv = iv;
        }

        public boolean isFlag() {
            return flag;
        }

        public void setFlag(boolean flag) {
            this.flag = flag;
        }
    }

    private enum SkipBannerModel {
        flagCurrentBanner
    }

    private void sendBannerMsgHandler(SkipBannerModel model) {
        Bundle b = new Bundle();
        b.putSerializable("model", model);
        Message msg = handler.obtainMessage();
        msg.setData(b);
        handler.sendMessage(msg);
    }

    private Handler handler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            try {
                SkipBannerModel model = (SkipBannerModel) msg.getData().getSerializable("model");
                switch (model) {
                    case flagCurrentBanner:
                        currentPosition++;
                        if (currentPosition >= adapter.getCount()) {
                            currentPosition = 0;
                        }
                        if (isOneshot) {
                            cancel();
                            return;
                        }
                        setCurrentPage(currentPosition);
                        setCurrentDot(currentPosition);
                        break;
                    default:
                        break;
                }
            } catch (Exception e) {
            }
        }

    };

    public void setAutoPlaying(boolean autoPlaying) {
        this.isAutoPlaying = autoPlaying;
    }

    public void setLayoutParams(int w, int h) {
        removeAllViews();

        PictureBrowsWiget.this.setLayoutParams(new LayoutParams(w, h));
        viewPager.setLayoutParams(new LayoutParams(w, h));
        addView(viewPager);
        invalidate();
    }

    public void setPictureType(PictureType pt) {
        this.pictureType = pt;
    }

    public void setOneShot(boolean oneShot) {
        this.isOneshot = oneShot;
    }

    public void setDotsDrawable(int drawableId) {
        this.drawableDots = drawableId;
    }

    public enum PictureType {
        CENTER, CENTER_CROP, CENTER_INSIDE, FIT_CENTER, FIT_END, FIT_START, FIT_XY, MATRIX, NORMAL
    }

    //清除
    public void cancel() {
        scheduledExecutorService.shutdown();
    }

    public class FixedSpeedScroller extends Scroller {

        private int _duration = 2000;

        public FixedSpeedScroller(Context context) {
            super(context);
        }

        public FixedSpeedScroller(Context context, Interpolator interpolator) {
            super(context, interpolator);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy, int duration) {
            super.startScroll(startX, startY, dx, dy, _duration);
        }

        @Override
        public void startScroll(int startX, int startY, int dx, int dy) {
            super.startScroll(startX, startY, dx, dy, _duration);
        }

        public void setmDuration(int time) {
            _duration = time * 1000;
        }

        public int getmDuration() {
            return _duration;
        }
    }

}

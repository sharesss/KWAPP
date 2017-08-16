package widget.image;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.ts.fmxt.R;

import utils.BitmapFillet;
import widget.image.cache.ImageCacheLoader;


/**
 * @author meteorshower
 *         <p/>
 *         图片异步请求加载类
 */
public class FMRadiusNetImageView extends ImageView {

    private int round;
    private String imageUrl;

    public FMRadiusNetImageView(Context context) {
        super(context);
    }

    public FMRadiusNetImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FMRadiusNetImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * 设置默认图片
     */
    public void setDefaultDrawable(int drawableId) {
        ImageCacheLoader.getInstance().setDefaultDrawable(drawableId);
    }

    /**
     * 设置默认图片
     */
    public void setDefaultDrawable(Drawable drawable) {
        ImageCacheLoader.getInstance().setDefaultDrawable(drawable);
    }

    /**
     * 加载图片
     */
    public void loadImage(String url) {
        setTag(url);
        loadImage(url, R.mipmap.em_empty_photo);
    }

    /**
     * 加载图片 ： 自定义默认图片
     */
    public void loadImage(String url, int defaultDrawable) {
        loadImage(url,20,defaultDrawable);
    }

    /**
     * 加载图片 ： 圆角，自定义默认图片
     */
    public void loadImage(String url, int round, int defaultDrawable) {
        setTag(url);
        DisplayImageOptions options = new DisplayImageOptions.Builder()
                .showStubImage(defaultDrawable)
                .showImageForEmptyUri(R.mipmap.em_empty_photo)
                .showImageOnFail(defaultDrawable)
                .imageScaleType(ImageScaleType.NONE)
                .cacheInMemory(true)
                .cacheOnDisc(true)
                .bitmapConfig(Bitmap.Config.ARGB_8888)  //设置图片的解码类型
                .displayer(new RoundedBitmapDisplayer(round))
                .build();

        ImageCacheLoader.getInstance().loadCacheImage(this, url, options);

    }

    /**
     * 加载圆角图片
     */
    public void loadRoundImage(String url, int round) {
        setTag(url);
        //loadImage(url, round, R.drawable.options_default_image);
    }

//	/** 头像组装url */
//	public void loadImageHost(String url){
//		String loadUrl = String.format("%s%s", HttpPathManager.IMAGE_AVATAR_HOST,url);
//		setTag(loadUrl);
//		ImageCacheLoader.getInstance().loadCacheImage(this, loadUrl);
//	}
//
//	/** 头像组装url round:圆角*/
//	public void loadImageHost(String url, int round){
//		String loadUrl = String.format("%s%s", HttpPathManager.IMAGE_AVATAR_HOST,url);
//		setTag(loadUrl);
//		ImageCacheLoader.getInstance().loadCacheImage(this, loadUrl, round, R.drawable.icon_defalut_avatar);
//	}

    /**
     * 默认Default ： 进度
     */
    public void loadImage(String url, ImageLoadingListener listner) {
        setTag(url);
        ImageCacheLoader.getInstance().loadCacheImage(this, url, listner);
    }
    public void loadImage(String url,boolean flg) {
        setTag(url);
        ImageCacheLoader.getInstance().loadCacheImage(this, url, new ImageLoadingListener(){

            @Override
            public void onLoadingStarted(String imageUri, View view) {

            }

            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

            }

            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap bm) {
                if (getWidth() > 0) {
                    bm= BitmapFillet.fillet(bm, 10, 0);
                    float es = (float) getWidth() / (float)bm.getWidth();
                    int height = (int) (bm.getHeight() * es);
                    ViewGroup.LayoutParams params = getLayoutParams();
                    params.height = height;
                    setLayoutParams(params);
                }
            }

            @Override
            public void onLoadingCancelled(String imageUri, View view) {

            }
        } );
    }

    public void clearCache() {
        ImageCacheLoader.getInstance().clearCacheMermey();
    }

    public int getRound() {
        return round;
    }

    public void setRound(int round) {
        this.round = round;
        ImageCacheLoader.getInstance().setRound(round);
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}

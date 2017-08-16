package widget.image;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import widget.image.cache.ImageCacheLoader;


/**
 * @author meteorshower
 * 
 *         图片异步请求加载类
 * 
 */
public class FMNetImageView extends ImageView {

	private int round;
	private String imageUrl;

	public FMNetImageView(Context context) {
		super(context);
	}

	public FMNetImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public FMNetImageView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	/** 设置默认图片 */
	public void setDefaultDrawable(int drawableId){
		ImageCacheLoader.getInstance().setDefaultDrawable(drawableId);
	}
	
	/** 设置默认图片 */
	public void setDefaultDrawable(Drawable drawable){
		ImageCacheLoader.getInstance().setDefaultDrawable(drawable);
	}
	
	/** 加载图片 */
	public void loadImage(String url) {
		setTag(url);
		ImageCacheLoader.getInstance().loadCacheImage(this, url);
	}
	
	/** 加载图片 ： 自定义默认图片 */
	public void loadImage(String url, int defaultDrawable) {
		setTag(url);
		ImageCacheLoader.getInstance().loadCacheImage(this, url, defaultDrawable);
	}
	
	/** 加载图片 ： 圆角，自定义默认图片 */
	public void loadImage(String url, int round, int defaultDrawable){
		setTag(url);
		ImageCacheLoader.getInstance().loadCacheImage(this, url, round, defaultDrawable);
	}
	
	/** 加载圆角图片 */
	public void loadRoundImage(String url, int round){
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

	/** 默认Default ： 进度 */
	public void loadImage(String url, ImageLoadingListener listner) {
		setTag(url);
		ImageCacheLoader.getInstance().loadCacheImage(this, url, listner);
	}
	
	public void clearCache(){
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

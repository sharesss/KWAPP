package widget.image.cache;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.io.File;

import utils.FileUtils;


/**
 * @author meteorshower 
 * String imageUri = "http://site.com/image.png"; // 网络图片
 * String imageUri = "file:///mnt/sdcard/image.png"; //SD卡图片 
 * String imageUri = "content://media/external/audio/albumart/13"; // 媒体文件夹
 * String imageUri = "assets://image.png"; // assets 
 * String imageUri = "drawable://" + R.drawable.image; // drawable文件
 */
public class ImageCacheLoader {

	private ImageLoader imageLoader = null;
	private DisplayImageOptions.Builder optionsBuilder = null;
	private static ImageCacheLoader mLoaderCache = null;
	
	public ImageCacheLoader(){
		imageLoader = ImageLoader.getInstance();
	}

	public static ImageCacheLoader getInstance() {
		if (mLoaderCache == null)
			mLoaderCache = new ImageCacheLoader();
		return mLoaderCache;
	}

	//初始化ImageCache参数
	public void initImageLoader(Context context, int defaultDrawableId) {
		// 1.new Md5FileNameGenerator() //使用MD5对UIL进行加密命名
		// 2.new HashCodeFileNameGenerator()//使用HASHCODE对UIL进行加密命名
		ImageLoaderConfiguration.Builder configBuilder = new ImageLoaderConfiguration.Builder(context);
		//configBuilder.memoryCacheExtraOptions(480, 800);//即保存的每个缓存文件的最大长宽
		configBuilder.threadPoolSize(10);// 线程池内加载的数量  
		configBuilder.threadPriority(Thread.NORM_PRIORITY - 2);// 线程优先级  
		configBuilder.denyCacheImageMultipleSizesInMemory();
		configBuilder.diskCacheFileNameGenerator(new Md5FileNameGenerator());
		configBuilder.diskCache(new UnlimitedDiskCache(new File(FileUtils.getImageCachePath())));// 自定义缓存路径
		configBuilder.diskCacheSize(50 * 1024 * 1024);// 设置磁盘最大缓存大小
		configBuilder.tasksProcessingOrder(QueueProcessingType.LIFO);
		configBuilder.diskCacheFileCount(100); // 缓存的文件数量
		configBuilder.writeDebugLogs(); // Remove for release app
        
		// Initialize ImageLoader with configuration.
		imageLoader.init(configBuilder.build());
		
		optionsBuilder = new DisplayImageOptions.Builder();
		
		optionsBuilder.imageScaleType(ImageScaleType.IN_SAMPLE_INT);
		optionsBuilder.showImageForEmptyUri(defaultDrawableId);//设置图片Uri为空或是错误的时候显示的图片
		optionsBuilder.showImageOnFail(defaultDrawableId);//加载失败默认图片
		optionsBuilder.cacheInMemory(true);//设置下载的图片是否缓存在内存中
		optionsBuilder.cacheOnDisk(true);//是否缓存SD卡
		optionsBuilder.considerExifParams(true);//是否考虑图片旋转
		optionsBuilder.bitmapConfig(Bitmap.Config.RGB_565);
		optionsBuilder.resetViewBeforeLoading(true);// 设置图片在下载前是否重置，复位 
		optionsBuilder.displayer(new FadeInBitmapDisplayer(100));//动画淡入
		
	}
	
	/** 设置默认加载图片 */
	public void setDefaultDrawable(int defaultDrawable){
		if(defaultDrawable > 0){
			optionsBuilder.showImageForEmptyUri(defaultDrawable);
			optionsBuilder.showImageOnFail(defaultDrawable);
		}
	}
	
	/** 设置默认加载图片 */
	public void setDefaultDrawable(Drawable defaultDrawable){
		if(defaultDrawable != null){
			optionsBuilder.showImageForEmptyUri(defaultDrawable);
			optionsBuilder.showImageOnFail(defaultDrawable);
		}
	}
	
	/** 设置圆角弧度 */
	public void setRound(int round){
//		optionsBuilder.displayer(new RoundedBitmapDisplayer(round));//设置图片圆角弧度值
	}
	
	/** 加载图片 */
	public void loadCacheImage(ImageView imageView,String url){
		imageLoader.loadImage(url, optionsBuilder.build(), new ImageCacheLoadingListener(imageView));
	}
	
	/** 加载图片 DefaultDrawable：默认图 */
	public void loadCacheImage(ImageView imageView,String url,int defaultDrawable){
		setDefaultDrawable(defaultDrawable);
		imageLoader.loadImage(url, optionsBuilder.build(), new ImageCacheLoadingListener(imageView));
	}
	
	/** 加载图片 DefaultDrawable：默认图 round：圆角 */
	public void loadCacheImage(ImageView imageView,String url,int round,int defaultDrawable){
		setDefaultDrawable(defaultDrawable);
		setRound(round);
		imageLoader.loadImage(url, optionsBuilder.build(), new ImageCacheLoadingListener(imageView));
	}
	
	/** 加载图片 Listener：动画监听器 */
	public void loadCacheImage(ImageView iv, String url,ImageLoadingListener listener) {
		imageLoader.displayImage(url, iv, optionsBuilder.build(), listener);
	}
	/** 加载图片 Listener：动画监听器 */
	public void loadCacheImage(ImageView iv, String url,DisplayImageOptions optionsBuilder) {
		imageLoader.displayImage(url,iv,optionsBuilder);
	}

	/** 获取异步图片Bitmap */
	public Bitmap loadImageBitmap(String url){
		return imageLoader.loadImageSync(url);
	}
	
	/** 内存回收 */
	public void clearCacheMermey() {
		imageLoader.clearMemoryCache();
	}
	
	private class ImageCacheLoadingListener extends SimpleImageLoadingListener{
		
		private ImageView imageView;
		
		public ImageCacheLoadingListener(ImageView imageView){
			this.imageView = imageView;
		}

		@Override
		public void onLoadingStarted(String imageUri, View view) {
			super.onLoadingStarted(imageUri, view);
			
		}

		@Override
		public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
			super.onLoadingFailed(imageUri, view, failReason);
			
		}

		@Override
		public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
//			super.onLoadingComplete(imageUri, view, loadedImage);
			if(imageUri.equals(String.valueOf(imageView.getTag()))){
				imageView.setImageBitmap(loadedImage);
			}
		}

		@Override
		public void onLoadingCancelled(String imageUri, View view) {
			super.onLoadingCancelled(imageUri, view);
			
		}
		
	}

}

package widget.Share;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;

import com.ts.fmxt.R;

import java.io.ByteArrayOutputStream;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import utils.StringUtils;
import widget.popup.BaseEventPopup;


/**
 * 自定义分享对话框
 */
public class PopupShareView extends BaseEventPopup implements OnClickListener {

    private Platform.ShareParams shareParams;
    private PlatformActionListener platformActionListener;
    private int shareType = Platform.SHARE_WEBPAGE;
    private String title = "疯蜜-投资自己活出自己";
    private String content = "爱TA,就给TA足够的安全感!100万保额免费领取";
    private String logo = "https://mmbiz.qlogo.cn/mmbiz/NodBD9g2VjiaBy2fqByYiaj7jxClzUxbMWQt6Pu6zTvlnBVZmicDEzN9NLqsevQ2ojxpibep60y1LL1Wtu5icLCltmQ/0?wx_fmt=png";
    private String url = "";
    private String wechatMomentsTitle;
    private String wechatTitle = "";
    private String fmTitle = "";


    public PopupShareView(Activity context) {
        super(context);
    }



    @Override
    public View setContentView(LayoutInflater inflater) {
        return inflater.inflate(R.layout.popup_share_view, null);

    }

    @Override
    public void findEventByID() {
        getView().findViewById(R.id.other_cancel).setOnClickListener(this);
        getView().findViewById(R.id.tv_fm).setOnClickListener(this);
        getView().findViewById(R.id.tv_wechat).setOnClickListener(this);
        getView().findViewById(R.id.tv_wechat_moments).setOnClickListener(this);
        getView().findViewById(R.id.app_widget).setOnClickListener(this);

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
    public void onClick(View v) {
        dismiss();
        switch (v.getId()) {
            case R.id.tv_wechat_moments:
                if (!StringUtils.isEmpty(wechatMomentsTitle))
                    title = wechatMomentsTitle;
                sharePlatform(1);
                break;
            case R.id.tv_wechat:
                if (!StringUtils.isEmpty(wechatTitle))
                    title = wechatTitle;
                sharePlatform(0);
                break;
            case R.id.tv_fm:
//                if (!StringUtils.isEmpty(wechatTitle))
//                    title = fmTitle;
//                shareFm();
                break;
            default:
                break;
        }
    }

    /**
     * 分享
     *
     * @param position
     */
    private void sharePlatform(int position) {
        Platform plat = null;
        initShareParams();
        plat = ShareSDK.getPlatform(getContext(), getPlatform(position));
        if (platformActionListener != null) {
            plat.setPlatformActionListener(platformActionListener);
        }
        plat.share(shareParams);
    }
    /**
     * 分享本APP
     *
     */
    private void shareFm(){
//        Intent intent = new Intent(getContext(), SearchActivity.class);
//        intent.putExtra("type",3);
//        getContext().startActivityForResult(intent, 3);

    }
    /**
     * 初始化分享参数
     *
     * @param
     */
    public void initShareParams() {
        Platform.ShareParams sp = new Platform.ShareParams();
        //sp.setShareType(Platform.SHARE_TEXT);
        sp.setShareType(shareType);
        sp.setTitle(title);
        sp.setText(content);
        sp.setUrl(url);


//        Bitmap bitmap = ImageLoader.getInstance().loadImageSync(logo);
//        long imageSize = getBitmapsize(bitmap);
//        if (imageSize > 32) {
//            sp.setImageData(imageZoom(bitmap));
//        } else {
            sp.setImageUrl(logo);
//        }
        shareParams = sp;
    }

    private Bitmap imageZoom(Bitmap bitMap) {
        //图片允许最大空间   单位：KB
        double maxSize = 32.00;
        //将bitmap放至数组中，意在bitmap的大小（与实际读取的原文件要大）
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitMap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        //将字节换成KB
        double mid = b.length / 1024;
        //判断bitmap占用空间是否大于允许最大空间  如果大于则压缩 小于则不压缩
        if (mid > maxSize) {
            //获取bitmap大小 是允许最大大小的多少倍
            double i = mid / maxSize;
            //开始压缩  此处用到平方根 将宽带和高度压缩掉对应的平方根倍 （1.保持刻度和高度和原bitmap比率一致，压缩后也达到了最大大小占用空间的大小）
            bitMap = zoomImage(bitMap, bitMap.getWidth() / Math.sqrt(i),
                    bitMap.getHeight() / Math.sqrt(i));

        }
        return bitMap;
    }

    public static Bitmap zoomImage(Bitmap bgimage, double newWidth,
                                   double newHeight) {
        // 获取这个图片的宽和高
        float width = bgimage.getWidth();
        float height = bgimage.getHeight();
        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();
        // 计算宽高缩放率
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap bitmap = Bitmap.createBitmap(bgimage, 0, 0, (int) width,
                (int) height, matrix, true);
        return bitmap;
    }

    public long getBitmapsize(Bitmap bitmap) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR1) {
            return bitmap.getByteCount();
        }
        return bitmap.getRowBytes() * bitmap.getHeight();

    }


    /**
     * 获取平台
     *
     * @param position
     * @return
     */
    private String getPlatform(int position) {
        String platform = "";
        switch (position) {
            case 0:
                platform = "Wechat";
                break;
            case 1:
                platform = "WechatMoments";
                break;
        }
        return platform;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public void setTitle(String title) {
        this.title = title;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setWechatMomentsTitle(String wechatMomentsTitle) {
        this.wechatMomentsTitle = wechatMomentsTitle;
    }

    public void setWechatTitle(String wechatTitle) {
        this.wechatTitle = wechatTitle;
    }

    public void setShareType(int shareType) {
        this.shareType = shareType;
    }
}

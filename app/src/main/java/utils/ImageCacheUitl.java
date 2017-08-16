package utils;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.view.View;

import com.ts.fmxt.FmxtApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import utils.helper.ToastHelper;

public class ImageCacheUitl {
    private static ImageCacheUitl self;

    private ImageCacheUitl() {
    }

    public static ImageCacheUitl getInstetn() {
        if (self == null) {
            self = new ImageCacheUitl();
        }
        return self;
    }

    // 保存图片到手机缓存 没使用
    public Boolean savaImage(String path, Bitmap bitmap) {
        Boolean returnObj = false;
        FileOutputStream fileOutputStream = null;
        File file = null;
        try {
            file = new File(getSDCarPath() + path);
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            file.createNewFile();
            fileOutputStream = new FileOutputStream(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (fileOutputStream != null) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 30, fileOutputStream);
            try {
                fileOutputStream.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
            returnObj = true;
        }
        try {
            if (fileOutputStream != null) {
                fileOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return returnObj;
    }

    public String getSDCarPath() {
        String root = "";
        File sdCardDir = null;
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            sdCardDir = Environment.getExternalStorageDirectory();// 获取SDCard目
            root = sdCardDir.toString() + "/fmb/";
        }
        return root;
    }

    /**
     * 按照比例大小压缩
     *
     * @param srcPath
     * @return
     */
    public static Bitmap getimage(String srcPath) {
        BitmapFactory.Options newOpts = new BitmapFactory.Options();
        //开始读入图片，此时把options.inJustDecodeBounds 设回true了  
        newOpts.inJustDecodeBounds = true;
        Bitmap bitmap = BitmapFactory.decodeFile(srcPath, newOpts);//此时返回bm为空

        newOpts.inJustDecodeBounds = false;
        int w = newOpts.outWidth;
        int h = newOpts.outHeight;
        //现在主流手机比较多是800*480分辨率，所以高和宽我们设置为  
        float hh = 1200f;//这里设置高度为800f  
        float ww = 800f;//这里设置宽度为480f  
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可  
        int be = 1;//be=1表示不缩放  
        if (w > h && w > ww) {//如果宽度大的话根据宽度固定大小缩放  
            be = (int) (newOpts.outWidth / ww);
        } else if (w < h && h > hh) {//如果高度高的话根据宽度固定大小缩放  
            be = (int) (newOpts.outHeight / hh);
        }
        if (be <= 0)
            be = 1;
        newOpts.inSampleSize = be;//设置缩放比例  
        //重新读入图片，注意此时已经把options.inJustDecodeBounds 设回false了  
        bitmap = BitmapFactory.decodeFile(srcPath, newOpts);
        return compressImage(bitmap, srcPath);//压缩好比例大小后再进行质量压缩
    }

    /**
     * 质量压缩
     *
     * @param image
     * @return
     */
    public static Bitmap compressImage(Bitmap image, String srcPath) {
        savaImagePath(image, srcPath);
        return image;
    }

    private static void savaImagePath(Bitmap bitmap, String path) {

        File file = new File(path);
        try {
            FileOutputStream out = new FileOutputStream(file);
            if (bitmap.compress(Bitmap.CompressFormat.PNG, 100, out)) {
                out.flush();
                out.close();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block  
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block  
            e.printStackTrace();
        }
    }

    /**
     * 加载本地图片
     *
     * @param url
     * @return
     */

    public static Bitmap getLoacalBitmap(String url) {
        try {
            FileInputStream fis = new FileInputStream(url);
            return BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static byte[] getBytes(InputStream is) throws IOException {
        ByteArrayOutputStream outstream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024]; // 用数据装  
        int len = -1;
        while ((len = is.read(buffer)) != -1) {
            outstream.write(buffer, 0, len);
        }
        outstream.close();

        // 关闭流一定要记得。  
        return outstream.toByteArray();
    }

    //对view截图并保存到相册
    public static Bitmap screenShot(View view, String fileName) throws Exception {
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        //上面2行必须加入，如果不加如view.getDrawingCache()返回null
        Bitmap bitmap = view.getDrawingCache();

        File appDir = new File(FileUtils.getSDPath(), "Thindo");
        String Name = fileName + ".jpg";
        File file = new File(appDir, Name);

        if (!appDir.exists()) {
            appDir.mkdir();
        }


        try {
            FileOutputStream fos = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            scanPhoto(file.getPath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bitmap;

    }

    private static void scanPhoto(String imgFileName) {
        Intent mediaScanIntent = new Intent(
                Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File file = new File(imgFileName);
        Uri contentUri = Uri.fromFile(file);
        mediaScanIntent.setData(contentUri);
        FmxtApplication.getContext().sendBroadcast(mediaScanIntent);
        ToastHelper.toastMessage(FmxtApplication.getContext(), "保存成功,路径:" + imgFileName);
    }

}

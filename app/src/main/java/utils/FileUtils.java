package utils;

import android.os.Environment;

import com.ts.fmxt.FmxtApplication;

import java.io.File;


public class FileUtils {

    // SD卡路径
    public static String getSDPath() {
        String DataPath;
        if (CheckSDExist() == true)
            DataPath = Environment.getExternalStorageDirectory().toString();
        else
            DataPath = FmxtApplication.getContext().getFilesDir().getPath();
        ;
        return DataPath;
    }

    // 获得应用文件夹路径
    public static String getRootPath() {
        return FileUtils.getSDPath() + File.separator + "Thindo"
                + File.separator;
    }
   public static String getUploadCache(){
       return getRootPath()+"upload_cache";
   }

    /**
     * 图片缓存地路径
     */
    public static String getImageCachePath() {
        return getRootPath() + "ImageLoader" + File.separator;
    }

    /**
     * 崩溃日志缓存路径
     */
    public static String getLogPath() {
        return getRootPath() + "log" + File.separator;
    }

    // 建立应用程序的相关路径
    public static void BuildAppPath() {
        FileUtils.mkdir(getImageCachePath());
    }

    public static boolean isExist(String path) {
        if (path == null)
            return false;
        File file = new File(path);
        return file.exists();
    }

    public static boolean mkdir(String path) {
        File file = new File(path);
        if (file.exists() == false)
            return file.mkdirs();
        return true;
    }

    public static boolean CheckSDExist() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
        return sdCardExist;
    }

    /**
     * 递删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public static void deleteFile(File file) {
        if (file.exists() == false) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    deleteFile(f);
                }
                file.delete();
            }
        }
    }


}

package utils;

import android.graphics.Bitmap;
import android.os.Environment;
import android.util.Log;

import com.ts.fmxt.FmxtApplication;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Randy
 * @Description:描述:下载工具类
 * @date 2015年6月25日 下午9:21:35
 * @Mail randy.yu@chaojiaoyi.com
 */
public class FileUtil {

    public static File updateDir = null;
    public static File updateFile = null;
    /***********
     * 保存升级APK的目录
     ***********/
    public static final String CHAOTRADE_UPDATE_APPLICATION = "chaoTradeUpdateApplication";

    public static boolean isCreateFileSucess;
    public static String SDPATH = Environment.getExternalStorageDirectory()
            + "/thindo/gif/";

    /**
     * 方法描述：createFile方法
     *
     * @param
     * @return
     * @see FileUtil
     */
    public static void createFile(String app_name) {

        if (android.os.Environment.MEDIA_MOUNTED.equals(android.os.Environment.getExternalStorageState())) {
            isCreateFileSucess = true;

            updateDir = new File(FileUtils.getRootPath() + File.separator + CHAOTRADE_UPDATE_APPLICATION + File.separator);
            updateFile = new File(updateDir + File.separator + String.format("%s.apk", app_name));

            if (!updateDir.exists()) {
                updateDir.mkdirs();
            }
            if (!updateFile.exists()) {
                try {
                    updateFile.createNewFile();
                } catch (IOException e) {
                    isCreateFileSucess = false;
                    e.printStackTrace();
                }
            }

        } else {
            isCreateFileSucess = false;
        }
    }


    public static String saveBitmap(Bitmap bm, String picName) {
        File file=null;
        try {
            file = new File(SDPATH, picName + ".png");
            if (!isFileExist("")) {
                File tempf = createSDDir("");
            }

            if (file.exists()) {
                file.delete();
            }
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Log.e("", "已经保存");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file.getPath();
    }

    public static void readInputstream(InputStream InputStream, String picName) {
        try {
            File f = new File(FmxtApplication.getContext().getCacheDir().getAbsolutePath(), picName + ".gif");
            if (f.exists()) {
                f.delete();
            }
            FileOutputStream out = new FileOutputStream(f);
            int readsize = 0;
            byte[] bytes = new byte[1024];

            while ((readsize = InputStream.read(bytes)) != -1) {
                out.write(bytes, 0, readsize);
            }
            out.flush();
            out.close();
            InputStream.close();
            Log.e("", "已经保存" + f.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //读取本地图片流
    public static InputStream getDiskBitmap(int GIF_PATH) {
        InputStream Input = null;
        try {
            File file = new File(FmxtApplication.getContext().getCacheDir().getAbsolutePath(), GIF_PATH + ".gif");
            if (file.exists()) {
                Input = new FileInputStream(file);
            }
        } catch (Exception e) {
        }
        return Input;
    }

    public static File createSDDir(String dirName) throws IOException {
        File dir = new File(SDPATH + dirName);
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {

            System.out.println("createSDDir:" + dir.getAbsolutePath());
            System.out.println("createSDDir:" + dir.mkdir());
        }
        return dir;
    }

    public static boolean isFileExist(String fileName) {
        File file = new File(SDPATH + fileName);
        file.isFile();
        return file.exists();
    }

    public static void delFile(String fileName) {
        File file = new File(SDPATH + fileName);
        if (file.isFile()) {
            file.delete();
        }
        file.exists();
    }

    public static void deleteDir() {
        File dir = new File(SDPATH);
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;

        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete();
            else if (file.isDirectory())
                deleteDir(); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    public static void deleteDirs() {
        File dir = new File(FmxtApplication.getContext().getExternalCacheDir().getAbsolutePath());
        if (dir == null || !dir.exists() || !dir.isDirectory())
            return;

        for (File file : dir.listFiles()) {
            if (file.isFile())
                file.delete();
            else if (file.isDirectory())
                deleteDirs(); // 递规的方式删除文件夹
        }
        dir.delete();// 删除目录本身
    }

    public static boolean fileIsExists(String path) {
        try {
            File f = new File(path);
            if (!f.exists()) {
                return false;
            }
        } catch (Exception e) {

            return false;
        }
        return true;
    }

    /**
     * 递归删除文件和文件夹
     *
     * @param file 要删除的根目录
     */
    public static void DeleteFile(File file) {
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
                    DeleteFile(f);
                }
                file.delete();
            }
        }
    }

    public static boolean isIMFileExist(String SDPATH) {
        String filepath1 = SDPATH + "chaotradefile/job1.png";
        String filepath2 = SDPATH + "chaotradefile/job2.png";
        String filepath3 = SDPATH + "chaotradefile/job3.png";
        String filepath4 = SDPATH + "chaotradefile/audio.mp3";
        File file1 = new File(filepath1);
        File file2 = new File(filepath2);
        File file3 = new File(filepath3);
        File file4 = new File(filepath4);
        if (file1.exists() && file2.exists() && file3.exists() && file4.exists()) {
            return true;
        } else {
            return false;
        }
    }

}
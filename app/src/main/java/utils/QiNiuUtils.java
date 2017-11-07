package utils;

import android.os.Bundle;
import android.util.Log;

import com.qiniu.android.http.ResponseInfo;
import com.qiniu.android.storage.KeyGenerator;
import com.qiniu.android.storage.UpCompletionHandler;
import com.qiniu.android.storage.UploadManager;
import com.qiniu.android.utils.UrlSafeBase64;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import http.Logger;

/**
 * Created by Administrator on 2016/11/17.
 * <p/>
 * 七牛图片上传工具类
 */

public class QiNiuUtils {
    private final Logger logger = new Logger(QiNiuUtils.class.getSimpleName());
    private static QiNiuUtils self;
    private final String serviceUri = "http://7xoor9.com1.z0.glb.clouddn.com/";

    private QiNiuUtils() {
    }

    public static QiNiuUtils getInstance() {
        if (self == null)
            self = new QiNiuUtils();
        return self;
    }

    //图片上传到七牛
    public void uploadImageRequest(final String dirPath, String token) {
        try {
            UploadManager mUploadManager = new UploadManager();
            mUploadManager.put(dirPath, String.format("fm_%s", DateFormatUtils.formatDateStr("yyyyMMddHHmmss")), token,
                    new UpCompletionHandler() {
                        @Override
                        public void complete(String key, ResponseInfo info, JSONObject res) {
                            String uri = String.format("%s%s", serviceUri, key);
                            Bundle bundle = new Bundle();
                            bundle.putString("data", uri);
                            bundle.putString("dirPath", dirPath);
                            ReceiverUtils.sendReceiver(ReceiverUtils.REGISTER_IMAGE_UPLOADER, bundle);
                            Log.i("qiniu", key + ",\r\n " + info + ",\r\n " + res + "uri=" + uri);
                        }
                    }, null);

        } catch (Exception e) {
            e.printStackTrace();
            logger.e("QiNiuUtils" + e.toString());
        }

    }

    class FMKeyGenerator implements KeyGenerator {
        private String dirPath = "";

        public FMKeyGenerator(String dirPath) {
            this.dirPath = dirPath;
        }

        @Override
        public String gen(String key, File file) {
            // 不必使用url_safe_base64转换，uploadManager内部会处理
            // 该返回值可替换为基于key、文件内容、上下文的其它信息生成的文件名
            String path = key + "_._" + new StringBuffer(file.getAbsolutePath()).reverse();
            Log.d("qiniu", path);
            File f = new File(dirPath, UrlSafeBase64.encodeToString(path));
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(f));
                String tempString = null;
                int line = 1;
                try {
                    while ((tempString = reader.readLine()) != null) {
//							System.out.println("line " + line + ": " + tempString);
                        Log.d("qiniu", "line " + line + ": " + tempString);
                        line++;
                    }

                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                } finally {
                    try {
                        reader.close();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }


            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return path;
        }
    }

}

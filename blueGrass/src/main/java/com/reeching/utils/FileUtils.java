package com.reeching.utils;

import android.os.Environment;

import java.io.File;

/**
 * Created by Administrator on 2017/3/10.
 */

public class FileUtils {
    private static String FILE_NAME = "userIcon.jpg";
    public static String PATH_PHOTOGRAPH = "/LXT/";

    public static File getDCIMFile(String filePath, String imageName) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) { // 文件可用
            File dirs = new File(Environment.getExternalStorageDirectory(),
                    "DCIM"+filePath);
            if (!dirs.exists())
                dirs.mkdirs();

            File file = new File(Environment.getExternalStorageDirectory(),
                    "DCIM"+filePath+imageName);
            if (!file.exists()) {
                try {
                    //在指定的文件夹中创建文件
                    file.createNewFile();
                } catch (Exception e) {
                }
            }
            return file;
        } else {
            return null;
        }

    }
}

package com.example.ecrbtb.utils;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.os.Environment.getExternalStoragePublicDirectory;

/**
 * Created by Administrator on 11/2 0002.
 */
public class SDCardUtils {


    private static final String APK_NAME = "app.apk";

    private static String Tag = "SDCardUtils";

    /**
     * 判断SDCard是否可用
     *
     * @return
     */
    public static boolean isSDCardEnable() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED) || !Environment.isExternalStorageRemovable();
    }

    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDCardPath() {
        return Environment.getExternalStorageDirectory().getAbsolutePath()
                + File.separator;
    }

    /**
     * 获取SD卡的剩余容量 单位byte
     *
     * @return
     */
    public static long getSDCardAllSize() {
        if (isSDCardEnable()) {
            StatFs stat = new StatFs(getSDCardPath());
            // 获取空闲的数据块的数量
            long availableBlocks = (long) stat.getAvailableBlocks() - 4;
            // 获取单个数据块的大小（byte）
            long freeBlocks = stat.getAvailableBlocks();
            return freeBlocks * availableBlocks;
        }
        return 0;
    }


    /**
     * 获取指定路径所在空间的剩余可用容量字节数，单位byte
     *
     * @param filePath
     * @return 容量字节 SDCard可用空间，内部存储可用空间
     */
    public static long getFreeBytes(String filePath) {
        // 如果是sd卡的下的路径，则获取sd卡可用容量
        if (filePath.startsWith(getSDCardPath())) {
            filePath = getSDCardPath();
        } else {// 如果是内部存储的路径，则获取内存存储的可用容量
            filePath = Environment.getDataDirectory().getAbsolutePath();
        }
        StatFs stat = new StatFs(filePath);
        long availableBlocks = (long) stat.getAvailableBlocks() - 4;
        return stat.getBlockSize() * availableBlocks;
    }


    /**
     * 获取缓存文件
     *
     * @param context
     * @param fileName
     * @return
     */
    public static File getCacheFile(Context context, String fileName) {
        File file = getExternalCacheDirFile(context);
        if (file == null) {
            file = context.getCacheDir();
        }
        return new File(file, fileName);
    }

    /**
     * 获取缓存文件的路径
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getCachePath(Context context, String fileName) {
        return getCacheFile(context, fileName).getAbsolutePath().toString();
    }


    /**
     * 获取缓存的路径
     *
     * @param context
     * @return
     */
    private static File getExternalCacheDirFile(Context context) {
        if (!isSDCardEnable()) {//当sd卡不可用的时候，返回空
            return null;
        }
        File dataDir = new File(new File(Environment.getExternalStorageDirectory().toString(), "Android"), "data");
        File appCacheDir = new File(new File(dataDir, context.getPackageName()), "cache");
        if (!appCacheDir.exists()) {
            if (!appCacheDir.mkdirs()) {
                appCacheDir.mkdirs();
                //Logger.d(Tag, "Unable to create external cache directory");
                return null;
            }
//            try {
//                new File(appCacheDir, "app.txt").createNewFile();
//            } catch (IOException e) {
//                Logger.d(Tag, "Can't create \".nomedia\" file in application external cache directory");
//            }
        }
        return appCacheDir;
    }


    /**
     * 获取系统存储路径
     *
     * @return
     */
    public static String getRootDirectoryPath() {
        return Environment.getRootDirectory().getAbsolutePath();
    }

    /**
     * @param mkName 图片存储的文件夹名称
     * @return
     */
    private static File getOutputMediaFile(String mkName) {
        File mediaStorageDir = new File(getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), mkName);
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = null;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");
        return mediaFile;
    }


    /**
     * 获取外部存储路径
     *
     * @param type
     * @return
     */
    private static File getFileSaveDir(String type) {
        File file = Environment.getExternalStoragePublicDirectory(type);
        return file;
    }


    /**
     * @param ctx
     * @param mkName
     * @return
     */
    public static File getMediaFile(Context ctx, String mkName) {
        String timeStamp = null;
        File file = null;
        String filePath;
        if (isSDCardEnable()) {
            filePath = getFileSaveDir(Environment.DIRECTORY_PICTURES).getAbsolutePath();
        } else {
            filePath = ctx.getCacheDir().getAbsolutePath();
        }
        filePath += File.separator + mkName + File.separator;
        timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        file = new File(filePath, timeStamp + ".jpg");
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    /**
     * @param ctx
     * @return
     */
    public static File getDownloadApkFile(Context ctx) {
        String filePath;
        if (isSDCardEnable()) {
            filePath = getFileSaveDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        } else {
            filePath = ctx.getCacheDir().getAbsolutePath() + File.separator + "Download";
        }
        File file = new File(filePath, APK_NAME);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * @param ctx
     * @return
     */
    public static String getDownFilePath(Context ctx) {
        String filePath = null;
        if (isSDCardEnable()) {
            filePath = getFileSaveDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        } else {
            filePath = ctx.getCacheDir().getAbsolutePath() + File.separator + "Download";
        }
        return filePath;
    }


    /**
     * 保存JSON数据到文件
     */
    public static void saveJsonToFile(Context context, String fileName, String json) {
        try {
            FileOutputStream fos;
            fos = new FileOutputStream(getCacheFile(context, fileName));
            fos.write(json.getBytes("UTF-8"));
            fos.flush();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 以字节获取到文件
     *
     * @return
     */
    public static byte[] getFileByByte(Context context, String fileName) {
        try {
            File file = new File(getCachePath(context, fileName));
            if (!file.exists()) {
                file.createNewFile();
            }
            FileInputStream fis = new FileInputStream(file);
            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = fis.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            outStream.close();
            fis.close();
            byte[] bytes = outStream.toByteArray();
            return bytes;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取文件的JSON数据
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getFileByJson(Context context, String fileName) {
        byte[] bytes = getFileByByte(context, fileName);
        if (bytes != null)
            return new String(bytes);
        return null;
    }

}

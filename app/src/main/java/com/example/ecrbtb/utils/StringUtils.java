package com.example.ecrbtb.utils;

import android.util.Base64;

import java.io.UnsupportedEncodingException;

/**
 * 字符串工具
 */
public final class StringUtils {

    public static boolean isEmpty(String str) {
        if (str != null && !"".equals(str)) {
            return false;
        } else {
            return true;
        }

    }

    /**
     * 加密 Base64
     *
     * @param str
     */
    public static String getBase64String(String str) {
        String enToStr = null;
        try {
            enToStr = new String(Base64.encode(str.getBytes("utf-8"), Base64.DEFAULT));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return enToStr;
    }

    /**
     * URL 转码
     *
     * @return String
     * @author lifq
     * @date 2015-3-17 下午04:10:28
     */
    public static String getURLEncoderString(String str) {
        String result = "";
        if (null == str) {
            return "";
        }
        try {
            result = java.net.URLEncoder.encode(str, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return result;
    }


    /**
     * @param str
     * @return
     */
    public static String getDecodeBase64String(String str) {
        // 对base64加密后的数据进行解密
        return new String(Base64.decode(str.getBytes(), Base64.DEFAULT));
    }
}

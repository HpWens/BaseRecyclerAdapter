package com.github.baserecycleradapter.utils;

import com.github.promeg.pinyinhelper.Pinyin;

/**
 * Created by jms on 2016/8/10.
 */
public class StringUtils {

    public static String transformPinYin(String character) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < character.length(); i++) {
            buffer.append(Pinyin.toPinyin(character.charAt(i)));
        }
        return buffer.toString();
    }

}

package com.example.ecrbtb.utils;

import java.util.regex.Pattern;

/**
 * 正则表达式
 */
public class RegularUtils {

    private static RegularUtils regular = null;

    // 构造
    private RegularUtils() {
    }

    // 获得对象
    public static RegularUtils getRegular() {
        if (regular == null) {
            regular = new RegularUtils();
        }
        return regular;
    }

    // 账号(6~16位,[A-Z][a-z]_[0-9]组成,第一个字必须为字母)
    public Pattern PatUserName = Pattern.compile("^[a-zA-Z][a-zA-Z\\w]{5,15}$");
    // 昵称(4~12位)
    public Pattern PatUserNickName = Pattern
            .compile("^[a-zA-Z\\d_\\~!@#\\$%\\^&\\*()_\\+\u4e00-\u9fa5]{4,12}$");

    // 弱密码
    public Pattern PatPassWord = Pattern
            .compile("^[a-zA-Z\\d_\\~!@#\\$%\\^&\\*()_\\+]{6,16}$");
    // 强密码
    public Pattern PatPassWord1 = Pattern
            .compile("^(?=.*[a-zA-Z].*)(?=.*[0-9].*)[a-zA-Z\\d_\\~!@#\\$%\\^&\\*()_\\+]{8,16}$");

    // 手势密码
    public Pattern PatGpWord = Pattern.compile("^\\d{8,18}$");
    // 邮箱
    public Pattern PatEmail = Pattern
            .compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
    // 邮箱只显示头尾
    public Pattern PatPartEmail = Pattern.compile("(.{2}).*(@{1})");

    // 手机
    public Pattern PatCellPhoneNum = Pattern.compile("^1[3,4,5,8]{1}\\d{9}$");
    // 手机只显示头尾
    public Pattern patPartPhoneNum = Pattern.compile("(\\d{3})\\d{4}(\\d{4})");

    public Pattern patLetter = Pattern.compile("^[a-zA-Z]{1,}$");
    public Pattern patNumLetter = Pattern.compile("^[a-zA-Z\\d]{1,}$");
    // 身份证号
    public Pattern patIdCard = Pattern
            .compile("(\\d{14}[0-9a-zA-Z])|(\\d{17}[0-9a-zA-Z])");
    // 姓名
    public Pattern patName = Pattern
            .compile("(([\u4E00-\u9FA5]{2,7})|([a-zA-Z]{3,10}))");

    //数字
    public Pattern patNumber = Pattern
            .compile("^[0-9]*$");


    //数字
    public Pattern patUrl = Pattern
            .compile("[a-zA-z]+://[^\\s]*");


    // 是否匹配
    public boolean isMatches(Pattern p, String s) {
        return p.matcher(s).matches();
    }


}

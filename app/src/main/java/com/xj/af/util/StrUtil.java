package com.xj.af.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StrUtil {

    /**
     * 字符串null也算null，返回true
     */
    public static boolean isBlank(String str) {
        if (str == null || str.equals("") || str.equals("null"))
            return true;
        else
            return false;
    }

    /**
     * 字符串null也算null，返回false
     */
    public static boolean isNotBlank(String str) {
        if (isBlank(str))
            return false;
        else
            return true;
    }

    /**
     * 验证邮箱地址
     */
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);
        return m.matches();
    }

    /**
     * 大于len返回ture
     * @param str
     * @param len
     * @return
     */
    public static boolean minLenth(String str, int len) {
        if (isBlank(str))
            return false;
        if (str.length() >= len)
            return true;
        return false;
    }

    /**
     * 没有返回true
     */
    public static boolean xiaoYu(String str, int len) {
        if (isBlank(str))
            return true;
        if (str.length() < len)
            return true;
        return false;
    }

    public static boolean daYu(String str, int len) {
        if (isBlank(str))
            return false;
        if (str.length() > len)
            return true;
        return false;
    }

    /**
     * 验证电话号码
     */
    public static boolean isMobileNO(String mobiles) {
        Pattern p = Pattern
                .compile("^((13[0-9])|(15[^4,\\D])|(170)|(18[0,5-9]))\\d{8}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 判断是否全是数字,如果是返回true
     */
    public static boolean isNumeric(String str) {
        Pattern pattern = Pattern.compile("[0-9.]*");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    /**
     * 判断是否数字、字母、下划线
     */
    public static boolean isNumAndZiMu(String str) {
        Pattern pattern = Pattern.compile("^\\w+$");
        Matcher isNum = pattern.matcher(str);
        if (!isNum.matches()) {
            return false;
        }
        return true;
    }

    public static boolean isBlank(String... str) {
        if (str == null || str.length == 0)
            return true;
        for (String s : str) {
            if (StrUtil.isBlank(s))
                return true;
        }
        return false;
    }

}

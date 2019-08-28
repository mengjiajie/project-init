package com.linlihouse.project.init.util;

import org.codehaus.plexus.util.StringUtils;

/**
 * Created with IntelliJ IDEA.
 * User: ZHJJ
 * Date: 2018/5/6
 * Time: 10:53
 * To change this template use File | Settings | File Templates.
 */
public class StringUtil {
    /**
     * 判断字符串是否为空
     */
    public static boolean isEmpty(String str) {
        if (str != null) {
            str = str.trim();
        }
        return StringUtils.isEmpty(str);
    }

    /**
     * 判断字符串是否非空
     */
    public static boolean isNotEmpty(String str) {
        return !isEmpty(str);
    }

    /**
     * 首字母大写
     *
     * @param string
     * @return
     */
    public static String toUpperCase(String string) {
        char[] charArray = string.toCharArray();
        charArray[0] = toUpperCase(charArray[0]);
        return String.valueOf(charArray);
    }

    /**
     * 字符转成大写
     *
     * @param chars
     * @return
     */
    public static char toUpperCase(char chars) {
        if (97 <= chars && chars <= 122) {
            chars ^= 32;
        }
        return chars;
    }

    /**
     * 首字母大写
     *LowerCase
     * @param string
     * @return
     */
    public static String toLowerCase(String string) {
        char[] charArray = string.toCharArray();
        charArray[0] = toLowerCase(charArray[0]);
        return String.valueOf(charArray);
    }

    /**
     * 字符转成大写
     *
     * @param chars
     * @return
     */
    public static char toLowerCase(char chars) {
        if (65 <= chars && chars <= 90) {
            chars += 32;
        }
        return chars;
    }
}

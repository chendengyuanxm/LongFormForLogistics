package com.unis.longformforlogistics.util;

/**
 * Author:Arnold
 * Date:2019/9/30 14:29
 */
public class TextUtils {

    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str)) {
            return true;
        }

        return false;
    }
}

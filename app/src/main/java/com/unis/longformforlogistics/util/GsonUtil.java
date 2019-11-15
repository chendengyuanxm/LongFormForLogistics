package com.unis.longformforlogistics.util;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Author:Arnold
 * Date:2019/9/30 10:02
 */
public class GsonUtil {

    public static Gson gson;

    public static <T> T fromJson(String jsonStr, Class<T> tClass) {
        try {
            if (gson == null) {
                gson = new Gson();
            }
            return gson.fromJson(jsonStr, tClass);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T fromJson(String jsonStr, Type type) {
        try {
            if (gson == null) {
                gson = new Gson();
            }
            return gson.fromJson(jsonStr, type);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String toJson(Object object) {
        try {
            if (gson == null) {
                gson = new Gson();
            }
            return gson.toJson(object);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}

package com.unis.longformforlogistics.genericTest;

import com.google.gson.Gson;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Author:Arnold
 * Date:2019/10/14 15:42
 */
public abstract class Animal<T>{

    public void parse(String json) {
        ParameterizedType pt  = (ParameterizedType) getClass().getGenericSuperclass();
        Type type = pt.getActualTypeArguments()[0];
        System.out.println("type:" + type.getTypeName());

        Gson gson = new Gson();
//        T t = gson.fromJson(json, new TypeToken<T>(){}.getType());
        T t = gson.fromJson(json, type);
        System.out.println(t.getClass().getName());
        System.out.println(t.toString());
    }
}

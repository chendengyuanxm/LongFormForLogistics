package com.unis.longformforlogistics.genericTest;

/**
 * Author:Arnold
 * Date:2019/10/14 15:44
 */
public class Student<T> extends Person<T>{
    public String name;
    public String age;

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", age='" + age + '\'' +
                '}';
    }

    @Override
    public void scan(T t) {

    }
}

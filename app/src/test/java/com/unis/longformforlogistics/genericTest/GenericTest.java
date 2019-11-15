package com.unis.longformforlogistics.genericTest;

import com.unis.longformforlogistics.model.Form;

import org.junit.Test;

/**
 * Author:Arnold
 * Date:2019/10/14 15:51
 */
public class GenericTest {

    @Test
    public void test() {
        Student<Form> student = new Student<Form>();
//        JSONObject jsonObject = new JSONObject();
//        try {
//            jsonObject.put("name", "Devin");
//            jsonObject.put("age", "18");
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
        String json = "{\"name\":\"Devin\", \"id\":\"18\"}";
        student.parse(json);
    }
}

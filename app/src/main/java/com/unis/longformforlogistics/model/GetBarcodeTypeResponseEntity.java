package com.unis.longformforlogistics.model;

/**
 * Author:Arnold
 * Date:2019/10/12 16:52
 */
public class GetBarcodeTypeResponseEntity extends JsResponse{

    public String type;

    public GetBarcodeTypeResponseEntity(String type) {
        this.type = type;
    }
}

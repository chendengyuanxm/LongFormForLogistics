package com.unis.longformforlogistics.model;

public class ScanResponseEntity extends JsResponse{
    public String barcode;

    public ScanResponseEntity(String barcode) {
        this.barcode = barcode;
    }
}

package com.unis.longformforlogistics.model;

public class ScanRequestEntity extends JsRequest{
    public String id;
    public String name;

    @Override
    public String toString() {
        return "ScanRequestEntity{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}

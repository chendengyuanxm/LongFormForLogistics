package com.unis.longformforlogistics.genericTest;

import javax.ws.rs.Path;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.jaxrs.annotation.NativeAndroid;

/**
 * Author:Arnold
 * Date:2019/10/14 15:43
 */
@Path("")
@Api(value = "person")
public abstract class Person<T> extends Animal<T>{

    @Path("scan")
    @ApiOperation(value = "scan")
    @NativeAndroid
    public abstract void scan(T t);
}

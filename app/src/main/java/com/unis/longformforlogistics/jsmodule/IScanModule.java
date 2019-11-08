package com.unis.longformforlogistics.jsmodule;

import com.unis.longformforlogistics.model.GetBarcodeTypeResponseEntity;
import com.unis.longformforlogistics.model.ScanRequestEntity;
import com.unis.longformforlogistics.model.ScanResponseEntity;
import com.unis.longformlib.CallbackContext;

import javax.ws.rs.Path;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.jaxrs.annotation.NativeAndroid;


/**
 * Author:Arnold
 * Date:2019/10/10 15:06
 */
@Path("/scanModule")
@Api(value = "scanModule")
public interface IScanModule {

    @Path("/scan")
    @NativeAndroid
    @ApiOperation(value = "scan", response = ScanResponseEntity.class)
    void scan(ScanRequestEntity req, @ApiParam(hidden = true) CallbackContext callbackContext);

    @Path("/getBarcodeType")
    @NativeAndroid
    @ApiOperation(value = "getBarcodeType", response = GetBarcodeTypeResponseEntity.class)
    void getBarcodeType(@ApiParam(hidden = true) CallbackContext callbackContext);
}

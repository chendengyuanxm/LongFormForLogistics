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
@Path("/orderModule")
@Api(value = "orderModule")
public interface IOrderModule {

    @Path("/openMap")
    @NativeAndroid
    @ApiOperation(value = "openMap")
    void openMap(@ApiParam(hidden = true) CallbackContext callbackContext);
}

package com.unis.longformforlogistics;

import com.unis.longformforlogistics.jsmodule.impl.JsModuleScan;
import com.unis.longformforlogistics.jsmodule.impl.OrderModule;
import com.unis.longformforlogistics.util.FileUtil;
import com.unis.longformforlogistics.util.SwaggerHelper;

import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.swagger.jaxrs.AndroidReader;
import io.swagger.models.Info;
import io.swagger.models.Swagger;
import io.swagger.util.Json;

/**
 * Author:Arnold
 * Date:2019/9/30 14:15
 */
public class SwaggerTest {

    @Test
    public void generateSwaggerSpec() {
        Info info = new Info()
                .version("V2")
                .title("WMS-APP API SET")
                .description("The API Set of WMS-APP for WISE. We Follow Swagger Specification. Here is the detail:[http://swagger.io/specification/]");
        Swagger swagger = new Swagger().info(info).platform("android");
        AndroidReader reader = new AndroidReader(swagger, null);
        Set clazzes = new HashSet<>();
        clazzes.add(JsModuleScan.class);
        clazzes.add(OrderModule.class);
        swagger = reader.read(clazzes);
        String swaggerSpec = Json.pretty(swagger);
        System.out.println(swaggerSpec);
        try {
            FileUtil.writeFile("swagger.txt", swaggerSpec, false);
        } catch (IOException e) {
            e.printStackTrace();
        }

        SwaggerHelper swaggerHelper = new SwaggerHelper();
        List<Map<String, Object>> nativeServices = swaggerHelper.getNativeService(Json.pretty(swagger));
        System.out.println(nativeServices.toString());
    }
}

package com.unis.longformforlogistics.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.swagger.models.Model;
import io.swagger.models.ModelImpl;
import io.swagger.models.Operation;
import io.swagger.models.Path;
import io.swagger.models.RefModel;
import io.swagger.models.Swagger;
import io.swagger.models.parameters.BodyParameter;
import io.swagger.models.parameters.Parameter;
import io.swagger.models.properties.ArrayProperty;
import io.swagger.models.properties.Property;
import io.swagger.models.properties.RefProperty;
import io.swagger.parser.Swagger20Parser;

/**
 * Author:Arnold
 * Date:2019/9/11 11:16
 */
public class SwaggerHelper {
    private static final String MODULE = "module";
    private static final String METHOD = "method";
    private static final String PARAMETERS = "parameters";
    private static final String RESPONSE = "response";

    public List<Map<String, Object>> getNativeService(String spec) {
        try {
            Swagger swagger = new Swagger20Parser().parse(spec);
            return buildNativeService(swagger);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private List<Map<String, Object>> buildNativeService(Swagger swagger) {
        List<Map<String, Object>> nativeServices = new ArrayList<>();

        if (swagger.getPaths() != null) {
            Collection<Path> paths = swagger.getPaths().values() ;
            for (Path path : paths) {
                Map<String, Object> nativeSerive = Maps.newHashMap();
                Operation operation = path.getAndroid();

                String module = operation.getTags().get(0);
                String method = operation.getOperationId();
                List<Map<String, Object>> parameters = new ArrayList<>();
                Object result = null;

                for (Parameter parameter : operation.getParameters()) {
                    if (parameter instanceof BodyParameter) {
                        parameters.add(buildBodyModel(swagger, (BodyParameter) parameter));
                    }
                }

                if (operation.getResponses().containsKey("default")) {
                    io.swagger.models.Response response = operation.getResponses().get("default");
                    result = handleProperty(swagger, response.getSchema());
                }

                nativeSerive.put(MODULE, module);
                nativeSerive.put(METHOD, method);
                nativeSerive.put(PARAMETERS, parameters);
                nativeSerive.put(RESPONSE, result);

                nativeServices.add(nativeSerive);
            }
        }

        return nativeServices;
    }

    private Map<String, Object> buildBodyModel(Swagger swagger, BodyParameter parameter) {
        Map<String, Object> body = Maps.newHashMap();
        Model schema = parameter.getSchema();
        if (schema instanceof RefModel) {
            String simpleRef = ((RefModel) schema).getSimpleRef();
            Model definitions = swagger.getDefinitions().get(simpleRef);
            definitions.getProperties().entrySet().forEach(e -> body.put(e.getKey(), handleProperty(swagger, e.getValue())));
        } else if (schema instanceof ModelImpl){
            body.put(parameter.getName(), ((ModelImpl) schema).getType());
        } else {
            schema.getProperties().entrySet().forEach(e -> body.put(e.getKey(), handleProperty(swagger, e.getValue())));
        }
        return body;
    }

    private Object handleProperty(Swagger swagger, Property property) {
        if (property == null) {
            return null;
        } else if (property instanceof ArrayProperty) {
            List<Object> items = Lists.newArrayList();
            Object result = handleProperty(swagger, ((ArrayProperty) property).getItems());
            if (result != null) {
                items.add(result);
            }
            return items;
        } else if (property instanceof RefProperty) {
            Map<String, Object> refMap = Maps.newHashMap();
            Model model = swagger.getDefinitions().get(((RefProperty) property).getSimpleRef());
            model.getProperties().entrySet().forEach(e -> refMap.put(e.getKey(), handleProperty(swagger, e.getValue())));
            return refMap;
        } else {
            String type = property.getType();
            if (Objects.equals("integer", type)) {
                return "number";
            }
            if (Objects.equals("date-time", property.getFormat())) {
                return "date";
            }

            return type;
        }
    }
}

package com.task02;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.syndicate.deployment.annotations.lambda.LambdaHandler;
import com.syndicate.deployment.annotations.lambda.LambdaUrlConfig;
import com.syndicate.deployment.model.RetentionSetting;
import com.syndicate.deployment.model.lambda.url.AuthType;
import com.syndicate.deployment.model.lambda.url.InvokeMode;


import java.util.HashMap;
import java.util.Map;

@LambdaHandler(
    lambdaName = "hello_world",
	roleName = "hello_world-role",
	isPublishVersion = false,

	logsExpiration = RetentionSetting.SYNDICATE_ALIASES_SPECIFIED
)


@LambdaUrlConfig(
    authType = AuthType.NONE,  
    invokeMode = InvokeMode.BUFFERED  
)
public class HelloWorld implements RequestHandler<Map<String, Object>, Map<String, Object>> {

    @Override
    public Map<String, Object> handleRequest(Map<String, Object> event, Context context) {
        // Extragerea căii și metodei din eveniment
        String path = (String) event.get("path");
        String method = (String) event.get("httpMethod");

        Map<String, Object> responseMap = new HashMap<>();

        if ("/hello".equals(path) && "GET".equalsIgnoreCase(method)) {
            // Răspuns 200 pentru calea /hello cu metoda GET
            responseMap.put("statusCode", 200);
            responseMap.put("body", "{\"message\": \"Hello from Lambda\"}");
        } else {
            // Răspuns 400 pentru orice alte căi sau metode
            responseMap.put("statusCode", 400);
            responseMap.put("body", String.format(
                "{\"message\": \"Bad request syntax or unsupported method. Request path: %s. HTTP method: %s\"}",
                path != null ? path : "unknown",  // Asigură-te că returnează "unknown" dacă path este null
                method != null ? method : "unknown"  // Asigură-te că returnează "unknown" dacă method este null
            ));
        }

        return responseMap;
	}
}

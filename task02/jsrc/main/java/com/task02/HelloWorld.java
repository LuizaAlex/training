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
        // Retrieve the path and method from the event map
        String path = (String) event.get("path");
        String method = (String) event.get("httpMethod");

        Map<String, Object> responseMap = new HashMap<>();

        if ("/hello".equals(path) && "GET".equalsIgnoreCase(method)) {
            // Return 200 response for /hello path
            responseMap.put("statusCode", 200);
            responseMap.put("body", "{\"message\": \"Hello from Lambda\"}");  // Return a JSON string
        } else {
            // Return 400 response for any other paths or methods
            responseMap.put("statusCode", 400);
            responseMap.put("body", String.format(
                "{\"message\": \"Bad request syntax or unsupported method. Request path: %s. HTTP method: %s\"}",
                path != null ? path : "null",
                method != null ? method : "null"
            ));  // Return a JSON string
        }

        return responseMap;
	}
}

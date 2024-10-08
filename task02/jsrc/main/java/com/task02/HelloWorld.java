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
        // Print the received event for debugging
        System.out.println("Received event: " + event);

        // Extract the path and method based on actual event structure
        String path = (String) event.get("rawPath");
        Map<String, Object> httpContext = (Map<String, Object>) ((Map<String, Object>) event.get("requestContext")).get("http");
        String method = (String) httpContext.get("method");
        
        // Define response map
        Map<String, Object> responseMap = new HashMap<>();
        Map<String, Object> bodyMap = new HashMap<>();

        if ("/hello".equals(path) && "GET".equalsIgnoreCase(method)) {
            bodyMap.put("statusCode", 200);
            bodyMap.put("message", "Hello from Lambda");
            responseMap.put("statusCode", 200);
            responseMap.put("body", bodyMap);
        } else {
            bodyMap.put("statusCode", 400);
            bodyMap.put("message", String.format(
                "Bad request syntax or unsupported method. Request path: %s. HTTP method: %s",
                path != null ? path : "unknown",
                method != null ? method : "unknown"
            ));
            responseMap.put("statusCode", 400);
            responseMap.put("body", bodyMap);
        }

        System.out.println("Response map: " + responseMap);
        return responseMap;
    }
}
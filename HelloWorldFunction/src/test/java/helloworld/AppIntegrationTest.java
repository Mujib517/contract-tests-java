package helloworld;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import org.junit.jupiter.api.Test;
import software.amazon.awssdk.core.SdkBytes;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.lambda.LambdaClient;
import software.amazon.awssdk.services.lambda.model.InvokeRequest;
import software.amazon.awssdk.services.lambda.model.InvokeResponse;
import software.amazon.awssdk.services.lambda.model.LogType;

import java.nio.charset.Charset;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AppIntegrationTest {
    @Test
    public void testLambdaIntegration() {
        App lambda = new App();
        APIGatewayProxyRequestEvent inputEvent = new APIGatewayProxyRequestEvent();
        inputEvent.setHttpMethod("GET");
        inputEvent.setPathParameters(Map.of("id", "1"));

        Context context = new TestContext();
        APIGatewayProxyResponseEvent outputEvent = lambda.handleRequest(inputEvent, context);

        assertEquals(200, outputEvent.getStatusCode());
        assertNotNull(outputEvent.getBody());
    }
}

package helloworld;

import au.com.dius.pact.core.model.Interaction;
import au.com.dius.pact.core.model.PactSource;
import au.com.dius.pact.provider.IProviderVerifier;
import au.com.dius.pact.provider.ProviderInfo;
import au.com.dius.pact.provider.junit5.TestTarget;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyRequestEvent;
import com.amazonaws.services.lambda.runtime.events.APIGatewayProxyResponseEvent;
import kotlin.Pair;
import org.apache.http.entity.ContentType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class AwsLambdaTarget implements TestTarget {

    private App lambda = new App();

    @NotNull
    @Override
    public Map<String, Object> executeInteraction(@Nullable Object o, @Nullable Object o1) {
        APIGatewayProxyRequestEvent inputEvent = new APIGatewayProxyRequestEvent();
        inputEvent.setHttpMethod("GET");
        inputEvent.setPathParameters(Map.of("id", "1"));
        Context context = new TestContext();
        APIGatewayProxyResponseEvent outputEvent = lambda.handleRequest(inputEvent, context);

        var list = new ArrayList<String>();
        list.add("application/json");
        Map<String, Object> map = new HashMap<>();
        map.put("statusCode", outputEvent.getStatusCode());
        map.put("data", outputEvent.getBody());
        map.put("headers", Map.of("Content-Type", list));
        map.put("contentType", ContentType.APPLICATION_JSON);

        return map;
    }

    @NotNull
    @Override
    public ProviderInfo getProviderInfo(@NotNull String s, @Nullable PactSource pactSource) {
        ProviderInfo p = new ProviderInfo();
        p.setName("Products API");
        p.setPath("/api/products/1");
        p.setPort(3000);
        p.setHost("localhost");
        return p;
    }

    @Override
    public boolean isHttpTarget() {
        return true;
    }

    @Nullable
    @Override
    public Pair<Object, Object> prepareRequest(@NotNull Interaction interaction, @NotNull Map<String, ?> map) {
        return null;
    }

    @Override
    public void prepareVerifier(@NotNull IProviderVerifier iProviderVerifier, @NotNull Object o) {

    }
}

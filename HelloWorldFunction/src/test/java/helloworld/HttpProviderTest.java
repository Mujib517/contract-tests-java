package helloworld;

import au.com.dius.pact.provider.junit.Consumer;
import au.com.dius.pact.provider.junit.Provider;
import au.com.dius.pact.provider.junit.State;
import au.com.dius.pact.provider.junit.loader.PactFolder;
import au.com.dius.pact.provider.junit5.HttpTestTarget;
import au.com.dius.pact.provider.junit5.PactVerificationContext;
import au.com.dius.pact.provider.junit5.PactVerificationInvocationContextProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestTemplate;
import org.junit.jupiter.api.extension.ExtendWith;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


@Provider("Products API")
@PactFolder("pacts")
@Consumer("Consumer")
public class HttpProviderTest {

    @BeforeEach
    void setupTestTarget(PactVerificationContext context) {
        System.out.println("setup");
        context.setTarget(new HttpTestTarget("localhost", 3000));
        System.out.println(context.getProviderInfo());
        System.out.println(context.getConsumerName());
    }

    @TestTemplate
    @ExtendWith(PactVerificationInvocationContextProvider.class)
    void pactVerificationTestTemplate(PactVerificationContext context) {
        System.out.println("Verifying");
        context.verifyInteraction();
    }

    @State("Product with ID 1 exists")
    public void getProductWithId() {
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:3000/api/products/1"))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            int statusCode = response.statusCode();
            if (statusCode != 200) throw new Exception("API Call failed");
        } catch (Exception e) {
        }
    }
}

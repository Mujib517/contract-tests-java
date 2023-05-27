package helloworld;

import au.com.dius.pact.consumer.dsl.PactDslJsonBody;
import au.com.dius.pact.consumer.dsl.PactDslWithProvider;
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt;
import au.com.dius.pact.consumer.junit5.PactTestFor;
import au.com.dius.pact.core.model.PactSpecVersion;
import au.com.dius.pact.core.model.RequestResponsePact;
import au.com.dius.pact.core.model.annotations.Pact;
import com.google.gson.Gson;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(PactConsumerTestExt.class)
@PactTestFor(providerName = "Products API", hostInterface = "localhost", port = "3001")
public class ConsumerContractTest {

    @Pact(consumer = "Consumer")
    public RequestResponsePact createPact(PactDslWithProvider builder) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Content-Type", "application/json");

        PactDslJsonBody expectedResponseBody = new PactDslJsonBody()
                .numberType("id", 1)
                .stringType("brand", "Apple")
                .stringType("model", "iPhone 13")
                .booleanValue("inStock", true)
                .numberType("price", 1000);

        var pact = builder
                .given("Product with ID 1 exists")
                .uponReceiving("A request to get product with ID 1")
                .path("/products/1")
                .method("GET")
                .willRespondWith()
                .status(200)
                .headers(headers)
                .body(expectedResponseBody)
                .toPact();

        pact.write("pacts/", PactSpecVersion.V3);

        return pact;
    }

    @Test
    @PactTestFor(pactMethod = "createPact")
    public void testProductApi() throws IOException {
        var httpClient = HttpClientBuilder.create().build();
        String apiUrl = "http://localhost:3001/products/1";
        HttpGet request = new HttpGet(apiUrl);
        var response = httpClient.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertEquals(200, statusCode);
        String responseBody = EntityUtils.toString(response.getEntity());
        var product = new Gson().fromJson(responseBody, Product.class);

        assertEquals(1, product.id);
        assertEquals("Apple", product.brand);
        assertEquals("iPhone 13", product.model);
        assertEquals(1000, product.price);
        assertEquals(true, product.inStock);
    }
}

package Tests;

import Base.BaseTest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;

public class InjectionAttackTest extends BaseTest {

    private final List<String> payloads = Arrays.asList(
            "' OR '1'='1",
            "<script>alert(1)</script>",
            "; DROP TABLE users;",
            "SELECT * FROM users",
            "admin'--",
            "\" OR \"\" = \"",
            "'; EXEC xp_cmdshell('dir'); --"
    );

    private final String postEndpoint = "/users";
    private final String putEndpoint = "/users/{id}";
    private final Map<String, ?> pathParams = Map.of("id", 123);

    private final String token = "token"; // Reuse admin token for input testing

    @Test
    public void testInjectionAttacksOnPostAndPut() {
        RequestSpecification spec = getSpec(token);

        for (String payload : payloads) {
            Map<String, Object> userBody = new HashMap<>();
            userBody.put("name", payload);
            userBody.put("email", "user+" + payload.replaceAll("[^a-zA-Z0-9]", "") + "@example.com");
            userBody.put("password", payload);

            System.out.println("\n---- Testing payload: " + payload + " ----");

            // POST /users
            Response postRes = sendRequest("POST", postEndpoint, spec, userBody, null);
            System.out.println("POST → Status: " + postRes.getStatusCode());
            Assert.assertTrue(postRes.getStatusCode() < 500, "POST caused server error for payload: " + payload);
            Assert.assertFalse(postRes.getStatusCode() == 200 || postRes.getStatusCode() == 201,
                    "POST should not succeed with malicious payload: " + payload);

            // PUT /users/{id}
            Response putRes = sendRequest("PUT", putEndpoint, spec, userBody, pathParams);
            System.out.println("PUT → Status: " + putRes.getStatusCode());
            Assert.assertTrue(putRes.getStatusCode() < 500, "PUT caused server error for payload: " + payload);
            Assert.assertFalse(putRes.getStatusCode() == 200,
                    "PUT should not succeed with malicious payload: " + payload);
        }
    }
}

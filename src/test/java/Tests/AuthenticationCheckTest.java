package Tests;

import Base.BaseTest;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class AuthenticationCheckTest extends BaseTest {

    private final String[] endpoints = {
            "/users",               // GET and POST
            "/users/{id}",          // PUT and DELETE
    };

    private final Map<String, ?> pathParams = Map.of("id", 123);

    @Test
    public void testAllEndpoints_withoutToken_shouldReturnUnauthorizedOrForbidden() {
        System.out.println("---- Testing WITHOUT Token ----");

        RequestSpecification spec = getSpecWithoutAuth();

        for (String endpoint : endpoints) {
            // GET
            Response getRes = sendRequest("GET", endpoint, spec, null, getPathParams(endpoint));
            System.out.println("GET " + endpoint + " → " + getRes.getStatusCode());
            Assert.assertTrue(getRes.getStatusCode() == 401 || getRes.getStatusCode() == 403, "Expected 401/403 for GET " + endpoint);

            // POST
            Response postRes = sendRequest("POST", endpoint, spec, null, getPathParams(endpoint));
            System.out.println("POST " + endpoint + " → " + postRes.getStatusCode());
            Assert.assertTrue(postRes.getStatusCode() == 401 || postRes.getStatusCode() == 403, "Expected 401/403 for POST " + endpoint);

            // PUT
            Response putRes = sendRequest("PUT", endpoint, spec, null, getPathParams(endpoint));
            System.out.println("PUT " + endpoint + " → " + putRes.getStatusCode());
            Assert.assertTrue(putRes.getStatusCode() == 401 || putRes.getStatusCode() == 403, "Expected 401/403 for PUT " + endpoint);

            // DELETE
            Response deleteRes = sendRequest("DELETE", endpoint, spec, null, getPathParams(endpoint));
            System.out.println("DELETE " + endpoint + " → " + deleteRes.getStatusCode());
            Assert.assertTrue(deleteRes.getStatusCode() == 401 || deleteRes.getStatusCode() == 403, "Expected 401/403 for DELETE " + endpoint);
        }
    }

    @Test
    public void testAllEndpoints_withInvalidToken_shouldReturnUnauthorizedOrForbidden() {
        System.out.println("---- Testing with INVALID Token ----");

        RequestSpecification spec = getSpec("invalid_token");

        for (String endpoint : endpoints) {
            Response res = sendRequest("GET", endpoint, spec, null, getPathParams(endpoint));
            System.out.println("GET " + endpoint + " → " + res.getStatusCode());
            Assert.assertTrue(res.getStatusCode() == 401 || res.getStatusCode() == 403, "Expected 401/403 for GET " + endpoint);

            res = sendRequest("POST", endpoint, spec, null, getPathParams(endpoint));
            System.out.println("POST " + endpoint + " → " + res.getStatusCode());
            Assert.assertTrue(res.getStatusCode() == 401 || res.getStatusCode() == 403, "Expected 401/403 for POST " + endpoint);

            res = sendRequest("PUT", endpoint, spec, null, getPathParams(endpoint));
            System.out.println("PUT " + endpoint + " → " + res.getStatusCode());
            Assert.assertTrue(res.getStatusCode() == 401 || res.getStatusCode() == 403, "Expected 401/403 for PUT " + endpoint);

            res = sendRequest("DELETE", endpoint, spec, null, getPathParams(endpoint));
            System.out.println("DELETE " + endpoint + " → " + res.getStatusCode());
            Assert.assertTrue(res.getStatusCode() == 401 || res.getStatusCode() == 403, "Expected 401/403 for DELETE " + endpoint);
        }
    }

    @Test
    public void testAllEndpoints_withExpiredToken_shouldReturnUnauthorizedOrForbidden() {
        System.out.println("---- Testing with EXPIRED Token ----");

        RequestSpecification spec = getSpec("expired_token");

        for (String endpoint : endpoints) {
            Response res = sendRequest("GET", endpoint, spec, null, getPathParams(endpoint));
            System.out.println("GET " + endpoint + " → " + res.getStatusCode());
            Assert.assertTrue(res.getStatusCode() == 401 || res.getStatusCode() == 403, "Expected 401/403 for GET " + endpoint);

            res = sendRequest("POST", endpoint, spec, null, getPathParams(endpoint));
            System.out.println("POST " + endpoint + " → " + res.getStatusCode());
            Assert.assertTrue(res.getStatusCode() == 401 || res.getStatusCode() == 403, "Expected 401/403 for POST " + endpoint);

            res = sendRequest("PUT", endpoint, spec, null, getPathParams(endpoint));
            System.out.println("PUT " + endpoint + " → " + res.getStatusCode());
            Assert.assertTrue(res.getStatusCode() == 401 || res.getStatusCode() == 403, "Expected 401/403 for PUT " + endpoint);

            res = sendRequest("DELETE", endpoint, spec, null, getPathParams(endpoint));
            System.out.println("DELETE " + endpoint + " → " + res.getStatusCode());
            Assert.assertTrue(res.getStatusCode() == 401 || res.getStatusCode() == 403, "Expected 401/403 for DELETE " + endpoint);
        }
    }

    private Map<String, ?> getPathParams(String endpoint) {
        return endpoint.contains("{id}") ? pathParams : null;
    }
}

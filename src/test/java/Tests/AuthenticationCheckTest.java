package Tests;

import BaseClass.BaseTest;
import Utilities.TestDataUtil;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Map;

public class AuthenticationCheckTest extends BaseTest {

    private final String[] endpoints = {
            "/users",               // GET and POST
            "/users/{id}",          // PUT and DELETE
    };

    private final Map<String, Object> pathParams = Map.of("id", 123);

    @Test
    public void testAllEndpoints_withoutToken_shouldReturnUnauthorizedOrForbidden() {
        System.out.println("---- Testing WITHOUT Token ----");

        for (String endpoint : endpoints) {
            // GET
            Response getRes = restRequest.sendRequest("GET", endpoint, null, getPathParams(endpoint), null);
            System.out.println("GET " + endpoint + " → " + getRes.getStatusCode());
            Assert.assertTrue(getRes.getStatusCode() == 401 || getRes.getStatusCode() == 403, "Expected 401/403 for GET " + endpoint);

            // POST
            Response postRes = restRequest.sendRequest("POST", endpoint, null, getPathParams(endpoint), null);
            System.out.println("POST " + endpoint + " → " + postRes.getStatusCode());
            Assert.assertTrue(postRes.getStatusCode() == 401 || postRes.getStatusCode() == 403, "Expected 401/403 for POST " + endpoint);

            // PUT
            Response putRes = restRequest.sendRequest("PUT", endpoint, null, getPathParams(endpoint), null);
            System.out.println("PUT " + endpoint + " → " + putRes.getStatusCode());
            Assert.assertTrue(putRes.getStatusCode() == 401 || putRes.getStatusCode() == 403, "Expected 401/403 for PUT " + endpoint);

            // DELETE
            Response deleteRes = restRequest.sendRequest("DELETE", endpoint, null, getPathParams(endpoint), null);
            System.out.println("DELETE " + endpoint + " → " + deleteRes.getStatusCode());
            Assert.assertTrue(deleteRes.getStatusCode() == 401 || deleteRes.getStatusCode() == 403, "Expected 401/403 for DELETE " + endpoint);
        }
    }

    @Test
    public void testAllEndpoints_withInvalidToken_shouldReturnUnauthorizedOrForbidden() {
        System.out.println("---- Testing with INVALID Token ----");

        Map<String, Object> headers = Map.of("Authorization", "Bearer " + TestDataUtil.getToken("invalid_token"));

        for (String endpoint : endpoints) {
            Response res = restRequest.sendRequest("GET", endpoint, null, getPathParams(endpoint), headers);
            System.out.println("GET " + endpoint + " → " + res.getStatusCode());
            Assert.assertTrue(res.getStatusCode() == 401 || res.getStatusCode() == 403, "Expected 401/403 for GET " + endpoint);

            res = restRequest.sendRequest("POST", endpoint, null, getPathParams(endpoint), headers);
            System.out.println("POST " + endpoint + " → " + res.getStatusCode());
            Assert.assertTrue(res.getStatusCode() == 401 || res.getStatusCode() == 403, "Expected 401/403 for POST " + endpoint);

            res = restRequest.sendRequest("PUT", endpoint, null, getPathParams(endpoint), headers);
            System.out.println("PUT " + endpoint + " → " + res.getStatusCode());
            Assert.assertTrue(res.getStatusCode() == 401 || res.getStatusCode() == 403, "Expected 401/403 for PUT " + endpoint);

            res = restRequest.sendRequest("DELETE", endpoint, null, getPathParams(endpoint), headers);
            System.out.println("DELETE " + endpoint + " → " + res.getStatusCode());
            Assert.assertTrue(res.getStatusCode() == 401 || res.getStatusCode() == 403, "Expected 401/403 for DELETE " + endpoint);
        }
    }

    @Test
    public void testAllEndpoints_withExpiredToken_shouldReturnUnauthorizedOrForbidden() {
        System.out.println("---- Testing with EXPIRED Token ----");

        Map<String, Object> headers = Map.of("Authorization", "Bearer " + TestDataUtil.getToken("expired_token"));

        for (String endpoint : endpoints) {
            Response res = restRequest.sendRequest("GET", endpoint, null, getPathParams(endpoint), headers);
            System.out.println("GET " + endpoint + " → " + res.getStatusCode());
            Assert.assertTrue(res.getStatusCode() == 401 || res.getStatusCode() == 403, "Expected 401/403 for GET " + endpoint);

            res = restRequest.sendRequest("POST", endpoint, null, getPathParams(endpoint), headers);
            System.out.println("POST " + endpoint + " → " + res.getStatusCode());
            Assert.assertTrue(res.getStatusCode() == 401 || res.getStatusCode() == 403, "Expected 401/403 for POST " + endpoint);

            res = restRequest.sendRequest("PUT", endpoint, null, getPathParams(endpoint), headers);
            System.out.println("PUT " + endpoint + " → " + res.getStatusCode());
            Assert.assertTrue(res.getStatusCode() == 401 || res.getStatusCode() == 403, "Expected 401/403 for PUT " + endpoint);

            res = restRequest.sendRequest("DELETE", endpoint, null, getPathParams(endpoint), headers);
            System.out.println("DELETE " + endpoint + " → " + res.getStatusCode());
            Assert.assertTrue(res.getStatusCode() == 401 || res.getStatusCode() == 403, "Expected 401/403 for DELETE " + endpoint);
        }
    }

    private Map<String, Object> getPathParams(String endpoint) {
        return endpoint.contains("{id}") ? pathParams : null;
    }
}

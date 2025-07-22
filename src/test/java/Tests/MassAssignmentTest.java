package Tests;

import BaseClass.BaseTest;
import Utilities.TestDataUtil;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

public class MassAssignmentTest extends BaseTest {

    private Map<String, Object> userTokenHeaders;

    @BeforeClass
    public void setUp() {
        userTokenHeaders = Map.of("Authorization", "Bearer " + TestDataUtil.getToken("user_token"));
    }

    @Test
    public void testMassAssignmentOnPost_shouldIgnoreRestrictedFields() {
        
        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Test User");
        payload.put("email", "testmass@example.com");
        payload.put("password", "StrongPass123!");
        payload.put("role", "admin");
        payload.put("created_at", "2022-01-01");

        Response res = restRequest.sendRequest("POST", "/users", payload, null, userTokenHeaders);

        int code = res.getStatusCode();
        String role = res.jsonPath().getString("role");
        String createdAt = res.jsonPath().getString("created_at");

        System.out.println("Status Code: " + code);
        System.out.println("Response: " + res.asPrettyString());

        Assert.assertTrue(code == 200 || code == 201, "Expected 2xx for valid POST");

        if (role != null) {
            Assert.assertNotEquals(role, "admin", "Mass assignment detected: role was modified.");
        }

        Assert.assertNull(createdAt, "Mass assignment detected: created_at should not be accepted.");
    }

    @Test
    public void testMassAssignmentOnPut_shouldIgnoreRestrictedFields() {
        
        int userId = 123; // Replace with actual user ID

        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Updated User");
        payload.put("role", "admin");
        payload.put("created_at", "1990-01-01");

        Response res = restRequest.sendRequest("PUT", "/users/{id}", payload, Map.of("id", userId), userTokenHeaders);

        int code = res.getStatusCode();
        String role = res.jsonPath().getString("role");
        String createdAt = res.jsonPath().getString("created_at");

        System.out.println("Status Code: " + code);
        System.out.println("Response: " + res.asPrettyString());

        Assert.assertTrue(code == 200 || code == 204, "Expected successful PUT response");

        if (role != null) {
            Assert.assertNotEquals(role, "admin", "Mass assignment detected: role was modified.");
        }

        Assert.assertNull(createdAt, "Mass assignment detected: created_at should not be accepted.");
    }
}

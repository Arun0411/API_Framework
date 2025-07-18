package Tests;

import Base.BaseTest;
import Utilities.TestDataUtil;
import com.fasterxml.jackson.databind.JsonNode;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MassAssignmentTest extends BaseTest {

    private String userToken;

    @BeforeClass
    public void setUp() throws IOException {
        JsonNode user = TestDataUtil.getUser("users.json", "user");
        userToken = TestDataUtil.getField(user, "token");
    }

    @Test
    public void testMassAssignmentOnPost_shouldIgnoreRestrictedFields() {
        RequestSpecification spec = getSpec(userToken).contentType(ContentType.JSON);

        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Test User");
        payload.put("email", "testmass@example.com");
        payload.put("password", "StrongPass123!");
        payload.put("role", "admin");             // Should be ignored
        payload.put("created_at", "2022-01-01");  // Should be ignored

        Response res = sendRequest("POST", "/users", spec, payload, null);

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
        RequestSpecification spec = getSpec(userToken).contentType(ContentType.JSON);

        int userId = 123; // Replace with actual user ID

        Map<String, Object> payload = new HashMap<>();
        payload.put("name", "Updated User");
        payload.put("role", "admin");              // Should not be updated
        payload.put("created_at", "1990-01-01");   // Should be ignored

        Response res = sendRequest("PUT", "/users/{id}", spec, payload, Map.of("id", userId));

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
